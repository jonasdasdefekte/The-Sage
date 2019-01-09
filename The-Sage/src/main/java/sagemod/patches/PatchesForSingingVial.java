package sagemod.patches;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import basemod.ReflectionHacks;
import sagemod.relics.SingingVial;
import sagemod.ui.SingingVialButton;

public class PatchesForSingingVial {

	@SpirePatch(clz = CardRewardScreen.class, method = SpirePatch.CLASS)
	public static class ButtonField {
		public static SpireField<SingingVialButton> button =
				new SpireField<>(SingingVialButton::new);
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "open")
	public static class OpenPatch {
		@SpireInsertPatch(rlocs = 13)
		public static void Insert(CardRewardScreen screen, ArrayList<AbstractCard> cards,
				RewardItem rItem, String header) {
			if (AbstractDungeon.player.hasRelic(SingingVial.ID)
					&& !SingingVialButton.possiblePotionUpgrades().isEmpty()) {
				ButtonField.button.get(screen).show(rItem);
			} else {
				ButtonField.button.get(screen).hide();
			}
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "open")
	public static class OpenTwitchHidePatch {
		@SpireInsertPatch(rlocs = 41)
		public static void Insert(CardRewardScreen screen, ArrayList<AbstractCard> cards,
				RewardItem rItem, String header) {
			ButtonField.button.get(screen).hide();
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "discoveryOpen", paramtypez = {})
	public static class DiscoveryOpenHidePatch1 {
		@SpireInsertPatch(rlocs = 8)
		public static void Insert(CardRewardScreen screen) {
			ButtonField.button.get(screen).hide();
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "discoveryOpen",
			paramtypez = {CardType.class})
	public static class DiscoveryOpenHidePatch2 {
		@SpireInsertPatch(rlocs = 8)
		public static void Insert(CardRewardScreen screen, CardType type) {
			ButtonField.button.get(screen).hide();
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "codexOpen")
	public static class CodexOpenHidePatch {
		@SpireInsertPatch(rlocs = 8)
		public static void Insert(CardRewardScreen screen) {
			ButtonField.button.get(screen).hide();
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "draftOpen")
	public static class DraftOpenHidePatch {
		@SpireInsertPatch(rlocs = 8)
		public static void Insert(CardRewardScreen screen) {
			ButtonField.button.get(screen).hide();
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "reopen")
	public static class ReopenPatch {
		@SpireInsertPatch(rloc = 21)
		public static void Insert(CardRewardScreen screen) {
			boolean draft =
					(boolean) ReflectionHacks.getPrivate(screen, CardRewardScreen.class, "draft");
			boolean codex =
					(boolean) ReflectionHacks.getPrivate(screen, CardRewardScreen.class, "codex");
			boolean discovery =
					(boolean) ReflectionHacks.getPrivate(screen, CardRewardScreen.class,
							"discovery");
			if (draft || codex || discovery) {
				ButtonField.button.get(screen).hide();
			} else if (AbstractDungeon.player.hasRelic(SingingVial.ID)
					&& !SingingVialButton.possiblePotionUpgrades().isEmpty()) {
				ButtonField.button.get(screen).show(screen.rItem);
			} else {
				ButtonField.button.get(screen).hide();
			}
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "update")
	public static class UpdatePatch {
		@SpireInsertPatch(rloc = 2)
		public static void Insert(CardRewardScreen screen) {
			ButtonField.button.get(screen).update();
		}
	}
	@SpirePatch(clz = CardRewardScreen.class, method = "render")
	public static class RenderPatch {
		@SpireInsertPatch(rloc = 2)
		public static void Insert(CardRewardScreen screen, SpriteBatch sb) {
			ButtonField.button.get(screen).render(sb);
		}
	}
}
