package Etat;

/**
 * Created by bonnal4u.
 */
public class Reversi extends Etat {
	private Integer grille[][];

	public Reversi() {
		grille = new Integer[8][8]; //0 = noir, 1 = blanc, -1 = vide
	}

	public int getCase(int ligne, int colonne) {
		return grille[ligne][colonne];
	}

	public int[] getSize() {
		return new int[]{grille.length, grille[0].length};
	}

	public boolean compare(Reversi rev) {
		if (getSize()[0] != rev.getSize()[0] || getSize()[1] != rev.getSize()[1])
			return false;

		int ligne = 0;
		while (ligne < getSize()[0]) {
			int colonne = 0;
			while (colonne < getSize()[1]) {
				if (rev.getCase(ligne, colonne) != getCase(ligne, colonne))
					return false;
				colonne++;
			}
			ligne++;
		}

		return true;
	}
}
