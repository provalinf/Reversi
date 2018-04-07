package Etat;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EtatReversi extends Etat {
	private int grille[][];
	private int tabPoids[][];
	private static final int POIDSMAX = 500;
	private static final int SIZE = 8;
	private static final int VIDE = -1;
	public static final int NOIR = 0;
	public static final int BLANC = 1;

	public EtatReversi() {
		tabPoids = setPoids();
		grille = new int[SIZE][SIZE]; //0 = noir, 1 = blanc, -1 = vide
		/*for (Integer[] aGrille : grille) Arrays.fill(aGrille, -1);*/
		etatInitial();
	}

	private int[][] setPoids() {
		int[][] res = new int[SIZE][SIZE];
		switch (SIZE) {
			case 8:
				int[][] res1 = {
						{500, -150, 30, 10, 10, 30, -150, 500},
						{-150, -250, 0, 0, 0, 0, -250, -150},
						{30, 0, 1, 2, 2, 1, 0, 30},
						{10, 0, 2, 16, 16, 2, 0, 10},
						{10, 0, 2, 16, 16, 2, 0, 10},
						{30, 0, 1, 2, 2, 1, 0, 30},
						{-150, -250, 0, 0, 0, 0, -250, -150},
						{500, -150, 30, 10, 10, 30, -150, 500}
				};
				res = res1;
				break;
			case 10:
				int[][] res2 = {
						{500, -150, 30, 10, 5, 5, 10, 30, -150, 500},
						{-150, -250, 0, 0, 0, 0, 0, 0, -250, -150},
						{30, 0, 2, 4, 4, 4, 4, 2, 0, 30},
						{20, 0, 1, 2, 2, 2, 2, 1, 0, 20},
						{10, 0, 2, 8, 16, 16, 8, 2, 0, 10},
						{10, 0, 2, 8, 16, 16, 8, 2, 0, 10},
						{20, 0, 1, 2, 2, 2, 2, 1, 0, 20},
						{30, 0, 2, 4, 4, 4, 4, 2, 0, 30},
						{-150, -250, 0, 0, 0, 0, 0, 0, -250, -150},
						{500, -150, 30, 10, 5, 5, 10, 30, -150, 500}
				};
				res = res2;
				break;
		}
        /*for (int i = 0; i < tabPoids.length; i++) {
            for (int j = 0; j < tabPoids[0].length; j++) {
                if ((i == 0 && j == 0) || (i == SIZE - 1 && j == SIZE - 1) || (i == 0 && j == SIZE - 1) || (i == SIZE - 1 && j == 0)) {
                    tabPoids[i][j] = POIDSMAX;
                }
*//*				if((i==0 && j ==1)||(i==SIZE-2&& j ==SIZE-1)||(i== 0 && j == SIZE-2)||(i == SIZE-2 && j == 0)){
					tabPoids[i][j] = POIDSMAX;
				}*//*
                else if ((i == 1 && j == 1) || (i == SIZE - 2 && j == SIZE - 2) || (i == 1 && j == SIZE - 2) || (i == SIZE - 2 && j == 1)) {
                    tabPoids[i][j] = -POIDSMAX / 2;
                } else tabPoids[i][j] = 0;
            }
        }
        for (int i = 0; i < tabPoids.length; i++) {
            for (int j = 0; j < tabPoids[0].length; j++) {
                System.out.print(tabPoids[i][j] + "\t");
            }
            System.out.println("\t");
        }*/
		return res;
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

	public int couleurAdverse(int color) {
		return color == NOIR ? BLANC : NOIR;
	}

	public void setCoup(int rowCoord, int colCoord, int color) {
		ArrayList<int[]> casesCaptures = new ArrayList<>();
		ArrayList<int[]> casesPotentielles = new ArrayList<>();

		if (colCoord + 1 < getSize()[1]) {
			if (getCase(rowCoord, colCoord + 1) == couleurAdverse(color)) {    // EST
				for (int col = colCoord + 1; col < getSize()[1]; col++) {
					if (getCase(rowCoord, col) == couleurAdverse(color)) {
						casesPotentielles.add(new int[]{rowCoord, col});
					} else if (getCase(rowCoord, col) == color) {
						casesCaptures.addAll(casesPotentielles);
						break;
					}
				}
			}

			if (rowCoord + 1 < getSize()[0]) {
				if (getCase(rowCoord + 1, colCoord + 1) == couleurAdverse(color)) {    // NORD-EST
					int row = rowCoord + 1, col = colCoord + 1;
					while (row < getSize()[0] && col < getSize()[1]) {
						if (getCase(row, col) == couleurAdverse(color)) {
							casesPotentielles.add(new int[]{row, col});
						} else if (getCase(row, col) == color) {
							casesCaptures.addAll(casesPotentielles);
							break;
						}
						row++;
						col++;
					}
					casesPotentielles.clear();
				}
			}
		}

		if (rowCoord + 1 < getSize()[0]) {
			if (getCase(rowCoord + 1, colCoord) == couleurAdverse(color)) {    // NORD
				for (int row = rowCoord + 1; row < getSize()[0]; row++) {
					if (getCase(row, colCoord) == couleurAdverse(color)) {
						casesPotentielles.add(new int[]{row, colCoord});
					} else if (getCase(row, colCoord) == color) {
						casesCaptures.addAll(casesPotentielles);
						break;
					}
				}
				casesPotentielles.clear();
			}

			if (colCoord - 1 >= 0) {
				if (getCase(rowCoord + 1, colCoord - 1) == couleurAdverse(color)) {    // NORD-OUEST
					int row = rowCoord + 1, col = colCoord - 1;
					while (row < getSize()[0] && col >= 0) {
						if (getCase(row, col) == couleurAdverse(color)) {
							casesPotentielles.add(new int[]{row, col});
						} else if (getCase(row, col) == color) {
							casesCaptures.addAll(casesPotentielles);
							break;
						}
						row++;
						col--;
					}
					casesPotentielles.clear();
				}
			}
		}


		if (colCoord - 1 >= 0) {
			if (getCase(rowCoord, colCoord - 1) == couleurAdverse(color)) {    // OUEST
				for (int col = colCoord - 1; col >= 0; col--) {
					if (getCase(rowCoord, col) == couleurAdverse(color)) {
						casesPotentielles.add(new int[]{rowCoord, col});
					} else if (getCase(rowCoord, col) == color) {
						casesCaptures.addAll(casesPotentielles);
						break;
					}
				}
				casesPotentielles.clear();
			}

			if (rowCoord - 1 >= 0) {
				if (getCase(rowCoord - 1, colCoord - 1) == couleurAdverse(color)) {    // SUD-OUEST
					int row = rowCoord - 1, col = colCoord - 1;
					while (row >= 0 && col >= 0) {
						if (getCase(row, col) == couleurAdverse(color)) {
							casesPotentielles.add(new int[]{row, col});
						} else if (getCase(row, col) == color) {
							casesCaptures.addAll(casesPotentielles);
							break;
						}
						row--;
						col--;
					}
					casesPotentielles.clear();
				}
			}
		}


		if (rowCoord - 1 >= 0) {
			if (getCase(rowCoord - 1, colCoord) == couleurAdverse(color)) {    // SUD
				for (int row = rowCoord - 1; row >= 0; row--) {
					if (getCase(row, colCoord) == couleurAdverse(color)) {
						casesPotentielles.add(new int[]{row, colCoord});
					} else if (getCase(row, colCoord) == color) {
						casesCaptures.addAll(casesPotentielles);
						break;
					}
				}
				casesPotentielles.clear();
			}

			if (colCoord + 1 < getSize()[1]) {
				if (getCase(rowCoord - 1, colCoord + 1) == couleurAdverse(color)) {    // SUD-EST
					int row = rowCoord - 1, col = colCoord + 1;
					while (row >= 0 && col < getSize()[1]) {
						if (getCase(row, col) == couleurAdverse(color)) {
							casesPotentielles.add(new int[]{row, col});
						} else if (getCase(row, col) == color) {
							casesCaptures.addAll(casesPotentielles);
							break;
						}
						row--;
						col++;
					}
					casesPotentielles.clear();
				}
			}
		}

		for (int[] capture : casesCaptures) {
			setCase(capture[0], capture[1], color);
		}
	}

	public List<Integer> coupPossibles(int couleur) {
		ArrayList<Integer> coups = new ArrayList<>();
		boolean jouable;

		for (int ligne = 0; ligne < getSize()[0]; ligne++) {
			for (int colonne = 0; colonne < getSize()[1]; colonne++) {
				jouable = false;
				if (getCase(ligne, colonne) == VIDE) {
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
		setCoup(coord[0], coord[1], couleur);
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

	public void copieEtatReversi(EtatReversi etat) {
		if (getSize()[0] != etat.getSize()[0] /*|| getSize()[1] != etat.getSize()[1]*/)
			grille = new int[etat.getSize()[0]][];
		for (int i = 0; i < getSize()[0]; i++) grille[i] = grille[i].clone();
	}

	public EtatReversi duplicateEtatReversi() {
		EtatReversi dup = new EtatReversi();
		dup.copieEtatReversi(this);
		return dup;
	}

	public int getNbPion(int couleurJoueur) {
		int count = 0;
		for (int[] row : grille)
			for (int col : row)
				if (col == couleurJoueur) count++;
		return count;
	}

	public int importance(int couleur) {
		int somme = 0;

		for (int i = 0; i < tabPoids.length; i++) {
			for (int j = 0; j < tabPoids[0].length; j++) {
				if (getCase(i, j) == couleur) {
					if (i == 0 && j == 0) {
						tabPoids[i][j + 1] = -tabPoids[i][j + 1];
						tabPoids[i + 1][j + 1] = -tabPoids[i + 1][j + 1];
						tabPoids[i + 1][j] = -tabPoids[i + 1][j];
					}
					if (i == 0 && j == SIZE - 1) {
						tabPoids[i][j - 1] = -tabPoids[i][j - 1];
						tabPoids[i + 1][j - 1] = -tabPoids[i + 1][j - 1];
						tabPoids[i + 1][j] = -tabPoids[i + 1][j];
					}
					if (i == SIZE - 1 && j == 0) {
						tabPoids[i - 1][j] = -tabPoids[i - 1][j];
						tabPoids[i - 1][j + 1] = -tabPoids[i - 1][j + 1];
						tabPoids[i][j + 1] = -tabPoids[i][j + 1];
					}
					if (i == SIZE - 1 && j == SIZE - 1) {
						tabPoids[i - 1][j] = -tabPoids[i - 1][j];
						tabPoids[i - 1][j - 1] = -tabPoids[i - 1][j - 1];
						tabPoids[i][j - 1] = -tabPoids[i][j - 1];
					}

					somme += tabPoids[i][j];
				}
			}

		}
		return somme;
	}
}
