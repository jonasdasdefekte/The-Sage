package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.Flight;

public class PourTarPitch extends AbstractSageCard {

	public static final String ID = "sagemod:Pour_Tar_Pitch";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 3;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 22;
	private static final int UPGRADE_ATTACK_DMG = 4;

	public PourTarPitch() {
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
	public void applyPowers() {
		super.applyPowers();
		if (hasPower(Flight.POWER_ID)) {
			target = CardTarget.ALL_ENEMY;
		} else {
			target = CardTarget.ENEMY;
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new PourTarPitch();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (isFlying()) {
			attackAllEnemies(AttackEffect.BLUNT_HEAVY);
		} else {
			attack(m, AttackEffect.SLASH_DIAGONAL);
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
