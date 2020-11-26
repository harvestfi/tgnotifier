package pro.belbix.tgnotifier.ws;

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
public class WebSocketServiceTest {

    @Autowired
    private WebSocketService webSocketService;

    @Test
    public void startWsTest() throws InterruptedException {
        webSocketService.start();
        while (true) {
            Thread.sleep(100);
        }
    }
}
