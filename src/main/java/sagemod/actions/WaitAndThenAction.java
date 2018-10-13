package sagemod.actions;

import java.util.Objects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

/**
 * Creates an action that ticks its duration and then executes a runnable
 */
public class WaitAndThenAction extends AbstractGameAction {

	private boolean executedRunnable;
	private Runnable run;

	public WaitAndThenAction(float duration, Runnable run) {
		Objects.requireNonNull(run, "run may not be null");
		setValues(null, null, 0);
		this.duration = duration;
		executedRunnable = false;
		this.run = run;
	}

	@Override
	public void update() {
		tickDuration();
		if (isDone && !executedRunnable) {
			executedRunnable = true;
			run.run();
		}
	}

}
