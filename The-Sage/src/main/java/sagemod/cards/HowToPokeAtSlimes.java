package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.Flight;

public class HowToPokeAtSlimes extends AbstractSageCard {

	public static final String ID = "sagemod:How_To_Poke_At_Slimes";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

	private static final int BASE_ATTACK_DMG = 0;
	private static final int UPGRADE_ATTACK_DMG = 3;

	public HowToPokeAtSlimes() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = BASE_ATTACK_DMG;
		isMultiDamage = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_ATTACK_DMG);
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
	
	@Override
	public void applyPowers() {
		super.applyPowers();
		updateExtendedDescription();
	}
	
	private int getBonusDamage() {
		if (isFlying()) {
			return player().getPower(Flight.POWER_ID).amount;
		}
		return 0;
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		updateExtendedDescription();
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToPokeAtSlimes();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();
		
		if (isFlying()) {
			damage += getBonusDamage();
			this.isDamageModified = true;
			for (int i = 0; i < multiDamage.length; i++) {
				multiDamage[i] = damage;
			}
		}
		
		for (int i = 0; i < effect; i++) {
			attackAllEnemies(AttackEffect.SLASH_HORIZONTAL);
		}

		useXEnergy();
	}
	
	private void updateExtendedDescription() {
		rawDescription = getLoadedDescription();
		rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
		rawDescription += damage + getBonusDamage();
		rawDescription += cardStrings.EXTENDED_DESCRIPTION[1];
		initializeDescription();
	}

	@Override
	public String getLoadedDescription() {
		return upgraded ? cardStrings.UPGRADE_DESCRIPTION : DESCRIPTION;
	}

}
