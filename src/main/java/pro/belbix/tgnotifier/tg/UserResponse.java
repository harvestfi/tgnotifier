package pro.belbix.tgnotifier.tg;

import lombok.Data;

@Data
public class UserResponse {
    private InlineButton[] buttons = null;
    private String message = null;

    public UserResponse(String message, InlineButton[] buttons){
        this.message = message;
        this.buttons = buttons;
    }
}
