package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Swipe extends AbstractSageCard {

	public static final String ID = "sagemod:Swipe";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.BASIC;
	private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

	private static final int ATTACK_DMG = 7;
	private static final int UPGRADE_ATTACK_DMG = 3;

	public Swipe() {
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
		return new Swipe();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		attackAllEnemies(AttackEffect.SLASH_HORIZONTAL);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
	
	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		return canOnlyUseWhileFlying(p, m);
	}
}
