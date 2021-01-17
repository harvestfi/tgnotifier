package pro.belbix.tgnotifier.tg;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pro.belbix.tgnotifier.Application;

import pro.belbix.tgnotifier.models.ImportantEventsDTO;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class ImportantEventsHandlerTest {
    @Autowired
    private TelegramBotService telegramBotService;

    public void init() {
        telegramBotService.init();
    }


    @Test
    public void testStrategyChange() {
        init();
        sendImportantEvent(
            "StrategyChanged",
            Long.valueOf("11521205"), 
            Long.valueOf("1608878295"),
            "0x180c496709023ce8952003a9ff385a3bbeb8b2c3", 
            "0xfde5dfb79d4a65913cb72ddd9148a768705e98d4",
            "DAI",
            "0x5050fcae09005a82be5865529d8d4dd786dadcf58ba2f00a72e47e7eefea7b6d",
            null,
            "{\"vaultAddress\":\"0xab7fa2b2985bccfc13c6d86b1d5a17486ab1e04c\",\"strategyTimeLock\":null}"
        );
    }

    @Test
    public void testStrategyAnnounce() {
        init();
        sendImportantEvent(
            "StrategyAnnounced",
            Long.valueOf("11517789"), 
            Long.valueOf("1608832645"),
            "0x5db1b2128bccc5b49f9ca7e3086b14fd4cf2ef64", 
            "0x93cee333c690cb91c39ac7b3294740651dc79c3d",
            "USDC",
            "0xa3109212af762244064e314f74ff5432532a6346b43360f43314b28a0cde22ac",
            null,
            "{\"vaultAddress\":\"0xf0358e8c3cd5fa238a29301d0bea3d63a17bedbe\",\"strategyTimeLock\":43200}"
        );
    }

    @Test
    public void testTokenMint() {
        init();
        sendImportantEvent(
            "TokenMinted",
            Long.valueOf("11550180"), 
            Long.valueOf("1609261950"),
            "null", 
            "null",
            "FARM",
            "0x33336e62c644b763776aae64b6f1ed27903405a92cca4501816515e19949ac4f",
            9129.323,
            "{\"vaultAddress\":\"0xa0246c9032bc3a600820415ae600c6388619a14d\",\"strategyTimeLock\":null}"
        );
        sendImportantEvent(
            "TokenMinted",
            Long.valueOf("11550180"), 
            Long.valueOf("1609261950"),
            "null", 
            "null",
            "FARM",
            "0x33336e62c644b763776aae64b6f1ed27903405a92cca4501816515e19949ac4f",
            2608.378,
            "{\"vaultAddress\":\"0xa0246c9032bc3a600820415ae600c6388619a14d\",\"strategyTimeLock\":null}"
        );
        sendImportantEvent(
            "TokenMinted",
            Long.valueOf("11550180"), 
            Long.valueOf("1609261950"),
            "null", 
            "null",
            "FARM",
            "0x33336e62c644b763776aae64b6f1ed27903405a92cca4501816515e19949ac4f",
            1304.189,
            "{\"vaultAddress\":\"0xa0246c9032bc3a600820415ae600c6388619a14d\",\"strategyTimeLock\":null}"
        );
    }


    private void sendImportantEvent(String event, Long block, Long blockDate, String newStrategy, 
                                    String oldStrategy, String vault, String hash, Double mintAmount, String info) {
     
        
        ImportantEventsDTO dto = new ImportantEventsDTO();
        dto.setEvent(event);
        dto.setBlock(block);
        dto.setBlockDate(blockDate);
        dto.setOldStrategy(oldStrategy);
        dto.setNewStrategy(newStrategy);
        dto.setVault(vault);
        dto.setHash(hash);
        dto.setMintAmount(mintAmount);
        dto.setInfo(info);

        telegramBotService.sendDto(dto);

        // wait for all messages to be sent
        try {
        Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
    }

}
