package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class PincerAttack extends AbstractSageCard {

	public static final String ID = "Pincer_Attack";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 6;
	private static final int UPGRADE_ATTACK_DMG = 2;

	public PincerAttack() {
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
		return new PincerAttack();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int extraDamage = 0;
		if (m.hasPower(ArtifactPower.POWER_ID)) {
			extraDamage += m.getPower(ArtifactPower.POWER_ID).amount;
		}
		attack(m, AttackEffect.SMASH, damage + extraDamage);
		rawDescription = getLoadedDescription();
		initializeDescription();
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		int extraDamage = 0;
		if (mo != null && mo.hasPower(ArtifactPower.POWER_ID)) {
			extraDamage += mo.getPower(ArtifactPower.POWER_ID).amount;
		}
		rawDescription = getLoadedDescription();
		rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + extraDamage
				+ cardStrings.EXTENDED_DESCRIPTION[1];
		initializeDescription();
	}


	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
