package pro.belbix.tgnotifier.models;

import com.vdurmont.emoji.EmojiParser;
import java.math.BigInteger;
import javax.persistence.Id;
import lombok.Data;

@Data
public class HarvestDTO implements DtoI {

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
            (description != null ? description + "\n" : "") +
                methodName() + " " +
                String.format("%,.2f$ ", amount) +
                vault + " " +
                String.format("%,.2f$ ", lastUsdTvl) + "\n" +
                String.format("%,.2f$ All TVL ", lastAllUsdTvl) +
                "<a href=\"https://etherscan.io/tx/" + hash + "\">Etherscan</a>" +
                "");
    }

    private String methodName() {
        if (methodName == null) {
            return "?\uD83D\uDE29?";
        }
        switch (methodName) {
            case "Deposit":
                return "\uD83D\uDCB0 Deposit";
            case "Withdraw":
                return "\uD83D\uDCA8 Withdraw";
            default:
                return "⁉️" + methodName;
        }
    }
}
