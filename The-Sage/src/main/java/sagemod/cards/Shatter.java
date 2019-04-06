package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.actions.ReduceFlightBlockableAction;

public class Shatter extends AbstractSageCard {

	public static final String ID = "sagemod:Shatter";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 3;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	public Shatter() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
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
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		return canOnlyUseWhileFlying(p, m);
	}
	
	@Override
	public void applyPowers() {
		baseDamage = damage = getDamage();
		super.applyPowers();
		updateExtendedDescription();
	}
	
	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		updateExtendedDescription();
	}
	
	private int getDamage() {
		return upgraded ? player().maxHealth : player().maxHealth / 2;
	}
	
	private void updateExtendedDescription() {
		rawDescription = getLoadedDescription();
		rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
		rawDescription += damage;
		rawDescription += cardStrings.EXTENDED_DESCRIPTION[1];
		initializeDescription();
	}

	@Override
	public AbstractCard makeCopy() {
		return new Shatter();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ReduceFlightBlockableAction(999, p));
		attack(m, AttackEffect.BLUNT_HEAVY);
	}

	@Override
	public String getLoadedDescription() {
		return upgraded ? cardStrings.UPGRADE_DESCRIPTION : DESCRIPTION;
	}

}
