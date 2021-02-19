package pro.belbix.tgnotifier.ws;

import java.util.ArrayList;
import java.util.List;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import pro.belbix.tgnotifier.Properties;

@Service
public class WebSocketService {

    private final Properties properties;
    private final UniFrameHandler uniFrameHandler;
    private final HarvestFrameHandler harvestFrameHandler;
    private final HardWorkFrameHandler hardWorkFrameHandler;
    private final ImportantEventsFrameHandler importantEventsFrameHandler;
    private final PriceEventsHandler priceEventsHandler;

    private WebSocketStompClient stompClient;

    public WebSocketService(Properties properties, UniFrameHandler uniFrameHandler,
                            HarvestFrameHandler harvestFrameHandler,
                            HardWorkFrameHandler hardWorkFrameHandler,
                            ImportantEventsFrameHandler importantEventsFrameHandler,
                            PriceEventsHandler priceEventsHandler) {
        this.properties = properties;
        this.uniFrameHandler = uniFrameHandler;
        this.harvestFrameHandler = harvestFrameHandler;
        this.hardWorkFrameHandler = hardWorkFrameHandler;
        this.importantEventsFrameHandler = importantEventsFrameHandler;
        this.priceEventsHandler = priceEventsHandler;
    }

    public void start() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);

        SessionHandler sessionHandler = new SessionHandler(this, uniFrameHandler,
            harvestFrameHandler,
            hardWorkFrameHandler,
            importantEventsFrameHandler,
            priceEventsHandler);

        stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setInboundMessageSizeLimit(1024 * 1024);
        stompClient.connect(properties.getWsUrl(), new WebSocketHttpHeaders(), sessionHandler);
    }

    public void stop() {
        stompClient.stop();
    }

}
