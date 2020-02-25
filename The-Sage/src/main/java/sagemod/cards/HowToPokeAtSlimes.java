package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HowToPokeAtSlimes extends AbstractSageCard {

	public static final String ID = "sagemod:How_To_Poke_At_Slimes";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

	private static final int BASE_ATTACK_DMG = 3;
	private static final int BASE_ADDED_TIMES = 1;
	private static final int UPGRADE_ADDED_TIMES = 1;

	public HowToPokeAtSlimes() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = BASE_ATTACK_DMG;
		baseMagicNumber = magicNumber = BASE_ADDED_TIMES;
		isMultiDamage = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_ADDED_TIMES);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToPokeAtSlimes();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();
		int times = effect + magicNumber;
		
		for (int i = 0; i < times; i++) {
			attackAllEnemies(AttackEffect.SLASH_HORIZONTAL);
		}

		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
