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
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    private long id;
    private String name;
    private Integer userId;
    private String lastCommand;

    private Double lastFarm;
    private Double farmChange;

    private Double lastTvl;
    private Double tvlChange;

    private Double lastHardWork;
    private Double hardWorkChange;

    private Double minFarmAmount;
    private Double minTvlAmount;
    private Double minHardWorkAmount;
    private String subscribedAddress;

    private Boolean strategyChange;
    private Boolean strategyAnnounce;
    private Double tokenMint;

    public String print() {
        return "User settings:\n" +
            FARM_CHANGE + " FARM change: " + farmChange + "\n" +
            FARM_MIN + " FARM min: " + minFarmAmount + "\n" +
            TVL_CHANGE + " TVL change: " + tvlChange + "\n" +
            TVL_MIN + " TVL min: " + minTvlAmount + "\n" +
            PS_APR_CHANGE + " PS APR change: " + hardWorkChange + "\n" +
            HARD_WORK_MIN + " Hard Work min: " + minHardWorkAmount + "\n" +
            SUBSCRIBE_ON_ADDRESS + " Subscribed on: " + subscribedAddress + "\n" +
            STRATEGY_CHANGE + " Subscribed: " + strategyChange + "\n" +
            STRATEGY_ANNOUNCE + " Subscribed: " + strategyAnnounce + "\n" +
            TOKEN_MINT + " Minted min: " + tokenMint + "\n"+
            "";
    }
}
