package sagemod.potions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.potions.BottledMiracle;
import com.megacrit.cardcrawl.relics.SacredBark;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

@SpirePatch(clz = BottledMiracle.class, method = SpirePatch.CONSTRUCTOR)
public class UpgradedBottledMiracle {

	public static void Raw(CtBehavior ctB) {
		CtClass clazz = ctB.getDeclaringClass();
		try {
			CtMethod onUpgrade = CtNewMethod.make(
					"public void  _sagemod_upgradedInitializeData() {" + 
							"sagemod.potions.UpgradedBottledMiracle._sagemod_onPotionUpgrade(this);}",clazz);
			clazz.addMethod(onUpgrade);
		} catch (CannotCompileException ex) {
			ex.printStackTrace();
		}
		
		try {
			CtMethod onUpgrade = CtNewMethod.make(
					"public void  _sagemod_upgradedUse(com.megacrit.cardcrawl.core.AbstractCreature c) {" + 
							"sagemod.potions.UpgradedBottledMiracle._sagemod_upgradedUse(this, c);}",clazz);
			clazz.addMethod(onUpgrade);
		} catch (CannotCompileException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void _sagemod_onPotionUpgrade(BottledMiracle b) {
		int descIndex = (AbstractDungeon.player != null
				&& AbstractDungeon.player.hasRelic(SacredBark.ID)) ? 1 : 0;
		b.description =  CardCrawlGame.languagePack
				.getPotionString("sagemod:UPGRADED:" + BottledMiracle.POTION_ID).DESCRIPTIONS[descIndex];
		b.tips.clear();
        b.tips.add(new PowerTip(b.name, b.description));
	}
	
	public static void _sagemod_upgradedUse(BottledMiracle b, AbstractCreature c) {
		if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			Miracle miracle = new Miracle();
			miracle.upgrade();
			AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(miracle, b.getPotency()));
		}
	}
}
