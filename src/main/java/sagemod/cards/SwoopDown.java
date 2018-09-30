package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sagemod.powers.SageFlight;

public class SwoopDown extends AbstractSageCard {

	public static final String ID = "Swoop_Down";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 8;
	private static final int UPGRADE_ATTACK_DMG = 2;
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
			upgradeDamage(UPGRADE_ATTACK_DMG);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new SwoopDown();
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		updateExtendedDescription();
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		updateExtendedDescription();
	}

	private void updateExtendedDescription() {
		int count = 0;
		if (player().hasPower(SageFlight.POWER_ID)) {
			count = ((SageFlight) player().getPower(SageFlight.POWER_ID)).getStoredAmount();
		}
		rawDescription = DESCRIPTION;
		rawDescription = rawDescription + EXTENDED_DESCRIPTION[0] + damage * count + EXTENDED_DESCRIPTION[1];
		initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int times = 0;
		if (player().hasPower(SageFlight.POWER_ID)) {
			times = ((SageFlight) player().getPower(SageFlight.POWER_ID)).getStoredAmount();
		}
		for (int i = 0; i < times; i++) {
			attack(m, AttackEffect.SLASH_VERTICAL);
		}
		AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, SageFlight.POWER_ID, magicNumber));
		rawDescription = DESCRIPTION;
		initializeDescription();
	}

	@Override
	public void onMoveToDiscard() {
		rawDescription = DESCRIPTION;
		initializeDescription();
	}

}
