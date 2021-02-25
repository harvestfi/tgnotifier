package pro.belbix.tgnotifier.tg;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pro.belbix.tgnotifier.models.DtoI;
import pro.belbix.tgnotifier.ws.FrameHandlerWithQueue;
import pro.belbix.tgnotifier.ws.HardWorkFrameHandler;
import pro.belbix.tgnotifier.ws.HarvestFrameHandler;
import pro.belbix.tgnotifier.ws.UniFrameHandler;
import pro.belbix.tgnotifier.ws.ImportantEventsFrameHandler;
import pro.belbix.tgnotifier.ws.PriceEventsHandler;

@Service
@Log4j2
public class MessageRouter {

    private final AtomicBoolean run = new AtomicBoolean(true);

    private final TelegramBotService telegramBotService;
    private final UniFrameHandler uniFrameHandler;
    private final HarvestFrameHandler harvestFrameHandler;
    private final HardWorkFrameHandler hardWorkFrameHandler;
    private final ImportantEventsFrameHandler importantEventsFrameHandler;
    private final PriceEventsHandler priceEventsHandler;

    public MessageRouter(TelegramBotService telegramBotService,
                         UniFrameHandler uniFrameHandler,
                         HarvestFrameHandler harvestFrameHandler,
                         HardWorkFrameHandler hardWorkFrameHandler,
                         ImportantEventsFrameHandler importantEventsFrameHandler,
                         PriceEventsHandler priceEventsHandler) {
        this.telegramBotService = telegramBotService;
        this.uniFrameHandler = uniFrameHandler;
        this.harvestFrameHandler = harvestFrameHandler;
        this.hardWorkFrameHandler = hardWorkFrameHandler;
        this.importantEventsFrameHandler = importantEventsFrameHandler;
        this.priceEventsHandler = priceEventsHandler;
    }

    @PostConstruct
    public void init() {
        routeFrom(uniFrameHandler);
        routeFrom(harvestFrameHandler);
        routeFrom(hardWorkFrameHandler);
        routeFrom(importantEventsFrameHandler);
        routeFrom(priceEventsHandler);
        log.info("Telegram Message Router initialized");
    }

    private void routeFrom(FrameHandlerWithQueue handler) {
        Thread thread = new Thread(new HandlerRouter(run, handler, telegramBotService));
        thread.setName("Router" + handler.getClass().getSimpleName());
        thread.start();
    }

    public static class HandlerRouter implements Runnable {

        private final AtomicBoolean run;
        private final FrameHandlerWithQueue handler;
        private final TelegramBotService telegramBotService;

        public HandlerRouter(AtomicBoolean run, FrameHandlerWithQueue handler,
                             TelegramBotService telegramBotService) {
            this.run = run;
            this.handler = handler;
            this.telegramBotService = telegramBotService;
        }

        @Override
        public void run() {
            while (run.get()) {
                try {
                    DtoI dto = handler.getQueue().take();
                    telegramBotService.sendDto(dto);
                } catch (Exception e) {
                    log.error("Error routing", e);
                }
            }
        }
    }


}
