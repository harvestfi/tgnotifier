package pro.belbix.tgnotifier.models;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;

@Data
public class PriceDTO implements DtoI {

    @Id
    private String id;
    private Long block;
    private Long blockDate;
    private String token;
    private Double tokenAmount;
    private String otherToken;
    private Double otherTokenAmount;
    private Double price;
    private Integer buy;
    private String source;
    private Double lpTotalSupply;
    private Double lpToken0Pooled;
    private Double lpToken1Pooled;

    private String description;

    @Override
    public String print() {
        return Instant.ofEpochSecond(blockDate) + " "
            + source + " "
            + token + " "
            + String.format("%.1f", price) + " "
            + buy + " "
            + id;
    }

    @Override
    public String printValueChanged(double percent) {
        return otherToken + "/" + token
            + " changed " + String.format("%.1f", percent) + "% "
            + "current price is " 
            + String.format("%.4f", price);
    }

}