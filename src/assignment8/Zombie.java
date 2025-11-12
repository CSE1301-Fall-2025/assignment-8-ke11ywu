package assignment8;

import edu.princeton.cs.introcs.StdDraw;
import support.cse131.NotYetImplementedException;

public class Zombie extends Entity {

	public static final double ZOMBIE_SPEED = 0.091;

	/**
	 * Create a new Zombie object
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 */
	public Zombie(double x, double y) {
		super(x, y, true, ZOMBIE_SPEED);
	}

	/**
	 * Grow the Zombie after consuming a Nonzombie
	 */
	public void consumeNonzombie() {
		if (this.getRadius() < 0.02) {
			this.setRadius(this.getRadius() * 1.20);
		} else {
			this.setRadius(0.02);
		}
	}

	/**
	 * Draw the Zombie
	 */
	public void draw() {
		StdDraw.setPenColor(0, 90, 0);
		StdDraw.filledCircle(this.getX(), this.getY(), this.getRadius());
	}

	/**
	 * Update the Zombie
	 * 
	 * @param entities the array of Entity objects in the simulation, consumed or
	 *                 not
	 * @return the new Entity object to take the place of the current one
	 */
	public Entity update(Entity[] entities) {
		boolean hasNonzombies = false;
		for (Entity e : entities) {
			if (e.isAlive() == true && e.isZombie() == false) {
				hasNonzombies = true;
				break;
			}
		}
		if (hasNonzombies == true) {
			this.moveToward(findClosestNonzombie(entities));
			this.checkBounds();
		}
		return this;
	}
}
