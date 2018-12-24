package sagemod.cards.debug;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import basemod.DevConsole;
import sagemod.cards.AbstractSageCard;
import sagemod.potions.UpgradedPotion;

public class ConsolePotion extends AbstractSageCard {

	public static final String ID = "Console_Potion";
	public static final String NAME = "Console Potion";
	private static final int COST = 0;
	public static final String DESCRIPTION =
			"The potion in slot 0 becomes the normal and in slot 1 becomes the upgraded current text of the console.";
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.SPECIAL;
	private static final CardTarget TARGET = CardTarget.SELF;

	public ConsolePotion() {
		this(0);
	}

	public ConsolePotion(int upgrades) {
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
		return new ConsolePotion(timesUpgraded);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		String id = DevConsole.currentText;
		AbstractPotion potion = PotionHelper.getPotion(id == null ? "null" : id);
		if (potion == null) {
			return;
		}
		if (p.potionSlots > 0) {
			p.obtainPotion(0, potion);
		}
		if (p.potionSlots > 1) {
			p.obtainPotion(1, UpgradedPotion.getUpgradeIfAvailable(potion.makeCopy()));
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
