package pro.belbix.tgnotifier.tg;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;

import static pro.belbix.tgnotifier.tg.Commands.INFO;
import static pro.belbix.tgnotifier.tg.Commands.FARM_CHANGE;
import static pro.belbix.tgnotifier.tg.Commands.FARM_MIN;
import static pro.belbix.tgnotifier.tg.Commands.TVL_CHANGE;
import static pro.belbix.tgnotifier.tg.Commands.TVL_MIN;
import static pro.belbix.tgnotifier.tg.Commands.PS_APR_CHANGE;
import static pro.belbix.tgnotifier.tg.Commands.HARD_WORK_MIN;
import static pro.belbix.tgnotifier.tg.Commands.SUBSCRIBE_ON_ADDRESS;
import static pro.belbix.tgnotifier.tg.Commands.STRATEGY_CHANGE;
import static pro.belbix.tgnotifier.tg.Commands.STRATEGY_ANNOUNCE;
import static pro.belbix.tgnotifier.tg.Commands.TOKEN_MINT;
import static pro.belbix.tgnotifier.tg.Commands.TOKEN_PRICE_SUBSCRIBE;
import static pro.belbix.tgnotifier.tg.Commands.TOKEN_PRICE_SUBSCRIBE_CHANGE;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MessageSender {

    private static final long WAIT_MILLIS = 100;
    private final Callback<SendMessage, SendResponse> callback = new TelegramCallback();
    private final TelegramBot bot;
    private final Map<Long, Instant> lastUserMessages = new HashMap<>();
    private final Map<Long, Integer> waitedMessages = new HashMap<>();
    ExecutorService executor = new ThreadPoolExecutor(
        2,
        100,
        60L, TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(1000));
    ;

    public MessageSender(TelegramBot bot) {
        this.bot = bot;
    }

    public void answerCallback(String id){
        bot.execute(new AnswerCallbackQuery(id));
    }

    public void send(long chatId, String message, InlineButton[] buttons, boolean sendMenu) {
        executor.submit(() -> {
            Instant lastMessage = lastUserMessages.get(chatId);
            if (lastMessage != null) {
                if (Duration.between(lastMessage, Instant.now()).toMillis() < WAIT_MILLIS) {
                    int waited = Optional.ofNullable(waitedMessages.get(chatId)).orElse(0) + 1;
                    waitedMessages.put(chatId, waited);
                    try {
                        Thread.sleep(WAIT_MILLIS * waited);
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            SendMessage sendMessage = new SendMessage(chatId, message).parseMode(HTML).disableWebPagePreview(true);

            if (buttons!=null && buttons.length >0){
                InlineKeyboardButton[] inlineButtons = new InlineKeyboardButton[buttons.length];
                for (int i = 0; i < buttons.length; i++) {
                    inlineButtons[i] = new InlineKeyboardButton(buttons[i].getText()).callbackData(buttons[i].getValue());
                }
                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(inlineButtons);
                sendMessage.replyMarkup(inlineKeyboard);
            }
            else if (sendMenu){
                Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                    new String[]{INFO},
                    new String[]{FARM_CHANGE, FARM_MIN, },
                    new String[]{TVL_CHANGE,TVL_MIN},
                    new String[]{PS_APR_CHANGE, HARD_WORK_MIN},
                    new String[]{STRATEGY_CHANGE, STRATEGY_ANNOUNCE},
                    new String[]{SUBSCRIBE_ON_ADDRESS, TOKEN_MINT},
                    new String[]{TOKEN_PRICE_SUBSCRIBE, TOKEN_PRICE_SUBSCRIBE_CHANGE}
                    )

                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .selective(true);
                sendMessage.replyMarkup(replyKeyboardMarkup);
            }

            bot.execute(sendMessage, callback);

            lastUserMessages.put(chatId, Instant.now());
        });
    }


}
