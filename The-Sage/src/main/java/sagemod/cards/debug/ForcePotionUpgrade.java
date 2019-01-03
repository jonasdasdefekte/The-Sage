package sagemod.cards.debug;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import sagemod.cards.AbstractSageCard;
import sagemod.potions.UpgradedPotion;

public class ForcePotionUpgrade extends AbstractSageCard {

	public static final String ID = "sagemod:Force_Potion_Upgrade";
	public static final String NAME = "Force Potion Upgrade";
	private static final int COST = 0;
	public static final String DESCRIPTION =
			"Forces the potion in slot !M! (number of upgrades) to be upgraded.";
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.SPECIAL;
	private static final CardTarget TARGET = CardTarget.SELF;


	public ForcePotionUpgrade() {
		this(0);
	}

	public ForcePotionUpgrade(int upgrades) {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		timesUpgraded = upgrades;
		baseMagicNumber = magicNumber = 0;
	}

	@Override
	public void upgrade() {
		upgradeMagicNumber(1);
		++timesUpgraded;
		upgraded = true;
		name = NAME + " (" + timesUpgraded + ")";
		initializeTitle();
	}

	@Override
	public boolean canUpgrade() {
		return true;
	}

	@Override
	public AbstractCard makeCopy() {
		return new ForcePotionUpgrade(timesUpgraded);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (magicNumber >= 0 && magicNumber < p.potionSlots) {
			AbstractPotion potion = p.potions.get(magicNumber);
			if (!(potion instanceof PotionSlot)) {
				p.obtainPotion(magicNumber, UpgradedPotion.forceUpgrade(potion));
			}
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
