package sagemod.powers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.FirePotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sagemod.potions.UpgradedPotion;
import sagemod.powers.Brew.Potion;

public class OnFirePower extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:On_Fire";
	private static final PowerStrings powerStrings =
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final String NORMAL_NAME = " " + new FirePotion().name;
	private static final String UPGRADED_NAME = " " +
			UpgradedPotion.getUpgradeIfAvailable(new FirePotion()).name;
	public static final int CARDS_NEEDED = 5;

	private static boolean upgradeNext;

	private int cardsPlayed;
	private int normalPotions;
	private int upgradedPotions;

	public OnFirePower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
	}

	public static void upgradeNext() {
		upgradeNext = true;
	}

	@Override
	public void atStartOfTurn() {
		flash();
		cardsPlayed = 0;
		amount = CARDS_NEEDED;
		updateDescription();
	}

	@Override
	public void onAfterCardPlayed(AbstractCard usedCard) {
		cardsPlayed++;
		amount = CARDS_NEEDED - cardsPlayed;
		if (cardsPlayed == CARDS_NEEDED) {
			// Brew potions immediately
			Brew.obtain(potionsCopy());
			flash();
		}
		if (amount <= 0) {
			amount = CARDS_NEEDED;
			cardsPlayed = 0;
		}
		updateDescription();
	}

	private Collection<Potion> potionsCopy() {
		List<Potion> list = new ArrayList<>();
		for (int i = 0; i < upgradedPotions; i++) {
			list.add(new Potion(0, UpgradedPotion.getUpgradeIfAvailable(new FirePotion())));
		}
		for (int j = 0; j < normalPotions; j++) {
			list.add(new Potion(0, new FirePotion()));
		}
		return list;
	}

	@Override
	public void onInitialApplication() {
		super.onInitialApplication();
		stackPower(1);
		amount = CARDS_NEEDED;
		updateDescription();
	}

	@Override
	public void stackPower(int stackAmount) {
		if (upgradeNext) {
			upgradeNext = false;
			upgradedPotions += stackAmount;
		} else {
			normalPotions += stackAmount;
		}
	}

	@Override
	public void reducePower(int reduceAmount) {
		if (normalPotions > 0) {
			if (normalPotions >= reduceAmount) {
				normalPotions = 0;
				reduceAmount -= normalPotions;
			} else {
				normalPotions -= reduceAmount;
				reduceAmount = 0;
			}
		}
		if (reduceAmount > 0) {
			if (upgradedPotions > 0) {
				if (upgradedPotions >= reduceAmount) {
					upgradedPotions = 0;
					reduceAmount -= upgradedPotions;
				} else {
					upgradedPotions -= reduceAmount;
					reduceAmount = 0;
				}
			}
		}
	}

	@Override
	public void updateDescription() {
		StringBuilder builder = new StringBuilder();
		if (amount > 1) {
			builder.append(DESCRIPTIONS[0]);
			builder.append(amount);
			builder.append(DESCRIPTIONS[1]);
		} else {
			builder.append(DESCRIPTIONS[0]);
			builder.append(amount);
			builder.append(DESCRIPTIONS[2]);
		}
		if (normalPotions + upgradedPotions > 1) {
			if (upgradedPotions > 0) {
				builder.append(upgradedPotions);
				if (upgradedPotions == 1) {
					builder.append(UPGRADED_NAME);
				} else {
					builder.append(DESCRIPTIONS[3]);
				}
				if (normalPotions > 0) {
					builder.append(DESCRIPTIONS[4]);
				}
			}
			if (normalPotions > 0) {
				builder.append(normalPotions);
				if (normalPotions == 1) {
					builder.append(NORMAL_NAME);
				} else {
					builder.append(DESCRIPTIONS[5]);
				}
			}
		} else {
			if (upgradedPotions > 0) {
				builder.append(upgradedPotions);
				builder.append(UPGRADED_NAME);
			} else {
				builder.append(normalPotions);
				builder.append(NORMAL_NAME);
			}
		}
		builder.append(DESCRIPTIONS[6]);
		description = builder.toString();
	}
}
