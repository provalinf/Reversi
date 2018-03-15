import Etat.Etat;
import Etat.EtatReversi;
import joueur.Joueur;
import joueur.JoueurReversi;

public class Launcher {

	public static void main(String[] args) {
		EtatReversi e = new EtatReversi();
		Joueur j = new JoueurReversi();
		System.out.println(e.toString());
		e.printNumCases();
		System.out.println(e.coupPossibles(0));
		System.out.println(e.coupPossibles(1));
	}
}
