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

    public CheckResult checkAndUpdate(UserEntity user, DtoI dto) {
        CheckResult result = new CheckResult();
        if (dto instanceof UniswapDTO) {
            checkUniswapDto(user, (UniswapDTO) dto, result);
        } else if (dto instanceof HarvestDTO) {
            checkHarvestDto(user, (HarvestDTO) dto, result);
        } else if (dto instanceof HardWorkDTO) {
            checkHardWorkDto(user, (HardWorkDTO) dto, result);
        }
        return result;
    }

    private void checkUniswapDto(UserEntity user, UniswapDTO dto, CheckResult checkResult) {
        if ("FARM".equals(dto.getCoin())) {
            if (user.getLastFarm() == null) {
                user.setLastFarm(dto.getLastPrice());
                dbService.save(user);
            }
            //sorted by priority
            checkCurrentValue(user.getMinFarmAmount(), dto.getAmount(), dto, checkResult);
            checkPricePercentChange(user.getLastFarm(), user.getFarmChange(), dto.getLastPrice(), dto, checkResult);
        }
    }

    private void checkHarvestDto(UserEntity user, HarvestDTO dto, CheckResult checkResult) {
        if (user.getLastTvl() == null) {
            user.setLastTvl(dto.getLastAllUsdTvl());
            dbService.save(user);
        }
        //sorted by priority
        checkCurrentValue(user.getMinTvlAmount(), dto.getUsdAmount(), dto, checkResult);
        checkPricePercentChange(user.getLastTvl(), user.getTvlChange(), dto.getLastAllUsdTvl(), dto, checkResult);
    }

    private void checkHardWorkDto(UserEntity user, HardWorkDTO dto, CheckResult checkResult) {
        if (user.getLastHardWork() == null) {
            user.setLastHardWork(dto.getPsApr());
            dbService.save(user);
        }
        //sorted by priority
        checkCurrentValue(user.getMinHardWorkAmount(), dto.getShareChangeUsd(), dto, checkResult);
        checkPricePercentChange(user.getLastHardWork(), user.getHardWorkChange(), dto.getPsApr(), dto, checkResult);
    }

    private void checkPricePercentChange(Double userLastValue, Double userChange, Double dtoLastValue, DtoI dto,
                                         CheckResult checkResult) {
        if (userLastValue == null) {
            return;
        }
        if (userChange != null &&
            userChange != 0.0 &&
            dtoLastValue != null) {
            double changePercent = ((dtoLastValue - userLastValue) / dtoLastValue) * 100;
            if (Math.abs(changePercent) > userChange) {
                if (properties.isShowDescriptions()) {
                    dto.setDescription("Trigger " + String.format("%.2f%% > %.2f%%", changePercent, userChange));
                }
                checkResult.setSuccess(true);
                checkResult.setMessage(dto.printValueChanged(changePercent));
            }
        }
    }

    private void checkCurrentValue(Double userMinAmount, Double dtoAmount, DtoI dto, CheckResult checkResult) {
        if (userMinAmount != null
            && userMinAmount != 0.0
            && dtoAmount != null
            && userMinAmount < dtoAmount) {
            if (properties.isShowDescriptions()) {
                dto.setDescription("Trigger " + String.format("%.2f > %.2f", dtoAmount, userMinAmount));
            }
            checkResult.setSuccess(true);
            checkResult.setMessage(dto.print());
        }
    }

}
