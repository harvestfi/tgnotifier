package pro.belbix.tgnotifier.ws;

import java.util.concurrent.BlockingQueue;
import pro.belbix.tgnotifier.models.DtoI;

public interface FrameHandlerWithQueue {

  BlockingQueue<DtoI> getQueue();

}
