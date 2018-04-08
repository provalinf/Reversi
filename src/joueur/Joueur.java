package joueur;

import Etat.Etat;

public abstract class Joueur {
	protected Etat.EtatReversi etat;
	protected int colorPlayer;

	public Joueur(Etat.EtatReversi etat, int colorPlayer) {
		this.etat = etat;
		this.colorPlayer = colorPlayer;
	}

	public int getColorPlayer() {
		return colorPlayer;
	}
}
