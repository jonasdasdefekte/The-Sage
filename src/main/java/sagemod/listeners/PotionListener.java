package sagemod.listeners;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.ExplosivePotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import basemod.ReflectionHacks;
import basemod.interfaces.PostPotionUseSubscriber;
import basemod.interfaces.PrePotionUseSubscriber;
import sagemod.powers.AlchemyExpertPower;
import sagemod.powers.ExtraPortionPower;
import sagemod.powers.TasteThisOnePower;
import sagemod.powers.Thirsty;

public class PotionListener implements PrePotionUseSubscriber, PostPotionUseSubscriber {

	private AbstractMonster getHoveredMonster() {
		return (AbstractMonster) ReflectionHacks.getPrivate(AbstractDungeon.topPanel.potionUi,
				PotionPopUp.class, "hoveredMonster");
	}

	private void multiplyPotencyBy(AbstractPotion p, float multiplier) {
		int potency = (int) ReflectionHacks.getPrivate(p, AbstractPotion.class, "potency");
		ReflectionHacks.setPrivate(p, AbstractPotion.class,
				"potency", (int) (potency * multiplier));
	}

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
		alchemyExpert(p);
	}

	public void postRealPotionUse(AbstractPotion p) {
		tasteThisOne(p);
	}

	private void usePotion(AbstractPotion p, AbstractMonster m) {
		preRealPotionUse(p);
		p.use(m);
		postRealPotionUse(p);
	}

	private void extraPortion(AbstractPotion p) {
		if (AbstractDungeon.player.hasPower(ExtraPortionPower.POWER_ID)) {
			AbstractPower power = AbstractDungeon.player.getPower(ExtraPortionPower.POWER_ID);
			AbstractMonster monster = getHoveredMonster();
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


	private void thirstyPre(AbstractPotion p) {
		if (p.ID.equals(ExplosivePotion.POTION_ID)) {
			for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
				if (mo.hasPower(Thirsty.POWER_ID)) {
					((Thirsty) mo.getPower(Thirsty.POWER_ID)).onExplosivePotionUsed();
				}
			}
		} else if (p.isThrown && !p.targetRequired) {
			AbstractMonster monsterWithThirsty = null;
			for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
				if (mo.hasPower(Thirsty.POWER_ID)) {
					monsterWithThirsty = mo;
					break;
				}
			}
			if (monsterWithThirsty != null) {
				multiplyPotencyBy(p, Thirsty.MULTIPLIER);
				monsterWithThirsty.getPower(Thirsty.POWER_ID).flash();
			}
		} else {
			AbstractMonster target = getHoveredMonster();
			if (target != null && target.hasPower(Thirsty.POWER_ID)) {
				multiplyPotencyBy(p, Thirsty.MULTIPLIER);
				target.getPower(Thirsty.POWER_ID).flash();
			}
		}
	}

	private void alchemyExpert(AbstractPotion p) {
		if (AbstractDungeon.player.hasPower(AlchemyExpertPower.POWER_ID)) {
			AbstractPower power = AbstractDungeon.player.getPower(AlchemyExpertPower.POWER_ID);
			int amount = power.amount;
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player,
					AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player, amount), amount));
			power.flash();
		}
	}

}
