package creatures;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.random;
import static java.lang.Math.sin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;


public class BouncingCreature extends AbstractCreature {

	private static final double MIN_SPEED = 3;
	private static final double MAX_SPEED = 10;

	/**
	 * Number of cycles after which we apply some random noise.
	 */
	private static final int NUMBER_OF_CYCLES_PER_CHANGE = 30;

	/**
	 * Current step number from the last noise application.
	 */
	protected int currCycle;

	public BouncingCreature(IEnvironment environment, Point2D position, double speed,
			double direction, Color color) {
		super(environment, position);

		this.speed = speed;
		this.direction = direction;
		this.color = color;

		currCycle = 0;
	}

	@Override
	public void act() {
		applyNoise();
		move();
	}

	/**
	 * Every number of cycles we apply some random noise over speed and
	 * direction
	 */
	public void applyNoise() {
		currCycle++;
		currCycle %= NUMBER_OF_CYCLES_PER_CHANGE;

		// every NUMBER_OF_CYCLES_PER_CHANGE we do the change
		if (currCycle == 0) {
			this.speed += ((random() * 2) - 1);

			// maintain the speed within some boundaries
			if (this.speed < MIN_SPEED) {
				this.speed = MIN_SPEED;
			} else if (this.speed > MAX_SPEED) {
				this.speed = MAX_SPEED;
			}

			setDirection(this.direction
					+ ((random() * PI / 2) - (PI / 4)));
		}
	}

	/**
	 * The actual move
	 */
	public void move() {
		Dimension s = environment.getSize();
		
		double newX = position.getX() + speed * cos(direction);
		// the reason there is a minus instead of a plus is that in our plane
		// Y coordinates rises downwards
		double newY = position.getY() - speed * sin(direction);

		double hw = s.getWidth() / 2;
		double hh = s.getHeight() / 2;

		if (newX < -hw) {
			newX = -hw;
			setDirection(-PI - direction);
		} else if (newX > hw) {
			newX = hw;
			setDirection(-PI - direction);
		}
		
		if (newY < -hh) {
			newY = -hh;
			setDirection(PI * 2 - direction);
		} else if (newY > hh) {
			newY = hh;
			setDirection(PI * 2 - direction);
		}

		setPosition(newX, newY);
	}

}
