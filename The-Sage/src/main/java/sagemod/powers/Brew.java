package sagemod.powers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.FtueTip.TipType;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import sagemod.SageMod;
import sagemod.actions.ExecuteLaterAction;
import sagemod.tips.SageTipTracker;

public class Brew extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:Brew";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	private static final TutorialStrings tutStrings =
			CardCrawlGame.languagePack.getTutorialString(SageTipTracker.OVER_BREW);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final String CANNOT_BREW_SOZU = DESCRIPTIONS[10];
	private static final String BREWED_A_POTION = DESCRIPTIONS[11];
	private static final String BREWED_SOME_POTIONS = DESCRIPTIONS[12];

	public static boolean isBrewRewards;

	private ArrayList<Potion> potions;

	public Brew(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		potions = new ArrayList<>();
	}

	@Override
	public void atStartOfTurn() {
		advancePotionQueue(1);
	}

	public void advancePotionQueue(int turns) {
		ArrayList<Potion> toRemove = new ArrayList<>();
		removePotionsFromRewards();
		potions.forEach(p -> {
			p.turns -= turns;
			if (p.turns < 1) {
				toRemove.add(p);
			}
		});
		if (!toRemove.isEmpty()) {
			obtain(toRemove);
		}
		potions.removeAll(toRemove);
		Collections.sort(potions);
		if (potions.isEmpty()) {
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
		} else {
			amount = potions.get(0).turns;
		}
		updateDescription();
		AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(Brew::checkTip));
	}

	public static void removePotionsFromRewards() {
		AbstractDungeon.getCurrRoom().rewards.removeIf(i -> i.type == RewardType.POTION);
		AbstractDungeon.combatRewardScreen.rewards.removeIf(i -> i.type == RewardType.POTION);
	}

	@Override
	public void updateDescription() {
		StringBuilder builder = new StringBuilder();
		// elaborate view
		if (potions.size() <= 3) {
			for (int i = 0; i < potions.size(); i++) {
				Potion p = potions.get(i);
				builder.append(DESCRIPTIONS[0]);
				builder.append(getNameInRed(p.potion.name));
				if (p.turns > 1) {
					builder.append(DESCRIPTIONS[3]);
					builder.append(p.turns);
					builder.append(DESCRIPTIONS[7]);
				} else {
					builder.append(DESCRIPTIONS[8]);
				}
				if (i != potions.size() - 1) {
					// new line
					builder.append(DESCRIPTIONS[9]);
				}
			}
		} else {
			// compact view
			ArrayList<Potion> compactList = getListForCompactView();
			for (int i = 0; i < compactList.size(); i++) {
				Potion p = compactList.get(i);
				builder.append(DESCRIPTIONS[1]);
				builder.append(p.times);
				builder.append(DESCRIPTIONS[2]);
				builder.append(getNameInRed(p.potion.name));
				builder.append(DESCRIPTIONS[4]);
				builder.append(p.turns);
				if (p.turns > 1) {
					builder.append(DESCRIPTIONS[6]);
				} else {
					builder.append(DESCRIPTIONS[5]);
				}
				if (i != compactList.size() - 1) {
					// new line
					builder.append(DESCRIPTIONS[9]);
				}
			}
		}
		description = builder.toString();
	}

	public static void obtain(Collection<Potion> collection) {
		int freeSlots = 0;
		for (AbstractPotion p : AbstractDungeon.player.potions) {
			if (p instanceof PotionSlot) {
				freeSlots++;
			}
		}

		boolean openScreen = freeSlots < collection.size();
		isBrewRewards = openScreen;

		for (Potion p : collection) {
			p.potion.scale = Settings.scale;
			if (openScreen) {
				AbstractDungeon.getCurrRoom().addPotionToRewards(p.potion);
			} else {
				AbstractDungeon.player.obtainPotion(p.potion);
			}
		}
		if (openScreen) {
			String title = collection.size() == 1 ? BREWED_A_POTION : BREWED_SOME_POTIONS;
			AbstractDungeon.combatRewardScreen.open(title);
			AbstractDungeon.combatRewardScreen.rewards.removeIf(i -> i.type != RewardType.POTION);
			AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0;
		}
		if (AbstractDungeon.player.hasPower(Brew.POWER_ID)) {
			AbstractDungeon.player.getPower(Brew.POWER_ID).flash();
		}
		AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(Brew::checkTip));
	}

	private String getNameInRed(String name) {
		return name.replaceAll(" ", " #r");
	}

	private ArrayList<Potion> getListForCompactView() {
		ArrayList<Potion> list = new ArrayList<>();
		for (Potion p : potions) {
			if (list.contains(p)) {
				list.get(list.indexOf(p)).times++;
			} else {
				p.times = 1;
				list.add(p);
			}
		}
		return list;
	}

	public void addPotionToQueue(int turns, AbstractPotion potion) {
		Potion p = new Potion(turns, potion);
		if (turns < amount) {
			amount = turns;
		}
		potions.add(p);
		Collections.sort(potions);
		updateDescription();
	}

	public static void addPotion(int turns, AbstractPotion potion, AbstractCreature owner) {
		if (AbstractDungeon.player.hasRelic(Sozu.ID)) {
			AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX,
					AbstractDungeon.player.dialogY, 2.0f, CANNOT_BREW_SOZU, true));
			AbstractDungeon.player.getRelic(Sozu.ID).flash();
			AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(
					AbstractDungeon.player, AbstractDungeon.player.getRelic(Sozu.ID)));
			return;
		}

		// flash brewing if player has it
		if (AbstractDungeon.player.hasPower(Brewing.POWER_ID)) {
			AbstractDungeon.player.getPower(Brewing.POWER_ID).flash();
		}

		if (turns <= 0) {
			obtain(Arrays.asList(new Potion(turns, potion)));
		} else {
			Brew power = null;
			if (owner.hasPower(POWER_ID)) {
				power = (Brew) owner.getPower(POWER_ID);
				power.addPotionToQueue(turns, potion);
				power.flash();
			} else {
				power = new Brew(owner, turns);
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, power, turns));
				power.addPotionToQueue(turns, potion);
			}

			AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(Brew::checkTip));
		}
	}

	private static void checkTip() {
		if (AbstractDungeon.player.hasPower(Brew.POWER_ID)) {
			Brew power = (Brew) AbstractDungeon.player.getPower(POWER_ID);
			int emptyPotionSlots = emptyPotionSlots();
			int potionsBrewedNext = (int) power.potions.stream().filter(p -> p.turns <= 1).count();
			if (potionsBrewedNext > emptyPotionSlots && AbstractDungeon.player.hasAnyPotions()
					&& !SageTipTracker.hasShown(SageTipTracker.OVER_BREW)) {
				SageTipTracker.neverShowAgain(SageTipTracker.OVER_BREW);
				AbstractDungeon.ftue = new FtueTip(tutStrings.LABEL[0], tutStrings.TEXT[0],
						Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, TipType.COMBAT);
			}
		}
	}

	private static int emptyPotionSlots() {
		int slots = 0;
		for (int i = 0; i < AbstractDungeon.player.potionSlots; i++) {
			if (AbstractDungeon.player.potions.get(i) instanceof PotionSlot) {
				slots++;
			}
		}
		return slots;
	}

	public static void brewAllPotions() {
		removePotionsFromRewards();
		// return if the player does not have potions to brew
		if (!AbstractDungeon.player.hasPower(Brew.POWER_ID)) {
			return;
		}

		Brew power = (Brew) AbstractDungeon.player.getPower(Brew.POWER_ID);

		Brew.obtain(power.potions);

		power.potions.clear();

		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(power.owner, power.owner, power));
	}

	public static class Potion implements Comparable<Potion> {

		int turns;
		AbstractPotion potion;
		int times;

		public Potion(int turns, AbstractPotion potion) {
			this.turns = turns;
			this.potion = potion;
			times = 1;
		}

		@Override
		public int compareTo(Potion o) {
			return turns - o.turns;
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof Potion) {
				Potion otherPotion = (Potion) other;
				boolean samePotion = otherPotion.potion != null && potion != null
						&& otherPotion.potion.ID.equals(potion.ID);
				return samePotion && otherPotion.turns == turns;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return turns;
		}

	}

	private static final float X = 40 * Settings.scale;
	private static final float Y = 800 * Settings.scale;

	@Override
	public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
		super.renderIcons(sb, x, y, c);
		int lastTurns = -1;
		int xOffset = 1;
		int hovered = -1;
		for (Potion p : potions) {
			float size = p.potion.hb.width * 0.75f;
			float width = p.potion.hb.width;
			float halfWidth = width * 0.5f;
			float height = p.potion.hb.height * 0.85f;
			float halfHeight = height * 0.5f;
			float yPos = Y - p.turns * size;
			if (lastTurns != p.turns) {
				xOffset = 1;
				super.renderIcons(sb, X, yPos, c);
				SageMod.brewFont.draw(sb, String.valueOf(p.turns), X, yPos);
				if (isHovered(X - halfWidth, yPos - halfHeight,
						width, height)) {
					hovered = p.turns;
				}
			}
			float xPos = X + xOffset * size;
			p.potion.scale = Settings.scale * 0.75f;
			if (isHovered(xPos - halfWidth, yPos - halfHeight,
					width, height)) {
				hovered = p.turns;
			}
			p.potion.move(xPos, yPos);
			p.potion.renderOutline(sb);
			p.potion.render(sb);
			xOffset++;
			lastTurns = p.turns;
		}

		if (hovered != -1) {
			ArrayList<PowerTip> tips = new ArrayList<>(1);
			String header = DESCRIPTIONS[13] + hovered;
			if (hovered == 1) {
				header += DESCRIPTIONS[5];
			} else {
				header += DESCRIPTIONS[7];
			}
			final int filter = hovered;
			String body = potions.stream()
					.filter(p -> p.turns == filter)
					.map(p -> p.potion.name)
					.collect(Collectors.joining(" NL "));
			tips.add(new PowerTip(header, body));
			renderTip(sb, tips);
		}

	}

	private void renderTip(SpriteBatch sb, ArrayList<PowerTip> tips) {
		if (InputHelper.mX < 1400.0F * Settings.scale) {
			TipHelper.queuePowerTips(InputHelper.mX + 60.0F * Settings.scale,
					InputHelper.mY - 30.0F * Settings.scale,
					tips);
		} else {
			TipHelper.queuePowerTips(InputHelper.mX - 350.0F * Settings.scale,
					InputHelper.mY - 50.0F * Settings.scale,
					tips);
		}
	}

	private boolean isHovered(float x, float y, float width, float height) {
		return ((InputHelper.mX > x) && (InputHelper.mX < x + width) && (InputHelper.mY > y)
				&& (InputHelper.mY < y + height));
	}

}
