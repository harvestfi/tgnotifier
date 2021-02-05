package pro.belbix.tgnotifier.tg;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pro.belbix.tgnotifier.Application;

import pro.belbix.tgnotifier.models.HardWorkDTO;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class HardWorkHandlerTest {
    @Autowired
    private TelegramBotService telegramBotService;

    public void init() {
        telegramBotService.init();
    }


    @Test
    public void testHardWork() {
        init();
        sendHardWork(
            6946030.406416202,
            Long.valueOf("11299287"), 
            Long.valueOf("1605933089"),
            2.5575488883534625, 
            0.007529204825915188,
            "SUSHI_ETH_USDC",
            3060D,
            8631D,
            68.7981194559267,
            "0xfaff1b27f5f17252bea0a2a1cc452f282fc644ec8ccfa963941f2c83ce6414aa_207"
        );
    }

    @Test
    public void testHardWork2() {
        init();
        sendHardWork(
            8707462.759383662,
            Long.valueOf("11445965"), 
            Long.valueOf("1607879942"),
            8.05837639182139, 
            0.023285779395268377,
            "SUSHI_ETH_USDC",
            10439D,
            253689D,
            56.56908412457272,
            "0x7769abc1da7be3ee551f08fdb1c78016024964b0299c805d37ca0baced19b707_197"
        );
    }

    private void sendHardWork(Double allProfit, Long block, Long blockDate, Double apr, 
                                    Double perc, String vault, 
                                    Double shareChangeUsd, Double shareUsdTotal, Double psApr,
                                    String id) {
     
        
        HardWorkDTO dto = new HardWorkDTO();
        dto.setAllProfit(allProfit);
        dto.setBlock(block);
        dto.setBlockDate(blockDate);
        dto.setApr(apr);
        dto.setPerc(perc);
        dto.setVault(vault);
        dto.setShareChangeUsd(shareChangeUsd);
        dto.setShareUsdTotal(shareUsdTotal);
        dto.setPsApr(psApr);
        dto.setId(id);

        telegramBotService.sendDto(dto);

        // wait for all messages to be sent
        try {
        Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
    }

}
