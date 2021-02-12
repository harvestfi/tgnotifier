package pro.belbix.tgnotifier.tg;

import lombok.Data;

@Data
public class InlineButton {
    private final String text;
    private final String value;

    public InlineButton(String text, String value){
        this.text = text;
        this.value = value;
    }
}
