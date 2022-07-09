package com.zamron.world.content.combat;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Locations;
import com.zamron.model.Locations.Location;
import com.zamron.world.entity.impl.Character;

public class CombatDistanceTask extends Task {

	/** The combat builder. */
	private CombatBuilder builder;

	/** The victim being hunted. */
	private Character victim;

	/**
	 * Create a new {@link CombatDistanceTask}.
	 * 
	 * @param builder
	 *            the combat builder.
	 * @param victim
	 *            the victim being hunted.
	 */
	public CombatDistanceTask(CombatBuilder builder, Character victim) {
		super(1, builder, true);
		this.builder = builder;
		this.victim = victim;
	}

	@Override
	public void execute() {
		
		builder.determineStrategy();
		builder.attackTimer = 0;
		builder.cooldown = 0;

		if(builder.getVictim() != null && !builder.getVictim().equals(victim)) {
			builder.reset(true);
			this.stop();
			return;
		}
		
		if(!Location.ignoreFollowDistance(builder.getCharacter())) {
			if (!builder.getCharacter().getPosition().isViewableFrom(victim.getPosition())) {
				builder.reset(true);
				this.stop();
				return;
			}
		}
		
		if(Locations.goodDistance(builder.getCharacter().getPosition(), victim.getPosition(), builder.getStrategy().attackDistance(builder.getCharacter()))) {
			sucessFul();
			this.stop();
			return;
		}
	}
	
	@Override
	public void stop() {
		setEventRunning(false);
		builder.setDistanceTask(null);
	}
	
	public void sucessFul() {
		builder.getCharacter().getMovementQueue().reset();
		builder.setVictim(victim);
		if (builder.getCombatTask() == null || !builder.getCombatTask().isRunning()) {
            builder.setCombatTask(new CombatHookTask(builder));
            TaskManager.submit(builder.getCombatTask());
        } else {
        	builder.getCombatTask().stop();
        	sucessFul();
        }
	}
}
