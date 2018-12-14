package sagemod.events;

import static sagemod.SageMod.EVENT_FOLDER;
import static sagemod.SageMod.getExistingOrPlaceholder;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import sagemod.relics.FlyingCarpet;

public class CarpetTrader extends AbstractImageEvent {

	public static final String ID = "Carpet_Trader";
	private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
	private static final String NAME = eventStrings.NAME;
	private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
	private static final String[] OPTIONS = eventStrings.OPTIONS;

	private CurrentScreen curScreen = CurrentScreen.INTRO;

	private enum CurrentScreen {
		INTRO, MAIN, DONE
	}

	public CarpetTrader() {
		super(NAME, DESCRIPTIONS[0], getExistingOrPlaceholder(EVENT_FOLDER, ID, ".jpg"));
		imageEventText.setDialogOption(OPTIONS[0]);
	}

	@Override
	protected void buttonEffect(int button) {
		AbstractPlayer player = AbstractDungeon.player;
		switch (curScreen) {
			case INTRO:
				imageEventText.updateBodyText(DESCRIPTIONS[1]);
				imageEventText.updateDialogOption(0, OPTIONS[2]);
				imageEventText.setDialogOption(OPTIONS[1]);
				curScreen = CurrentScreen.MAIN;
				break;
			case MAIN:
				switch (button) {
					case 0:
						int hpLoss = (int) (player.maxHealth * 0.7F);
						player.maxHealth = hpLoss;
						if (player.maxHealth <= 0) {
							player.maxHealth = 1;
						}

						if (player.currentHealth > player.maxHealth) {
							player.currentHealth = player.maxHealth;
						}

						RelicLibrary.getRelic(FlyingCarpet.ID).instantObtain();
						imageEventText.updateBodyText(DESCRIPTIONS[3]);
						imageEventText.clearAllDialogs();
						imageEventText.setDialogOption(OPTIONS[3]);
						break;
					case 1:
					default:
						imageEventText.updateBodyText(DESCRIPTIONS[2]);
						imageEventText.clearAllDialogs();
						imageEventText.setDialogOption(OPTIONS[3]);
						break;
				}
				curScreen = CurrentScreen.DONE;
				break;
			case DONE:
			default:
				openMap();
				break;

		}
	}

}
