package pro.belbix.tgnotifier.utils;

public class ConstantsCommands {

  public static final String WELCOME_MESSAGE =
      "Welcome! By default you are not subscribed to any events. "
          + "Select an option from the menu below.";
  public final static String PERCENT_VALUE_CALLBACK = "Now send percent value";
  public final static String VALUE_CALLBACK = "Now send correct number value";
  public final static String TOKEN_CALLBACK = "Now send correct token name";
  public final static String ADDRESS_CALLBACK = "Now send correct hash for address";
  public final static String CONFIRM_CALLBACK = "Are you sure? Type `yes` to confirm";

  public final static String START = "/start";

  public final static String INFO = "⚙ Show Settings";
  public final static String INFO_DESC = "Show your settings";

  public final static String FARM_NOTIFICATIONS = "🌱   FARM Price Notifications";
  public final static String TVL_NOTIFICATIONS = "💰   TVL Change Notifications";
  public final static String STRATEGY_NOTIFICATIONS = "🗳   Strategy Change Notifications";
  public final static String PS_NOTIFICATIONS = "🚜   Profit Share Notifications";

  public final static String FARM_CHANGE = "FARM Price Change %";
  public final static String FARM_CHANGE_DESC = "FARM price change notification, in %";

  public final static String FARM_MIN = "FARM Txs in USD";
  public final static String FARM_MIN_DESC =
      "You will receive notifications about Uniswap FARM/USDC transactions "
          + "including more FARM than this value";

  public final static String TVL_CHANGE = "TVL Change in %";
  public final static String TVL_CHANGE_DESC = "All TVL change notification, in %";

  public final static String TVL_MIN = "TVL Txs in USD";
  public final static String TVL_MIN_DESC = "You will receive notifications including changes "
      + "in TVL USD more than this value";

  public final static String PS_APR_CHANGE = "PS APR Change in %";
  public final static String PS_APR_CHANGE_DESC = "Profit Share APR change notification, in %";

  public final static String HARD_WORK_MIN = "HardWork Change in USD";
  public final static String HARD_WORK_MIN_DESC =
      "You will receive notifications about doHardWork calls "
          + "earned more USD than this value";

  public final static String SUBSCRIBE_ON_ADDRESS = "Address Events";
  public final static String SUBSCRIBE_ON_DESC = "Subscribe to all events related to this address";

  public final static String STRATEGY_CHANGE = "Strategy Change";
  public final static String STRATEGY_CHANGE_DESC = "Receive notifications about Vault strategy changes";

  public final static String STRATEGY_ANNOUNCE = "Strategy Announce";
  public final static String STRATEGY_ANNOUNCE_DESC = "Receive notifications about Vault strategy announces";

  public final static String TOKEN_MINT = "Token Mint";
  public final static String TOKEN_MINT_DESC =
      "Receive notifications about FARM token (new emissions) "
          + "minted more than set value";

  public final static String TOKEN_PRICE_SUBSCRIBE = "🪙   Token Price Subscribe";
  public final static String TOKEN_PRICE_SUBSCRIBE_DESC = "Receive notifications about a token price change";

  public final static String TOKEN_PRICE_SUBSCRIBE_CHANGE = "Token Price Change Subscribe";
  public final static String TOKEN_PRICE_SUBSCRIBE_CHANGE_DESC = "Percentage to receive notifications on price change";

  public final static String HELP_TEXT = "Select an entry from the menu below.";
  public final static String UNKNOWN_COMMAND = "Incorrect or unknown command.";

  public final static String TEXT_SELECT_OPTION="Select Option";

  public final static String[] COMMANDS = new String[]{START, INFO, FARM_NOTIFICATIONS,
      TVL_NOTIFICATIONS,
      STRATEGY_NOTIFICATIONS, PS_NOTIFICATIONS, FARM_CHANGE, FARM_MIN, TVL_CHANGE, TVL_MIN,
      PS_APR_CHANGE,
      HARD_WORK_MIN, SUBSCRIBE_ON_ADDRESS, STRATEGY_CHANGE, STRATEGY_ANNOUNCE, TOKEN_MINT,
      TOKEN_PRICE_SUBSCRIBE,
      TOKEN_PRICE_SUBSCRIBE_CHANGE};

  public final static String FARM_CHANGE_TEXT =FARM_CHANGE_DESC + "\n" + PERCENT_VALUE_CALLBACK ;
  public final static String FARM_MIN_TEXT =FARM_MIN_DESC + "\n" + VALUE_CALLBACK;
  public final static String TVL_CHANGE_TEXT =TVL_CHANGE_DESC + "\n" + PERCENT_VALUE_CALLBACK;
  public final static String TVL_MIN_TEXT =TVL_MIN_DESC + "\n" + VALUE_CALLBACK;
  public final static String PS_APR_CHANGE_TEXT =PS_APR_CHANGE_DESC + "\n" + PERCENT_VALUE_CALLBACK;
  public final static String HARD_WORK_MIN_TEXT =HARD_WORK_MIN_DESC + "\n" + VALUE_CALLBACK;
  public final static String SUBSCRIBE_ON_ADDRESS_TEXT =SUBSCRIBE_ON_ADDRESS + "\n" + ADDRESS_CALLBACK;
  public final static String STRATEGY_CHANGE_TEXT =STRATEGY_CHANGE_DESC + "\n" + CONFIRM_CALLBACK;
  public final static String STRATEGY_ANNOUNCE_TEXT =STRATEGY_ANNOUNCE_DESC + "\n" + CONFIRM_CALLBACK;
  public final static String TOKEN_MINT_TEXT =TOKEN_MINT_DESC + "\n" + VALUE_CALLBACK;
  public final static String TOKEN_PRICE_SUBSCRIBE_TEXT =TOKEN_PRICE_SUBSCRIBE + "\n" + TOKEN_CALLBACK;
  public final static String TOKEN_PRICE_SUBSCRIBE_CHANGE_TEXT =TOKEN_PRICE_SUBSCRIBE_CHANGE_DESC + "\n" + PERCENT_VALUE_CALLBACK;
}
