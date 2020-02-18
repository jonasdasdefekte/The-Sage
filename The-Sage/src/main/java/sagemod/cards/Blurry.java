package sagemod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.Flight;

public class Blurry extends AbstractSageCard {

	public static final String ID = "sagemod:Blurry";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int TEMP_HP_AMT = 8;
	private static final int UPGRADE_TEMP_HP_AMT = 4;

	public Blurry() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = TEMP_HP_AMT;
		exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeMagicNumber(UPGRADE_TEMP_HP_AMT);
			upgradeName();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Blurry();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int tempHP = getTempHpGain();
		if (tempHP > 0) {
			AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(p, p, tempHP));
		}
		rawDescription = getLoadedDescription();
		initializeDescription();
	}

	private int getTempHpGain() {
		int tempHP = magicNumber;
		AbstractPlayer p = player();
		if (p.hasPower(Flight.POWER_ID)) {
			tempHP -= p.getPower(Flight.POWER_ID).amount;
		}
		return tempHP;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		int tempHP = getTempHpGain();
		rawDescription = getLoadedDescription();
		rawDescription +=
				cardStrings.EXTENDED_DESCRIPTION[0] + tempHP + cardStrings.EXTENDED_DESCRIPTION[1];
		initializeDescription();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
