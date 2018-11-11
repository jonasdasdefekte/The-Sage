package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class VigorousBodyPower extends AbstractSagePower {

	public static final String POWER_ID = "Vigorous_Body";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private boolean upgraded;

	public VigorousBodyPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
	}

	@Override
	public void updateDescription() {
		String firstPart = upgraded ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
		description = firstPart + amount + DESCRIPTIONS[2];
	}

	public void onArtifactTriggered(ArtifactPower power) {
		if (upgraded || power.owner.isPlayer) {
			AbstractDungeon.actionManager.addToBottom(
					new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
							new StrengthPower(AbstractDungeon.player, amount), amount));
			flash();
		}
	}

	public void upgrade() {
		upgraded = true;
		updateDescription();
	}


}
