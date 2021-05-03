package pro.belbix.tgnotifier.tg;

import static pro.belbix.tgnotifier.tg.ButtonsCreater.createInlineButtons;

import static pro.belbix.tgnotifier.utils.ConstantsCommands.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import pro.belbix.tgnotifier.db.entity.TokenWatchEntity;
import pro.belbix.tgnotifier.db.entity.UserEntity;
import pro.belbix.tgnotifier.tg.model.UserResponse;

public class Commands {

  public static UserResponse responseForCommand(String command) {
    if (command == null) {
      return new UserResponse(UNKNOWN_COMMAND, null);
    }

    switch (command) {
      case START:
        return new UserResponse(WELCOME_MESSAGE, null, true);
      case FARM_NOTIFICATIONS:
      case PS_NOTIFICATIONS:
      case STRATEGY_NOTIFICATIONS:
      case TVL_NOTIFICATIONS:
        return new UserResponse(TEXT_SELECT_OPTION, createInlineButtons(command));
      case FARM_CHANGE:
        return new UserResponse(FARM_CHANGE_TEXT, createInlineButtons(command));
      case FARM_MIN:
        return new UserResponse(FARM_MIN_TEXT, createInlineButtons(command));
      case TVL_CHANGE:
        return new UserResponse(TVL_CHANGE_TEXT, createInlineButtons(command));
      case TVL_MIN:
        return new UserResponse(TVL_MIN_TEXT, createInlineButtons(command));
      case PS_APR_CHANGE:
        return new UserResponse(PS_APR_CHANGE_TEXT, createInlineButtons(command));
      case HARD_WORK_MIN:
        return new UserResponse(HARD_WORK_MIN_TEXT, createInlineButtons(command));
      case SUBSCRIBE_ON_ADDRESS:
        return new UserResponse(SUBSCRIBE_ON_ADDRESS_TEXT, null, false);
      case STRATEGY_CHANGE:
        return new UserResponse(STRATEGY_CHANGE_TEXT, createInlineButtons(command));
      case STRATEGY_ANNOUNCE:
        return new UserResponse(STRATEGY_ANNOUNCE_TEXT, createInlineButtons(command));
      case TOKEN_MINT:
        return new UserResponse(TOKEN_MINT_TEXT, createInlineButtons(command));
      case TOKEN_PRICE_SUBSCRIBE:
        return new UserResponse(TOKEN_PRICE_SUBSCRIBE_TEXT, null, false);
      case TOKEN_PRICE_SUBSCRIBE_CHANGE:
        return new UserResponse(TOKEN_PRICE_SUBSCRIBE_CHANGE_TEXT, createInlineButtons(command));
    }
    return new UserResponse(UNKNOWN_COMMAND, null);
  }

  public static String nextCommand(@NotNull String command) {
    if (TOKEN_PRICE_SUBSCRIBE.equals(command)) {
      return TOKEN_PRICE_SUBSCRIBE_CHANGE;
    }

    return null;
  }

  public static boolean fillFieldForCommand(@NotNull String command,
      @NotNull UserEntity userEntity, String text) {
    switch (command) {
      case FARM_CHANGE:
        userEntity.setFarmChange(textToDouble(text));
        break;
      case FARM_MIN:
        userEntity.setMinFarmAmount(textToDouble(text));
        break;
      case TVL_CHANGE:
        userEntity.setTvlChange(textToDouble(text));
        break;
      case TVL_MIN:
        userEntity.setMinTvlAmount(textToDouble(text));
        break;
      case PS_APR_CHANGE:
        userEntity.setHardWorkChange(textToDouble(text));
        break;
      case HARD_WORK_MIN:
        userEntity.setMinHardWorkAmount(textToDouble(text));
        break;
      case SUBSCRIBE_ON_ADDRESS:
        String existAddresses = userEntity.getSubscribedAddress();
        existAddresses = existAddresses == null ? "" : existAddresses + ",";
        String hash = textToHash(text);
        if (existAddresses.contains(hash)) {
          userEntity.setSubscribedAddress(existAddresses.replace(hash + ",", ""));
        } else {
          userEntity.setSubscribedAddress(existAddresses + hash);
        }
        break;
      case STRATEGY_CHANGE:
        userEntity.setStrategyChange(checkConfirmation(text));
        break;
      case STRATEGY_ANNOUNCE:
        userEntity.setStrategyAnnounce(checkConfirmation(text));
        break;
      case TOKEN_MINT:
        userEntity.setTokenMint(textToDouble(text));
        break;
      case TOKEN_PRICE_SUBSCRIBE:
        userEntity.setSelectedToken(text);
        break;
      case TOKEN_PRICE_SUBSCRIBE_CHANGE:
        insertOrUpdateToken(userEntity, userEntity.getSelectedToken(), textToDouble(text));
        break;
      default:
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
    throw new IllegalStateException(
        "Incorrect value, type `yes` to confirm or type `no` to cancel");
  }

  private static void insertOrUpdateToken(UserEntity userEntity, String tokenName,
      Double change) {
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
