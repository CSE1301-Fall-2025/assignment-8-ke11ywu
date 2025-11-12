package assignment8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import edu.princeton.cs.introcs.StdDraw;
import support.cse131.NotYetImplementedException;
import support.cse131.Timing;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ZombieSimulator {
	private static final String ZOMBIE_TOKEN_VALUE = "Zombie";
	private Entity[] entities;

	/**
	 * Constructs a ZombieSimulator with an empty array of Entities.
	 */
	public ZombieSimulator(int n) {
		this.entities = new Entity[n];
	}

	/**
	 * @return the current array of entities
	 */
	public Entity[] getEntities() {
		return this.entities;
	}

	/**
	 * Reads an entire zombie simulation file from a specified ArgsProcessor, adding
	 * each Entity to the array of entities.
	 *
	 * Assume that N (the integer indicating how many entities are in the
	 * simulation) has already been read in
	 * and passed into the constructor.
	 *
	 * @param in Scanner to read the complete zombie simulation file format.
	 */
	public void readEntities(Scanner in) {
		for (int i = 0; i < entities.length; i++) {
			boolean isZombie;
			String state = in.next();
			Entity newEnt;
			if (state.equals("Zombie")) { // using == will check reference equality instead of value equality
				isZombie = true;
			} else {
				isZombie = false;
			}
			double xPos = in.nextDouble();
			double yPos = in.nextDouble();
			if (isZombie == true){
				newEnt = new Zombie(xPos, yPos);
			} else {
				newEnt = new Nonzombie(xPos, yPos);
			}
			entities[i] = newEnt;
		}
	}

	/**
	 * @return the number of zombies in entities.
	 */
	public int getZombieCount() {
		int count = 0;
		for (Entity e : entities) {
			if (e.isZombie() == true) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @return the number of nonzombies in entities.
	 */
	public int getNonzombieCount() {
		return this.entities.length - this.getZombieCount();
	}

	/**
	 * Draws a frame of the simulation.
	 */
	public void draw() {
		StdDraw.clear();

		// NOTE: feel free to edit this code to support additional features
		for (Entity entity : getEntities()) {
			entity.draw();
			StdDraw.setPenColor(0, 0, 0);
			StdDraw.text(0.92, 0.92, this.getZombieCount() + "/" + this.entities.length);
		}

		StdDraw.show(); // commit deferred drawing as a result of enabling double buffering
	}

	/**
	 * Updates the entities for the current frame of the simulation given the amount
	 * of time passed since the previous frame.
	 * 
	 * Note: some entities may be consumed and will not remain for future frames of
	 * the simulation.
	 * 
	 */
	public void update() {
		Entity[] updatedEnts = new Entity[this.entities.length];
		for (int i = 0; i < this.entities.length; i++) {
			updatedEnts[i] = this.entities[i].update(this.entities);
		}
		this.entities = updatedEnts;
	}

	/**
	 * Runs the zombie simulation.
	 * 
	 * @param args arguments from the command line
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		StdDraw.enableDoubleBuffering(); // reduce unpleasant drawing artifacts, speed things up

		JFileChooser chooser = new JFileChooser("zombieSims");
		chooser.showOpenDialog(null);
		File f = new File(chooser.getSelectedFile().getPath());
		Scanner in = new Scanner(f); // making Scanner with a File

		ZombieSimulator zombieSimulator = new ZombieSimulator(in.nextInt());
		zombieSimulator.readEntities(in);

		while (zombieSimulator.getNonzombieCount() >= 0) {

			zombieSimulator.update();
			zombieSimulator.draw();

			StdDraw.pause(20);

		}
	}
}
