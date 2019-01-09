package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.actions.ReduceFlightBlockableAction;
import sagemod.powers.Brew;
import sagemod.powers.Flight;

public class Lunchtime extends AbstractSageCard {

	public static final String ID = "sagemod:Lunchtime";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int FLIGHT_LOSS = 2;
	private static final int UPGRADE_FLIGHT_LOSS = -1;

	public Lunchtime() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = FLIGHT_LOSS;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_FLIGHT_LOSS);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Lunchtime();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (p.hasPower(Flight.POWER_ID)) {
			AbstractDungeon.actionManager
					.addToBottom(new ReduceFlightBlockableAction(magicNumber, p));
			Brew.brewAllPotions();
		}
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		return canOnlyUseWhileFlying(p, m);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
