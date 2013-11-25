package creatures;

import static commons.Utils.filter;
import static java.lang.Math.abs;
import static java.lang.Math.toDegrees;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Random;

import commons.Utils.Predicate;

public class Herbivore extends AbstractCreature {
	
	/** State eating plant or not **/
	protected boolean eating ;
	
	/** number max of neighbor allowed **/
	protected int maxNeighbor ;
	
	/** number of seeds carried **/
	protected int seeds ;
	
	/** boolean saying if the herbivore just start eating or not **/
	protected boolean feeding ;
	
	/** time when a herbivore saw a plant **/
	double time ;
	
	/** length of the eating time **/
	double wait ;
	
	/** length of the time after eating where a herbivore cannot eat again **/
	double digestTime ;
	
	/** boolean saying if a herbivore is feeded or not **/
	boolean feeded ;
	
	/** probability for a herbivore to move out if there is too much people around him **/
	double asocial ;
	
	/** life of the herbivore **/
	protected double life ;
	
	/** consequences of hungry on life **/
	protected double hungry = 1 ;
	
	/** amount of seed collected while eating **/
	protected double harvest = 1 ;
	
	/** amount of chance in percentage to give birth while eating (0 to 100) **/
	protected int birth = 10 ;
	
	static class HerbivoresAroundHerbivore implements Predicate<ICreature> {
		private final Herbivore observer;

		public HerbivoresAroundHerbivore(Herbivore herbivore) {
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
		this.color = Color.blue;
		maxNeighbor = 10 ;
		seeds = 0 ;
		eating = false ;
		life = 100 ;
		feeding = false ;
		time = 0 ;
		wait = 5000 ;
		digestTime = 5000 ;
		feeded = false ;
		asocial = 10 ;
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

		// iterate over all nearby creatures
		Iterable<ICreature> creatures = creaturesAround(this);
		int count = 0;
		for (ICreature c : creatures) {
			if(c.getSpeed() > 0){
				avgSpeed += c.getSpeed();
				avgDir += c.getDirection();
				minDist = Math.min(minDist, c.distanceFromAPoint(getPosition()));
				count++;
				
				if (this.distanceFromAPoint(c.getPosition()) <= this.visionDistance
						&& c.getClass() == Plant.class
						&& (System.currentTimeMillis() > time + digestTime)
						) {
							//distanceFromAPoint(c.getPosition()
					eating = true ;
				}
				
			}
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
			
			//if there is too much people around
			if (count > maxNeighbor) {
				Random rand = new Random();
				//if the creature don't life people, she moves out
				if (rand.nextInt(100) <= asocial) {
					escapeDir = this.direction + Math.toRadians(135) ;
					incX = speed * Math.cos(escapeDir);
					incY = - speed * Math.sin(escapeDir);
					move(incX, incY);
				}
			}else{
				// we should not move closer than dist - MIN_DIST
				move(incX, incY);
			}
		}
		
		
		if (eating == true) {			
			//if the herbivore just saw the plant and not actually start eating
			if (feeding == false && eating == true) {
				time = System.currentTimeMillis() ;
				feeding = true ;
				this.speed = 0 ;
				seeds += harvest ;
				//reduce plant life ?
				
				//probability to create a new herbivore while stoping
				Random rand = new Random() ;
				int newOne = rand.nextInt(100);
				//TODO
				//reduce plant life ?
				if (newOne < birth) new HerbivoresAroundHerbivore(this) ;
			}else if (eating == true && feeding == true) {
				if (System.currentTimeMillis() < time + wait) {
					this.speed = 0 ;
					seeds += harvest ;
					//reduce plant life ?
					Random rand = new Random() ;
					int newOne = rand.nextInt(100);
					//TODO
					if (newOne < birth) new HerbivoresAroundHerbivore(this) ;
				}else{ //time's up
					feeding = false ;
					eating = false ;
					feeded = true ;
				}
			}
			
			
		}else{
			//amount of life lost when hungry
			life -= hungry ;
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
			if (this.life <= 0) {
				//this.die();
			}
		}
	}

	public double getLife() {
		return life;
	}

	public Iterable<ICreature> creaturesAround(
			Herbivore herbivore) {
		return filter(environment.getCreatures(), new HerbivoresAroundHerbivore(this));
	}
	
	@Override
	public void paint(Graphics2D g2) {
		// center the point
		g2.translate(position.getX(), position.getY());
		// center the surrounding rectangle
		g2.translate(-size / 2, -size / 2);
		// center the arc
		// rotate towards the direction of our vector
		g2.rotate(-direction, size / 2, size / 2);

		// useful for debugging
		// g2.drawRect(0, 0, size, size);

		// set the color
		g2.setColor(color);
		// we need to do PI - FOV since we want to mirror the arc
		g2.fillArc(0, 0, size, size, (int) toDegrees(-fieldOfView / 2),
				(int) toDegrees(fieldOfView));

	}
	
}
