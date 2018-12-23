package sagemod.cards.deprecated;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import sagemod.cards.AbstractSageCard;
import sagemod.character.SageColorEnum;
import sagemod.powers.deprecated.Virus;

@Deprecated
public class ViralStrike extends AbstractSageCard {

	public static final String ID = "Viral_Strike";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.SPECIAl;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 6;
	private static final int POISON_AMT = 7;
	private static final int UPGRADE_ENERGY_TO = 1;

	public ViralStrike() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET, SageColorEnum.THE_SAGE, true);
		baseDamage = ATTACK_DMG;
		baseMagicNumber = magicNumber = POISON_AMT;

		tags.add(CardTags.STRIKE);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(UPGRADE_ENERGY_TO);;
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new ViralStrike();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		attack(m, AttackEffect.SLASH_VERTICAL);
		if (!m.hasPower(Virus.POWER_ID)) {
			applyPower(new Virus(m, -1), m);
		}
		applyPower(new PoisonPower(m, p, POISON_AMT), m);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
