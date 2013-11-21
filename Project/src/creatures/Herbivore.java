package creatures;

import static commons.Utils.filter;
import static java.lang.Math.abs;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Random;

import commons.Utils.Predicate;

public class Herbivore extends AbstractCreature {
	
	static class CreaturesAroundCreature implements Predicate<ICreature> {
		private final Herbivore observer;

		public CreaturesAroundCreature(Herbivore herbivore) {
			this.observer = herbivore;
		}

		@Override
		public boolean apply(ICreature input) {
			if (input == observer) {
				return false;
			}
			double dirAngle = input.directionFormAPoint(observer.getPosition(),
					observer.getDirection());

			return abs(dirAngle) < (observer.getFieldOfView() / 2)
					&& observer.distanceFromAPoint(input.getPosition()) <= observer
							.getLengthOfView();

		}
	}


	/** Minimal distance between this creature and the ones around. */
	private final static double MIN_DIST = 10d;

	/** Minimal speed in pixels per loop. */
	private final static double MIN_SPEED = 3d;

	public Herbivore(IEnvironment environment, Point2D position, double direction, double speed, 
			Color color) {
		super(environment, position);
		this.direction = direction;
		this.speed = speed;
		this.color = color;
		maxNeighbor = 10 ;
		seeds = 0 ;
		eating = false ;
		life = 100 ;
	}

	public void act() {
		// speed - will be used to compute the average speed of the nearby
		// creatures including this instance
		double avgSpeed = speed;
		// direction - will be used to compute the average direction of the
		// nearby creatures including this instance
		double avgDir = direction;
		// direction for creatures moving away of the group
		double escapeDir ;
		// distance - used to find the closest nearby creature
		double minDist = Double.MAX_VALUE;
		
		//double lifeZone = minDist * 10 ; //espace vital
		
		double time ; // heure au moment de l arret
		double wait = 5000 ; // duree de l attente

		// iterate over all nearby creatures
		Iterable<ICreature> creatures = creaturesAround(this);
		int count = 0;
		for (ICreature c : creatures) {
			avgSpeed += c.getSpeed();
			avgDir += c.getDirection();
			minDist = Math.min(minDist, c.distanceFromAPoint(getPosition()));
			count++;
		}
		
		// average
		avgSpeed = avgSpeed / (count + 1);
		// min speed check
		if (avgSpeed < MIN_SPEED) {
			avgSpeed = MIN_SPEED;
		}
		// average
		avgDir = avgDir / (count + 1);

		// apply - change this creature state
		this.direction = avgDir;
		this.speed = avgSpeed;
		
		// if we are not too close move closer
		if (minDist > MIN_DIST) {
			// we move always the maximum
			double incX = speed * Math.cos(avgDir);
			double incY = - speed * Math.sin(avgDir);
			
			if (count > maxNeighbor) {
				escapeDir = this.direction + Math.toRadians(120) ;
				incX = speed * Math.cos(escapeDir);
				incY = - speed * Math.sin(escapeDir);
				move(incX, incY);
			}else{
				// we should not moved closer than a dist - MIN_DIST
				move(incX, incY);
			}
		}
		
		//TODO
		/*
		 * if (this.fieldOfView voit de la bouffe) eating = true ;
		 */
		
		if (eating == false) {
			//amount of life lost when hungry
			life -= hungry ;
			//TODO //par qui ?
			if (this.life <= 0) {
				//this.die;
				return ; //faudra mettre le cas faux en dernier, pour simplifier les choses
			}
			//probability to plant a seed while traveling
			if (seeds > 0) {
				Random rand = new Random();
				if (rand.nextInt(100) <= 10 ) {
					//TODO
					/* on plante une nouvelle plante, faut faire la fonction */
					//newPlant(this.position);
					seeds -= 1 ;
				}
			}
		}else{
			//Happy Meal || Itadakimasu
			//TODO là on bloque tout avec le while ... :s
			time = System.currentTimeMillis() ;
			
			while (time + wait > System.currentTimeMillis() ) {
				this.speed = 0 ;
				seeds += harvest ;
				Random rand = new Random() ;
				int newOne = rand.nextInt(100);
				//TODO
				if (newOne < birth) new CreaturesAroundCreature(this) ;
			}
			
		}
	}

	public Iterable<ICreature> creaturesAround(
			Herbivore herbivore) {
		return filter(environment.getCreatures(), new CreaturesAroundCreature(this));
	}
	
}