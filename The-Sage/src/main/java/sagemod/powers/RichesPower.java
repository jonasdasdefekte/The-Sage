package sagemod.powers;

import java.util.ArrayList;
import java.util.Arrays;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.BloodPotion;
import com.megacrit.cardcrawl.potions.FruitJuice;
import com.megacrit.cardcrawl.potions.RegenPotion;

public class RichesPower extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:Riches";
	private static final PowerStrings powerStrings =
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public static final ArrayList<String> EXCLUDED = new ArrayList<>(
			Arrays.asList(FruitJuice.POTION_ID, BloodPotion.POTION_ID, RegenPotion.POTION_ID));

	// actual implementation in PotionListener
	public RichesPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[0];
		} else {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
		}
	}

}
