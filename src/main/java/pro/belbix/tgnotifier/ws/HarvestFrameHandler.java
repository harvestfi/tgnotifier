package pro.belbix.tgnotifier.ws;

import java.lang.reflect.Type;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import pro.belbix.tgnotifier.models.DtoI;
import pro.belbix.tgnotifier.models.HarvestDTO;

@Log4j2
@Component
public class HarvestFrameHandler implements StompFrameHandler, FrameHandlerWithQueue {

    public static final String PRICE_STUB_TYPE = "price_stub";
    private final BlockingQueue<DtoI> queue = new ArrayBlockingQueue<>(1000);

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return HarvestDTO.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        try {
            HarvestDTO dto = (HarvestDTO) payload;
            if (PRICE_STUB_TYPE.equals(dto.getMethodName())) {
                log.info("Received stub price " + dto.getPrices());
                return;
            }
            log.info("Received harvest " + dto.print());
            queue.put(dto);
        } catch (Exception e) {
            log.info("Error with harvest " + payload, e);
        }
    }

    @Override
    public BlockingQueue<DtoI> getQueue() {
        return queue;
    }

}
