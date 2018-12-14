package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BookwormPower extends AbstractSagePower {

	public static final String POWER_ID = "Bookworm";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public BookwormPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
	}

	@Override
	public void updateDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(DESCRIPTIONS[0]);
		for (int i = 0; i < amount; ++i) {
			sb.append("[E] ");
		}
		sb.append(DESCRIPTIONS[1]);
		description = sb.toString();
	}

	@Override
	public void onAfterCardPlayed(AbstractCard usedCard) {
		if (usedCard != null && usedCard.cost == -1) {
			AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(amount));
			flash();
		}
	}

}
