package pro.belbix.tgnotifier;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    Properties.class
})
public class AppConfig {

}
