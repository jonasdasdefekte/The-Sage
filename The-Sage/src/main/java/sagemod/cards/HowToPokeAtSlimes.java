package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;

public class HowToPokeAtSlimes extends AbstractSageCard {

	public static final String ID = "How_To_Poke_At_Slimes";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

	private static final int ATTACK_DMG = 5;
	private static final int UPGRADE_ATTACK_DMG = 2;
	private static final int FRAIL_GAIN = 4;
	private static final int UPGRADE_FRAIL_GAIN = -1;

	public HowToPokeAtSlimes() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
		baseMagicNumber = magicNumber = FRAIL_GAIN;
		isMultiDamage = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_ATTACK_DMG);
			upgradeMagicNumber(UPGRADE_FRAIL_GAIN);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToPokeAtSlimes();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();
		int frail = Math.max(0, magicNumber - effect);

		if (frail > 0) {
			applyPowerToSelf(new FrailPower(p, frail, false));
		}
		if (effect > 0) {
			gainEnergy(effect);
		}
		attackAllEnemies(AttackEffect.SLASH_HORIZONTAL);
		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
