package pro.belbix.tgnotifier.tg;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.time.Duration;
import java.time.Instant;
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

    public void send(long chatId, String message) {
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
            bot.execute(new SendMessage(chatId, message).parseMode(HTML).disableWebPagePreview(true), callback);
            lastUserMessages.put(chatId, Instant.now());
        });
    }


}
