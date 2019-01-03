package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;

public class RicketyDefensePower extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:Rickety_Defense";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public RicketyDefensePower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, -1);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
		priority = 99;
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		super.atEndOfTurn(isPlayer);
		if (isPlayer) {
			AbstractPlayer player = AbstractDungeon.player;
			if (player.hasPower(FrailPower.POWER_ID)) {
				int frail = player.getPower(FrailPower.POWER_ID).amount;
				int block = player.currentBlock;
				if (frail > block) {
					AbstractDungeon.actionManager
					.addToBottom(new GainBlockAction(player, player, frail - block));
					flash();
				}
			}
		}
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}

}
