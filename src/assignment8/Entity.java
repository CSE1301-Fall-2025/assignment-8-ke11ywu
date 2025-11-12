package assignment8;

import java.awt.Color;

import edu.princeton.cs.introcs.StdDraw;
import support.cse131.NotYetImplementedException;

public class Entity {
	private double x, y, speed, radius;
	private boolean isZombie, isAlive;

	/*
	 * Default constructor - should not be used
	 */
	public Entity() {
		this.x = 0;
		this.y = 0;
		this.speed = 0;
		isZombie = false;
		isAlive = true;
		this.radius = 0.01;
	}
	
	/**
	 * @param x        the x-coordinate of this Entity's center.
	 * @param y        the y-coordinate of this Entity's center.
	 * @param isZombie the undead state of this Entity.
	 *                 true if zombie, false otherwise.
	 * @param speed	   the entities speed
	 */
	public Entity(double x, double y, boolean isZombie, double speed) {
		// TODO
		this.x = x;
		this.y = y;
		this.isZombie = isZombie;
		this.isAlive = true;
		this.speed = speed;
		this.radius = 0.008;
	}

	/**
	 * @return the center x-coordinate
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * @return the center y-coordinate
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * 
	 * @return the Entity's radius
	 */
	public double getRadius() {
		return this.radius;
	}
	
	
    public void setRadius(double d) {
    	this.radius = d;
	}

	/**
	 * 
	 * @return the status of the Entity
	 */
	public boolean isAlive() {
		return this.isAlive;
	}

	/**
	 * Updates the Entity's consumed status after consumption
	 */
	public void wasConsumed(){
		this.isAlive = false;
	}

	/**
	 * 
	 * @return if the Entity is a Zombie (this is a placeholder - it should never be called)
	 */
	public boolean isZombie() {
		return this.isZombie;
	}


	/**
	 * @param xOther x-coordinate of the other point.
	 * @param yOther y-coordinate of the other point.
	 * @return distance between this Entity's center and the specified other point.
	 */
	public double distanceCenterToPoint(double xOther, double yOther) {
		double underSqrt = Math.pow(this.x - xOther, 2) + Math.pow(this.y - yOther, 2);
		return Math.sqrt(underSqrt);
	}

	/**
	 * @param other the other Entity
	 * @return the distance between this Entity's center and the specified other
	 *         Entity's center.
	 */
	public double distanceCenterToCenter(Entity other) {
		return distanceCenterToPoint(other.getX(), other.getY());
	}

	/**
	 * @param xOther      the x-coordinate of another Entity's center.
	 * @param yOther      the y-coordinate of another Entity's center.
	 * @param radiusOther the radius of another Entity.
	 * @return the distance between this Entity's edge and the specified other
	 *         Entity's edge.
	 */
	public double distanceEdgeToEdge(double xOther, double yOther, double radiusOther) {
		return distanceCenterToPoint(xOther, yOther) - this.radius - radiusOther;
	}

	/**
	 * @param other the other Entity.
	 * @return the distance between this Entity's edge and the specified other
	 *         Entity's edge.
	 */
	public double distanceEdgeToEdge(Entity other) {
		return distanceEdgeToEdge(other.getX(), other.getY(), other.getRadius());
	}

	/**
	 * @param xOther      the x-coordinate of another Entity's center.
	 * @param yOther      the y-coordinate of another Entity's center.
	 * @param radiusOther the radius of another Entity.
	 * @return true if the bounding circle of this Entity overlaps with the bounding
	 *         circle of the specified other Entity, false otherwise.
	 */
	public boolean isTouching(double xOther, double yOther, double radiusOther) {
		if (distanceEdgeToEdge(xOther, yOther, radiusOther) <= 0){
			return true;
		}
		return false;
	}

	/**
	 * @param other the other Entity
	 * @return true if the bounding circle of this Entity overlaps with the bounding
	 *         circle of the specified other Entity, false otherwise.
	 */
	public boolean isTouching(Entity other) {
		return isTouching(other.getX(), other.getY(), other.getRadius());
	}

	/**
	 * @param xOther x-coordinate of the other point.
	 * @param yOther y-coordinate of the other point.
	 * @param amount the amount to move toward the point.
	 */
	public void moveToward(double xOther, double yOther, double amount) {
		double xVector = xOther - getX();
		double yVector = yOther - getY();
		double angle = Math.atan2(yVector, xVector);
		double xAmount = amount * Math.cos(angle);
		double yAmount = amount * Math.sin(angle);

		this.x += xAmount;
		this.y += yAmount;

	}

	/**
	 * @param other  the other Entity
	 */
	public void moveToward(Entity other) {
		moveToward(other.getX(), other.getY(), speed);
	}

	/**
	 * @param xOther x-coordinate of the other point.
	 * @param yOther y-coordinate of the other point.
	 */
	public void moveAwayFrom(double xOther, double yOther) {
		moveToward(xOther, yOther, -speed);
	}

	/**
	 * @param other  the other Entity
	 */
	public void moveAwayFrom(Entity other) {
		moveAwayFrom(other.getX(), other.getY());
	}

	/**
	 * @param entities          this array of entities to search.
	 * @param includeZombies    whether to include zombies in this search or not.
	 * @param includeNonzombies whether to include nonzombies in this search or not.
	 * @return the closest Entity to this Entity in entities (which is not this).
	 */
	private Entity findClosest(Entity[] entities, boolean includeZombies, boolean includeNonzombies) {
		Entity closest = null;
		double closestDist = Double.MAX_VALUE;
		for (Entity other : entities) {
			// NOTE:
			// We almost always want to use the equals method when comparing Objects.
			// In this case, we check if the two entities are the exact same instance.
			if (this != other && other != null && other.isAlive()) {
				if ((other.isZombie() && includeZombies) || (!other.isZombie() && includeNonzombies)) {
					double dist = distanceEdgeToEdge(other);
					if (dist < closestDist) {
						closest = other;
						closestDist = dist;
					}
				}
			}
		}
		return closest;
	}

	/**
	 * @param entities the array of entities to search.
	 * @return the closest nonzombie to this Entity in entities (which is not this).
	 */
	public Nonzombie findClosestNonzombie(Entity[] entities) {
		Entity e = findClosest(entities, false, true);
		return (Nonzombie)e;
	}

	/**
	 * @param entities this array of entities to search.
	 * @return the closest zombie to this Entity in entities (which is not this).
	 */
	public Zombie findClosestZombie(Entity[] entities) {
		Entity e = findClosest(entities, true, false);
		if(e == null) {
			return null;
		}
		return (Zombie)e;
	}

	/**
	 * @param entities this array of entities to search.
	 * @return the closest Entity to this Entity in entities (which is not this).
	 */
	public Entity findClosestEntity(Entity[] entities) {
		return findClosest(entities, true, true);
	}
	
	/**
	 * If the entity has moved out of bounds, returns it inbounds
	 */
	public void checkBounds() {
		if (this.y < 0.01){
			this.y = 0.01;
		}
		if (this.y > 0.99){
			this.y = 0.99;
		}
		if (this.x < 0.01){
			this.x = 0.01;
		}
		if (this.x > 0.99){
			this.x = 0.99;
		}
	}
	
	/**
	 * Placeholder method - this should never be called and it should be overridden!
	 */
	public void draw() {
		StdDraw.setPenColor(Color.PINK);
		StdDraw.point(x, y);
	}

	/**
	 * Placeholder method - this should never be called and it should be overridden!
	 */
	public Entity update(Entity[] entities) {
		return this;
	}

}
