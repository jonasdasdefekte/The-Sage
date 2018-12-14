package sagemod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.FirePotion;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sagemod.cards.OnFire;

public class OnFirePower extends AbstractSagePower {

	public static final String POWER_ID = "On_Fire";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private int cardsPlayed;

	public OnFirePower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
	}


	@Override
	public void atStartOfTurn() {
		flash();
		cardsPlayed = 0;
		amount = OnFire.CARDS_NEEDED;
		updateDescription();
	}

	@Override
	public void onAfterCardPlayed(AbstractCard usedCard) {
		cardsPlayed++;
		amount = OnFire.CARDS_NEEDED - cardsPlayed;
		if (cardsPlayed == OnFire.CARDS_NEEDED) {
			// Brew potion immediately
			Brew.addPotion(0, new FirePotion(), owner);
			flash();
		}
		if (amount <= 0) {
			amount = OnFire.CARDS_NEEDED;
			cardsPlayed = 0;
		}
		updateDescription();
	}

	@Override
	public void stackPower(int stackAmount) {
		// do not stack
	}

	@Override
	public void reducePower(int reduceAmount) {
		// do not reduce
	}

	@Override
	public void updateDescription() {
		if (amount > 1) {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
		} else {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
		}
	}

}
