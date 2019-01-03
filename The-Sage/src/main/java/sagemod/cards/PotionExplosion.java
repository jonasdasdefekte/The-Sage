package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.listeners.PotionListener;

public class PotionExplosion extends AbstractSageCard {

	public static final String ID = "sagemod:Potion_Explosion";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

	private static final int ATTACK_DMG = 6;
	private static final int UPGRADE_ATTACK_DMG = 2;

	// Implementation in PotionListener and PatchForPotionExplosion

	public PotionExplosion() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
		isMultiDamage = true;
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
		return new PotionExplosion();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		for (int i = 0; i < PotionListener.potionsUsed; i++) {
			attackAllEnemies(AttackEffect.FIRE);
		}
	}

	private void updateExtendedDescription() {
		rawDescription = getLoadedDescription();
		if (PotionListener.potionsUsed == 1) {
			rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
		} else {
			rawDescription += cardStrings.EXTENDED_DESCRIPTION[1] + PotionListener.potionsUsed
					+ cardStrings.EXTENDED_DESCRIPTION[2];
		}
		initializeDescription();
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

	@Override
	public void atTurnStart() {
		super.atTurnStart();
		updateExtendedDescription();
	}

	@Override
	public void triggerWhenDrawn() {
		super.triggerWhenDrawn();
		updateExtendedDescription();
	}

	@Override
	public void onMoveToDiscard() {
		rawDescription = getLoadedDescription();
		initializeDescription();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
