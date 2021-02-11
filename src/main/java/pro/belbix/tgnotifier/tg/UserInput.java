package pro.belbix.tgnotifier.tg;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.CallbackQuery;

import lombok.Data;

@Data
public class UserInput {
    private final long chatId = 0;
    private final String text = null;
    private final Message message = null;

    public UserInput(Message message){
        this.chatId = message.chat().id();
        this.text = message.text();
        this.message = message;
    }

    public UserInput(CallbackQuery callback){
        this.chatId = callback.message().chat().id();
        this.text = callback.data();
        this.message = callback.message();

    }
}
