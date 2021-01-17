package pro.belbix.tgnotifier.tg;

import org.springframework.stereotype.Service;
import pro.belbix.tgnotifier.Properties;
import pro.belbix.tgnotifier.db.DbService;
import pro.belbix.tgnotifier.db.entity.UserEntity;
import pro.belbix.tgnotifier.models.DtoI;
import pro.belbix.tgnotifier.models.ImportantEventsDTO;


@Service
public class ImportantEventsHandler {
    private final DbService dbService;
    private final Properties properties;

    public ImportantEventsHandler(DbService dbService, Properties properties) {
        this.dbService = dbService;
        this.properties = properties;
    }

    public CheckResult checkAndUpdate(UserEntity user, DtoI dto) {
        if (dto instanceof ImportantEventsDTO) {
            CheckResult result = new CheckResult();
            ImportantEventsDTO eventDto = (ImportantEventsDTO) dto; 
        
            if ("StrategyChanged".equals(eventDto.getEvent())) {
                 checkStrategyChangedDto(user, eventDto, result);
            } else if ("StrategyAnnounced".equals(eventDto.getEvent())) {
                checkStrategyAnnouncedDto(user, eventDto, result);
            } else if ("TokenMinted".equals(eventDto.getEvent())) {
                checkTokenMintedDto(user, eventDto, result);
            } else {
                return null;
            }
            return result;
        } else {
            return null;
        }
    }

    private void checkStrategyChangedDto(UserEntity user, ImportantEventsDTO dto, CheckResult checkResult) {
        if (user.getStrategyChange()) {
            if (properties.isShowDescriptions()) {
                dto.setDescription("Trigger " + user.getStrategyChange());
            }
            checkResult.setSuccess(true);
            checkResult.setMessage(dto.printStrategyChanged());
        }
    }

    private void checkStrategyAnnouncedDto(UserEntity user, ImportantEventsDTO dto, CheckResult checkResult) {
        if (user.getStrategyAnnounce()) {
            if (properties.isShowDescriptions()) {
                dto.setDescription("Trigger " + user.getStrategyAnnounce());
            }
            checkResult.setSuccess(true);
            checkResult.setMessage(dto.printStrategyAnnounced());
        }
    }

    private void checkTokenMintedDto(UserEntity user, ImportantEventsDTO dto, CheckResult checkResult) {     
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
