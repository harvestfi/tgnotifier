package pro.belbix.tgnotifier.tg;

import static pro.belbix.tgnotifier.properties.ConstantsTest.REPEAT_TIME;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pro.belbix.tgnotifier.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TelegramBotServiceTest {

  @Autowired
  private TelegramBotService telegramBotService;

  @Test
  public void startTestTelegram() throws InterruptedException {
    telegramBotService.init();

    telegramBotService.sendMessage(0, "test", null, false);

    for (int i = 0; i < REPEAT_TIME; i++) {
      Thread.sleep(100);
    }
  }
}
