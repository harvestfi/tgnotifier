package pro.belbix.tgnotifier.tg;

import lombok.Data;

@Data
public class CheckResult {

    private boolean success = false;
    private String message = "Empty message";

}
