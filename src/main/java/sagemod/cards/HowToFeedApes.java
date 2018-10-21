package sagemod.cards;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.BloodPotion;
import com.megacrit.cardcrawl.potions.FruitJuice;
import com.megacrit.cardcrawl.potions.RegenPotion;

import basemod.helpers.TooltipInfo;
import sagemod.potions.FlightPotion;
import sagemod.powers.Brew;

public class HowToFeedApes extends AbstractSageCard {

	public static final String ID = "How_To_Feed_Apes";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	public static final List<Supplier<? extends AbstractPotion>> POSSIBLE_POTIONS = Arrays.asList(FlightPotion::new,
			BloodPotion::new, RegenPotion::new, FruitJuice::new);
	public static final String TITLE = "Possible Potions";
	public static final String POTION_NAMES = getPotionNames();

	private static String getPotionNames() {
		return POSSIBLE_POTIONS.stream().map(s -> s.get().name).collect(Collectors.joining(" NL ")).toString();
	}

	private static final int TURNS = 4;
	private static final int UPGRADE_TURNS = -1;

	public HowToFeedApes() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		initBrewIn(TURNS);
		exhaust = true;
	}


	@Override
	public List<TooltipInfo> getCustomTooltips() {
		return Arrays.asList(new TooltipInfo(TITLE, POTION_NAMES));
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBrewIn(UPGRADE_TURNS);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToFeedApes();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int turns = Math.max(0, brewIn - getXEffect());

		AbstractPotion potion = POSSIBLE_POTIONS.get(AbstractDungeon.potionRng.random(0, POSSIBLE_POTIONS.size() - 1))
				.get();

		Brew.addPotion(turns, potion, p);

		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
