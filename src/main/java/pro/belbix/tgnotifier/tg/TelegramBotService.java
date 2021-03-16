package pro.belbix.tgnotifier.tg;

import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;
import static pro.belbix.tgnotifier.tg.Commands.COMMANDS;
import static pro.belbix.tgnotifier.tg.Commands.INFO;
import static pro.belbix.tgnotifier.tg.Commands.UNKNOWN_COMMAND;
import static pro.belbix.tgnotifier.tg.Commands.WELCOME_MESSAGE;
import static pro.belbix.tgnotifier.tg.Commands.responseForCommand;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pro.belbix.tgnotifier.Properties;
import pro.belbix.tgnotifier.db.DbService;
import pro.belbix.tgnotifier.db.entity.UserEntity;
import pro.belbix.tgnotifier.models.DtoI;

@Log4j2
@Service
public class TelegramBotService {

  private final DbService dbService;
  private final Properties properties;
  private final DefaultMessageHandler defaultMessageHandler;
  private final AddressesMessageHandler addressesMessageHandler = new AddressesMessageHandler();
  private final ImportantEventsHandler importantEventsHandler;
  private MessageSender messageSender;

  public TelegramBotService(DbService dbService, Properties properties,
      DefaultMessageHandler defaultMessageHandler,
      ImportantEventsHandler importantEventsHandler) {
    this.properties = properties;
    this.dbService = dbService;
    this.defaultMessageHandler = defaultMessageHandler;
    this.importantEventsHandler = importantEventsHandler;
  }

  public void init() {
    TelegramBot bot = new TelegramBot(properties.getTelegramToken());
    bot.setUpdatesListener(this::updatesListener);
    messageSender = new MessageSender(bot);
    log.info("Telegram Bot started");
  }

  private int updatesListener(List<Update> updates) {
    log.info("Get updates " + updates.size());
    try {
      for (Update u : updates) {
        UserInput input = getUserInput(u);

        if (input == null) {
          continue;
        }
        long chatId = input.getChatId();
        if (!dbService.isKnownChatId(chatId)) {
          log.info("Chat added " + chatId);
          sendMessage(chatId, WELCOME_MESSAGE, null, true);
          saveNewUser(input);
          continue;
        }
        handleMessage(input);
      }
    } catch (Exception e) {
      log.error("Update listener err", e);
    }
    return CONFIRMED_UPDATES_ALL;
  }

  private UserInput getUserInput(Update u) {
    if (u == null) {
      return null;
    }
    if (u.message() != null) {
      return new UserInput(u.message());
    }
    if (u.callbackQuery() != null) {
      messageSender.answerCallback(u.callbackQuery().id());
      return new UserInput(u.callbackQuery());
    }
    return null;
  }

  public void sendMessage(long chatId, String message, InlineButton[] buttons, boolean sendMenu) {
    messageSender.send(chatId, message, buttons, sendMenu);
  }

  private void saveNewUser(UserInput input) {
    UserEntity userEntity = new UserEntity();
    userEntity.setId(input.getChatId());
    userEntity.setName(input.getMessage().from().username());
    userEntity.setUserId(input.getMessage().from().id());
    dbService.saveNewUser(userEntity);
  }

  private void handleMessage(UserInput input) {
    String text = input.getText();
    long chatId = input.getChatId();
    log.info("Received message from user " + text);
    try {
      if (Arrays.asList(COMMANDS).contains(text)) {
        handleCommand(input);
      } else {
        handleValue(input);
      }
    } catch (Exception e) {
      log.error("Error handle message " + text, e);
      sendMessage(chatId, "Error while handling your request, use correct syntax.", null, true);
    }
  }

  private void handleCommand(UserInput input) {
    String text = input.getText();
    long chatId = input.getChatId();
    if (text.startsWith(INFO)) {
      sendUserInfo(chatId);
    } else {
      UserResponse callback = responseForCommand(text);
      if (!UNKNOWN_COMMAND.equals(callback.getMessage())) {
        dbService.updateLastCommand(chatId, text);
      }
      sendMessage(chatId, callback.getMessage(), callback.getButtons(), callback.isSendMenu());
    }
  }

  private void handleValue(UserInput input) {
    String text = input.getText();
    long chatId = input.getChatId();

    UserResponse result = dbService.updateValueForLastCommand(chatId, text);
    log.info("Value updated with result " + result);
    sendMessage(chatId, result.getMessage(), result.getButtons(), result.isSendMenu());
  }

  private void sendUserInfo(long chatId) {
    sendMessage(chatId, dbService.findById(chatId).print(), null, true);
  }

  public void sendDto(DtoI dto) {
    if (dto == null) {
      return;
    }

    for (UserEntity user : dbService.findAllChats()) {
      try {
        CheckResult checkResult = defaultMessageHandler.checkAndUpdate(user, dto);
        if (checkResult.isSuccess()) {
          sendMessage(user.getId(), checkResult.getMessage(), null, true);
        }
        String ownerMsg = addressesMessageHandler.check(user, dto);
        if (ownerMsg != null) {
          sendMessage(user.getId(), ownerMsg, null, true);
        }
        CheckResult eventResult = importantEventsHandler.checkAndUpdate(user, dto);
        if (eventResult != null && eventResult.isSuccess()) {
          sendMessage(user.getId(), eventResult.getMessage(), null, true);
        }
      } catch (Exception e) {
        log.error("Error while handle " + dto.print());
      }
    }
  }


}
