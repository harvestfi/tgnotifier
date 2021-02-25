package pro.belbix.tgnotifier.tg;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import pro.belbix.tgnotifier.db.entity.TokenWatchEntity;
import pro.belbix.tgnotifier.db.entity.UserEntity;

public class Commands {

    public static final String WELCOME_MESSAGE = "Welcome! By default you are not subscribed to any events. "
        + "Select an option from the menu below.";
    public final static String PERCENT_VALUE_CALLBACK = "Now send percent value";
    public final static String VALUE_CALLBACK = "Now send correct number value";
    public final static String TOKEN_CALLBACK = "Now send correct token name";
    public final static String ADDRESS_CALLBACK = "Now send correct hash for address";
    public final static String CONFIRM_CALLBACK = "Are you sure? Type `yes` to confirm";

    public final static String INFO = "⚙ Show Settings";
    public final static String INFO_DESC = "Show your settings";

    public final static String FARM_NOTIFICATIONS = "🌱   FARM Price Notifications";
    public final static String TVL_NOTIFICATIONS = "💰   TVL Change Notifications";
    public final static String STRATEGY_NOTIFICATIONS = "🗳   Strategy Change Notifications";
    public final static String PS_NOTIFICATIONS = "🚜   Profit Share Notifications";

    public final static String FARM_CHANGE = "FARM Price Change %";
    public final static String FARM_CHANGE_DESC = "FARM price change notification, in %";

    public final static String FARM_MIN = "FARM Txs in USD";
    public final static String FARM_MIN_DESC = "You will receive notifications about Uniswap FARM/USDC transactions "
        + "including more FARM than this value";

    public final static String TVL_CHANGE = "TVL Change in %";
    public final static String TVL_CHANGE_DESC = "All TVL change notification, in %";

    public final static String TVL_MIN = "TVL Txs in USD";
    public final static String TVL_MIN_DESC = "You will receive notifications including changes "
        + "in TVL USD more than this value";

    public final static String PS_APR_CHANGE = "PS APR Change in %";
    public final static String PS_APR_CHANGE_DESC = "Profit Share APR change notification, in %";

    public final static String HARD_WORK_MIN = "HardWork Change in USD";
    public final static String HARD_WORK_MIN_DESC = "You will receive notifications about doHardWork calls "
        + "earned more USD than this value";

    public final static String SUBSCRIBE_ON_ADDRESS = "Address Events";
    public final static String SUBSCRIBE_ON_DESC = "Subscribe to all events related to this address";

    public final static String STRATEGY_CHANGE = "Strategy Change";
    public final static String STRATEGY_CHANGE_DESC = "Receive notifications about Vault strategy changes";

    public final static String STRATEGY_ANNOUNCE = "Strategy Announce";
    public final static String STRATEGY_ANNOUNCE_DESC = "Receive notifications about Vault strategy announces";

    public final static String TOKEN_MINT = "Token Mint";
    public final static String TOKEN_MINT_DESC = "Receive notifications about FARM token (new emissions) "
        + "minted more than set value";

    public final static String TOKEN_PRICE_SUBSCRIBE = "🪙   Token Price Subscribe";
    public final static String TOKEN_PRICE_SUBSCRIBE_DESC = "Receive notifications about a token price change";

    public final static String TOKEN_PRICE_SUBSCRIBE_CHANGE = "Token Price Change Subscribe";
    public final static String TOKEN_PRICE_SUBSCRIBE_CHANGE_DESC = "Percentage to receive notifications on price change";

    public final static String HELP_TEXT = "Select an entry from the menu below.";
    public final static String UNKNOWN_COMMAND = "Incorrect or unknown command.";

    public final static String[] COMMANDS = new String[]{INFO, FARM_NOTIFICATIONS, TVL_NOTIFICATIONS,
        STRATEGY_NOTIFICATIONS, PS_NOTIFICATIONS, FARM_CHANGE, FARM_MIN, TVL_CHANGE, TVL_MIN, PS_APR_CHANGE,
        HARD_WORK_MIN, SUBSCRIBE_ON_ADDRESS, STRATEGY_CHANGE, STRATEGY_ANNOUNCE, TOKEN_MINT, TOKEN_PRICE_SUBSCRIBE,
        TOKEN_PRICE_SUBSCRIBE_CHANGE};

    public static UserResponse responseForCommand(String command) {
        if (command == null) {
            return new UserResponse(UNKNOWN_COMMAND, null);
        }
        switch (command) {
            case FARM_NOTIFICATIONS:
                InlineButton[] buttonsFarmNotifications = {
                    new InlineButton(FARM_CHANGE, FARM_CHANGE),
                    new InlineButton(FARM_MIN, FARM_MIN)
                };
                return new UserResponse("Select Option", buttonsFarmNotifications);
            case TVL_NOTIFICATIONS:
                InlineButton[] buttonsTvlNotifications = {
                    new InlineButton(TVL_CHANGE, TVL_CHANGE),
                    new InlineButton(TVL_MIN, TVL_MIN)
                };
                return new UserResponse("Select Option", buttonsTvlNotifications);
            case STRATEGY_NOTIFICATIONS:
                InlineButton[] buttonsStrategyNotifications = {
                    new InlineButton(STRATEGY_CHANGE, STRATEGY_CHANGE),
                    new InlineButton(STRATEGY_ANNOUNCE, STRATEGY_ANNOUNCE)
                };
                return new UserResponse("Select Option", buttonsStrategyNotifications);
            case PS_NOTIFICATIONS:
                InlineButton[] buttonsPSNotifications = {
                    new InlineButton(PS_APR_CHANGE, PS_APR_CHANGE),
                    new InlineButton(HARD_WORK_MIN, HARD_WORK_MIN)
                };
                return new UserResponse("Select Option", buttonsPSNotifications);
            case FARM_CHANGE:
                InlineButton[] buttonsFarmChange = {
                    new InlineButton("10", "10"),
                    new InlineButton("Cancel", "0")
                };
                return new UserResponse(FARM_CHANGE_DESC + "\n" + PERCENT_VALUE_CALLBACK, buttonsFarmChange);
            case FARM_MIN:
                InlineButton[] buttonsFarmMin = {
                    new InlineButton("1000", "1000"),
                    new InlineButton("Cancel", "0")
                };
                return new UserResponse(FARM_MIN_DESC + "\n" + VALUE_CALLBACK, buttonsFarmMin);
            case TVL_CHANGE:
                InlineButton[] buttonsTVLChange = {
                    new InlineButton("10", "10"),
                    new InlineButton("Cancel", "0")
                };
                return new UserResponse(TVL_CHANGE_DESC + "\n" + PERCENT_VALUE_CALLBACK, buttonsTVLChange);
            case TVL_MIN:
                InlineButton[] buttonsTVLMin = {
                    new InlineButton("10000", "10000"),
                    new InlineButton("Cancel", "0")
                };
                return new UserResponse(TVL_MIN_DESC + "\n" + VALUE_CALLBACK, buttonsTVLMin);
            case PS_APR_CHANGE:
                InlineButton[] buttonsAPRChange = {
                    new InlineButton("10", "10"),
                    new InlineButton("Cancel", "0")
                };
                return new UserResponse(PS_APR_CHANGE_DESC + "\n" + PERCENT_VALUE_CALLBACK, buttonsAPRChange);
            case HARD_WORK_MIN:
                InlineButton[] buttonsHrdwMin = {
                    new InlineButton("10000", "10000"),
                    new InlineButton("Cancel", "0")
                };
                return new UserResponse(HARD_WORK_MIN_DESC + "\n" + VALUE_CALLBACK, buttonsHrdwMin);
            case SUBSCRIBE_ON_ADDRESS:
                return new UserResponse(SUBSCRIBE_ON_ADDRESS + "\n" + ADDRESS_CALLBACK, null, false);
            case STRATEGY_CHANGE:
                InlineButton[] buttonsStgChange = {
                    new InlineButton("Yes", "Yes"),
                    new InlineButton("No", "No"),
                    new InlineButton("Cancel", "0")
                };
                return new UserResponse(STRATEGY_CHANGE_DESC + "\n" + CONFIRM_CALLBACK, buttonsStgChange);
            case STRATEGY_ANNOUNCE:
                InlineButton[] buttonsStgAnnounce = {
                    new InlineButton("Yes", "Yes"),
                    new InlineButton("No", "No"),
                    new InlineButton("Cancel", "0")
                };
                return new UserResponse(STRATEGY_ANNOUNCE_DESC + "\n" + CONFIRM_CALLBACK, buttonsStgAnnounce);
            case TOKEN_MINT:
                InlineButton[] buttonsTokenMint = {
                    new InlineButton("1000", "1000"),
                    new InlineButton("Cancel", "0")
                };
                return new UserResponse(TOKEN_MINT_DESC + "\n" + VALUE_CALLBACK, buttonsTokenMint);
            case TOKEN_PRICE_SUBSCRIBE:
                return new UserResponse(TOKEN_PRICE_SUBSCRIBE + "\n" + TOKEN_CALLBACK, null, false);
            case TOKEN_PRICE_SUBSCRIBE_CHANGE:
                InlineButton[] buttonsTokenPriceSubscribeChange = {
                    new InlineButton("10", "10"),
                    new InlineButton("Cancel", "0")
                };
                return new UserResponse(TOKEN_PRICE_SUBSCRIBE_CHANGE_DESC + "\n" + PERCENT_VALUE_CALLBACK,
                    buttonsTokenPriceSubscribeChange);
        }
        return new UserResponse(UNKNOWN_COMMAND, null);
    }

    public static String nextCommand(@NotNull String command) {
        switch (command) {
            case TOKEN_PRICE_SUBSCRIBE:
                return TOKEN_PRICE_SUBSCRIBE_CHANGE;
        }

        return null;
    }

    public static boolean fillFieldForCommand(@NotNull String command, @NotNull UserEntity userEntity, String text) {
        if (FARM_CHANGE.equals(command)) {
            userEntity.setFarmChange(textToDouble(text));
        } else if (FARM_MIN.equals(command)) {
            userEntity.setMinFarmAmount(textToDouble(text));
        } else if (TVL_CHANGE.equals(command)) {
            userEntity.setTvlChange(textToDouble(text));
        } else if (TVL_MIN.equals(command)) {
            userEntity.setMinTvlAmount(textToDouble(text));
        } else if (PS_APR_CHANGE.equals(command)) {
            userEntity.setHardWorkChange(textToDouble(text));
        } else if (HARD_WORK_MIN.equals(command)) {
            userEntity.setMinHardWorkAmount(textToDouble(text));
        } else if (SUBSCRIBE_ON_ADDRESS.equals(command)) {
            String existAddresses = userEntity.getSubscribedAddress();
            existAddresses = existAddresses == null ? "" : existAddresses + ",";
            String hash = textToHash(text);
            if (existAddresses.contains(hash)) {
                userEntity.setSubscribedAddress(existAddresses.replace(hash + ",", ""));
            } else {
                userEntity.setSubscribedAddress(existAddresses + hash);
            }
        } else if (STRATEGY_CHANGE.equals(command)) {
            userEntity.setStrategyChange(checkConfirmation(text));
        } else if (STRATEGY_ANNOUNCE.equals(command)) {
            userEntity.setStrategyAnnounce(checkConfirmation(text));
        } else if (TOKEN_MINT.equals(command)) {
            userEntity.setTokenMint(textToDouble(text));
        } else if (TOKEN_PRICE_SUBSCRIBE.equals(command)) {
            userEntity.setSelectedToken(text);
        } else if (TOKEN_PRICE_SUBSCRIBE_CHANGE.equals(command)) {
            insertOrUpdateToken(userEntity, userEntity.getSelectedToken(), textToDouble(text));
        } else {
            return false;
        }
        return true;
    }

    private static double textToDouble(String text) {
        try {
            return Double.parseDouble(text.replaceAll(",", ".")
                .replaceAll("[^0-9.]+", "").trim());
        } catch (Exception e) {
            throw new IllegalStateException("Incorrect value");
        }
    }

    private static String textToHash(String text) {
        try {
            text = text.trim();
            if (text.startsWith("0x")) {
                return text;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Incorrect value");
        }
        throw new IllegalStateException("Incorrect value, not a hash");
    }

    private static boolean checkConfirmation(String text) {
        text = text.trim();
        List<String> confirm = Arrays.asList("yes", "y", "true", "1");
        List<String> deny = Arrays.asList("no", "n", "false", "0");

        if (confirm.contains(text.toLowerCase())) {
            return true;
        } else if (deny.contains(text.toLowerCase())) {
            return false;
        }
        throw new IllegalStateException("Incorrect value, type `yes` to confirm or type `no` to cancel");
    }

    private static void insertOrUpdateToken(UserEntity userEntity, String tokenName, Double change) {
        if (tokenName == null) {
            throw new IllegalStateException("Token not selected");
        }
        Set<TokenWatchEntity> userTokens = userEntity.getTokenWatch();

        TokenWatchEntity existingToken = userTokens.stream()
            .filter(token -> token.getTokenName().equals(tokenName))
            .findAny()
            .orElse(null);

        if (existingToken != null) {
            existingToken.setPriceChange(change);
        } else {
            TokenWatchEntity newToken = new TokenWatchEntity();
            newToken.setTokenName(tokenName);
            newToken.setPriceChange(change);
            newToken.setUser(userEntity);
            userTokens.add(newToken);
        }
    }
}
