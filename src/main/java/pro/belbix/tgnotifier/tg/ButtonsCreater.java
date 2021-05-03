package pro.belbix.tgnotifier.tg;

import static pro.belbix.tgnotifier.utils.ConstantsCommands.FARM_CHANGE;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.FARM_MIN;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.FARM_NOTIFICATIONS;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.HARD_WORK_MIN;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.PS_APR_CHANGE;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.PS_NOTIFICATIONS;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.STRATEGY_ANNOUNCE;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.STRATEGY_CHANGE;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.STRATEGY_NOTIFICATIONS;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.TOKEN_MINT;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.TOKEN_PRICE_SUBSCRIBE_CHANGE;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.TVL_CHANGE;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.TVL_MIN;
import static pro.belbix.tgnotifier.utils.ConstantsCommands.TVL_NOTIFICATIONS;

import pro.belbix.tgnotifier.tg.model.InlineButton;

public class ButtonsCreater {

  public static InlineButton[] createInlineButtons(String button) {
    switch (button) {
      case FARM_NOTIFICATIONS:
        return new InlineButton[]{
            new InlineButton(FARM_CHANGE, FARM_CHANGE),
            new InlineButton(FARM_MIN, FARM_MIN)
        };
      case TVL_NOTIFICATIONS:
        return new InlineButton[]{
            new InlineButton(TVL_CHANGE, TVL_CHANGE),
            new InlineButton(TVL_MIN, TVL_MIN)
        };
      case STRATEGY_NOTIFICATIONS:
        return new InlineButton[]{
            new InlineButton(STRATEGY_CHANGE, STRATEGY_CHANGE),
            new InlineButton(STRATEGY_ANNOUNCE, STRATEGY_ANNOUNCE)
        };
      case PS_NOTIFICATIONS:
        return new InlineButton[]{
            new InlineButton(PS_APR_CHANGE, PS_APR_CHANGE),
            new InlineButton(HARD_WORK_MIN, HARD_WORK_MIN)
        };
      case FARM_CHANGE:
      case PS_APR_CHANGE:
      case TVL_CHANGE:
      case TOKEN_PRICE_SUBSCRIBE_CHANGE:
        return new InlineButton[]{
            new InlineButton("10", "10"),
            new InlineButton("Cancel", "0")
        };
      case FARM_MIN:
      case TOKEN_MINT:
        return new InlineButton[]{
            new InlineButton("1000", "1000"),
            new InlineButton("Cancel", "0")
        };
      case TVL_MIN:
      case HARD_WORK_MIN:
        return new InlineButton[]{
            new InlineButton("10000", "10000"),
            new InlineButton("Cancel", "0")
        };
      case STRATEGY_CHANGE:
      case STRATEGY_ANNOUNCE:
        return new InlineButton[]{
            new InlineButton("Yes", "Yes"),
            new InlineButton("No", "No"),
            new InlineButton("Cancel", "0")
        };

    }
    return null;
  }
}
