package sagemod.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sagemod.powers.Disoriented;

public class CreepyCarousel extends AbstractSageRelic {

	public static final String ID = "Creepy_Carousel";
	public static final RelicTier TIER = RelicTier.RARE;
	public static final LandingSound SOUND = LandingSound.HEAVY;
	private static final int TURNS_NEEDED = 5;
	private static final int DISORIENTED_GAIN = 1;


	public CreepyCarousel() {
		super(ID, TIER, SOUND);
		counter = TURNS_NEEDED;
	}

	@Override
	public void atTurnStart() {
		counter--;
		if (counter <= 0) {
			flash();
			AbstractMonster mo = AbstractDungeon.getCurrRoom().monsters.getRandomMonster();
			appearAbove(mo);
			applyPower(new Disoriented(mo, DISORIENTED_GAIN), mo);
			counter = TURNS_NEEDED;
		}
	}


	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + TURNS_NEEDED + DESCRIPTIONS[1] + DISORIENTED_GAIN
				+ DESCRIPTIONS[2];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new CreepyCarousel();
	}

}
