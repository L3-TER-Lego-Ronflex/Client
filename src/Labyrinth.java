/*
^       N
|       ^
Y     W<+>E
|       v
0-X->   S
*/
public interface Labyrinth {
	
	/**
	 * @param pos Position from where we look for walls
	 * @param ori Orientation from the Position from where we look for a wall
	 * @param true if there is a wall, else false
	 */
	public boolean isWall(Position pos, Orientation ori);
	
	/**
	 * @param pos Position from where we want to set walls
	 * @param ori Orientation from the Position from where we want to set a wall
	 * @return b true if we want to set a wall, false if we want to remove a wall
	 */
	public void setWall(Position pos, Orientation ori, boolean b);
	
	/**
	 * @param pos Position to get explored state
	 * @return true if explored, else false
	 */
	public boolean isExplored(Position pos);
	
	/**
	 * @param pos Position to explore
	 * @param b If it set explored or not
	 * @return New Position if the Position to explore has changed
	 */
	public Position setExplored(Position pos, boolean b);
	
	/**
	 * @return Width of the Labyrinth (number of rows)
	 */
	public int getWidth();
	
	/**
	 * @return Height of the Labyrinth (number of lines)
	 */
	public int getHeight();
	
	/**
	 * @return Starting Position in the Labyrinth
	 */
	public Position getStart();
	
	/**
	 * @return Goal Position in the Labyrinth
	 */
	public Position getEnd();
	
	/**
	 * @param pos Goal Position in the Labyrinth
	 */
	public void setEnd(Position pos);
	
	/**
	 * Find the quickest path to go from the start to the end of the Labyrinth.
	 * @return A sequence of instructions to follow the path
	 */
	public String findPath(Position start, Position end);
}
