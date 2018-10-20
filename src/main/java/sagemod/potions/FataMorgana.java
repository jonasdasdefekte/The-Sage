package sagemod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import basemod.abstracts.CustomPotion;
import sagemod.Keywords;
import sagemod.powers.Disoriented;

public class FataMorgana extends CustomPotion {

	public static final String NAME = "Fata Morgana";
	public static final String POTION_ID = "Fata_Morgana";
	public static final int POTENCY = 1;
	public static final String DESCRIPTION = "Apply " + POTENCY + " Disoriented.";
	public static final PotionRarity RARITY = PotionRarity.RARE;
	public static final PotionSize SIZE = PotionSize.H;
	public static final PotionColor COLOR = PotionColor.WHITE;
	public static final Color LIQUID_COLOR = Color.RED;
	public static final Color HYBRID_COLOR = Color.RED;
	public static final Color SPOTS_COLOR = Color.BLACK;

	public FataMorgana() {
		super(NAME, POTION_ID, RARITY, SIZE, COLOR);
		description = DESCRIPTION;
		potency = getPotency();
		isThrown = true;
		targetRequired = true;
		tips.add(new PowerTip(name, description));
		tips.add(Keywords.makePowerTip(Keywords.DISORIENTED));
	}

	@Override
	public int getPotency(int ascensionLevel) {
		return POTENCY;
	}

	@Override
	public AbstractPotion makeCopy() {
		return new FataMorgana();
	}

	@Override
	public void use(AbstractCreature target) {
		AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(target, AbstractDungeon.player, new Disoriented(target, potency), potency));
	}

}
