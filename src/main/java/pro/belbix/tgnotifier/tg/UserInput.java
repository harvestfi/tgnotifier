package pro.belbix.tgnotifier.tg;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import lombok.Data;

@Data
public class UserInput {

    private final long chatId;
    private final String text;
    private final Message message;

    public UserInput(Message message) {
        this.chatId = message.chat().id();
        this.text = message.text();
        this.message = message;
    }

    public UserInput(CallbackQuery callback) {
        this.chatId = callback.message().chat().id();
        this.text = callback.data();
        this.message = callback.message();
    }
}
