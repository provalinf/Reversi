import Etat.EtatReversi;
import joueur.Joueur;
import joueur.JoueurReversi;

import static Etat.EtatReversi.BLANC;
import static Etat.EtatReversi.NOIR;

public class Launcher {

	public static void main(String[] args) {
		EtatReversi e = new EtatReversi();
		Joueur j = new JoueurReversi(e);

		e.printNumCases();
		System.out.println(e.coupPossibles(0));
		System.out.println(e.coupPossibles(1));
		int lastColor = NOIR;
		System.out.println(e.toString());
		while (e.isCoupPossible()) {
			System.out.println("Les " + lastColor + " doivent jouer");
			e.jeuTerminal(lastColor);
			if (e.coupPossibles(lastColor == NOIR ? BLANC : NOIR).size() > 0) {
				lastColor = lastColor == NOIR ? BLANC : NOIR;
			}
			System.out.println(e.toString());
		}
		System.out.println("Le jeu terminé");
		System.out.println("Les " + e.getWinner() + " gagnent");

	}
}
