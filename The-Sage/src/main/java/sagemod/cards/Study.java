package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.actions.HandToTopOfDrawPileAction;

public class Study extends AbstractSageCard {

	public static final String ID = "sagemod:Study";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int DRAW_AMT = 3;
	private static final int PUT_ON_TOP_MAX = 1;
	private static final int PUT_ON_TOP_MIN = 1;
	private static final int UPGRADED_MIN = 0;

	int min;

	public Study() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = DRAW_AMT;
		initSageMisc(PUT_ON_TOP_MAX);
		min = PUT_ON_TOP_MIN;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			min = UPGRADED_MIN;
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Study();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		draw(magicNumber);
		AbstractDungeon.actionManager
				.addToBottom(new HandToTopOfDrawPileAction(min, sageMisc, false,
				cardStrings.EXTENDED_DESCRIPTION[0]));
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
