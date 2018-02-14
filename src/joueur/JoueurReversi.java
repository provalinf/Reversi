package joueur;

public class JoueurReversi extends Joueur {

	private int tourJoueur;

	public JoueurReversi() {
		tourJoueur = 1; //joueur qui joue
	}

	public int getTourJoueur() {
		return tourJoueur;
	}

	public void successeurs(){
		tourJoueur = tourJoueur+1%2; // permet de changer de joueur automatiquement

	}
}
