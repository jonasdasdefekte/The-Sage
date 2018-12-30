package sagemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import basemod.abstracts.CustomRelic;
import sagemod.SageMod;

public abstract class AbstractSageRelic extends CustomRelic {

	private static final String PREFIX = "sage/relics/";
	private static final String OUTLINE_PREFIX = "sage/relics/outline/";
	private static final String POSTFIX = ".png";

	public AbstractSageRelic(String id, RelicTier tier, LandingSound sfx) {
		super(id, getImg(id), getOutline(id),
				tier, sfx);
	}

	private static Texture getImg(String id) {
		return ImageMaster.loadImage(SageMod.getExistingOrPlaceholder(PREFIX, id, POSTFIX));
	}

	private static Texture getOutline(String id) {
		return ImageMaster.loadImage(SageMod.getExistingOrPlaceholder(OUTLINE_PREFIX, id, POSTFIX));
	}

	protected void applyPowerToSelf(AbstractPower power) {
		applyPower(power, player());
	}

	protected void applyPower(AbstractPower power, AbstractCreature target) {
		AbstractDungeon.actionManager
		.addToBottom(new ApplyPowerAction(target, player(), power, power.amount));
	}

	protected AbstractPlayer player() {
		return AbstractDungeon.player;
	}

	public void appearAbove(AbstractCreature creature) {
		AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(creature, this));
	}

}
