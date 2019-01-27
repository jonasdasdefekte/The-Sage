package sagemod.cards;

import com.evacipated.cardcrawl.mod.stslib.variables.RefundVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.Flight;

public class StepBack extends AbstractSageCard {

	public static final String ID = "sagemod:Step_Back";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int BLOCK_AMT = 5;
	private static final int UPGRADE_BLOCK_AMT = 3;
	private static final int FLIGHT_AMT = 1;
	private static final int REFUND_AMOUNT = 2;

	public StepBack() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseBlock = BLOCK_AMT;
		baseMagicNumber = magicNumber = FLIGHT_AMT;
		initSageMisc(REFUND_AMOUNT);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BLOCK_AMT);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new StepBack();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		block();
		if (isFlying()) {
			RefundVariable.setBaseValue(this, REFUND_AMOUNT);
		} else {
			RefundVariable.setBaseValue(this, 0);
			applyPowerToSelf(new Flight(p, magicNumber));
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
