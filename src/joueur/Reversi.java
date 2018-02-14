package joueur;

import joueur.Joueur;

public class Reversi extends Joueur {
	private int tourJoueur;

	public Reversi() {
		tourJoueur = 0; //joueur qui joue
	}

	public int getTourJoueur() {
		return tourJoueur;
	}
}
