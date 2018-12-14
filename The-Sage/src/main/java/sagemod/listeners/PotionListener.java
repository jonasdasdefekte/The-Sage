package sagemod.listeners;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.ExplosivePotion;
import com.megacrit.cardcrawl.potions.FearPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import basemod.ReflectionHacks;
import basemod.interfaces.PostPotionUseSubscriber;
import basemod.interfaces.PrePotionUseSubscriber;
import sagemod.actions.ExecuteLaterAction;
import sagemod.potions.UpgradedPotion;
import sagemod.powers.AlchemyExpertPower;
import sagemod.powers.EndlessFearPower;
import sagemod.powers.ExtraPortionPower;
import sagemod.powers.PotionTrancePower;
import sagemod.powers.TasteThisOnePower;
import sagemod.powers.Thirsty;
import sagemod.relics.Blowpipe;

public class PotionListener implements PrePotionUseSubscriber, PostPotionUseSubscriber {

	private AbstractMonster getHoveredMonster() {
		return (AbstractMonster) ReflectionHacks.getPrivate(AbstractDungeon.topPanel.potionUi,
				PotionPopUp.class, "hoveredMonster");
	}

	private void multiplyPotencyBy(AbstractPotion p, float multiplier) {
		if (p instanceof UpgradedPotion) {
			((UpgradedPotion) p).multiplyPotencyBy(multiplier);
			return;
		}
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
		blowpipe(p);
		thirsty(p, getHoveredMonster());
		alchemyExpert(p);
	}

	public void postRealPotionUse(AbstractPotion p) {
		tasteThisOne(p);
		potionTrance(p);
		endlessFear(p);
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
			usePotion(p.makeCopy(), monster);
			power.flash();

			AbstractDungeon.actionManager
			.addToBottom(new ReducePowerAction(AbstractDungeon.player,
					AbstractDungeon.player, power, 1));
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

	private void thirsty(AbstractPotion p, AbstractMonster target) {
		if (p.ID.equals(ExplosivePotion.POTION_ID)) {
			for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
				if (mo.hasPower(Thirsty.POWER_ID)) {
					((Thirsty) mo.getPower(Thirsty.POWER_ID)).onExplosivePotionUsed();
				}
			}
		} else if (p.isThrown) {
			if (p.targetRequired) {
				if (target != null && target.hasPower(Thirsty.POWER_ID)) {
					multiplyPotencyBy(p, Thirsty.MULTIPLIER);
					target.getPower(Thirsty.POWER_ID).flash();
				}
			} else {
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

	private void potionTrance(AbstractPotion p) {
		if (AbstractDungeon.player.hasPower(PotionTrancePower.POWER_ID)) {
			AbstractPower power = AbstractDungeon.player.getPower(PotionTrancePower.POWER_ID);
			AbstractDungeon.actionManager
			.addToBottom(new DrawCardAction(AbstractDungeon.player, power.amount));
			power.flash();
		}
	}

	private void blowpipe(AbstractPotion p) {
		if (AbstractDungeon.player.hasRelic(Blowpipe.ID) && p.isThrown && p.targetRequired) {
			Blowpipe relic = (Blowpipe) AbstractDungeon.player.getRelic(Blowpipe.ID);
			AbstractMonster target = getHoveredMonster();
			if (target == null) {
				return;
			}
			for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
				if (target != mo) {
					AbstractPotion copy = p.makeCopy();
					thirsty(copy, mo);
					copy.use(mo);
					relic.flash();
					relic.appearAbove(mo);
				}
			}
		}
	}

	private void endlessFear(AbstractPotion p) {
		if (AbstractDungeon.player.hasPower(EndlessFearPower.POWER_ID)) {
			if (!p.ID.equals(FearPotion.POTION_ID)) {
				AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(() -> {
					AbstractDungeon.player.obtainPotion(new FearPotion());
					AbstractDungeon.player.getPower(EndlessFearPower.POWER_ID).flash();
				}));
			}
		}

	}
}
