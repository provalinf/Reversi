import Etat.EtatReversi;
import joueur.JoueurReversi;
import view.DrawingWindow;
import view.Plateau;

import static Etat.EtatReversi.BLANC;
import static Etat.EtatReversi.NOIR;
import static joueur.JoueurReversi.PROFONDEUR;

public class Launcher {

	public static void main(String[] args) {
		EtatReversi e = new EtatReversi();
		JoueurReversi b = new JoueurReversi(e, BLANC);
		JoueurReversi n = new JoueurReversi(e, NOIR);

		DrawingWindow dw = new DrawingWindow("Reversi", 1200, 700);
		Plateau plateau = new Plateau(dw, e);
		b.debug(plateau);

		//e.printNumCases();
		/*e.printNumCases();
		System.out.println(e.coupPossibles(0));
		System.out.println(e.coupPossibles(1));*/
		int lastColor = NOIR;
		//System.out.println(e.toString());
		while (e.isCoupPossible()) {
			plateau.dessinerPlateau();
			/*int[] coordCaseClic = plateau.clicCase();
			while (!e.coupPossibles(lastColor).contains(e.getNumCase(coordCaseClic[0], coordCaseClic[1]))) {
				System.out.println("Coup impossible");
				System.out.println("Cliquez sur une autre case");
				coordCaseClic = plateau.clicCase();
			}
			e.setCase(coordCaseClic[0], coordCaseClic[1], lastColor);
			e.setCoup(coordCaseClic[0], coordCaseClic[1], lastColor);*/
			System.out.println("BLANC");
			//b.max(e, b.getColorPlayer(), PROFONDEUR, 2);


			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			int[] resB = b.DecisionMinimax(e, PROFONDEUR, 3, false);
			if (resB.length == 1) {
				System.out.println("Perdu");
			} else {
				e.setCase(resB[0], resB[1], b.getColorPlayer());
				e.setCoup(resB[0], resB[1], b.getColorPlayer());
			}
			plateau.setEtat(e);
			plateau.dessinerPlateau();
			System.out.println();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println("NOIR");
			//n.max(e, n.getColorPlayer(), PROFONDEUR, 0);
			int[] resN = n.DecisionMinimax(e, PROFONDEUR, 3, false);
			if (resN.length == 1) {
				System.out.println("Perdu");
			} else {
				e.setCase(resN[0], resN[1], n.getColorPlayer());
				e.setCoup(resN[0], resN[1], n.getColorPlayer());
			}
			plateau.setEtat(e);
			plateau.dessinerPlateau();
			System.out.println();
			System.out.println("blanc"+e.getNbPion(BLANC));
			System.out.println("noir"+e.getNbPion(NOIR));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			System.out.println("Les " + lastColor + " doivent jouer");
			//e.jeuTerminal(lastColor);
			if (e.coupPossibles(lastColor == NOIR ? BLANC : NOIR).size() > 0) {
				lastColor = lastColor == NOIR ? BLANC : NOIR;
			}
			//System.out.println(e.toString());
		}
		System.out.println("Le jeu terminé");
		System.out.println("Les " + e.getWinner() + " gagnent");

	}
}
