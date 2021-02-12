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
        return "FARM change: " + farmChange + "\n" +
            "FARM min: " + minFarmAmount + "\n" +
            "TVL change: " + tvlChange + "\n" +
            "TVL min: " + minTvlAmount + "\n" +
            "PS APR change: " + hardWorkChange + "\n" +
            "Hard Work min: " + minHardWorkAmount + "\n" +
            "Subscribed Address: " + subscribedAddress + "\n" +
            "Strategy Change: " + strategyChange + "\n" +
            "Strategy Announce: " + strategyAnnounce + "\n" +
            "Minted min: " + tokenMint + "\n"+
            "";
    }
}
