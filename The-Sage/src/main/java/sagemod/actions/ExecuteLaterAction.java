package sagemod.actions;

import java.util.Objects;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class ExecuteLaterAction extends AbstractGameAction {

	private Runnable toRun;

	public ExecuteLaterAction(Runnable toRun) {
		this.toRun = Objects.requireNonNull(toRun, "toRun must not be null");
		duration = Settings.ACTION_DUR_XFAST;
		actionType = ActionType.SPECIAL;
	}

	@Override
	public void update() {
		if (duration == Settings.ACTION_DUR_XFAST) {
			isDone = true;
			toRun.run();
		}
	}

}
