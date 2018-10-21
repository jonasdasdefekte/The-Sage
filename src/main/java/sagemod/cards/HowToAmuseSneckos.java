package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;

public class HowToAmuseSneckos extends AbstractSageCard {

	public static final String ID = "How_To_Amuse_Sneckos";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int FRAIL_GAIN = 4;
	private static final int UPGRADE_FRAIL_GAIN = -1;
	private static final int ADDITIONAL_DRAW_AND_ENERGY = 0;
	private static final int UPGRADE_ADDITIONAL_DRAW_AND_ENERGY = 1;

	public HowToAmuseSneckos() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = FRAIL_GAIN;
		misc = ADDITIONAL_DRAW_AND_ENERGY;

		exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_FRAIL_GAIN);
			misc += UPGRADE_ADDITIONAL_DRAW_AND_ENERGY;

			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToAmuseSneckos();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();

		int drawAndEnergyGain = effect + misc;
		if (drawAndEnergyGain > 0) {
			draw(drawAndEnergyGain);
			gainEnergy(drawAndEnergyGain);
		}

		int frailGain = magicNumber - effect;
		if (frailGain > 0) {
			applyPowerToSelf(new FrailPower(p, frailGain, false));
		}

		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
