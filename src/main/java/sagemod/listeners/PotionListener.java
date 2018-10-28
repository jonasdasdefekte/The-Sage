package sagemod.listeners;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;

import basemod.ReflectionHacks;
import basemod.interfaces.PostPotionUseSubscriber;
import basemod.interfaces.PrePotionUseSubscriber;
import sagemod.powers.ExtraPortionPower;
import sagemod.powers.TasteThisOnePower;

public class PotionListener implements PrePotionUseSubscriber, PostPotionUseSubscriber {

	@Override
	public void receivePrePotionUse(AbstractPotion p) {
		preRealPotionUse(p);
		extraPortion(p);
	}

	@Override
	public void receivePostPotionUse(AbstractPotion p) {
		postRealPotionUse(p);
	}

	public void preRealPotionUse(AbstractPotion p) {
		thirstyPre(p);
	}

	public void postRealPotionUse(AbstractPotion p) {
		tasteThisOne(p);
		thirstyPost(p);
	}

	private void usePotion(AbstractPotion p, AbstractMonster m) {
		preRealPotionUse(p);
		p.use(m);
		postRealPotionUse(p);
	}

	private void extraPortion(AbstractPotion p) {
		if (AbstractDungeon.player.hasPower(ExtraPortionPower.POWER_ID)) {
			AbstractPower power = AbstractDungeon.player.getPower(ExtraPortionPower.POWER_ID);
			AbstractMonster monster = (AbstractMonster) ReflectionHacks.getPrivate(AbstractDungeon.topPanel.potionUi,
					PotionPopUp.class, "hoveredMonster");
			monster = monster == null ? AbstractDungeon.getRandomMonster() : monster;
			usePotion(p, monster);
			power.flash();

			AbstractDungeon.actionManager
			.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, power, 1));
		}
	}

	private void tasteThisOne(AbstractPotion p) {
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

	public static volatile boolean wasPotionUsed = false;
	private static final int DELAY = 1000;

	private void thirstyPre(AbstractPotion p) {
		wasPotionUsed = true;
	}

	private void thirstyPost(AbstractPotion p) {
		// Ugly hack so that there is a big enough time window to amplify potion damage
		new Thread(() -> {
			try {
				Thread.sleep(DELAY);
				wasPotionUsed = false;
			} catch (InterruptedException e) {} finally {

			}
		}, "DelayedPotionUseThread").start();

	}

}
