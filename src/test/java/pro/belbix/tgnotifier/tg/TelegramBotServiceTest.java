package pro.belbix.tgnotifier.tg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pro.belbix.tgnotifier.properties.ConstantsTest.REPEAT_TIME;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.COMMANDS;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.INFO;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.WELCOME_MESSAGE;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.aspectj.bridge.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pro.belbix.tgnotifier.Application;
import pro.belbix.tgnotifier.Properties;
import pro.belbix.tgnotifier.db.DbService;
import pro.belbix.tgnotifier.db.entity.UserEntity;
import pro.belbix.tgnotifier.models.HardWorkDTO;
import pro.belbix.tgnotifier.tg.model.CheckResult;
import pro.belbix.tgnotifier.tg.model.UserInput;
import pro.belbix.tgnotifier.tg.model.UserResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TelegramBotServiceTest {

  private TelegramBot bot;
  private final Properties properties = new Properties();

  @MockBean
  private DbService dbService;

  @MockBean
  private DefaultMessageHandler defaultMessageHandler;

  @MockBean
  private AddressesMessageHandler addressesMessageHandler;
  @MockBean
  private ImportantEventsHandler importantEventsHandler;

  @Autowired
  @InjectMocks
  private TelegramBotService telegramBotService;

  @Before
  public void setUp() {
    bot = new TelegramBot.Builder(properties.getTelegramToken()).debug().build();
    telegramBotService.messageSender = Mockito.mock(MessageSender.class);
  }

  @SneakyThrows
  @Test
  public void test_updateListener_isKnownChat() {
    String testJson = "{\"ok\":true,\"result\":[{\"update_id\":\"1\",\"message\":{\"message_id\":\"1\",\"chat\":{\"id\":\"1\"},\"from\":{\"username\":\"tetUser\",\"id\":\"1\"}}}]}";
    Mockito.when(dbService.isKnownChatId(1)).thenReturn(false);
    Mockito.doNothing().when(dbService).save(new UserEntity());

    sendJsonAndUpdateListener(testJson);

  }

  @SneakyThrows
  @Test
  public void test_updateListener_isUnKnownChat_knownCommand_sendUserInfo() {
    String testJson = "{\"ok\":true,\"result\":[{\"update_id\":\"1\",\"message\":{\"message_id\":\"1\",\"text\":\"⚙ Show Settings\",\"chat\":{\"id\":\"1\"},\"from\":{\"username\":\"tetUser\",\"id\":\"1\"}}}]}";
    Mockito.when(dbService.isKnownChatId(1)).thenReturn(true);
    Mockito.when(dbService.findById(1)).thenReturn(new UserEntity());
    Mockito.doNothing().when(telegramBotService.messageSender).send(1, "no active subscriptions", null, true);

    sendJsonAndUpdateListener(testJson);
  }

  @SneakyThrows
  @Test
  public void test_updateListener_isUnKnownChat_knownCommand_otherKnownCommand() {
    String testJson = "{\"ok\":true,\"result\":[{\"update_id\":\"1\",\"message\":{\"message_id\":\"1\",\"text\":\"Token Price Change Subscribe\",\"chat\":{\"id\":\"1\"},\"from\":{\"username\":\"tetUser\",\"id\":\"1\"}}}]}";
    Mockito.when(dbService.isKnownChatId(1)).thenReturn(true);

    sendJsonAndUpdateListener(testJson);
  }

  @SneakyThrows
  @Test
  public void test_updateListener_isUnKnownChat_handle_value() {
    String testJson = "{\"ok\":true,\"result\":[{\"update_id\":\"1\",\"message\":{\"message_id\":\"1\",\"text\":\"12\",\"chat\":{\"id\":\"1\"},\"from\":{\"username\":\"tetUser\",\"id\":\"1\"}}}]}";
    Mockito.when(dbService.isKnownChatId(1)).thenReturn(true);
    Mockito.when(dbService.updateValueForLastCommand(1, "12"))
        .thenReturn(new UserResponse("12", null));

    sendJsonAndUpdateListener(testJson);
  }

  @SneakyThrows
  @Test
  public void test_updateListener_isUnKnownChat_handle_exception() {
    String testJson = "{\"ok\":true,\"result\":[{\"update_id\":\"1\",\"message\":{\"message_id\":\"1\",\"text\":\"12\",\"chat\":{\"id\":\"1\"},\"from\":{\"username\":\"tetUser\",\"id\":\"1\"}}}]}";
    Mockito.when(dbService.isKnownChatId(1)).thenReturn(true);
    Mockito.when(dbService.updateValueForLastCommand(1, "12")).thenReturn(null);

    sendJsonAndUpdateListener(testJson);
  }

  @Test
  public void test_sendDto() {
    ArrayList<UserEntity> userEntityList = new ArrayList<UserEntity>();
    UserEntity userEntity=new UserEntity();
    userEntity.setId(1);
    userEntityList.add(userEntity);

    UserEntity wrongEntity=new UserEntity();
    wrongEntity.setId(2);
    userEntityList.add(wrongEntity);

    CheckResult checkResult=new CheckResult();
    checkResult.setSuccess(true);
    checkResult.setMessage("test Message");

    HardWorkDTO dto = new HardWorkDTO();

    Mockito.when(dbService.findAllChats()).thenReturn(userEntityList);
    Mockito.when(defaultMessageHandler.checkAndUpdate(userEntity, dto)).thenReturn(checkResult);
    Mockito.when(addressesMessageHandler.check(userEntity, dto)).thenReturn("ownerMsg");
    Mockito.when(importantEventsHandler.checkAndUpdate(userEntity, dto)).thenReturn(checkResult);
    Mockito.when(importantEventsHandler.checkAndUpdate(wrongEntity, dto)).thenThrow(NullPointerException.class);

    telegramBotService.sendDto(dto);

    Mockito.verify(defaultMessageHandler,Mockito.times(1)).checkAndUpdate(userEntity, dto);
    Mockito.verify(telegramBotService.messageSender,Mockito.times(2)).send(1,"test Message",null,true);
    Mockito.verify(telegramBotService.messageSender,Mockito.times(1)).send(1,"ownerMsg",null,true);
    Mockito.verify(importantEventsHandler,Mockito.times(1)).checkAndUpdate(userEntity, dto);
  }

  @SneakyThrows
  private void sendJsonAndUpdateListener(String json) {
    Properties properties = new Properties();
    withLatch(1, latch -> {
      bot = new TelegramBot.Builder(properties.getTelegramToken())
          .okHttpClient(new OkHttpClient.Builder()
              .addInterceptor(chain -> {
                Response response = new Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_2)
                    .code(1)
                    .message("")
                    .build();
                response = response.newBuilder().body(ResponseBody
                    .create(MediaType.parse("application/json"),
                        json
                    )).build();
                return response;
              })
              .build())
          .build();
      bot.setUpdatesListener(updates -> {
        telegramBotService.updatesListener(updates);
        return 1;
      });
    });
  }

  private void withLatch(int secTimeout, Consumer<CountDownLatch> body)
      throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    body.accept(latch);
    latch.await(secTimeout, TimeUnit.SECONDS);
    bot.removeGetUpdatesListener();
  }

  @Test
  public void startTestTelegram() throws InterruptedException {
    telegramBotService.sendMessage(0, "test", null, false);

    for (int i = 0; i < REPEAT_TIME; i++) {
      Thread.sleep(100);
    }
  }

  @Test
  public void testSendMessageWithButton() {
    telegramBotService.sendMessage(0, "test", null, false);

  }
}
