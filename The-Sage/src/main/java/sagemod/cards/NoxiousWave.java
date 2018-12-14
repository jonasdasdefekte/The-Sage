package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import sagemod.actions.ExecuteLaterAction;

public class NoxiousWave extends AbstractSageCard {

	public static final String ID = "Noxious_Wave";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 6;

	public NoxiousWave() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;

		tags.add(CardTags.STRIKE);
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
		return new NoxiousWave();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		attack(m, AttackEffect.BLUNT_LIGHT);
		if (m.hasPower(PoisonPower.POWER_ID)) {
			// triggers poison
			m.getPower(PoisonPower.POWER_ID).atStartOfTurn();
			if (upgraded) {
				AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(() -> {
					if (m.hasPower(PoisonPower.POWER_ID)) {
						m.getPower(PoisonPower.POWER_ID).atStartOfTurn();
					}
				}));

			}
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
