package sagemod.listeners;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import basemod.interfaces.OnCardUseSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;

public class LectureListener implements OnCardUseSubscriber, OnStartBattleSubscriber {

	public static int xCardsPlayedThisCombat;

	@Override
	public void receiveCardUsed(AbstractCard c) {
		if (c.cost == -1 || c.costForTurn == -1) {
			xCardsPlayedThisCombat++;
		}
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		xCardsPlayedThisCombat = 0;
	}


}
