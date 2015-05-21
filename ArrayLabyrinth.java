import java.util.ArrayList;


public class ArrayLabyrinth implements Labyrinth {
	
	private final int width;
	private final int height;
	private boolean[][] horizontalWalls;
	private boolean[][] verticalWalls;
	private boolean[][] explored;

	public ArrayLabyrinth(int width, int height) {
		this.width = width;
		this.height = height;
		this.horizontalWalls = new boolean[height - 1][width];
		this.verticalWalls = new boolean[height][width - 1];
		this.explored = new boolean[height][width];
	}

	@Override
	public void fromString(String str) {
		int cpt = 0;
		
		for (int i = 0; i < this.height - 1; i++) {
			for (int j = 0; j < this.width; j++) {
				if (str.charAt(cpt) == '1') {
					this.horizontalWalls[i][j] = true;
				} else {
					this.horizontalWalls[i][j] = false;
				}
				cpt++;
			}
		}
		
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width - 1; j++) {
				if (str.charAt(cpt) == '1') {
					this.verticalWalls[i][j] = true;
				} else {
					this.verticalWalls[i][j] = false;
				}
				cpt++;
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		
		for (int i = 0; i < this.height - 1; i++) {
			for (int j = 0; j < this.width; j++) {
				if (this.horizontalWalls[i][j]) {
					str.append('1');
				} else {
					str.append('0');
				}
			}
		}
		
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width - 1; j++) {
				if (this.verticalWalls[i][j]) {
					str.append('1');
				} else {
					str.append('0');
				}
			}
		}
		
		return str.toString();
	}

	@Override
	public boolean isWallNorth(Position pos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWallSouth(Position pos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWallEast(Position pos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWallWest(Position pos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExplored(Position pos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWallNorth(Position pos, boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWallSouth(Position pos, boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWallEast(Position pos, boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWallWest(Position pos, boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setExplored(Position pos, boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Position getStart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position getEnd() {
		// TODO Auto-generated method stub
		return null;
	}

}
