package creatures;

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

import creatures.visual.CreatureSimulator;

public class BouncingCreatureTest {
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 100;
	final double h = 100;
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
	}

	@Test
	public void testUpperRightCorner() throws Exception {
		BouncingCreature creature = new BouncingCreature(environment, new Point2D.Double(w/2, -h/2), 1, toRadians(45), Color.RED);
		creature.move();
		
		assertEquals(toRadians(45+180), creature.getDirection(), 0.01);
		assertEquals(w/2, creature.getPosition().getX(), .01);
		assertEquals(-h/2, creature.getPosition().getY(), .01);
    }	
	
	@Test
	public void testDirectBottom() throws Exception {
		BouncingCreature creature = new BouncingCreature(environment, new Point2D.Double(w/2, h/2), 1, toRadians(270), Color.RED);
		creature.move();

		assertEquals(toRadians(90), creature.getDirection(), 0.01);
		assertEquals(w/2, creature.getPosition().getX(), .01);
		assertEquals(h/2, creature.getPosition().getY(), .01);
		
	}

	@Test
	public void testSlankingBottom() throws Exception {
		BouncingCreature creature = new BouncingCreature(environment, new Point2D.Double(w/2, h/2), 1, toRadians(180+45), Color.RED);
		creature.move();

		assertEquals(toRadians(90+45), creature.getDirection(), 0.01);
		assertEquals(h/2, creature.getPosition().getY(), .01);		
	}
	
}
