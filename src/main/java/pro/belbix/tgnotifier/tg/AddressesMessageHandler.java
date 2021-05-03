package pro.belbix.tgnotifier.tg;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pro.belbix.tgnotifier.db.entity.UserEntity;
import pro.belbix.tgnotifier.models.DtoI;
import pro.belbix.tgnotifier.models.HarvestDTO;
import pro.belbix.tgnotifier.models.UniswapDTO;

@Service
public class AddressesMessageHandler {

  public String check(UserEntity user, DtoI dto) {
    if (dto instanceof UniswapDTO) {
      UniswapDTO uniswapDTO = (UniswapDTO) dto;
      if (checkOwner(user.getSubscribedAddress(), uniswapDTO.getOwner())) {
        return createResponse(uniswapDTO.getOwner(), uniswapDTO.print());
      }
    } else if (dto instanceof HarvestDTO) {
      HarvestDTO harvestDTO = (HarvestDTO) dto;
      if (checkOwner(user.getSubscribedAddress(), harvestDTO.getOwner())) {
        return createResponse(harvestDTO.getOwner(), harvestDTO.print());
      }
    }
    return null;
  }

  private boolean checkOwner(String userSubscriptions, String owner) {
    if (StringUtils.isEmpty(userSubscriptions) || StringUtils.isEmpty(owner)) {
      return false;
    }
    for (String address : userSubscriptions.split(",")) {
      if (owner.equals(address)) {
        return true;
      }
    }
    return false;
  }

  private String createResponse(String owner, String print) {
    return "\uD83E\uDD14 Some activity with " + owner + "\n" + print;
  }
}
