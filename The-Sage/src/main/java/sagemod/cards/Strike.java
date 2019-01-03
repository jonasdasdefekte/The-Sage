package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import basemod.helpers.BaseModCardTags;

public class Strike extends AbstractSageCard {

	public static final String ID = "sagemod:Strike";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.BASIC;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 6;
	private static final int UPGRADE_ATTACK_DMG = 3;

	public Strike() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;

		tags.add(CardTags.STRIKE);
		tags.add(BaseModCardTags.BASIC_STRIKE);
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
		return new Strike();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		attack(m, AttackEffect.SLASH_DIAGONAL);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
