package pro.belbix.tgnotifier.tg;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.CallbackQuery;

import lombok.Data;

@Data
public class UserInput {
    private long chatId = 0;
    private String text = null;
    private Message message = null;

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
