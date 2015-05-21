/*
^       N
|       ^
Y     W<+>E
|       v
0-X->   S

Axiomes:
Labyrinthe l1 = l2.fromString(l1.toString())

l.isWallNorth(Position(x, y)) = l.isWallSouth(Position(x, y + 1))
l.isWallEast(Position(x, y)) = l.isWallWest(Position(x + 1, y))

l.isWallNorth(Position(x, l.getHeight() - 1)) = true
l.isWallSouth(Position(x, 0)) = true
l.isWallEast(Position(l.getWidth() - 1, y)) = true
l.isWallWest(Position(0, y)) = true

String representation:

Example for a 3x3 labyrinth:
bbbbbbbbbbbb
b (0: no wall or 1: wall): the walls, horizontally (south to north), then vertically (west to east), line by line, no edges
*/
public interface Labyrinth {
	public String toString();
	public void fromString(String str);
	public boolean isWallNorth(Position pos);
	public boolean isWallSouth(Position pos);
	public boolean isWallEast(Position pos);
	public boolean isWallWest(Position pos);
	public boolean isExplored(Position pos);
	public void setWallNorth(Position pos, boolean b);
	public void setWallSouth(Position pos, boolean b);
	public void setWallEast(Position pos, boolean b);
	public void setWallWest(Position pos, boolean b);
	public void setExplored(Position pos, boolean b);
	public int getWidth();
	public int getHeight();
	public Position getStart();
	public Position getEnd();
}
