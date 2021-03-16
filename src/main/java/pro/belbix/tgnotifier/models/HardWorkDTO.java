package pro.belbix.tgnotifier.models;

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
        "\uD83D\uDE9C " + printUTC() + " did <code>HardWork #" + callsQuantity + "</code> \n" +
            "\uD83C\uDFE6 Vault <b>" + vault + "</b>  " + link() + "\n" +
            String.format("\uD83D\uDC68\u200D\uD83C\uDF3E profit <code>$%,.2f</code> ",
                shareChangeUsd) +
            "and " + String.format("<code>%,.2f</code>", farmBuyback) +
            " FARM to PS\n" +
            String.format("\uD83D\uDCB0 Total harvested <code>$%,.2f</code>", shareUsdTotal) +
            " over <code>" + TimeUnit.SECONDS.toDays(periodOfWork) + "</code> days\n" +
            String.format("	\uD83D\uDCCA Average weekly TVL <code>$%,.2f</code>", weeklyAverageTvl)
            +
            "\n" +
            (description != null ? description + "\n" : "") +
            "");
  }

  private String printUTC() {
    return "<code>" + LocalDateTime.ofInstant(Instant.ofEpochSecond(blockDate), ZoneOffset.UTC)
        + " UTC</code>";
  }

  private String link() {
    String hash = id;
    if (id != null && id.contains("_")) {
      hash = id.split("_")[0];
    }
    return "<a href=\"https://etherscan.io/tx/" + hash + "\">\uD83D\uDD0D Etherscan</a>";
  }

  @Override
  public String printValueChanged(double percent) {
    return EmojiParser.parseToUnicode(
        "\uD83D\uDE9C " + printUTC() + " did <code>HardWork #" + callsQuantity + "</code> \n" +
            "\uD83C\uDFE6 Vault <b>" + vault + "</b> income APR changed by " +
            String.format("<code>%.2f%%</code> ", percent) + "\n" +
            "\uD83D\uDCC8 new APR " + String.format("<code>%.2f%%</code> ", apr) +
            link() + "\n" +
            String.format("\uD83D\uDC68\u200D\uD83C\uDF3E profit <code>$%,.2f</code> ",
                shareChangeUsd) +
            "and " + String.format("<code>%,.2f</code>", farmBuyback) +
            " FARM to PS\n" +
            String.format("\uD83D\uDCB0 Total harvested <code>$%,.2f</code>", shareUsdTotal) +
            " over <code>" + TimeUnit.SECONDS.toDays(periodOfWork) + "</code> days\n" +
            String.format("	\uD83D\uDCCA Average weekly TVL <code>$%,.2f</code>", weeklyAverageTvl)
            +
            "\n" +
            (description != null ? description + "\n" : "") +
            "");
  }
}
