package Etat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EtatReversi extends Etat {
	private Integer grille[][];
	private static final int SIZE = 8;
	private static final int VIDE = -1;
	private static final int NOIR = 0;
	private static final int BLANC = 1;

	public EtatReversi() {
		grille = new Integer[SIZE][SIZE]; //0 = noir, 1 = blanc, -1 = vide
		/*for (Integer[] aGrille : grille) Arrays.fill(aGrille, -1);*/
		etatInitial();
	}

	public int getCase(int ligne, int colonne) {
		return grille[ligne][colonne];
	}

	public int[] getSize() {
		return new int[]{grille.length, grille[0].length};
	}

	private void etatInitial() {
		/*grille[getSize()[0] / 2][getSize()[0] / 2] = 0;
		grille[getSize()[0] / 2][getSize()[1] / 2] = 1;
		grille[getSize()[1] / 2][getSize()[0] / 2] = 1;
		grille[getSize()[1] / 2][getSize()[1] / 2] = 0;*/
		for (int i = 0; i < grille.length; i++) {
			for (int j = 0; j < grille[0].length; j++) {
				if ((i == SIZE / 2-1 && j == SIZE / 2-1) || (i == SIZE / 2 && j == SIZE / 2 )) {
					grille[i][j] = 0;
				}
				else if ((i == SIZE / 2-1 && j == SIZE / 2 ) || (i == SIZE / 2  && j == SIZE / 2-1)) {
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

	public List<Integer> coupPossibles(int couleur) {
		ArrayList<Integer> coups = new ArrayList<>();
		int ligne = 0;
		while (ligne < getSize()[0]) {
			int colonne = 0;
			while (colonne < getSize()[1]) {
				if (getCase(ligne, colonne) == VIDE) {
					if (colonne - 1 >= 0) {
						if (getCase(ligne, colonne - 1) != VIDE) {

						}
					}
				}
			}
		}
		return coups;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				sb.append(grille[i][j]+"\t");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
