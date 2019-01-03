package sagemod.relics;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CabbageHead extends AbstractSageRelic {

	public static final String ID = "sagemod:Cabbage_Head";
	public static final RelicTier TIER = RelicTier.COMMON;
	public static final LandingSound SOUND = LandingSound.FLAT;
	private static final int DEXTERITY_AMT = 2;

	// Actual Implementation in PatchesForCabbageHead
	public CabbageHead() {
		super(ID, TIER, SOUND);
	}

	@Override
	public void onEquip() {
		if (AbstractDungeon.player.hasPower(FrailPower.POWER_ID)) {
			gainDexterity();
		}
	}

	public void gainDexterity() {
		applyPowerToSelf(new DexterityPower(AbstractDungeon.player, DEXTERITY_AMT));
		flash();
		appearAbove(AbstractDungeon.player);
	}

	public void loseDexterity() {
		AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player,
				AbstractDungeon.player, DexterityPower.POWER_ID, DEXTERITY_AMT));
		flash();
		appearAbove(AbstractDungeon.player);
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + DEXTERITY_AMT + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new CabbageHead();
	}

}
