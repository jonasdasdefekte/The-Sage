package sagemod.cards.debug;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import sagemod.actions.ExecuteLaterAction;
import sagemod.cards.AbstractSageCard;
import sagemod.potions.UpgradedPotion;

public class CyclePotions extends AbstractSageCard {

	public static final String ID = "sagemod:Cycle_Potions";
	public static final String NAME = "Cycle Potions";
	private static final int COST = 0;
	public static final String DESCRIPTION =
			"Draw 1 card. The potion in slot 0 becomes normal and in slot 1 becomes upgraded ";
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.SPECIAL;
	private static final CardTarget TARGET = CardTarget.SELF;


	private static List<String> potions = new ArrayList<>();
	private int currentPotion;
	private AbstractPotion nextPotion;

	public CyclePotions() {
		this(0);
	}

	public CyclePotions(int upgrades) {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		timesUpgraded = upgrades;
		baseMagicNumber = magicNumber = 0;
		currentPotion = 0;
		shouldNotDisplayInScreen = true;
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
		return new CyclePotions(timesUpgraded);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (potions.isEmpty()) {
			initPotionList();
		}
		if (p.potionSlots > 0) {
			p.obtainPotion(0, nextPotion);
		}
		if (p.potionSlots > 1) {
			p.obtainPotion(1, UpgradedPotion.getUpgradeIfAvailable(nextPotion.makeCopy()));
		}
		nextPotion();
		AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(() -> draw(1)));
	}

	private void nextPotion() {
		if (potions.isEmpty()) {
			initPotionList();
		}
		currentPotion++;
		if (currentPotion < 0 || currentPotion >= potions.size()) {
			currentPotion = 0;
		}
		loadPotion();
	}

	private void loadPotion() {
		nextPotion = PotionHelper.getPotion(potions.get(currentPotion));
		rawDescription = DESCRIPTION + nextPotion.ID + ".";
		initializeDescription();
	}

	@Override
	public void atTurnStart() {
		if (potions.isEmpty()) {
			initPotionList();
		}
		loadPotion();
	}

	@Override
	public void applyPowers() {
		if (potions.isEmpty()) {
			initPotionList();
		}
		loadPotion();
	}

	@Override
	public void triggerWhenDrawn() {
		if (potions.isEmpty()) {
			initPotionList();
		}
		loadPotion();
	}

	private void initPotionList() {
		PlayerClass before = null;
		if (AbstractDungeon.player != null) {
			before = AbstractDungeon.player.chosenClass;
		}
		Set<String> set = new HashSet<>();
		for (PlayerClass c : PlayerClass.values()) {
			PotionHelper.initialize(c);
			set.addAll(PotionHelper.potions);
		}
		potions.addAll(set);
		potions.sort(String.CASE_INSENSITIVE_ORDER);
		if (before != null) {
			PotionHelper.initialize(before);
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
