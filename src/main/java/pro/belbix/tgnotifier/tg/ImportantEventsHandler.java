package pro.belbix.tgnotifier.tg;

import static pro.belbix.tgnotifier.utils.Constants.*;

import org.springframework.stereotype.Service;
import pro.belbix.tgnotifier.Properties;
import pro.belbix.tgnotifier.db.entity.UserEntity;
import pro.belbix.tgnotifier.models.DtoI;
import pro.belbix.tgnotifier.models.ImportantEventsDTO;
import pro.belbix.tgnotifier.tg.model.CheckResult;

@Service
public class ImportantEventsHandler {

  private final Properties properties;

  public ImportantEventsHandler(Properties properties) {
    this.properties = properties;
  }

  public CheckResult checkAndUpdate(UserEntity user, DtoI dto) {
    if (dto instanceof ImportantEventsDTO) {
      CheckResult result = new CheckResult();
      ImportantEventsDTO eventDto = (ImportantEventsDTO) dto;
      eventDto.updateInfo();

      switch (eventDto.getEvent()) {
        case STRATEGY_CHANGED:
          checkStrategyChangedDto(user, eventDto, result);
          return result;
        case STRATEGY_ANNOUNCED:
          checkStrategyAnnouncedDto(user, eventDto, result);
          return result;
        case TOKEN_MINTED:
          checkTokenMintedDto(user, eventDto, result);
          return result;
        default:
          return null;
      }
    } else {
      return null;
    }
  }

  private void checkStrategyChangedDto(UserEntity user, ImportantEventsDTO dto,
      CheckResult checkResult) {
    if (user.getStrategyChange() != null && user.getStrategyChange()) {
      if (properties.isShowDescriptions()) {
        dto.setDescription("Trigger " + user.getStrategyChange());
      }
      checkResult.setSuccess(true);
      checkResult.setMessage(dto.printStrategyChanged());
    }
  }

  private void checkStrategyAnnouncedDto(UserEntity user, ImportantEventsDTO dto,
      CheckResult checkResult) {
    if (user.getStrategyAnnounce() != null && user.getStrategyAnnounce()) {
      if (properties.isShowDescriptions()) {
        dto.setDescription("Trigger " + user.getStrategyAnnounce());
      }
      checkResult.setSuccess(true);
      checkResult.setMessage(dto.printStrategyAnnounced());
    }
  }

  private void checkTokenMintedDto(UserEntity user, ImportantEventsDTO dto,
      CheckResult checkResult) {
    if (user.getTokenMint() != null
        && user.getTokenMint() != 0.0
        && dto.getMintAmount() != null
        && user.getTokenMint() < dto.getMintAmount()) {
      if (properties.isShowDescriptions()) {
        dto.setDescription("Trigger " + user.getTokenMint());
      }
      checkResult.setSuccess(true);
      checkResult.setMessage(dto.printTokenMinted());
    }
  }


}
