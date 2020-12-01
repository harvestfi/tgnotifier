package pro.belbix.tgnotifier.tg;

import org.springframework.stereotype.Service;
import pro.belbix.tgnotifier.Properties;
import pro.belbix.tgnotifier.db.DbService;
import pro.belbix.tgnotifier.db.entity.UserEntity;
import pro.belbix.tgnotifier.models.DtoI;
import pro.belbix.tgnotifier.models.HardWorkDTO;
import pro.belbix.tgnotifier.models.HarvestDTO;
import pro.belbix.tgnotifier.models.UniswapDTO;

@Service
public class DefaultMessageHandler {

    private final DbService dbService;
    private final Properties properties;

    public DefaultMessageHandler(DbService dbService, Properties properties) {
        this.dbService = dbService;
        this.properties = properties;
    }

    public boolean checkAndUpdate(UserEntity user, DtoI dto) {
        boolean result = false;
        boolean userUpdated = false;
        if (dto instanceof UniswapDTO) {
            UniswapDTO uniswapDTO = (UniswapDTO) dto;
            if (user.getLastFarm() == null) {
                user.setLastFarm(uniswapDTO.getLastPrice());
                dbService.save(user);
            }
            if (checkLastValue(user.getLastFarm(), user.getFarmChange(), uniswapDTO.getLastPrice(), dto)) {
                user.setLastFarm(uniswapDTO.getLastPrice());
                userUpdated = true;
            }

            if (checkCurrentValue(user.getMinFarmAmount(), uniswapDTO.getAmount(), dto)) {
                result = true;
            }
        } else if (dto instanceof HarvestDTO) {
            HarvestDTO harvestDTO = (HarvestDTO) dto;
            if (user.getLastTvl() == null) {
                user.setLastTvl(harvestDTO.getLastAllUsdTvl());
                dbService.save(user);
            }
            if (checkLastValue(user.getLastTvl(), user.getTvlChange(), harvestDTO.getLastAllUsdTvl(), dto)) {
                user.setLastTvl(harvestDTO.getLastAllUsdTvl());
                userUpdated = true;
            }

            if (checkCurrentValue(user.getMinTvlAmount(), harvestDTO.getUsdAmount(), dto)) {
                result = true;
            }
        } else if (dto instanceof HardWorkDTO) {
            HardWorkDTO hardWorkDTO = (HardWorkDTO) dto;
            if (user.getLastHardWork() == null) {
                user.setLastHardWork(hardWorkDTO.getPsApr());
                dbService.save(user);
            }
            if (checkLastValue(user.getLastHardWork(), user.getHardWorkChange(), hardWorkDTO.getPsApr(), dto)) {
                user.setLastHardWork(hardWorkDTO.getPsApr());
                userUpdated = true;
            }

            if (checkCurrentValue(user.getMinHardWorkAmount(), hardWorkDTO.getShareChangeUsd(), dto)) {
                result = true;
            }
        }

        if (userUpdated) {
            dbService.save(user);
            result = true;
        }

        return result;
    }



    private boolean checkLastValue(Double userLastValue, Double userChange, Double dtoLastValue, DtoI dto) {
        if (userLastValue == null) {
            return false;
        }
        if (userChange != null &&
            userChange != 0.0 &&
            dtoLastValue != null) {
            double changePerc = ((dtoLastValue - userLastValue) / dtoLastValue) * 100;
            if (Math.abs(changePerc) > userChange) {
                if (properties.isShowDescriptions()) {
                    dto.setDescription("Trigger " + String.format("%.2f%% > %.2f%%", changePerc, userChange));
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkCurrentValue(Double userMinAmount, Double dtoAmount, DtoI dto) {
        if (userMinAmount != null
            && userMinAmount != 0.0
            && dtoAmount != null
            && userMinAmount < dtoAmount) {
            if (properties.isShowDescriptions()) {
                dto.setDescription("Trigger " + String.format("%.2f > %.2f", dtoAmount, userMinAmount));
            }
            return true;
        }
        return false;
    }

}
