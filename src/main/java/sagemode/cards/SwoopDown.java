package sagemode.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sagemod.powers.SageFlight;

public class SwoopDown extends AbstractSageCard {

	public static final String ID = "Swoop_Down";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 4;
	private static final int UPGRADE_ATTACK_DMG = 1;

	public SwoopDown() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;

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
	public void use(AbstractPlayer p, AbstractMonster m) {
		int times = 0;
		if (player().hasPower(SageFlight.POWER_ID)) {
			times = ((SageFlight) player().getPower(SageFlight.POWER_ID)).getStoredAmount();
		}
		for (int i = 0; i < times; i++) {
			attack(m, AttackEffect.SLASH_HORIZONTAL);
		}
	}

}
