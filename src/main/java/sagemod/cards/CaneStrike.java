package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;

public class CaneStrike extends AbstractSageCard {

	public static final String ID = "Cane_Strike";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 7;
	private static final int UPGRADE_ATTACK_DMG = 3;

	private boolean isAttackDoubled = false;

	public CaneStrike() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;

		tags.add(CardTags.STRIKE);
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
		return new CaneStrike();
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		if (player().hasPower(FrailPower.POWER_ID)) {
			if (!isAttackDoubled) {
				isAttackDoubled = true;
				damage *= 2;
				baseDamage *= 2;
			}
			isDamageModified = true;
		} else {
			if (isAttackDoubled) {
				isAttackDoubled = false;
				damage /= 2;
				baseDamage /= 2;
			}
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (p.hasPower(FrailPower.POWER_ID)) {
			attack(m, AttackEffect.BLUNT_HEAVY);
		} else {
			attack(m, AttackEffect.BLUNT_LIGHT);
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
