package pro.belbix.tgnotifier.db.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NamedEntityGraph(
    name = "users-graph.all",
    attributeNodes = {
        @NamedAttributeNode(value = "tokenWatch", subgraph = "tokenwatch.all")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "tokenwatch.all",
            attributeNodes = {
                @NamedAttributeNode("id"),
            }
        ),
    }
)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
        fetch = FetchType.EAGER, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<TokenWatchEntity> tokenWatch;
    private String selectedToken;

    public String print() {

        String subscribedTokens = "";

        for (TokenWatchEntity token : tokenWatch) {
            subscribedTokens += token.print();
        }

        return "FARM change: " + farmChange + "\n" +
            "FARM min: " + minFarmAmount + "\n" +
            "TVL change: " + tvlChange + "\n" +
            "TVL min: " + minTvlAmount + "\n" +
            "PS APR change: " + hardWorkChange + "\n" +
            "Hard Work min: " + minHardWorkAmount + "\n" +
            "Subscribed Address: " + subscribedAddress + "\n" +
            "Strategy Change: " + strategyChange + "\n" +
            "Strategy Announce: " + strategyAnnounce + "\n" +
            "Selected Tokens: \n" +
            subscribedTokens;
    }
}
