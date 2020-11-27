package pro.belbix.tgnotifier.tg;

import javax.validation.constraints.NotNull;
import pro.belbix.tgnotifier.db.entity.UserEntity;

public class Commands {

    public static final String WELCOME_MESSAGE = "Welcome! By default you are not subscribed to any events. "
        + "Use /help to get commands";
    public final static String PERCENT_VALUE_CALLBACK = "Now set percent value";
    public final static String VALUE_CALLBACK = "Now set correct number value";

    public final static String INFO = "/info";
    public final static String INFO_DESC = "Show your settings";

    public final static String FARM_CHANGE = "/farm_change";
    public final static String FARM_CHANGE_DESC = "FARM price change notification, in %";

    public final static String FARM_MIN = "/farm_min";
    public final static String FARM_MIN_DESC = "You will receive notifications about Uniswap FARM/USDC transactions "
        + "including more FARM than this value";

    public final static String TVL_CHANGE = "/tvl_change";
    public final static String TVL_CHANGE_DESC = "All TVL change notification, in %";

    public final static String TVL_MIN = "/tvl_min";
    public final static String TVL_MIN_DESC = "You will receive notifications including changes "
        + "in TVL USD more than this value";

    public final static String PS_APR_CHANGE = "/ps_apr_change";
    public final static String PS_APR_CHANGE_DESC = "Profit Share APR change notification, in %";

    public final static String HARD_WORK_MIN = "/hard_work_min";
    public final static String HARD_WORK_MIN_DESC = "You will receive notifications about doHardWork calls "
        + "earned more USD than this value";

    public final static String HELP = "/help";
    public final static String HELP_TEXT = "Available commands:\n"
        + INFO + " - " + INFO_DESC + "\n"
        + FARM_CHANGE + " - " + FARM_CHANGE_DESC + "\n"
        + FARM_MIN + " - " + FARM_MIN_DESC + "\n"
        + TVL_CHANGE + " - " + TVL_CHANGE_DESC + "\n"
        + TVL_MIN + " - " + TVL_MIN_DESC + "\n"
        + PS_APR_CHANGE + " - " + PS_APR_CHANGE_DESC + "\n"
        + HARD_WORK_MIN + " - " + HARD_WORK_MIN_DESC + "\n"
        + "Set value to 0 for unsubscribe.";
    public final static String UNKNOWN_COMMAND = "Incorrect or unknown command. Use " + HELP;

    public static String responseForCommand(String command) {
        if (command == null) {
            return UNKNOWN_COMMAND;
        }
        switch (command) {
            case FARM_CHANGE:
                return FARM_CHANGE_DESC + "\n" + PERCENT_VALUE_CALLBACK;
            case FARM_MIN:
                return FARM_MIN_DESC + "\n" + VALUE_CALLBACK;
            case TVL_CHANGE:
                return TVL_CHANGE_DESC + "\n" + PERCENT_VALUE_CALLBACK;
            case TVL_MIN:
                return TVL_MIN_DESC + "\n" + VALUE_CALLBACK;
            case PS_APR_CHANGE:
                return PS_APR_CHANGE_DESC + "\n" + PERCENT_VALUE_CALLBACK;
            case HARD_WORK_MIN:
                return HARD_WORK_MIN_DESC + "\n" + VALUE_CALLBACK;
        }
        return UNKNOWN_COMMAND;
    }

    public static boolean fillFieldForCommand(@NotNull String command, @NotNull UserEntity userEntity, double value) {
        if (FARM_CHANGE.equals(command)) {
            userEntity.setFarmChange(value);
        } else if (FARM_MIN.equals(command)) {
            userEntity.setMinFarmAmount(value);
        } else if (TVL_CHANGE.equals(command)) {
            userEntity.setTvlChange(value);
        } else if (TVL_MIN.equals(command)) {
            userEntity.setMinTvlAmount(value);
        } else if (PS_APR_CHANGE.equals(command)) {
            userEntity.setHardWorkChange(value);
        } else if (HARD_WORK_MIN.equals(command)) {
            userEntity.setMinHardWorkAmount(value);
        } else {
            return false;
        }
        return true;
    }
}
