package sagemod.listeners;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import basemod.interfaces.PostPowerApplySubscriber;
import sagemod.patches.PatchesForVirus;
import sagemod.powers.Virus;

public class VirusListener implements PostPowerApplySubscriber {

	@Override
	public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target,
			AbstractCreature source) {
		if (power.ID.equals(PoisonPower.POWER_ID) && target.hasPower(Virus.POWER_ID)) {
			PoisonPower po = (PoisonPower) power;
			if (PatchesForVirus.appliedByVirus.get(po)) {
				return;
			}
			for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
				if (mo != target && AbstractDungeon.player != target) {
					PoisonPower newPoison =
							new PoisonPower(mo, target, power.amount);
					PatchesForVirus.appliedByVirus.set(newPoison, true);
					AbstractDungeon.actionManager.addToBottom(
							new ApplyPowerAction(mo, target, newPoison,
									power.amount));
				}
			}
		}
	}


}
