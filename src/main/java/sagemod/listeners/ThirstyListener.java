package sagemod.listeners;

import com.megacrit.cardcrawl.potions.AbstractPotion;

import basemod.interfaces.PostPotionUseSubscriber;
import basemod.interfaces.PrePotionUseSubscriber;

public class ThirstyListener implements PrePotionUseSubscriber, PostPotionUseSubscriber {

	public static volatile boolean wasPotionUsed = false;
	private static final int DELAY = 1000;

	@Override
	public void receivePrePotionUse(AbstractPotion p) {
		wasPotionUsed = true;
	}

	@Override
	public void receivePostPotionUse(AbstractPotion p) {
		// TODO maybe do this with wait action and custom action instead
		new Thread(() -> {
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {} finally {
				wasPotionUsed = false;
			}
		}, "DelayedPotionUseThread").start();

	}

}
