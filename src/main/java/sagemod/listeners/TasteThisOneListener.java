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
			AbstractPower power = AbstractDungeon.player.getPower(TasteThisOnePower.POWER_ID);
			AbstractMonster m = AbstractDungeon.getRandomMonster();

			if (!m.isDying && m.currentHealth > 0 && !m.isEscaping) {

				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player,
						new PoisonPower(m, AbstractDungeon.player, power.amount), power.amount));

				power.flash();
			}

		}
	}

}
