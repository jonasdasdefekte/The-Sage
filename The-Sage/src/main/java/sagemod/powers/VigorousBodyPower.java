package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class VigorousBodyPower extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:Vigorous_Body";
	private static final PowerStrings powerStrings =
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private int upgradedAmount;
	private int unupgradedAmount;

	public VigorousBodyPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
	}

	@Override
	public void updateDescription() {
		StringBuilder builder = new StringBuilder();
		builder.append(DESCRIPTIONS[0]);
		builder.append(unupgradedAmount + upgradedAmount);
		builder.append(DESCRIPTIONS[2]);
		if (upgradedAmount > 0) {
			builder.append(DESCRIPTIONS[3]);
			builder.append(DESCRIPTIONS[1]);
			builder.append(upgradedAmount);
			builder.append(DESCRIPTIONS[2]);
		}
		description = builder.toString();
	}

	public void onArtifactTriggered(ArtifactPower power) {
		int strength = 0;
		if (power.owner.isPlayer) {
			strength += unupgradedAmount;
		}
		strength += upgradedAmount;
		if (strength > 0) {
			AbstractDungeon.actionManager.addToBottom(
					new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
							new StrengthPower(AbstractDungeon.player, strength), strength));
			flash();
		}
	}

	public void upgrade(int upgradedAmount) {
		this.upgradedAmount += upgradedAmount;
		amount = Math.max(upgradedAmount, unupgradedAmount);
		updateDescription();
	}

	public void increaseUnupgraded(int by) {
		unupgradedAmount += by;
		amount = Math.max(upgradedAmount, unupgradedAmount);
		updateDescription();
	}

}
