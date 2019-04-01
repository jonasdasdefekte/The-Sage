package sagemod.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import sagemod.actions.ExecuteLaterAction;

public class DeadlyContraptionPower extends AbstractSagePower implements HealthBarRenderPower, InvisiblePower {

	public static final String POWER_ID = "sagemod:Deadly_Contraption";
	private static final PowerStrings powerStrings =
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public static final Color HEALTH_BAR_RENDER_COLOR = new Color(0xEAD290FF);

	public DeadlyContraptionPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, -1);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[2];
		} else {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
		}
	}

	@Override
	public void atStartOfTurn() {
		super.atStartOfTurn();
		if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT
				&& !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			if (owner.hasPower(ArtifactPower.POWER_ID)) {
				int artifact = owner.getPower(ArtifactPower.POWER_ID).amount;
				AbstractDungeon.actionManager
						.addToBottom(new ExecuteLaterAction(this::flashWithoutSound));
				AbstractDungeon.actionManager
						.addToBottom(new LoseHPAction(owner, owner, artifact,
								AttackEffect.NONE));
			}
		}
	}
	
	@Override
	public int getHealthBarAmount() {
		if (owner.hasPower(ArtifactPower.POWER_ID)) {
			return owner.getPower(ArtifactPower.POWER_ID).amount;
		}
		return 0;
	}

	@Override
	public Color getColor() {
		return HEALTH_BAR_RENDER_COLOR;
	}

}
