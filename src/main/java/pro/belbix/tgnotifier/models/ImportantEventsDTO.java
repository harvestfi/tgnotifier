package pro.belbix.tgnotifier.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiParser;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.persistence.Id;
import lombok.Data;

@Data
public class ImportantEventsDTO implements DtoI {

  @Id
  private String id;
  private String hash;
  private Long block;
  private Long blockDate;
  private String event;
  private String oldStrategy;
  private String newStrategy;
  private String vault;
  private Double mintAmount;
  private String info;

  private String description;

  private ImportantEventsInfo additionalInfo;

  public void updateInfo() {
    this.additionalInfo = parseInfo();
  }

  private ImportantEventsInfo parseInfo() {
    ObjectMapper mapper = new ObjectMapper();
    ImportantEventsInfo additionalInfo = null;
    try {
      additionalInfo = mapper.readValue(info, ImportantEventsInfo.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return additionalInfo;
  }

  @Override
  public String print() {
    return blockDate + " "
        + event + " "
        + vault + " "
        + "old: " + oldStrategy + " "
        + "new: " + newStrategy + " "
        + "minted: " + mintAmount + " "
        + info;
  }

  public String printStrategyChanged() {
    return EmojiParser.parseToUnicode(
        "\u2757 " + printUTC(blockDate) + " strategy <b>" + linkTx(hash, "changed") + "</b> for "
            + linkVault()
            + "\n" +
            linkStrategies() + " " +
            linkDiff() + "\n" +
            (description != null ? description + "\n" : "") +
            "");
  }

  private String printUTC(long date) {
    return "<code>" + LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneOffset.UTC)
        + " UTC</code>";
  }

  private String linkTx(String tx, String text) {
    return "<a href=\"https://etherscan.io/tx/" + tx + "\">" + text + "</a>";
  }

  private String linkVault() {
    return (additionalInfo != null
        ? "<a href=\"https://etherscan.io/address/" + additionalInfo.getVaultAddress() + "\">"
        + vault + "</a>"
        : vault);
  }

  private String linkStrategies() {
    return "<a href=\"https://etherscan.io/address/" + oldStrategy
        + "\">\uD83D\uDD0D Old Strategy</a>" +
        " <a href=\"https://etherscan.io/address/" + newStrategy
        + "\">\uD83D\uDD0E New Strategy</a>";
  }

  private String linkDiff() {
    return "<a href=\"https://yieldfarming.info/tools/diff/?contract1=" +
        oldStrategy +
        "&contract2=" +
        newStrategy +
        "\">\uD83D\uDC68\u200D\uD83C\uDF3E Contracts Diff</a>";
  }

  public String printStrategyAnnounced() {
    return EmojiParser.parseToUnicode(
        "\u26A0\uFE0F " + printUTC(blockDate) + " strategy <b>" + linkTx(hash, "announced")
            + "</b> for "
            + linkVault() + "\n" +
            linkStrategies() + " " +
            linkDiff() + "\n" +
            printEarliestEffective() + "\n" +
            (description != null ? description + "\n" : "") +
            "");
  }

  private String printEarliestEffective() {
    return (additionalInfo != null
        ? "\u23F1\uFE0F earliest effective " + printUTC(
        blockDate + additionalInfo.getStrategyTimeLock())
        : "");

  }

  public String printTokenMinted() {
    return EmojiParser.parseToUnicode(
        "\u26A1 " + printUTC(blockDate) + " <b>minted</b> <code>" + String
            .format("%.2f", mintAmount) + "</code> "
            + linkVault() + " \uD83D\uDE9C\n" +
            linkTx(hash, "\uD83D\uDD0D transaction") + "\n" +
            (description != null ? description + "\n" : "") +
            "");
  }

  @Override
  public String printValueChanged(double percent) {
    return null;
  }

}
