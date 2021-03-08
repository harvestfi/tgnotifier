package pro.belbix.tgnotifier.ws;

import java.lang.reflect.Type;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import pro.belbix.tgnotifier.models.DtoI;
import pro.belbix.tgnotifier.models.HardWorkDTO;

@Log4j2
@Component
public class HardWorkFrameHandler implements StompFrameHandler, FrameHandlerWithQueue {

  private final BlockingQueue<DtoI> queue = new ArrayBlockingQueue<>(1000);

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return HardWorkDTO.class;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    try {
      HardWorkDTO dto = (HardWorkDTO) payload;
      log.info("Received hard work " + dto.print());
      queue.put(dto);
    } catch (Exception e) {
      log.info("Error with hard work " + payload, e);
    }
  }

  @Override
  public BlockingQueue<DtoI> getQueue() {
    return queue;
  }


}
