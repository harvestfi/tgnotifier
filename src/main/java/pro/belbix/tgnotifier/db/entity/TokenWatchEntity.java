package pro.belbix.tgnotifier.db.entity;

import static pro.belbix.tgnotifier.tg.Commands.FARM_CHANGE;
import static pro.belbix.tgnotifier.tg.Commands.FARM_MIN;
import static pro.belbix.tgnotifier.tg.Commands.HARD_WORK_MIN;
import static pro.belbix.tgnotifier.tg.Commands.PS_APR_CHANGE;
import static pro.belbix.tgnotifier.tg.Commands.SUBSCRIBE_ON_ADDRESS;
import static pro.belbix.tgnotifier.tg.Commands.TVL_CHANGE;
import static pro.belbix.tgnotifier.tg.Commands.TVL_MIN;
import static pro.belbix.tgnotifier.tg.Commands.STRATEGY_CHANGE;
import static pro.belbix.tgnotifier.tg.Commands.STRATEGY_ANNOUNCE;
import static pro.belbix.tgnotifier.tg.Commands.TOKEN_MINT;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;

import lombok.Data;

@Entity
@Table(name = "tokenwatch")
@Data
public class TokenWatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "FK_USER", nullable = false, updatable = false)
    private UserEntity user;

    private String tokenName;
    private Double lastPrice;
    private Double priceChange;

    public String print() {
        return 
            "-----------\n" +
            "Token Name: " + tokenName + "\n"+
            "Subscribed Token Change: " + priceChange + "\n" +
            "Last Price: " + String.format("%.1f", lastPrice) + "\n" +
            "";
    }
}
