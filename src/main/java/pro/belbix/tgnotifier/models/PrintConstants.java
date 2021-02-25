package pro.belbix.tgnotifier.models;

public class PrintConstants {

    public static String percentChangeType(double percent) {
        //negative
        if (percent < 0) {
            if (percent < -3) {
                if (percent < -10) {
                    return "\uD83D\uDE28\uD83D\uDE31";
                }
                return "\uD83D\uDE28";
            }
            return "\uD83D\uDE24";
        }

        //positive
        if (percent < 3) {
            return "\uD83E\uDD11";
        }
        return "\uD83E\uDD11\uD83E\uDD73";
    }

}
