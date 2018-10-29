package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import sagemod.powers.AncientPoisonPower;

public class AncientPoison extends AbstractSageCard {

	public static final String ID = "Ancient_Poison";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.POWER;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POISON_AMT = 4;
	private static final int UPGRADE_POISON_AMT = 3;

	public AncientPoison() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = POISON_AMT;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_POISON_AMT);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new AncientPoison();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (!p.hasPower(AncientPoisonPower.POWER_ID)) {
			applyPowerToSelf(new AncientPoisonPower(p, 1));
		} else {
			p.getPower(AncientPoisonPower.POWER_ID).flash();
		}
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			applyPower(new PoisonPower(mo, p, magicNumber), mo);
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
