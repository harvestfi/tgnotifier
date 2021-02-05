package pro.belbix.tgnotifier.models;

import com.vdurmont.emoji.EmojiParser;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
            "\uD83D\uDE9C <code>" + LocalDateTime.ofInstant(Instant.ofEpochSecond(blockDate), ZoneOffset.UTC) + " UTC</code> <b>" + vault + "</b>" +
                String.format(" increased by <code>$%,.2f</code> ", shareChangeUsd) +
                String.format("\uD83D\uDCC8 APR changed by <code>%,.2f%% </code> ", apr) +
                link() + "\n" +
                String.format("\uD83D\uDCB0 Vault profit to date <code>$%,.2f </code>", shareUsdTotal) +
                String.format("\uD83C\uDFE6 All Vaults <code>$%,.2f</code>", allProfit) +
                "\n" +
                String.format("\uD83D\uDC68\u200D\uD83C\uDF3E PS APR <code>%,.2f%%</code> ", psApr) +
                (description != null ? description + "\n" : "") +
                "");
    }

    @Override
    public String printValueChanged(double percent) {
        return EmojiParser.parseToUnicode(
            "\uD83D\uDE9C <code>" + LocalDateTime.ofInstant(Instant.ofEpochSecond(blockDate), ZoneOffset.UTC) + " UTC</code> <b>" +
                vault + "</b> Income APR changed by " +
                String.format("<code>%.2f%%</code> ", percent) +
                link() + "\n" +
                "\uD83D\uDCC8 new APR " + String.format("<code>%.2f%%</code> ", apr) +
                String.format("\uD83D\uDC68\u200D\uD83C\uDF3E PS APR <code>%,.2f%%</code> ", psApr) +
                "\n" +
                String.format("\uD83C\uDFE6 Profit of All Vaults <code>$%,.2f</code>  ", allProfit) +
                (description != null ? description + "\n" : "") +
                "");
    }

    private String link() {
        String hash = id;
        if (id != null && id.contains("_")) {
            hash = id.split("_")[0];
        }
        return "<a href=\"https://etherscan.io/tx/" + hash + "\">\uD83D\uDD0D Etherscan</a>";
    }
}
