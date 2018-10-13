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
