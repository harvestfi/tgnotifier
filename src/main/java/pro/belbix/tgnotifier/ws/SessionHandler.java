package pro.belbix.tgnotifier.ws;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

@Log4j2
public class SessionHandler extends StompSessionHandlerAdapter {

    public final static String UNI_TRANSACTIONS_TOPIC_NAME = "/topic/transactions";
    public final static String HARVEST_TRANSACTIONS_TOPIC_NAME = "/topic/harvest";
    public final static String HARD_WORK_TOPIC_NAME = "/topic/hardwork";
    public final static String IMPORTANT_EVENTS_TOPIC_NAME = "/topic/events";
    public final static String PRICES_TOPIC_NAME = "/topic/prices";

    private final WebSocketService webSocketService;
    private final UniFrameHandler uniFrameHandler;
    private final HarvestFrameHandler harvestFrameHandler;
    private final HardWorkFrameHandler hardWorkFrameHandler;
    private final ImportantEventsFrameHandler importantEventsFrameHandler;
    private final PriceEventsHandler priceEventsHandler;

    public SessionHandler(WebSocketService webSocketService, UniFrameHandler uniFrameHandler,
                          HarvestFrameHandler harvestFrameHandler,
                          HardWorkFrameHandler hardWorkFrameHandler,
                          ImportantEventsFrameHandler importantEventsFrameHandler,
                          PriceEventsHandler priceEventsHandler) {
        this.webSocketService = webSocketService;
        this.uniFrameHandler = uniFrameHandler;
        this.harvestFrameHandler = harvestFrameHandler;
        this.hardWorkFrameHandler = hardWorkFrameHandler;
        this.importantEventsFrameHandler = importantEventsFrameHandler;
        this.priceEventsHandler = priceEventsHandler;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("WebSocket connected");
        session.subscribe(UNI_TRANSACTIONS_TOPIC_NAME, uniFrameHandler);
        session.subscribe(HARVEST_TRANSACTIONS_TOPIC_NAME, harvestFrameHandler);
        session.subscribe(HARD_WORK_TOPIC_NAME, hardWorkFrameHandler);
        session.subscribe(IMPORTANT_EVENTS_TOPIC_NAME, importantEventsFrameHandler);
        session.subscribe(PRICES_TOPIC_NAME, priceEventsHandler);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                                Throwable exception) {
        log.error("WS exception", exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("WS transport error", exception);
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ignored) {
        }
        webSocketService.stop();
        webSocketService.start();
    }
}
