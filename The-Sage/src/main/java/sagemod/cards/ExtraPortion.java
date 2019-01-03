package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.ExtraPortionPower;

public class ExtraPortion extends AbstractSageCard {

	public static final String ID = "sagemod:Extra_Portion";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POTION_COUNT = 1;
	private static final int UPGRADE_POTION_COUNT = 1;

	public ExtraPortion() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = POTION_COUNT;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_POTION_COUNT);
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new ExtraPortion();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPowerToSelf(new ExtraPortionPower(p, magicNumber));
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
