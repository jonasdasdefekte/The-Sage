package sagemod.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sagemod.actions.ExecuteLaterAction;
import sagemod.powers.SageFlight;

public class BalloonAnimal extends AbstractSageRelic {

	public static final String ID = "Balloon_Animal";
	public static final RelicTier TIER = RelicTier.UNCOMMON;
	public static final LandingSound SOUND = LandingSound.MAGICAL;
	private static final int DRAW_AMT = 1;


	public BalloonAnimal() {
		super(ID, TIER, SOUND);
	}


	@Override
	public void atTurnStart() {
		AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(this::maybeDraw));
	}

	private void maybeDraw() {
		if (playerHasNoFlight()) {
			AbstractDungeon.actionManager
					.addToBottom(new DrawCardAction(AbstractDungeon.player, DRAW_AMT));
			flash();
			appearAbove(AbstractDungeon.player);
		}
	}

	private boolean playerHasNoFlight() {
		return !AbstractDungeon.player.hasPower(SageFlight.POWER_ID);
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + DRAW_AMT + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new BalloonAnimal();
	}

}
