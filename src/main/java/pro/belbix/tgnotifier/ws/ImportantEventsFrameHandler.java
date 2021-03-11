package pro.belbix.tgnotifier.ws;

import java.lang.reflect.Type;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import pro.belbix.tgnotifier.models.DtoI;
import pro.belbix.tgnotifier.models.ImportantEventsDTO;

@Log4j2
@Component
public class ImportantEventsFrameHandler implements StompFrameHandler, FrameHandlerWithQueue {

  private final BlockingQueue<DtoI> queue = new ArrayBlockingQueue<>(1000);

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return ImportantEventsDTO.class;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    try {
      ImportantEventsDTO dto = (ImportantEventsDTO) payload;
      log.info("Received important event " + dto.print());
      queue.put(dto);
    } catch (Exception e) {
      log.info("Error with important event " + payload, e);
    }
  }

  @Override
  public BlockingQueue<DtoI> getQueue() {
    return queue;
  }


}
