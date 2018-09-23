package sagemode.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sagemod.powers.SageFlight;

public class OnTheHead extends AbstractSageCard {

	public static final String ID = "On_The_Head";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.BASIC;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 5;
	private static final int UPGRADE_ATTACK_DMG = 3;
	private static final int DRAW_AND_ENERGY_GAIN = 1;

	public OnTheHead() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
		baseMagicNumber = magicNumber = DRAW_AND_ENERGY_GAIN;

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
		return new OnTheHead();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		damage(m, AttackEffect.SLASH_HORIZONTAL);
		if (hasPower(SageFlight.POWER_ID)) {
			draw(magicNumber);
			gainEnergy(magicNumber);
		}
	}

}
