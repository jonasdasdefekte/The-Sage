package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.TasteThisOnePower;

public class TasteThisOne extends AbstractSageCard {

	public static final String ID = "sagemod:Taste_This_One";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.POWER;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int DAMAGE_AMT = 10;
	private static final int UPGRADE_DAMAGE_AMT = 5;

	public TasteThisOne() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = DAMAGE_AMT;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_DAMAGE_AMT);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new TasteThisOne();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPowerToSelf(new TasteThisOnePower(p, magicNumber));
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
