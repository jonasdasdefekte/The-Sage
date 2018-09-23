package sagemod.powers;

import java.util.ArrayList;
import java.util.Collections;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class Brew extends AbstractSagePower {

	public static final String POWER_ID = "Brew";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private ArrayList<Potion> potions;

	public Brew(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		potions = new ArrayList<>();
	}

	@Override
	public void atStartOfTurn() {
		ArrayList<Potion> toRemove = new ArrayList<>();
		potions.forEach(p -> {
			if (p.turns-- <= 1) {
				toRemove.add(p);
				if (owner instanceof AbstractPlayer) {
					AbstractPlayer player = (AbstractPlayer) owner;
					player.obtainPotion(p.potion);
					flash();
					// TODO maybe if player can not gain potion let him use or discard it or just
					// increase the turns by 1 again and let the player say something
				}
			}
		});
		potions.removeAll(toRemove);
		Collections.sort(potions);
		if (potions.isEmpty()) {
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
		} else {
			amount = potions.get(0).turns;
		}
		updateDescription();
	}

	@Override
	public void updateDescription() {
		// TODO add alternative for when there are too many potions
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < potions.size(); i++) {
			Potion p = potions.get(i);
			builder.append(DESCRIPTIONS[0]);
			builder.append(p.potion.name);
			if (p.turns > 1) {
				builder.append(DESCRIPTIONS[1]);
				builder.append(p.turns);
				builder.append(DESCRIPTIONS[2]);
			} else {
				builder.append(DESCRIPTIONS[3]);
			}
			if (i != potions.size() - 1) {
				builder.append(DESCRIPTIONS[4]);
			}
		}
		description = builder.toString();
	}

	public void addPotion(int turns, AbstractPotion potion) {
		Potion p = new Potion(turns, potion);
		if (turns < amount) {
			amount = turns;
		}
		potions.add(p);
		Collections.sort(potions);
		updateDescription();
	}

	public static void addPotion(int turns, AbstractPotion potion, AbstractCreature owner) {
		if (turns <= 0) {
			AbstractDungeon.player.obtainPotion(potion);
		} else {
			Brew power = null;
			if (owner.hasPower(POWER_ID)) {
				power = (Brew) owner.getPower(POWER_ID);
				power.addPotion(turns, potion);
				power.flash();
			} else {
				power = new Brew(owner, turns);
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, power, turns));
				power.addPotion(turns, potion);
			}
		}
	}

	class Potion implements Comparable<Potion> {

		int turns;
		AbstractPotion potion;

		public Potion(int turns, AbstractPotion potion) {
			this.turns = turns;
			this.potion = potion;
		}

		@Override
		public int compareTo(Potion o) {
			return turns - o.turns;
		}

	}

}
