package pro.belbix.tgnotifier.models;

import static pro.belbix.tgnotifier.utils.Constants.HREF_ETHERSCAN_ADDRESS;
import static pro.belbix.tgnotifier.utils.Constants.HREF_ETHERSCAN_TX;
import static pro.belbix.tgnotifier.utils.Constants.HREF_YIELDFARMING;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiParser;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
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
        "\u2757 "
            + printUTC(blockDate)
            + getStrategyLink(hash, "changed")
            + linkVault(additionalInfo, vault) + "\n"
            + linkStrategies(oldStrategy, newStrategy) + " "
            + linkDiff(oldStrategy, newStrategy)
            + getDescription(description)
            + "");
  }

  public String printStrategyAnnounced() {
    return EmojiParser.parseToUnicode(
        "\u26A0\uFE0F "
            + printUTC(blockDate)
            + getStrategyLink(hash, "announced")
            + linkVault(additionalInfo, vault) + "\n"
            + linkStrategies(oldStrategy, newStrategy) + " "
            + linkDiff(oldStrategy, newStrategy) + "\n"
            + printEarliestEffective() + "\n"
            + getDescription(description)
            + "");
  }

  public String printTokenMinted() {
    return EmojiParser.parseToUnicode(
        "\u26A1 "
            + printUTC(blockDate)
            + getMintText(mintAmount)
            + linkVault(additionalInfo, vault) + " \uD83D\uDE9C\n"
            + linkTx(hash, "\uD83D\uDD0D transaction") + "\n"
            + getDescription(description)
            + "");
  }

  @Override
  public String printValueChanged(double percent) {
    return null;
  }

  private String printEarliestEffective() {
    if (additionalInfo == null) {
      return "";
    }
    return "\u23F1\uFE0F earliest effective "
        + printUTC(blockDate + additionalInfo.getStrategyTimeLock());
  }

  private String getMintText(Double mintAmount) {
    if (mintAmount == null) {
      return "";
    }
    return " <b>minted</b> <code>"
        + String.format("%.2f", mintAmount)
        + "</code> ";
  }

  private String printUTC(long date) {
    return "<code>" + LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneOffset.UTC)
        + " UTC</code>";
  }

  private String getStrategyLink(String hash, String text) {
    return " strategy <b>" + linkTx(hash, text) + "</b> for ";
  }

  private String getDescription(String description) {
    return (description != null ? description + "\n" : "");
  }

  private String linkTx(String tx, String text) {
    return HREF_ETHERSCAN_TX + tx + "\">" + text + "</a>";
  }

  private String linkVault(ImportantEventsInfo additionalInfo, String vault) {
    if (additionalInfo == null) {
      if (vault == null) {
        return " ";
      } else {
        return vault;
      }
    }
    return HREF_ETHERSCAN_ADDRESS
        + additionalInfo.getVaultAddress()
        + "\">"
        + vault + "</a>";
  }

  private String linkStrategies(String oldStrategy, String newStrategy) {
    String text = "";

    if (oldStrategy != null) {
      text =
          HREF_ETHERSCAN_ADDRESS + oldStrategy
              + "\">\uD83D\uDD0D Old Strategy</a>" + " ";
    }

    if (newStrategy != null) {
      text = text
          + HREF_ETHERSCAN_ADDRESS + newStrategy
          + "\">\uD83D\uDD0E New Strategy</a>";
    }
    return text;
  }

  private String linkDiff(String oldStrategy, String newStrategy) {
    if (oldStrategy == null || newStrategy == null) {
      return "";
    }
    return HREF_YIELDFARMING
        + oldStrategy
        + "&contract2="
        + newStrategy +
        "\">\uD83D\uDC68\u200D\uD83C\uDF3E Contracts Diff</a>"
        + "\n";
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
}
