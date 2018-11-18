package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HowToAmuseSneckos extends AbstractSageCard {

	public static final String ID = "How_To_Amuse_Sneckos";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int UPGRADED_ENERGY_GAIN = 1;

	public HowToAmuseSneckos() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);

	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();

			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToAmuseSneckos();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();

		int draw = effect;
		if (draw > 0) {
			draw(draw);
		}

		if (upgraded) {
			gainEnergy(UPGRADED_ENERGY_GAIN);
		}

		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
