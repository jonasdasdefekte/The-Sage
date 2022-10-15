package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.actions.ReduceFlightBlockableAction;
import sagemod.powers.Flight;

public class SwoopDown extends AbstractSageCard {

	public static final String ID = "sagemod:Swoop_Down";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

	private static final int ATTACK_DMG = 8;
	private static final int LOSE_FLIGHT = 1;

	public SwoopDown() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
		baseMagicNumber = magicNumber = LOSE_FLIGHT;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new SwoopDown();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int times = 0;
		if (isFlying()) {
			times = player().getPower(Flight.POWER_ID).amount;
		}
		for (int i = 0; i < times; i++) {
			attack(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true), AttackEffect.SLASH_VERTICAL);
		}
		if (!upgraded) {
			AbstractDungeon.actionManager.addToBottom(new ReduceFlightBlockableAction(magicNumber, p));
		}
	}

	@Override
	public String getLoadedDescription() {
		return upgraded ? cardStrings.UPGRADE_DESCRIPTION : DESCRIPTION;
	}

}
