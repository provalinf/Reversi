package joueur;


public class Reversi extends Joueur {
	private Integer grille[][];
	private int tourJoueur;
	private int size = 8;

	public Reversi() {
		grille = new Integer[size][size]; //0 = noir, 1 = blanc, -1 = vide
		etatInitial();
		tourJoueur = 1; //joueur qui joue
	}

	private void etatInitial(){
		for (int i = 0; i < grille.length; i++) {
			for (int j = 0; j < grille[0].length; j++) {
				if((i == size/2 && j == size/2)||(i == size/2+1 && j == size/2+1)){
					grille[i][j] = 0;
				}
				if ((i == size/2 && j == size/2+1)||(i== size/2+1 && j == size/2)){
					grille[i][j] = 1;
				}
				else grille[i][j] = -1;
			}
		}
	}
	public Integer[][] getGrille() {
		return grille;
	}

	public int getTourJoueur() {
		return tourJoueur;
	}

	public void successeurs(){
		tourJoueur = tourJoueur+1%2; // permet de changer de joueur automatiquement

	}
}
