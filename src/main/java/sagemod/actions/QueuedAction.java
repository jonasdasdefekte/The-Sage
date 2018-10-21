package sagemod.actions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class QueuedAction extends AbstractGameAction {

	private Queue<AbstractGameAction> queue;

	public QueuedAction(AbstractGameAction... actions) {
		queue = new LinkedList<>(Arrays.asList(actions));
	}

	@Override
	public void update() {
		if (!isDone) {
			AbstractGameAction action = queue.peek();
			if (action != null && !action.isDone) {
				action.update();
				if (action.isDone) {
					queue.poll();
				}
			}
		}
		if (queue.isEmpty()) {
			isDone = true;
		}
	}

}
