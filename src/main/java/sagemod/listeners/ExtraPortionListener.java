package sagemod.listeners;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;

import basemod.ReflectionHacks;
import basemod.interfaces.PrePotionUseSubscriber;
import sagemod.powers.ExtraPortionPower;

public class ExtraPortionListener implements PrePotionUseSubscriber {

	@Override
	public void receivePrePotionUse(AbstractPotion p) {
		if (AbstractDungeon.player.hasPower(ExtraPortionPower.POWER_ID)) {
			AbstractPower power = AbstractDungeon.player.getPower(ExtraPortionPower.POWER_ID);
			AbstractMonster monster = (AbstractMonster) ReflectionHacks.getPrivate(AbstractDungeon.topPanel.potionUi,
					PotionPopUp.class,
					"hoveredMonster");
			monster = monster == null ? AbstractDungeon.getRandomMonster() : monster;
			p.use(monster);
			power.flash();

			AbstractDungeon.actionManager
					.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, power, 1));
		}
	}

}
