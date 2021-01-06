package pro.belbix.tgnotifier.models;

import com.vdurmont.emoji.EmojiParser;
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

    private String description;

    @Override
    public String print() {
        return EmojiParser.parseToUnicode(
            String.format("\uD83D\uDE9C %,.2f$ for ", shareChangeUsd) +
                vault + " " +
                String.format(", all profit %,.2f$ ", shareUsdTotal) +
                String.format(" and %,.2f%% APR ", apr) +
                "\n" +
                String.format("All vaults profit %,.2f$", allProfit) +
                String.format(" PS APR %,.2f%% ", psApr) +
//                "\n" +
                link() +
                (description != null ? description + "\n" : "") +
                "");
    }

    @Override
    public String printValueChanged(double percent) {
        return EmojiParser.parseToUnicode(
            "\uD83D\uDE9C "
                + vault + " Income changed on " +
                String.format("%.1f%% ", percent) +
                " APR: " + apr +
                "\n" +
                String.format("All vaults profit %,.2f$", allProfit) +
                String.format(" PS APR %,.2f%% ", psApr) +
//                "\n" +
                link() +
                (description != null ? description + "\n" : "") +
                "");
    }

    private String link() {
        String hash = id;
        if (id != null && id.contains("_")) {
            hash = id.split("_")[0];
        }
        return "<a href=\"https://etherscan.io/tx/" + hash + "\">Etherscan</a>";
    }
}
