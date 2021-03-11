package pro.belbix.tgnotifier;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tgnotifier")
public class Properties {

  private String telegramToken = "";
  private String wsUrl = "";
  private boolean showDescriptions = false;

}
