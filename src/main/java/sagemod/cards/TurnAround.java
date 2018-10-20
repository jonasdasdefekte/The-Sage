package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;

public class TurnAround extends AbstractSageCard {

	public static final String ID = "Turn_Around";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 8;
	private static final int UPGRADE_ATTACK_DMG = 2;
	private static final int FRAIL_GAIN = 2;

	public TurnAround() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
		baseMagicNumber = magicNumber = FRAIL_GAIN;
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
		return new TurnAround();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		// start with frail amount you would gain
		int times = magicNumber;
		if (p.hasPower(FrailPower.POWER_ID)) {
			times += p.getPower(FrailPower.POWER_ID).amount;
		}
		applyPowerToSelf(new FrailPower(p, magicNumber, false));
		for (int i = 0; i < times; i++) {
			attack(m, AttackEffect.SLASH_DIAGONAL);
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
