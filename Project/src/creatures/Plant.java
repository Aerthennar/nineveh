package creatures;

import static commons.Utils.filter;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.toDegrees;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Random;

import commons.Utils.Predicate;
import creatures.visual.CreatureSimulator;

public class Plant extends AbstractCreature{

	private static int MIN_SEED;
	private static int MAX_SEED;
	private static int LIFE_DEGEN_SPEED;

	private int life_delay = 100;

	private int life;

	static class PlantsAroundPlant implements Predicate<ICreature> {
		private final Plant observer;

		public PlantsAroundPlant(Plant observer) {
			this.observer = observer;
		}

		@Override
		public boolean apply(ICreature input) {
			if (input == observer || !(input.getClass().isAssignableFrom(Plant.class))) {
				return false;
			}
			return abs(observer.distanceFromAPoint(input.getPosition())) <= observer.visionDistance;
		}
	}

	public Plant (IEnvironment environment, Point2D position, double speed, double direction, Color color) {
		super(environment, position);
		this.life = MIN_SEED + new Random().nextInt(MAX_SEED - MIN_SEED + 1);
		this.color = Color.green;
	}	

	public void decreaseLife(int value){
		life_delay -= value;
		if(life_delay <= 0){
			life -= 1;
			life_delay += 100;
		}
	}
	
	public void isEaten(){
		life -= 1;
	}

	public int getLife(){
		return this.life;
	}

	public void setLife(int life){
		this.life = life;
	}

	/*
	 * (non-Javadoc)
	 * @see simulator.IActionable#act()
	 * Count plants around and if there's :
	 * - 2 -> No action
	 * - 3 -> Change position
	 * - Other -> Loose life 
	 */
	public void act() {
		if(life <= 0){
			CreatureSimulator cs = (CreatureSimulator)this.getEnvironment();
			cs.removeCreature(this);
		} else {
			// iterate over all nearby plants
			Iterable<ICreature> creatures = plantsAround(this);
			int count = 0;
			for (ICreature c : creatures) {
				if (this.distanceFromAPoint(c.getPosition()) <= this.visionDistance){
					count++;
				}
			}
			switch (count)
			{
			case 2:
				//No action
				break;        
			case 3:
				//Change position
				setPosition(position.getX()+Math.random()-0.5, position.getY()+Math.random()-0.5);
				break;        
			default:
				//decrease life
				decreaseLife(LIFE_DEGEN_SPEED);  
				break;
			}
		}
	}


	//Return filtered plants list
	public Iterable<ICreature> plantsAround(Plant plant) {
		return filter(environment.getCreatures(), new PlantsAroundPlant(this));
	}

	public static void setMinSeed(int min){
		MIN_SEED = min;
	}
	public static void setMaxSeed(int max){
		MAX_SEED = max;
	}

	public static void setLifeDegenSpeed(int value){
		LIFE_DEGEN_SPEED = value;
	}

	@Override
	public void paint(Graphics2D g2) {
		// center the point
		g2.translate(position.getX(), position.getY());
		// center the surrounding rectangle
		g2.translate(-size / 2, -size / 2);
		// center the arc
		// rotate towards the direction of our vector

		// useful for debugging
		// g2.drawRect(0, 0, size, size);

		// set the color
		g2.setColor(color);
		g2.fillOval(-life / 2, -life / 2, life, life);
	}
}
