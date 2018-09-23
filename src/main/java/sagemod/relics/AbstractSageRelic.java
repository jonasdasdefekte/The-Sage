package sagemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.abstracts.CustomRelic;

public class AbstractSageRelic extends CustomRelic {

	public AbstractSageRelic(String id, RelicTier tier, LandingSound sfx) {
		super(id, getImg(id), tier, sfx);
	}

	private static Texture getImg(String id) {
		return ImageMaster.loadImage("sage/relics/" + id + ".png");
	}

	protected void applyPowerToSelf(AbstractPower power) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player(), player(), power, power.amount));
	}

	protected AbstractCreature player() {
		return AbstractDungeon.player;
	}

}
