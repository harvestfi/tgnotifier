package pro.belbix.tgnotifier.utils;

import static pro.belbix.tgnotifier.utils.Constants.SAD_SMILE;

public class NumberHelper {

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

  public static String formatNumber(Double number) {
    if (number == null) {
      return " ";
    }
    return String.format("%,.2f$ ", number);
  }

  public static String formatPercent(Double number) {
    if (number == null) {
      return " ";
    }
    return String.format("%.1f%%", number);
  }
}
