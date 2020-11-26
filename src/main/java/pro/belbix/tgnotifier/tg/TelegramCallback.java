package pro.belbix.tgnotifier.tg;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TelegramCallback implements Callback<SendMessage, SendResponse> {

    @Override
    public void onResponse(SendMessage request, SendResponse response) {
        log.info("Bot response for " + request.toWebhookResponse()
            + " " + response.errorCode()
            + " " + response.description());
    }

    @Override
    public void onFailure(SendMessage request, IOException e) {
        log.error("Bot Error for " + request.toWebhookResponse(), e);
    }
}
