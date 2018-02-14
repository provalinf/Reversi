package Etat;

import java.util.Arrays;

public class EtatReversi extends Etat {
	private Integer grille[][];
	private static final int SIZE = 7;

	public EtatReversi() {
		grille = new Integer[SIZE][SIZE]; //0 = noir, 1 = blanc, -1 = vide
		etatInitial();
	}

	public int getCase(int ligne, int colonne) {
		return grille[ligne][colonne];
	}

	public int[] getSize() {
		return new int[]{grille.length, grille[0].length};
	}

	private void etatInitial() {
		for (int i = 0; i < grille.length; i++) {
			for (int j = 0; j < grille[0].length; j++) {
				if ((i == SIZE / 2 && j == SIZE / 2) || (i == SIZE / 2 + 1 && j == SIZE / 2 + 1)) {
					grille[i][j] = 0;
				}
				else if ((i == SIZE / 2 && j == SIZE / 2 + 1) || (i == SIZE / 2 + 1 && j == SIZE / 2)) {
					grille[i][j] = 1;
				}
				else grille[i][j] = -1;
			}
		}
	}

	public boolean compare(EtatReversi rev) {
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				sb.append(grille[i][j]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
