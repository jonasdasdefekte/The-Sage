package sagemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;


/**
 * Copied from <a href=
 * "https://github.com/twanvl/sts-mad-science-mod/blob/master/src/main/java/madsciencemod/actions/common/HandToTopOfDrawPileAction.java">MadScientist</a>
 *
 * <pre>
MIT License

Copyright (c) 2018 Twan van Laarhoven

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 * </pre>
 */
public class HandToTopOfDrawPileAction extends AbstractGameAction {

	private AbstractPlayer p = AbstractDungeon.player;
	private boolean upgrade;
	private int minAmount, maxAmount;
	private String title;

	public HandToTopOfDrawPileAction(int minAmount, int maxAmount, boolean upgrade, String title) {
		duration = Settings.ACTION_DUR_FAST;
		actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.upgrade = upgrade;
		this.title = title;
	}

	@Override
	public void update() {
		if (duration == Settings.ACTION_DUR_FAST) {
			if (p.hand.isEmpty()) {
				isDone = true;
				return;
			}
			if (p.hand.size() == 1 && minAmount >= 1) {
				AbstractCard c = p.hand.getTopCard();
				if (upgrade && c.canUpgrade()) {
					c.upgrade();
				}
				p.hand.moveToDeck(c, false);
				AbstractDungeon.player.hand.refreshHandLayout();
				isDone = true;
				return;
			}
			AbstractDungeon.handCardSelectScreen.open(title, maxAmount,
					minAmount < maxAmount, minAmount == 0);
			tickDuration();
			return;
		}
		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
				if (upgrade && c.canUpgrade()) {
					c.upgrade();
				}
				p.hand.moveToDeck(c, false);
			}
			AbstractDungeon.player.hand.refreshHandLayout();
			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
		}
		tickDuration();
	}

}
