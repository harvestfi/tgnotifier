package pro.belbix.tgnotifier.ws;

import java.lang.reflect.Type;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import pro.belbix.tgnotifier.models.DtoI;
import pro.belbix.tgnotifier.models.UniswapDTO;

@Log4j2
@Component
public class UniFrameHandler implements StompFrameHandler, FrameHandlerWithQueue {

    private final BlockingQueue<DtoI> queue = new ArrayBlockingQueue<>(1000);

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return UniswapDTO.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        try {
            UniswapDTO dto = (UniswapDTO) payload;
            log.info("Received uni " + dto.print());
            queue.put(dto);
        } catch (Exception e) {
            log.info("Error with uni " + payload, e);
        }
    }

    @Override
    public BlockingQueue<DtoI> getQueue() {
        return queue;
    }
}
