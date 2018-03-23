package Etat;

/**
 * Created by Valentin.
 */
public class Pion {
	private int row, col, color;

	public Pion(int row, int col, int color) {
		this.row = row;
		this.col = col;
		this.color = color;
	}

	public Pion() {
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getColor() {
		return color;
	}

	public Pion setRow(int row) {
		this.row = row;
		return this;
	}

	public Pion setCol(int col) {
		this.col = col;
		return this;
	}

	public Pion setColor(int color) {
		this.color = color;
		return this;
	}
}
