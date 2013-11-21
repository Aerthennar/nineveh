package creatures;

import static commons.Utils.filter;
import static java.lang.Math.abs;
import static java.lang.Math.pow;

import java.awt.Color;
import java.awt.geom.Point2D;

import commons.Utils.Predicate;

public class Plante extends AbstractCreature{
	
	private static int MIN_SEED = 3;
	private static int MAX_SEED = 10;
	private int life = 10;
	
	public int TIME_DEGEN_LIFE;
	
	static class PlantesAroundPlante implements Predicate<ICreature> {
		private final Plante observer;

		public PlantesAroundPlante(Plante observer) {
			this.observer = observer;
		}

		@Override
		public boolean apply(ICreature input) {
			if (input == observer) {
				return false;
			}
			return abs(observer.distanceFromAPoint(input.getPosition())) <= observer.visionDistance;
		}
	}
	
	public Plante (IEnvironment environment, Point2D position, double speed, double direction, Color color) {
		super(environment, position);
		life = (int) (MIN_SEED + Math.random()*(MAX_SEED - MIN_SEED));
		this.color = color;
	}

	public void looseLife(int value){
		life -= value;
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
		Iterable<ICreature> creatures = plantesAround(this);
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
			  //Loose life
			  looseLife(TIME_DEGEN_LIFE);  
			  break;
		}
	}
	
	//Retourne filtred plants list
	public Iterable<ICreature> plantesAround(Plante plante) {
		return filter(environment.getCreatures(), new PlantesAroundPlante(this));
	}
}
