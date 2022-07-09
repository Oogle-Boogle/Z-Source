package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.CombatIcon;
import com.zamron.model.Direction;
import com.zamron.model.Flag;
import com.zamron.model.Graphic;
import com.zamron.model.Hit;
import com.zamron.model.Hitmask;
import com.zamron.model.Position;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 * 
 * @author Emerald
 * Start date: 31.05.2019
 * 
 */

public class SuicsBoss implements CombatStrategy {
	
	private enum Stage {
		FIRST,
		SECOND;
	}
	
	private static Stage stage = Stage.FIRST;

	@Override
	public boolean canAttack(Character entity, Character victim) {
		
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		
		return null;
	}
	
	static int tick = 0;
	
	//public static boolean attackable = true; // needs tob e static
	
	private static boolean onSecondSide;
	
	private final Position firstSide = new Position(2713, 9186);
	private final Position secondSide = new Position(2719, 9185);
	
	public static int switchIndex = 0; // needs to be static
	
	static int firstStageTick = 0;
	static int secondStageTick = 0;
	
	private static boolean secondTaskHasFinished = true;
	private static boolean firstTaskHasFinished = true;
	
	public static void resetAll() {
		tick = 0;
		onSecondSide = false;
		switchIndex = 0;
		firstStageTick = 0;
		secondStageTick = 0;
		secondTaskHasFinished = true;
		firstTaskHasFinished = true;
		stage = Stage.FIRST;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC npc = (NPC) entity;
		Player player = (Player) victim;
		
		/**
		 * actual attacks
		 */
		if(stage.equals(Stage.FIRST)) {
			
			if(firstTaskHasFinished) {
				firstTaskHasFinished = false;
				TaskManager.submit(new Task(3, npc, false) {
					@Override
					protected void execute() {
						if(victim == null || !victim.getCombatBuilder().isAttacking()) {
							stop();
							return;
						}
						firstStageTick++;
						switch(firstStageTick) {
							
						case 1:
						case 3:
							victim.performGraphic(new Graphic(500));
							victim.dealDamage(new Hit(Misc.random(850), Hitmask.RED, CombatIcon.MAGIC));
							npc.performAnimation(new Animation(80));
							if(Misc.random(100) > 95) {
							npc.forceChat("DIE!");
							}
							break;
							
						case 5:
						case 6:
						case 7:
						case 8:
						case 9:
							if(Misc.random(100) > 95) {
							npc.forceChat("This could hurt");
							}
							victim.performGraphic(new Graphic(500));
							victim.dealDamage(new Hit(Misc.random(1300), Hitmask.RED, CombatIcon.MAGIC));
							break;
							
						case 10:
						case 11:
						case 12:
						case 13:
						case 14:
						case 15:
						case 16:
						case 17:
							if(Misc.random(100) > 95) {
							npc.forceChat("Hello fella, this is my noob attack :(");
							}
							victim.dealDamage(new Hit(Misc.random(650)));
							npc.performAnimation(new Animation(80));
							break;
						}
						
						if(firstStageTick > 17) {
							firstStageTick = 0;
							firstTaskHasFinished = true;
							stop();
							return;
						}
					}
				});
			}
		} else {
			if(secondTaskHasFinished) {
				secondTaskHasFinished = false;
			TaskManager.submit(new Task(3, npc, false) {
				@Override
				protected void execute() {
					if(victim == null) {
						//System.out.println("Victim was null, so stopped.");
						stop();
						return;
					}
					secondStageTick++;
					switch(secondStageTick) {
					case 1:
					case 3:
					case 5:
					case 7:
					case 9:
					case 11:
						new Projectile(npc, victim, 500, 44, 2, 75, 43, 0).sendProjectile();
						victim.dealDamage(new Hit(Misc.random(750), Hitmask.RED, CombatIcon.MAGIC));
						break;
					case 15:
					case 17:
					case 19:
					case 21:
					case 23:
						new Projectile(npc, victim, 500, 44, 2, 75, 43, 0).sendProjectile();
						victim.dealDamage(new Hit(Misc.random(950), Hitmask.RED, CombatIcon.MAGIC));
						break;
						
					case 25:
					case 26:
					case 27:
					case 28:
					case 29:
					case 30:
						new Projectile(npc, victim, 500, 44, 2, 75, 43, 0).sendProjectile(); // (Final)
						victim.dealDamage(new Hit(Misc.random(950), Hitmask.RED, CombatIcon.MAGIC));
						break;
					}
					if(secondStageTick == 14) {
						npc.forceChat("TRANSFORMING!!!");
						npc.setTransformationId(6599);
						npc.getUpdateFlag().flag(Flag.TRANSFORM);
					}
					
					if(secondStageTick == 24) {
						npc.forceChat("TRANSFORMING!!!");
						npc.setTransformationId(6603);
						npc.getUpdateFlag().flag(Flag.TRANSFORM);
					}
					
					if(secondStageTick > 30) {
						npc.forceChat("TRANSFORMING!!!");
						npc.setTransformationId(6595);
						npc.getUpdateFlag().flag(Flag.TRANSFORM);
						secondStageTick = 0;
						secondTaskHasFinished = true;
						stop();
						return;
					}
					//System.out.println("Task has finished, status: " + secondTaskHasFinished);
				}
				
			});
		}
		}
		
		/**
		 * Transformation
		 */
		
		if(npc.getConstitution() < 3000000 && switchIndex == 0 || entity.getConstitution() < 500000 && switchIndex == 1) {
			if(switchIndex == 1) { 
			stage = Stage.FIRST;
			} else {
				stage = Stage.SECOND;
			}
			switchIndex++;
			victim.getCombatBuilder().reset(true);
			TaskManager.submit(new Task(2, npc, true) {
				@Override
				protected void execute() {
					World.getPlayers().forEach(x -> {
						if(x.inDragon) {
							player.attackable = false;
						}
					});
					switch(tick) {
					case 0:
					case 1:
					case 2:
					npc.performGraphic(new Graphic(onSecondSide ? 3001 : 3000));
						break;
						
					case 3:
						npc.moveTo(onSecondSide ? firstSide : secondSide);
						onSecondSide = !onSecondSide ? true : false;
						npc.setDirection(onSecondSide ? Direction.EAST : Direction.WEST);
						break;
						
					case 5:
						npc.setTransformationId(onSecondSide ? 6595 : 6593);
						npc.forceChat("TRANSFORMING!!!");
						npc.getUpdateFlag().flag(Flag.TRANSFORM);
						break;
						
					case 6:
						stop();
						player.attackable = true;
						tick = 0;
						break;
					}
					tick++;
					
				}
			});
		}
		return true;
	}

	@Override
	public int attackDelay(Character npc) {
		return npc.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character npc) {
		return 5;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}

}
