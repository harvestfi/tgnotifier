package pro.belbix.tgnotifier.models;

import static pro.belbix.tgnotifier.utils.Constants.HREF_ETHERSCAN_TX;

import com.vdurmont.emoji.EmojiParser;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;
import javax.persistence.Id;
import lombok.Data;

@Data
public class HardWorkDTO implements DtoI {

  @Id
  private String id;
  private String vault;
  private long block;
  private long blockDate;
  private double shareChange;
  private double shareChangeUsd;
  private double shareUsdTotal;
  private double tvl;
  private double allProfit;
  private long periodOfWork;
  private long psPeriodOfWork;
  private double perc;
  private double apr;
  private double psTvlUsd;
  private double psApr;
  private double farmBuyback;
  private double farmBuybackSum;
  private int callsQuantity;
  private int poolUsers;
  private double savedGasFees;
  private double savedGasFeesSum;
  private double fee;
  private Double weeklyAverageTvl;

  private String description;

  @Override
  public String print() {
    return EmojiParser.parseToUnicode(
        "\uD83D\uDE9C "
            + printUTC(blockDate)
            + getCallsQuantityText(callsQuantity)
            + getVaultText(vault)
            + getEtherscanTextLink(id)
            + formatProfit(shareChangeUsd)
            + formatFarmBuyback(farmBuyback)
            + formatSharedUsdTotal(shareUsdTotal)
            + getPeriodOfWork(periodOfWork)
            + formatWeeklyAverageTvl(weeklyAverageTvl)
            + getDescription(description)
            + "");
  }

  @Override
  public String printValueChanged(double percent) {
    return EmojiParser.parseToUnicode(
        "\uD83D\uDE9C "
            + printUTC(blockDate)
            + getCallsQuantityText(callsQuantity)
            + getVaultText(vault)
            + formatPercent(percent)
            + "\n"
            + "\uD83D\uDCC8 new APR "
            + formatPercent(apr)
            + getEtherscanTextLink(id)
            + formatProfit(shareChangeUsd)
            + formatFarmBuyback(farmBuyback)
            + formatSharedUsdTotal(shareUsdTotal)
            + getPeriodOfWork(periodOfWork)
            + formatWeeklyAverageTvl(weeklyAverageTvl)
            + getDescription(description)
            + "");
  }

  private String getDescription(String description) {
    return (description != null ? description + "\n" : "");
  }

  private String formatWeeklyAverageTvl(Double weeklyAverageTvl) {
    return String.format("	\uD83D\uDCCA Average weekly TVL <code>$%,.2f</code>", weeklyAverageTvl)
        + "\n";
  }

  private String getPeriodOfWork(long periodOfWork) {
    return " over <code>" + TimeUnit.SECONDS.toDays(periodOfWork) + "</code> days\n";
  }

  private String formatSharedUsdTotal(double shareUsdTotal) {
    return String.format("\uD83D\uDCB0 Total harvested <code>$%,.2f</code>", shareUsdTotal);
  }

  private String formatFarmBuyback(double farmBuyback) {
    return " and " + String.format("<code>%,.2f</code>", farmBuyback) + " FARM to PS\n";
  }

  private String formatProfit(double shareChangeUsd) {
    return String
        .format("\uD83D\uDC68\u200D\uD83C\uDF3E profit <code>$%,.2f</code>", shareChangeUsd);
  }

  private String getVaultText(String vault) {
    if (vault == null) {
      return " ";
    }
    return "\uD83C\uDFE6 Vault <b>" + vault + "</b>  ";
  }

  private String getCallsQuantityText(int callsQuantity) {
    return " did <code>HardWork #" + callsQuantity + "</code> \n";
  }

  private String printUTC(long blockDate) {
    return "<code>"
        + LocalDateTime.ofInstant(Instant.ofEpochSecond(blockDate), ZoneOffset.UTC)
        + " UTC</code>";
  }

  private String getEtherscanTextLink(String id) {
    if (id == null) {
      return " ";
    }
    return HREF_ETHERSCAN_TX
        + checkId(id)
        + "\">\uD83D\uDD0D Etherscan</a>"
        + "\n";
  }

  private String checkId(String id) {
    return id.contains("_") ? id.split("_")[0] : id;
  }

  private String formatPercent(double percent) {
    return String.format("<code>%.2f%%</code> ", percent);
  }
}
