package pro.belbix.tgnotifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pro.belbix.tgnotifier.tg.TelegramBotService;
import pro.belbix.tgnotifier.ws.WebSocketService;

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
        while (true) {
            Thread.sleep(100);
        }
    }
}
