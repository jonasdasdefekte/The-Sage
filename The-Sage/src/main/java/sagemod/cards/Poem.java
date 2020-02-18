package sagemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.PoemPower;

public class Poem extends AbstractSageCard {

	public static final String ID = "sagemod:Poem";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int DRAW_AMT = 1;
	private static final int BASE_ENERGY_GAIN = 3;
	private static final int UPGRADE_ENERGY_GAIN = 1;

	public Poem() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		initSageMisc(DRAW_AMT);
		baseMagicNumber = magicNumber = BASE_ENERGY_GAIN;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_ENERGY_GAIN);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Poem();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();

		draw(sageMisc);
		gainEnergy(effect + magicNumber);
		addToBot(new ApplyPowerAction(p, p, new PoemPower(p, magicNumber), magicNumber));
		
		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
