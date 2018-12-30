package sagemod.cards.event;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.cards.AbstractSageCard;
import sagemod.relics.ByrdFeather;

public class ByrdFeatherPreview extends AbstractSageCard {

	public static final String ID = "Byrd_Feather_Preview";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.STATUS;
	private static final CardRarity RARITY = CardRarity.SPECIAL;
	private static final CardTarget TARGET = CardTarget.NONE;

	public ByrdFeatherPreview() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = ByrdFeather.FLY_AMT;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new ByrdFeatherPreview();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
