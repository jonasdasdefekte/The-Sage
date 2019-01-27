package sagemod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.variables.RefundVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HowToBefriendATurtle extends AbstractSageCard {

	public static final String ID = "sagemod:How_To_Befriend_A_Turtle";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int EXTRA_TEMP_HP = 0;
	private static final int UPGRADE_EXTRA_TEMP_HP = 3;
	private static final int REFUND_AMOUNT = 3;
	private static final int UPGRADE_REFUND_AMOUNT = 3;

	public HowToBefriendATurtle() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = EXTRA_TEMP_HP;
		RefundVariable.setBaseValue(this, REFUND_AMOUNT);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_EXTRA_TEMP_HP);
			RefundVariable.upgrade(this, UPGRADE_REFUND_AMOUNT);
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToBefriendATurtle();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();

		int tempHp = effect + magicNumber;
		if (tempHp > 0) {
			AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(p, p, tempHp));
		}

		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return upgraded ? cardStrings.UPGRADE_DESCRIPTION : DESCRIPTION;
	}

}
