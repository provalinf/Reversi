package joueur;

import Etat.Etat;

public abstract class Joueur {
	protected Etat etat;
	protected int colorPlayer;

	public Joueur(Etat etat, int colorPlayer) {
		this.etat = etat;
		this.colorPlayer = colorPlayer;
	}

	public int getColorPlayer() {
		return colorPlayer;
	}
}
