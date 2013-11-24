package creatures;

import static commons.Utils.filter;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.toDegrees;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import commons.Utils.Predicate;

public class Plant extends AbstractCreature{
	
	private static int MIN_SEED = 3;
	private static int MAX_SEED = 10;
	private int life;
	
	public int TIME_DEGEN_LIFE = 1;
	
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
	//	life = (int) (MIN_SEED + Math.random()*(MAX_SEED - MIN_SEED));
		life = 100;
		this.color = Color.green;
	}

	public void decreaseLife(int value){
		life -= value;
	}
	
	public int getLife(){
		return this.life;
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
			  decreaseLife(TIME_DEGEN_LIFE);  
			  break;
		}
	}
	
	//Return filtered plants list
	public Iterable<ICreature> plantsAround(Plant plant) {
		return filter(environment.getCreatures(), new PlantsAroundPlant(this));
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
		g2.fillOval(-life, -life, life * 2, life * 2);
	}
}
