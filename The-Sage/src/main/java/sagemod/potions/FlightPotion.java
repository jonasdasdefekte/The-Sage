package sagemod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import basemod.abstracts.CustomPotion;
import sagemod.Keywords;
import sagemod.powers.Flight;

public class FlightPotion extends CustomPotion {

	public static final String POTION_ID = "sagemod:Flight_Potion";
	public static final PotionStrings potionStrings =
			CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
	public static final int POTENCY = 1;
	public static final PotionRarity RARITY = PotionRarity.COMMON;
	public static final PotionSize SIZE = PotionSize.S;
	public static final PotionColor COLOR = PotionColor.WHITE;
	public static final Color LIQUID_COLOR = Color.SKY;
	public static final Color HYBRID_COLOR = Color.SKY;
	public static final Color SPOTS_COLOR = Color.WHITE;

	public FlightPotion() {
		super(NAME, POTION_ID, RARITY, SIZE, COLOR);
		isThrown = false;
	}
	
	@Override
	public void initializeData() {
		potency = getPotency();
		description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
		tips.clear();
		tips.add(new PowerTip(name, description));
		tips.add(Keywords.makePowerTip(Keywords.FLIGHT));
	}

	@Override
	public int getPotency(int ascensionLevel) {
		return POTENCY;
	}

	@Override
	public AbstractPotion makeCopy() {
		return new FlightPotion();
	}

	@Override
	public void use(AbstractCreature target) {
		target = AbstractDungeon.player;
		if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
			AbstractDungeon.actionManager
					.addToBottom(new ApplyPowerAction(target, target, new Flight(target, potency),
							potency));
		}
	}

}
