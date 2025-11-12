package assignment8;

import edu.princeton.cs.introcs.StdDraw;
import support.cse131.NotYetImplementedException;

public class Nonzombie extends Entity {

	public static final double NONZOMBIE_SPEED = 0.01;

	/**
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 */
	public Nonzombie(double x, double y) {
		super(x, y, false, NONZOMBIE_SPEED);
	}

	/**
	 * Create a Zombie object in place of the current Nonzombie
	 * 
	 * @return the new Zombie object
	 */
	public Zombie convert() {
		Zombie newZomb = new Zombie(this.getX(), this.getY());
		return newZomb;
	}

	/**
	 * Draw a Nonzombie
	 */
	public void draw() {
		StdDraw.setPenColor(255, 207, 210);
		StdDraw.point(this.getX(), this.getY());
	}

	/**
	 * Update the Nonzombie
	 * 
	 * @param entities the array of Entity objects in the simulation, consumed or
	 *                 not
	 * @return the new Entity object to take the place of the current one
	 */
	public Entity update(Entity[] entities) {
		boolean hasZombies = false;
		for (Entity e : entities) {
			if (e.isZombie() == true) {
				hasZombies = true;
				break;
			}
		}
		if (hasZombies == true) {
			this.moveAwayFrom(findClosestZombie(entities));
			this.checkBounds();
		}
		if (this.isTouching(findClosestZombie(entities))){
			if (Math.random() < 0.8){
				return this.convert();
			} else {
				findClosestZombie(entities).consumeNonzombie();
				this.wasConsumed();
			}
		}
		return this;
	}

}
