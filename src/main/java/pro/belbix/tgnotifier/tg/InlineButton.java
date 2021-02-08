package pro.belbix.tgnotifier.tg;

import lombok.Data;

@Data
public class InlineButton {
    private String text = null;
    private String value = null;

    public InlineButton(String text, String value){
        this.text = text;
        this.value = value;
    }
}
