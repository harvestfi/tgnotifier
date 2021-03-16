package pro.belbix.tgnotifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pro.belbix.tgnotifier.tg.TelegramBotService;
import pro.belbix.tgnotifier.ws.WebSocketService;

import static pro.belbix.tgnotifier.properties.ConstantsTest.REPEAT_TIME;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class ApplicationTest {

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private TelegramBotService telegramBotService;

    @Test
    public void main() throws InterruptedException {
        webSocketService.start();
        telegramBotService.init();

        for (int i = 0; i < REPEAT_TIME; i++) {
            Thread.sleep(100);
        }
    }
}
