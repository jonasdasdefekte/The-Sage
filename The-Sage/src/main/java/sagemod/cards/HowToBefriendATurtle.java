package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class HowToBefriendATurtle extends AbstractSageCard {

	public static final String ID = "sagemod:How_To_Befriend_A_Turtle";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int BASE_TIMES = 1;
	private static final int UPGRADE_BASE_TIMES = 1;

	public HowToBefriendATurtle() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = BASE_TIMES;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BASE_TIMES);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToBefriendATurtle();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();
		
		int amount = magicNumber * effect;
		
		if (amount > 0) {
			applyPowerToSelf(new PlatedArmorPower(p, amount));
		}

		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
