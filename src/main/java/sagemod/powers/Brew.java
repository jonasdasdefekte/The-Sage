package sagemod.powers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import sagemod.SageMod;

public class Brew extends AbstractSagePower {

	public static final String POWER_ID = "Brew";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final String CANNOT_BREW_SOZU = DESCRIPTIONS[9];
	private static final String BREWED_A_POTION = DESCRIPTIONS[10];
	private static final String BREWED_SOME_POTIONS = DESCRIPTIONS[11];

	public static boolean isBrewRewards;

	private ArrayList<Potion> potions;

	public Brew(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		potions = new ArrayList<>();
	}

	@Override
	public void atStartOfTurn() {
		ArrayList<Potion> toRemove = new ArrayList<>();
		removePotionsFromRewards();
		potions.forEach(p -> {
			if (p.turns-- <= 1) {
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
					builder.append(DESCRIPTIONS[6]);
				} else {
					builder.append(DESCRIPTIONS[7]);
				}
				if (i != potions.size() - 1) {
					// new line
					builder.append(DESCRIPTIONS[8]);
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
					builder.append(DESCRIPTIONS[8]);
				}
			}
		}
		description = builder.toString();
	}

	private static void obtain(Collection<Potion> collection) {
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
		}
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

	static class Potion implements Comparable<Potion> {

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
		for (Potion p : potions) {
			float yPos = Y - p.turns * p.potion.hb.height * 0.75f;
			if (lastTurns != p.turns) {
				xOffset = 1;
				super.renderIcons(sb, X, yPos, c);
				SageMod.brewFont.draw(sb, String.valueOf(p.turns), X, yPos);
			}
			float xPos = X + xOffset * p.potion.hb.width * 0.75f;
			p.potion.scale = Settings.scale * 0.75f;
			p.potion.move(xPos, yPos);
			p.potion.render(sb);
			xOffset++;
			lastTurns = p.turns;
		}

	}

}
