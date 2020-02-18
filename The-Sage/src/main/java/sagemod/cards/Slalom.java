package sagemod.cards;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sagemod.powers.Flight;

public class Slalom extends AbstractSageCard {

	public static final String ID = "sagemod:Slalom";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int BASE_FLIGHT_AMT = 2;
	private static final int UPGRADE_FLIGHT_AMT = 2;

	public Slalom() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = BASE_FLIGHT_AMT;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_FLIGHT_AMT);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Slalom();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (isFlying()) {
			AbstractPower power = p.getPower(Flight.POWER_ID);
			if (magicNumber <= 0) {
				AbstractDungeon.actionManager
						.addToBottom(new RemoveSpecificPowerAction(p, p, power));
			} else {
				power.amount = magicNumber;
				power.flash();
			}
		} else if (magicNumber > 0) {
			applyPowerToSelf(new Flight(p, magicNumber));
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
