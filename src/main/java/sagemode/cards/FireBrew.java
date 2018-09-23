package sagemode.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.FirePotion;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import basemod.helpers.BaseModTags;
import basemod.helpers.CardTags;
import sagemod.powers.Brew;

public class FireBrew extends AbstractSageCard {

	public static final String ID = "Fire_Brew";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.BASIC;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int TURNS = 4;
	private static final int UPGRADE_TURNS = -1;

	public FireBrew() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = TURNS;

		CardTags.addTags(this, BaseModTags.GREMLIN_MATCH);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_TURNS);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new FireBrew();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (energyOnUse < EnergyPanel.totalCount) {
			energyOnUse = EnergyPanel.totalCount;
		}

		int effect = EnergyPanel.totalCount;
		if (energyOnUse != -1) {
			effect = energyOnUse;
		}
		if (player().hasRelic(ChemicalX.ID)) {
			effect += 2;
			player().getRelic(ChemicalX.ID).flash();
		}
		int turns = Math.max(0, magicNumber - effect);

		Brew.getOrCreate(turns, new FirePotion(), player());

		if (!freeToPlayOnce) {
			player().energy.use(EnergyPanel.totalCount);
		}
	}

}
