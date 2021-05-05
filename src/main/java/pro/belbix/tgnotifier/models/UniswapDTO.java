package pro.belbix.tgnotifier.models;

import static pro.belbix.tgnotifier.utils.Constants.HREF_ETHERSCAN_TX;
import static pro.belbix.tgnotifier.utils.Constants.SAD_SMILE;
import static pro.belbix.tgnotifier.utils.NumberHelper.percentChangeType;

import com.vdurmont.emoji.EmojiParser;
import java.math.BigInteger;
import javax.persistence.Id;
import lombok.Data;

@Data
public class UniswapDTO implements DtoI {

  @Id
  private String id;
  private String type;
  private String coin;
  private String owner;
  private double amount;
  private String otherCoin;
  private double otherAmount;
  private Double ethAmount;
  private String hash;
  private BigInteger block;
  private boolean confirmed = false;
  private Double lastPrice;
  private Double lastGas;
  private Long blockDate;
  private Integer ownerCount;
  private Double psWeekApy;
  private Double psIncomeUsd;

  private String description;

  @Override
  public String print() {
    return EmojiParser.parseToUnicode(
        type(type) + " "
            + farmAmount(amount)
            + separateCoins(type) + " "
            + otherCoin(otherAmount, otherCoin)
            + price(lastPrice)
            + getEtherscanTextLink(hash)
            + getDescription(description)
            + "");
  }

  @Override
  public String printValueChanged(double percent) {
    return EmojiParser.parseToUnicode(
        percentChangeType(percent) + " "
            + getPriceChangeText(coin, percent)
            + price(lastPrice)
            + getEtherscanTextLink(hash)
            + getDescription(description)
            + "");
  }

  private String type(String type) {
    if (type == null) {
      return SAD_SMILE;
    }
    switch (type) {
      case "BUY":
        return "\uD83D\uDE80 Buy";
      case "ADD":
        return "\uD83D\uDCB5 Add liquidity";
      case "SELL":
        return "\uD83D\uDD3B Sell";
      case "REM":
        return "\uD83D\uDD2A Remove liquidity";
      default:
        return SAD_SMILE;
    }
  }

  private String getPriceChangeText(String coin, double percent) {
    if (coin == null) {
      return "";
    }
    return coin + " price changed on "
        + String.format("%.1f%%", percent)
        + "\n";

  }

  private String farmAmount(double amount) {
    return String.format("%.2f", amount) + " FARM ";
  }

  private String separateCoins(String type) {
    if (type == null) {
      return SAD_SMILE;
    }
    switch (type) {
      case "BUY":
      case "SELL":
        return "for";
      case "ADD":
      case "REM":
        return "and";
      default:
        return SAD_SMILE;
    }
  }

  private String getDescription(String description) {
    return (description != null ? description + "\n" : "");
  }

  private String getEtherscanTextLink(String hash) {
    if (hash == null) {
      return " ";
    }
    return HREF_ETHERSCAN_TX + hash + "\">Etherscan</a>";
  }

  private String otherCoin(double otherAmount, String otherCoin) {
    if (otherCoin == null) {
      return " ";
    }
    return String.format("%.2f", otherAmount) + " " + otherCoin + "\n";
  }

  private String price(Double lastPrice) {
    return String.format("%.2f", lastPrice) + "\uD83D\uDCB2 per FARM ";
  }
}
