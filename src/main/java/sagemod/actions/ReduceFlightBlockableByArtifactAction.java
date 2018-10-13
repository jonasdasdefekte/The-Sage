package sagemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import sagemod.powers.SageFlight;

public class ReduceFlightBlockableByArtifactAction extends AbstractGameAction {

	private int amount;

	public ReduceFlightBlockableByArtifactAction(int amount, AbstractPlayer p) {
		this.amount = amount;
		target = p;
		duration = Settings.ACTION_DUR_XFAST;
		actionType = AbstractGameAction.ActionType.SPECIAL;
	}

	@Override
	public void update() {
		if (target.hasPower(ArtifactPower.POWER_ID)) {
			CardCrawlGame.sound.play("NULLIFY_SFX");
			target.getPower(ArtifactPower.POWER_ID).flashWithoutSound();
			target.getPower(ArtifactPower.POWER_ID).onSpecificTrigger();
		} else {
			AbstractDungeon.actionManager
					.addToBottom(new ReducePowerAction(target, target, SageFlight.POWER_ID, amount));
		}
		isDone = true;
	}

}
