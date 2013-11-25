package creatures;

import static commons.Utils.filter;
import static java.lang.Math.abs;
import static java.lang.Math.toDegrees;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Random;

import commons.Utils.Predicate;
import creatures.visual.CreatureSimulator;

public class Herbivore extends AbstractCreature {

	private static int MIN_LIFE;
	private static int MAX_LIFE;

	private static int BIRTH_RATE;
	private static int ESCAPE_RATE;
	private static int LIFE_DEGEN_SPEED;
	private static int SEED_GATHERING_RATE;

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

			return abs(dirAngle) < (observer.getFieldOfView())
					&& observer.distanceFromAPoint(input.getPosition()) <= observer
					.getLengthOfView();
		}
	}


	/** life of the herbivore **/
	protected double life ;

	/** a value between 0 and 100 used to slow down */
	protected double life_delay = 100;

	/** consequences of hungry on life **/
	protected double hungry ;

	/** Minimal distance between this creature and the ones around. */
	private final static double MIN_DIST = 10d;

	/** Minimal speed in pixels per loop. */
	private final static double MIN_SPEED = 3d;

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

	public Herbivore(IEnvironment environment, Point2D position, double direction, double speed, 
			Color color) {
		super(environment, position);
		this.direction = direction;
		this.speed = speed;
		this.color = Color.blue;
		maxNeighbor = 10 ;
		seeds = 0 ;
		this.life = MIN_LIFE + new Random().nextInt(MAX_LIFE - MIN_LIFE + 1);
		feeding = false ;
		time = 0 ;
		wait = 5000 ;
		digestTime = 5000 ;
		feeded = false ;
		hungry = 1 ;
	}

	public void eatPlant(Plant plant) {
		if (feeding == false) {
			time = System.currentTimeMillis();
			feeding = true ;
			color = Color.red;
		}
		if (System.currentTimeMillis() < time + wait) {
			this.speed = 0 ;
			if(new Random().nextInt(100) < SEED_GATHERING_RATE){
				seeds += 1;
				plant.isEaten();
			}
		}else{
			feeding = false ;
			color = Color.blue;
			feeded = true ;
		}
	}

	public void act() {
		if(life <= 0){
			CreatureSimulator cs = (CreatureSimulator)this.getEnvironment();
			cs.removeCreature(this);
		} else {
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

			//check end of feeded
			if (System.currentTimeMillis() > time + wait + digestTime)
				feeded = false ;

			// iterate over all nearby herbivores
			Iterable<ICreature> creatures = herbivoresAround(this);
			int count = 0;
			for (ICreature c : creatures) {
				if (c.getClass() == Herbivore.class) {
					if(c.getSpeed() > 0){
						avgSpeed += c.getSpeed();
						avgDir += c.getDirection();
						minDist = Math.min(minDist, c.distanceFromAPoint(getPosition()));
						count++;				
					}
				}else{
					if (this.distanceFromAPoint(c.getPosition()) <= this.visionDistance
							&& (System.currentTimeMillis() > time + digestTime)
							&& (c.getClass() == Plant.class)
							&& (feeded == false)
							) {
						eatPlant((Plant) c);
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
					//if the creature don't like people, it moves out
					if (rand.nextInt(100) <= ESCAPE_RATE) {
						escapeDir = this.direction + Math.toRadians(135) ;
						incX = speed * Math.cos(escapeDir);
						incY = - speed * Math.sin(escapeDir);
						move(incX, incY);
					}
				}else{
					// we should not move closer than dist - MIN_DIST
					if(feeding){}
					else {
						move(incX, incY);
					}
				}
			}
			if(feeding){
				if(new Random().nextInt(100) < BIRTH_RATE){
					CreatureSimulator cs = (CreatureSimulator)this.getEnvironment();
					cs.addCreature(new Herbivore(cs, position, count, avgSpeed, color));
				}		
			} else {
				if(seeds > 0){
					plantSeed();
				}
				decreaseLife(LIFE_DEGEN_SPEED);
			}
		}
	}

	public double getLife() {
		return life;
	}

	public Iterable<ICreature> herbivoresAround(
			Herbivore herbivore) {
		return filter(environment.getCreatures(), new HerbivoresAroundHerbivore(this));
	}
	
	private void plantSeed(){
		seeds -= 1;
		double behind = (this.getDirection() + Math.PI) % (2 * Math.PI);
		CreatureSimulator cs = (CreatureSimulator)this.getEnvironment();
		Point2D point = new Point2D.Double(this.position.getX() + Math.cos(behind) * this.fieldOfView * 3, this.position.getY() + Math.sin(behind) * this.fieldOfView * 3);
		Plant p = new Plant(cs, point, 0, 0, Color.green);
		cs.addCreature(p);
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

	public static void setBirthRate(int rate){
		BIRTH_RATE = rate;
	}

	public static void setEscapeRate(int rate){
		ESCAPE_RATE = rate;
	}

	public static void setLifeDegenSpeed(int value){
		LIFE_DEGEN_SPEED = value;
	}

	public static void setSeedGatheringRate(int value){
		SEED_GATHERING_RATE = value;
	}

	public static void setMinLife(int value){
		MIN_LIFE = value;
	}

	public static void setMaxLife(int value){
		MAX_LIFE = value;
	}

	private void decreaseLife(int value){
		this.life_delay -= value;
		if(life_delay <= 0){
			this.life -= 1;
			life_delay += 100;
		}
	}
}
