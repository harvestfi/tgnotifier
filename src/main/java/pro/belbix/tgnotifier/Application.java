package pro.belbix.tgnotifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pro.belbix.tgnotifier.tg.TelegramBotService;
import pro.belbix.tgnotifier.ws.WebSocketService;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
    context.getBean(TelegramBotService.class).init();
    context.getBean(WebSocketService.class).start();
  }
}
