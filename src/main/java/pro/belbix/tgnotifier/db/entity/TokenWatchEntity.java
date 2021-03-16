package pro.belbix.tgnotifier.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tokenwatch")
@Data
@EqualsAndHashCode(exclude = {"user"})
public class TokenWatchEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false, updatable = false)
  private UserEntity user;

  private String tokenName;
  private Double lastPrice;
  private Double priceChange;

  public String print() {
    return
        "-----------\n" +
            "Token Name: " + tokenName + "\n" +
            "Subscribed Token Change: " + priceChange + "\n" +
            "Last Price: " + String.format("%.1f", lastPrice) + "\n" +
            "";
  }
}
