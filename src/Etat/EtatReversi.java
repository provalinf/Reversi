package Etat;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EtatReversi extends Etat {
	private Integer grille[][];
	private static final int SIZE = 8;
	private static final int VIDE = -1;
	public static final int NOIR = 0;
	public static final int BLANC = 1;

	public EtatReversi() {
		grille = new Integer[SIZE][SIZE]; //0 = noir, 1 = blanc, -1 = vide
		/*for (Integer[] aGrille : grille) Arrays.fill(aGrille, -1);*/
		etatInitial();
	}

	public int getCase(int ligne, int colonne) {
		return grille[ligne][colonne];
	}

	public void setCase(int ligne, int colonne, int couleur) {
		grille[ligne][colonne] = couleur;
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
				if ((i == SIZE / 2 - 1 && j == SIZE / 2 - 1) || (i == SIZE / 2 && j == SIZE / 2)) {
					grille[i][j] = 0;
				} else if ((i == SIZE / 2 - 1 && j == SIZE / 2) || (i == SIZE / 2 && j == SIZE / 2 - 1)) {
					grille[i][j] = 1;
				} else grille[i][j] = -1;
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
		boolean jouable;

		for (int ligne = 0; ligne < getSize()[0]; ligne++) {
			for (int colonne = 0; colonne < getSize()[1]; colonne++) {
				jouable = false;
				if (ligne - 1 >= 0) {
					if (colonne - 1 >= 0 && getCase(ligne - 1, colonne - 1) != VIDE && getCase(ligne - 1, colonne - 1) != couleur) {        // Nord-Ouest
						int i = 2;
						while (!jouable && ligne - i >= 0 && colonne - i >= 0 && getCase(ligne - i, colonne - i) != VIDE) {
							if (getCase(ligne - i, colonne - i) == couleur) jouable = true;
							i++;
						}
					}
					if (!jouable && getCase(ligne - 1, colonne) != VIDE && getCase(ligne - 1, colonne) != couleur) {        // Nord
						int i = 2;
						while (!jouable && ligne - i >= 0 && getCase(ligne - i, colonne) != VIDE) {
							if (getCase(ligne - i, colonne) == couleur) jouable = true;
							i++;
						}
					}
					if (!jouable && colonne + 1 < getSize()[1] && getCase(ligne - 1, colonne + 1) != VIDE && getCase(ligne - 1, colonne + 1) != couleur) {        // Nord-Est
						int i = 2;
						while (!jouable && ligne - i >= 0 && colonne + i < getSize()[1] && getCase(ligne - i, colonne + i) != VIDE) {
							if (getCase(ligne - i, colonne + i) == couleur) jouable = true;
							i++;
						}
					}
				}

				if (!jouable && colonne + 1 < getSize()[1] && getCase(ligne, colonne + 1) != VIDE && getCase(ligne, colonne + 1) != couleur) {        // Est
					int i = 2;
					while (!jouable && colonne + i < getSize()[1] && getCase(ligne, colonne + i) != VIDE) {
						if (getCase(ligne, colonne + i) == couleur) jouable = true;
						i++;
					}
				}
				if (!jouable && colonne - 1 >= 0 && getCase(ligne, colonne - 1) != VIDE && getCase(ligne, colonne - 1) != couleur) {        // Ouest
					int i = 2;
					while (!jouable && colonne - i >= 0 && getCase(ligne, colonne - i) != VIDE) {
						if (getCase(ligne, colonne - i) == couleur) jouable = true;
						i++;
					}
				}

				if (ligne + 1 < getSize()[0]) {
					if (!jouable && colonne - 1 >= 0 && getCase(ligne + 1, colonne - 1) != VIDE && getCase(ligne + 1, colonne - 1) != couleur) {        // Sud-Ouest
						int i = 2;
						while (!jouable && ligne + i < getSize()[0] && colonne - i >= 0 && getCase(ligne + i, colonne - i) != VIDE) {
							if (getCase(ligne + i, colonne - i) == couleur) jouable = true;
							i++;
						}
					}
					if (!jouable && getCase(ligne + 1, colonne) != VIDE && getCase(ligne + 1, colonne) != couleur) {        // Sud
						int i = 2;
						while (!jouable && ligne + i < getSize()[0] && getCase(ligne + i, colonne) != VIDE) {
							if (getCase(ligne + i, colonne) == couleur) jouable = true;
							i++;
						}
					}
					if (!jouable && colonne + 1 < getSize()[1] && getCase(ligne + 1, colonne + 1) != VIDE && getCase(ligne + 1, colonne + 1) != couleur) {        // Sud-Est
						int i = 2;
						while (!jouable && ligne + i < getSize()[0] && colonne + i < getSize()[1] && getCase(ligne + i, colonne + i) != VIDE) {
							if (getCase(ligne + i, colonne + i) == couleur) jouable = true;
							i++;
						}
					}
				}
				if (jouable) coups.add(getNumCase(ligne, colonne));

			}
		}
		return coups;
	}

	public void printNumCases() {
		for (int ligne = 0; ligne < getSize()[0]; ligne++) {
			for (int colonne = 0; colonne < getSize()[1]; colonne++) {
				System.out.print(getNumCase(ligne, colonne) + "\t");
			}
			System.out.println();
		}
	}

	public int getNumCase(int ligne, int colonne) {
		return colonne + (getSize()[1] * ligne);
	}

	public int[] getCoordCase(int numCase) {
		return new int[]{numCase / getSize()[1], numCase % getSize()[1]};
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				sb.append(grille[i][j] + "\t");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void jeuTerminal(int couleur) {
		int caseChoisie;
		Scanner reader = new Scanner(System.in);
		System.out.println("Entrer le numero de la case : ");
		caseChoisie = reader.nextInt();
		while (!coupPossibles(couleur).contains(caseChoisie)) {
			System.out.println("Coup impossible");
			System.out.println("Entrer un autre numero de case : ");
			caseChoisie = reader.nextInt();
		}
		int[] coord = getCoordCase(caseChoisie);
		setCase(coord[0], coord[1], couleur);
	}

	public boolean isCoupPossible() {
		if (coupPossibles(NOIR).size() == 0 && coupPossibles(BLANC).size() == 0) return false;
		int row = 0, col;
		while (row < getSize()[0]) {
			col = 0;
			while (col < getSize()[1]) {
				if (getCase(row, col) != NOIR && getCase(row, col) != BLANC) {
					return true;
				}
				col++;
			}
			row++;
		}

		return false;
	}

	public int getWinner() {
		int nbWhite = 0, nbBlack = 0;
		for (int row = 0; row < getSize()[0]; row++) {
			for (int col = 0; col < getSize()[1]; col++) {
				if (getCase(row, col) == BLANC) nbWhite++;
				else if (getCase(row, col) == NOIR) nbBlack++;
			}
		}
		if (nbWhite == nbBlack) return -1;    // Egalité
		return nbBlack > nbWhite ? NOIR : BLANC;
	}

	public Etat minimax(int c){
		int scoreMax = -1000;
		int score = 0;
		List<Integer> successeur = coupPossibles(1);
		for (int i = 0; i < successeur.size(); i++) {
			score = evaluation(c, successeur.get(i));
		}
	}

	private int evaluation(int c, Integer integer) {

	}
}
