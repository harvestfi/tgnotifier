package pro.belbix.tgnotifier.tg;

import lombok.Data;

@Data
public class UserResponse {

  private InlineButton[] buttons = null;
  private String message = null;
  private boolean sendMenu = false;

  public UserResponse(String message, InlineButton[] buttons) {
    this.message = message;
    this.buttons = buttons;
  }

  public UserResponse(String message, InlineButton[] buttons, boolean sendMenu) {
    this.message = message;
    this.buttons = buttons;
    this.sendMenu = sendMenu;
  }
}
