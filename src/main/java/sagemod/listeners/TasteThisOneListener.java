package sagemod.listeners;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

import basemod.interfaces.PostPotionUseSubscriber;
import sagemod.powers.TasteThisOnePower;

public class TasteThisOneListener implements PostPotionUseSubscriber {

	@Override
	public void receivePostPotionUse(AbstractPotion p) {
		if (AbstractDungeon.player.hasPower(TasteThisOnePower.POWER_ID)) {
			boolean actuallyDidSomething = false;
			AbstractPower power = AbstractDungeon.player.getPower(TasteThisOnePower.POWER_ID);

			for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
				if (m.isDying || m.currentHealth <= 0 || m.isEscaping) {
					continue;
				}
				actuallyDidSomething = true;
				int amount = power.amount;
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player,
						new PoisonPower(m, AbstractDungeon.player, amount), amount));
			}

			if (actuallyDidSomething) {
				power.flash();
				// TODO play potion sound
			}
		}
	}

}
