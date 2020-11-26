package pro.belbix.tgnotifier.tg;

import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;
import static com.pengrad.telegrambot.model.request.ParseMode.HTML;
import static pro.belbix.tgnotifier.tg.Commands.HELP;
import static pro.belbix.tgnotifier.tg.Commands.HELP_TEXT;
import static pro.belbix.tgnotifier.tg.Commands.INFO;
import static pro.belbix.tgnotifier.tg.Commands.UNKNOWN_COMMAND;
import static pro.belbix.tgnotifier.tg.Commands.WELCOME_MESSAGE;
import static pro.belbix.tgnotifier.tg.Commands.responseForCommand;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pro.belbix.tgnotifier.Properties;
import pro.belbix.tgnotifier.db.DbService;
import pro.belbix.tgnotifier.db.entity.UserEntity;
import pro.belbix.tgnotifier.models.DtoI;
import pro.belbix.tgnotifier.models.HardWorkDTO;
import pro.belbix.tgnotifier.models.HarvestDTO;
import pro.belbix.tgnotifier.models.UniswapDTO;

@Log4j2
@Service
public class TelegramBotService {

    private final Callback<SendMessage, SendResponse> callback = new TelegramCallback();
    private final DbService dbService;
    private final Properties properties;
    private TelegramBot bot;

    public TelegramBotService(DbService dbService, Properties properties) {
        this.properties = properties;
        this.dbService = dbService;
    }

    public void init() {
        bot = new TelegramBot(properties.getTelegramToken());
        bot.setUpdatesListener(this::updatesListener);
        log.info("Telegram Bot started");
    }

    private int updatesListener(List<Update> updates) {
        for (Update u : updates) {
            long chatId = u.message().chat().id();
            if (!dbService.isKnownChatId(chatId)) {
                log.info("Chat added " + chatId);
                sendMessage(chatId, WELCOME_MESSAGE);
                saveNewUser(u.message());
                continue;
            }
            handleMessage(u.message());
        }
        return CONFIRMED_UPDATES_ALL;
    }

    public void sendMessage(long chatId, String message) {
        bot.execute(new SendMessage(chatId, message).parseMode(HTML).disableWebPagePreview(true), callback);
    }

    private void saveNewUser(Message m) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(m.chat().id());
        userEntity.setName(m.from().username());
        userEntity.setUserId(m.from().id());
        dbService.saveNewUser(userEntity);
    }

    private void handleMessage(Message m) {
        String text = m.text();
        long chatId = m.chat().id();
        log.info("Received message from user " + text);
        try {
            if (text.startsWith("/")) {
                handleCommand(m);
            } else {
                handleValue(m);
            }
        } catch (Exception e) {
            log.error("Error handle message " + text, e);
            sendMessage(chatId, "Error while handling your request, use correct syntax. " + HELP);
        }
    }

    private void handleCommand(Message m) {
        String text = m.text();
        long chatId = m.chat().id();
        if (text.startsWith(HELP)) {
            sendMessage(chatId, HELP_TEXT);
        } else if (text.startsWith(INFO)) {
            sendUserInfo(chatId);
        } else {
            String callback = responseForCommand(text);
            if (!UNKNOWN_COMMAND.equals(callback)) {
                dbService.updateLastCommand(chatId, text);
            }
            sendMessage(chatId, callback);
        }
    }

    private void handleValue(Message m) {
        String text = m.text();
        long chatId = m.chat().id();

        String result = dbService.updateValueForLastCommand(chatId, text) + ". " + HELP;
        log.info("Value updated with result " + result);
        sendMessage(chatId, result);
    }

    private void sendUserInfo(long chatId) {
        sendMessage(chatId, dbService.findById(chatId).print());
    }

    public void sendDto(DtoI dto) {
        if (dto == null) {
            return;
        }

        for (UserEntity user : dbService.findAllChats()) {
            try {
                if (checkAndUpdate(user, dto)) {
                    sendMessage(user.getId(), dto.print());
                }
            } catch (Exception e) {
                log.error("Error while handle " + dto.print());
            }
        }
    }

    private boolean checkAndUpdate(UserEntity user, DtoI dto) {
        boolean result = false;
        boolean userUpdated = false;
        if (dto instanceof UniswapDTO) {
            UniswapDTO uniswapDTO = (UniswapDTO) dto;
            if (user.getLastFarm() == null) {
                user.setLastFarm(uniswapDTO.getLastPrice());
                dbService.save(user);
            }
            if (checkLastValue(user.getLastFarm(), user.getFarmChange(), uniswapDTO.getLastPrice(), dto)) {
                user.setLastFarm(uniswapDTO.getLastPrice());
                userUpdated = true;
            }

            if (checkCurrentValue(user.getMinFarmAmount(), uniswapDTO.getAmount(), dto)) {
                result = true;
            }
        } else if (dto instanceof HarvestDTO) {
            HarvestDTO harvestDTO = (HarvestDTO) dto;
            if (user.getLastTvl() == null) {
                user.setLastTvl(harvestDTO.getLastAllUsdTvl());
                dbService.save(user);
            }
            if (checkLastValue(user.getLastTvl(), user.getTvlChange(), harvestDTO.getLastAllUsdTvl(), dto)) {
                user.setLastTvl(harvestDTO.getLastAllUsdTvl());
                userUpdated = true;
            }

            if (checkCurrentValue(user.getMinTvlAmount(), harvestDTO.getUsdAmount(), dto)) {
                result = true;
            }
        } else if (dto instanceof HardWorkDTO) {
            HardWorkDTO hardWorkDTO = (HardWorkDTO) dto;
            if (user.getLastHardWork() == null) {
                user.setLastHardWork(hardWorkDTO.getPsApr());
                dbService.save(user);
            }
            if (checkLastValue(user.getLastHardWork(), user.getHardWorkChange(), hardWorkDTO.getPsApr(), dto)) {
                user.setLastHardWork(hardWorkDTO.getPsApr());
                userUpdated = true;
            }

            if (checkCurrentValue(user.getMinHardWorkAmount(), hardWorkDTO.getShareChangeUsd(), dto)) {
                result = true;
            }
        }

        if (userUpdated) {
            dbService.save(user);
            result = true;
        }

        return result;
    }

    private boolean checkLastValue(Double userLastValue, Double userChange, Double dtoLastValue, DtoI dto) {
        if (userLastValue == null) {
            return false;
        }
        if (userChange != null &&
            userChange != 0.0 &&
            dtoLastValue != null) {
            double changePerc = ((dtoLastValue - userLastValue) / dtoLastValue) * 100;
            if (Math.abs(changePerc) > userChange) {
                if (properties.isShowDescriptions()) {
                    dto.setDescription("Trigger " + String.format("%.2f%% > %.2f%%", changePerc, userChange));
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkCurrentValue(Double userMinAmount, Double dtoAmount, DtoI dto) {
        if (userMinAmount != null
            && userMinAmount != 0.0
            && dtoAmount != null
            && userMinAmount < dtoAmount) {
            if (properties.isShowDescriptions()) {
                dto.setDescription("Trigger " + String.format("%.2f > %.2f", dtoAmount, userMinAmount));
            }
            return true;
        }
        return false;
    }

}
