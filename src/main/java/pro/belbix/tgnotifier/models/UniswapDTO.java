package pro.belbix.tgnotifier.models;

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
            (description != null ? description + "\n" : "") +
                type() + " " +
                farmAmount() + " " +
                separateCoins() + " " +
                otherCoin() + "\n" +
                price() + "\uD83D\uDCB2 per FARM " +
                "<a href=\"https://etherscan.io/tx/" + hash + "\">Etherscan</a>" +
                "");
    }

    private String type() {
        if (type == null) {
            return "?\uD83D\uDE29?";
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
                return "?\uD83D\uDE29?";
        }
    }

    private String farmAmount() {
        return String.format("%.2f", amount) + " FARM";
    }

    private String separateCoins() {
        if (type == null) {
            return "?\uD83D\uDE29?";
        }
        switch (type) {
            case "BUY":
            case "SELL":
                return "for";
            case "ADD":
            case "REM":
                return "and";
            default:
                return "?\uD83D\uDE29?";
        }
    }

    private String otherCoin() {
        return String.format("%.2f", otherAmount) + " " + otherCoin;
    }

    private String price() {
        return String.format("%.2f", lastPrice);
    }

}
