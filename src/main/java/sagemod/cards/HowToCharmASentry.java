package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class HowToCharmASentry extends AbstractSageCard {

	public static final String ID = "How_To_Charm_A_Sentry";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final AttackEffect[] EFFECTS = {AttackEffect.SLASH_HORIZONTAL,
			AttackEffect.SLASH_DIAGONAL, AttackEffect.SLASH_VERTICAL, AttackEffect.SLASH_HEAVY};
	private static final int ATTACK_DMG = 2;
	private static final int UPGRADE_ATTACK_DAMAGE = 2;

	public HowToCharmASentry() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_ATTACK_DAMAGE);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToCharmASentry();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();
		if (effect > 0) {
			for (int i = 0; i < effect; i++) {
				attack(m, EFFECTS[i % EFFECTS.length]);
			}
			applyPower(new ArtifactPower(m, effect), m);
		}
		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
