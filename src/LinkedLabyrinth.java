import java.util.Iterator;
import java.util.LinkedList;


public class LinkedLabyrinth implements Labyrinth {
	private LinkedList<LinkedList<Boolean>> horizontalWalls;
	private LinkedList<LinkedList<Boolean>> verticalWalls;
	private LinkedList<LinkedList<Boolean>> explored;
	private Position start;
	private Position end;
	private int width;
	private int height;

	public LinkedLabyrinth() {
		this.width = 1;
		this.height = 1;
		
		LinkedList<Boolean> l;
		
		this.horizontalWalls = new LinkedList<LinkedList<Boolean>>();
		l = new LinkedList<Boolean>();
		l.add(false);
		this.horizontalWalls.add(l);
		l = new LinkedList<Boolean>();
		l.add(false);
		this.horizontalWalls.add(l);
		
		this.verticalWalls = new LinkedList<LinkedList<Boolean>>();
		l = new LinkedList<Boolean>();
		l.add(false);
		l.add(false);
		this.verticalWalls.add(l);
		
		this.explored = new LinkedList<LinkedList<Boolean>>();
		l = new LinkedList<Boolean>();
		l.add(false);
		this.explored.add(l);
		
		this.start = new Position(0, 0);
		// this.end = null;
	}

	public void fromString(String str) {
		// Not implemented because not needed
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		
		str.append(this.width + ";" + this.height + ";" + this.start.getX() + ";" + this.start.getY() + ";" + this.end.getX() + ";" + this.end.getY() + ";");
		
		for (int i = 0; i < this.height + 1; i++) {
			for (int j = 0; j < this.width; j++) {
				if (this.horizontalWalls.get(i).get(j)) {
					str.append('1');
				} else {
					str.append('0');
				}
			}
		}
		
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width + 1; j++) {
				if (this.verticalWalls.get(i).get(j)) {
					str.append('1');
				} else {
					str.append('0');
				}
			}
		}
		
		return str.toString();
	}
	
	@Override
	public boolean isWall(Position pos, Orientation ori) {
		if (ori.equals(Orientation.EAST)) {
			return this.verticalWalls.get(pos.getY()).get(pos.getX() + 1);
		} else if (ori.equals(Orientation.NORTH)) {
			return this.horizontalWalls.get(pos.getY() + 1).get(pos.getX());
		} else if (ori.equals(Orientation.WEST)) {
			return this.verticalWalls.get(pos.getY()).get(pos.getX());
		} else if (ori.equals(Orientation.SOUTH)) {
			return this.horizontalWalls.get(pos.getY()).get(pos.getX());
		}
		return true;
	}
	
	@Override
	public void setWall(Position pos, Orientation ori, boolean b) {
		if (ori.equals(Orientation.EAST)) {
			this.verticalWalls.get(pos.getY()).set(pos.getX() + 1, b);
		} else if (ori.equals(Orientation.NORTH)) {
			this.horizontalWalls.get(pos.getY() + 1).set(pos.getX(), b);
		} else if (ori.equals(Orientation.WEST)) {
			this.verticalWalls.get(pos.getY()).set(pos.getX(), b);
		} else if (ori.equals(Orientation.SOUTH)) {
			this.horizontalWalls.get(pos.getY()).set(pos.getX(), b);
		}
	}

	@Override
	public boolean isExplored(Position pos) {
		try {
			return this.explored.get(pos.getY()).get(pos.getX());
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	@Override
	public Position setExplored(Position pos, boolean b) {
		Position modifiedPos;
		if (pos.getX() < 0) {
			extend(Orientation.WEST);
			modifiedPos = new Position(pos.getX() + 1, pos.getY());
		} else if (pos.getX() >= this.width) {
			extend(Orientation.EAST);
			modifiedPos = pos;
		} else if (pos.getY() < 0) {
			extend(Orientation.SOUTH);
			modifiedPos = new Position(pos.getX(), pos.getY() + 1);
		} else if (pos.getY() >= this.height) {
			extend(Orientation.NORTH);
			modifiedPos = pos;
		} else {
			modifiedPos = pos;
		}
		
		this.explored.get(modifiedPos.getY()).set(modifiedPos.getX(), b);
		return modifiedPos;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public Position getStart() {
		return this.start;
	}

	@Override
	public Position getEnd() {
		return this.end;
	}

	@Override
	public void setEnd(Position pos) {
		this.end = pos;
	}
	
	@Override
	public String findPath(Position start, Position end) {
		// We fill a matrix with minimum cost to go to a square from the start plus one
		int[][] matrix = new int[this.width][this.height];
		propagate(1, start, matrix);
		
		// We create an Orientation List to make a path, it is from the start to the end
		Position pos = end;
		LinkedList<Orientation> ll = new LinkedList<Orientation>();
		Orientation ori = hasNextLowerAccessible(pos, matrix);
		ll.add(0, ori);
		pos = pos.next(ori);
		while (!pos.equals(start)) {
			ori = hasNextLowerAccessible(pos, matrix);
			ll.add(0, ori);
			pos = pos.next(ori);
		}
		
		// We invert the List so we have the right path from the start to the end
		int llsize = ll.size();
		for (int i = 0; i < llsize; i++) {
			ll.set(i, ll.get(i).rotate(true).rotate(true));
		}
		
		StringBuffer bs = new StringBuffer();
		bs.append('f');
		Iterator<Orientation> it = ll.iterator();
		ori = it.next();
		while(it.hasNext()) {
			Orientation next = it.next();
			if (ori.equals(next)) { // If same orientation, we don't turn
				bs.append('f');
			} else if (ori.rotate(true).equals(next)) { // If left to get next orientation
				bs.append("lf");
			} else if (ori.rotate(false).equals(next)) { // If right to get next orientation
				bs.append("rf");
			}
			ori = next;
		}
		
		return bs.toString();
	}
	
	private void propagate(int i, Position pos, int[][] matrix) {
		matrix[pos.getX()][pos.getY()] = i++;
		if (!this.isWall(pos, Orientation.NORTH) && matrix[pos.getX()][pos.getY() + 1] == 0) {
			propagate(i, pos.next(Orientation.NORTH), matrix);
		}
		if (!this.isWall(pos, Orientation.SOUTH) && matrix[pos.getX()][pos.getY() - 1] == 0) {
			propagate(i, pos.next(Orientation.SOUTH), matrix);
		}
		if (!this.isWall(pos, Orientation.EAST) && matrix[pos.getX() + 1][pos.getY()] == 0) {
			propagate(i, pos.next(Orientation.EAST), matrix);
		}
		if (!this.isWall(pos, Orientation.WEST) && matrix[pos.getX() - 1][pos.getY()] == 0) {
			propagate(i, pos.next(Orientation.WEST), matrix);
		}
	}
	
	private Orientation hasNextLowerAccessible(Position pos, int[][] matrix) {
		if (!this.isWall(pos, Orientation.NORTH) && matrix[pos.getX()][pos.getY() + 1] == matrix[pos.getX()][pos.getY()] - 1) {
			return Orientation.NORTH;
		}
		if (!this.isWall(pos, Orientation.SOUTH) && matrix[pos.getX()][pos.getY() - 1] == matrix[pos.getX()][pos.getY()] - 1) {
			return Orientation.SOUTH;
		}
		if (!this.isWall(pos, Orientation.EAST) && matrix[pos.getX() + 1][pos.getY()] == matrix[pos.getX()][pos.getY()] - 1) {
			return Orientation.EAST;
		}
		if (!this.isWall(pos, Orientation.WEST) && matrix[pos.getX() - 1][pos.getY()] == matrix[pos.getX()][pos.getY()] - 1) {
			return Orientation.WEST;
		}
		return null;
	}
	
	private void extend(Orientation ori) {
		if (ori.equals(Orientation.EAST)) {
			
			for (int i = 0; i < this.height; i++) {
				this.verticalWalls.get(i).add(false);
				this.horizontalWalls.get(i).add(false);
				this.explored.get(i).add(false);
			}
			this.horizontalWalls.get(this.height).add(false);
			
			this.width++;
			
		} else if (ori.equals(Orientation.NORTH)) {
			
			LinkedList<Boolean> v = new LinkedList<Boolean>();
			LinkedList<Boolean> h = new LinkedList<Boolean>();
			LinkedList<Boolean> e = new LinkedList<Boolean>();
			for (int i = 0; i < this.width; i++) {
				v.add(false);
				h.add(false);
				e.add(false);
			}
			v.add(false);
			this.verticalWalls.add(v);
			this.horizontalWalls.add(h);
			this.explored.add(e);
			
			this.height++;
			
		} else if (ori.equals(Orientation.WEST)) {
			
			for (int i = 0; i < this.height; i++) {
				this.verticalWalls.get(i).add(0, false);
				this.horizontalWalls.get(i).add(0, false);
				this.explored.get(i).add(0, false);
			}
			this.horizontalWalls.get(this.height).add(0, false); // Bottom wall
			
			this.width++;
			
			this.start = new Position(this.start.getX() + 1, this.start.getY());
			if (this.end != null) {
				this.end = new Position(this.end.getX() + 1, this.end.getY());
			}
		} else if (ori.equals(Orientation.SOUTH)) {
			
			LinkedList<Boolean> v = new LinkedList<Boolean>();
			LinkedList<Boolean> h = new LinkedList<Boolean>();
			LinkedList<Boolean> e = new LinkedList<Boolean>();
			for (int i = 0; i < this.width; i++) {
				v.add(false);
				h.add(false);
				e.add(false);
			}
			v.add(false);
			this.verticalWalls.add(0, v);
			this.horizontalWalls.add(0, h);
			this.explored.add(0, e);
			
			this.height++;
			
			this.start = new Position(this.start.getX(), this.start.getY() + 1);
			if (this.end != null) {
				this.end = new Position(this.end.getX(), this.end.getY() + 1);
			}
			
		}
	}
	
	public String graphicalRepresentation() {
		// Width: 2 * width + 1
		// Height: 2 * height + 1
		StringBuffer sb = new StringBuffer((2 * this.width + 2) * (2 * this.height + 1) - 1);
		
		//for (int i = 0; i < this.height; i++) {
		for (int i = this.height - 1; i >= 0; i--) {
			// Horizontal walls line
			for (int j = 0; j < this.width; j++) {
				sb.append('+');
				if (this.horizontalWalls.get(i + 1).get(j)) {
					sb.append('-');
				} else {
					sb.append(' ');
				}
			}
			sb.append("+\n");
			
			// Vertical walls line
			for (int j = 0; j < this.width; j++) {
				//sb.append(' ');
				if (this.verticalWalls.get(i).get(j)) {
					sb.append('|');
				} else {
					sb.append(' ');
				}
				
				// Square
				if (new Position(j, i).equals(this.start)) {
					sb.append('S');
				} else if (this.end != null && new Position(j, i).equals(this.end)) {
					sb.append('E');
				} else {
					sb.append(' ');
				}
			}
			if (this.verticalWalls.get(i).get(this.width)) {
				sb.append('|');
			} else {
				sb.append(' ');
			}
			sb.append('\n');
		}
		
		// Last line
		for (int i = 0; i < this.width; i++) {
			sb.append('+');
			if (this.horizontalWalls.get(0).get(i)) {
				sb.append('-');
			} else {
				sb.append(' ');
			};
		}
		sb.append("+");
		
		return sb.toString();
	}
	
	public boolean isNotExplored() {
		Iterator<LinkedList<Boolean>> ile = this.explored.iterator();
		while (ile.hasNext()) {
			if (ile.next().contains(false)) {
				return true;
			}
		}
		return false;
	}
}
