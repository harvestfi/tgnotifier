package pro.belbix.tgnotifier.models;

import static pro.belbix.tgnotifier.utils.Constants.HREF_ETHERSCAN_TX;
import static pro.belbix.tgnotifier.utils.Constants.MONEY_BUG_SMILE;
import static pro.belbix.tgnotifier.utils.Constants.SAD_SMILE;
import static pro.belbix.tgnotifier.utils.Constants.WIND_SMILE;
import static pro.belbix.tgnotifier.utils.NumberHelper.formatNumber;
import static pro.belbix.tgnotifier.utils.NumberHelper.formatPercent;
import static pro.belbix.tgnotifier.utils.NumberHelper.percentChangeType;

import com.vdurmont.emoji.EmojiParser;
import java.math.BigInteger;
import javax.persistence.Id;
import lombok.Data;

@Data
public class HarvestDTO implements DtoI {

  public static final String DEPOSIT_METHOD = "Deposit";
  public static final String WITHDRAW_METHOD = "Withdraw";
  public static final String PRICE_STUB_METHOD = "price_stub";

  @Id
  private String id;
  private String hash;
  private BigInteger block;
  private boolean confirmed = false;
  private Long blockDate;
  private String methodName;
  private String owner;
  private Double amount;
  private Double amountIn;
  private String vault;
  private Double lastGas;
  private Double lastTvl;
  private Double lastUsdTvl;
  private Integer ownerCount;
  private Double sharePrice;
  private Double usdAmount;
  private String prices;
  private String lpStat;
  private Double lastAllUsdTvl;
  private String description;

  @Override
  public String print() {
    return EmojiParser.parseToUnicode(
        methodName() + " "
            + formatNumber(usdAmount)
            + formatString(vault)
            + formatNumber(lastUsdTvl) + "\n"
            + formatAllTVLText(lastAllUsdTvl)
            + getEtherscanTextLink(hash)
            + formatDescription(description)
            + "");
  }

  @Override
  public String printValueChanged(double percent) {
    return EmojiParser.parseToUnicode(
        percentChangeType(percent) + " "
            + formatTVLchangeOnText(vault)
            + formatPercent(percent) + "\n"
            + formatAllTVLText(lastAllUsdTvl)
            + getEtherscanTextLink(hash)
            + formatDescription(description)
            + "");
  }

  private String methodName() {
    if (methodName == null) {
      return SAD_SMILE;
    }

    switch (methodName) {
      case DEPOSIT_METHOD:
        return MONEY_BUG_SMILE + " " + methodName;
      case WITHDRAW_METHOD:
        return WIND_SMILE + " " + methodName;
      default:
        return "⁉️" + methodName;
    }
  }

  private String formatAllTVLText(Double number) {
    if (number == null) {
      return " ";
    }
    return formatNumber(lastAllUsdTvl) + "All TVL ";
  }

  private String formatString(String text) {
    if (text == null) {
      return " ";
    }
    return text + " ";
  }

  private String formatTVLchangeOnText(String text) {
    if (text == null) {
      return " ";
    }
    return text + " TVL changed on ";
  }

  private String getEtherscanTextLink(String hashText) {
    if (hashText == null) {
      return " ";
    }
    return HREF_ETHERSCAN_TX + hashText + "\">Etherscan</a>";
  }

  private String formatDescription(String descriptionText) {
    if (descriptionText == null) {
      return " ";
    }
    return (description != null ? description + "\n" : "");
  }
}
