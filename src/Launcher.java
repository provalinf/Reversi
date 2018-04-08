import Etat.EtatReversi;
import joueur.JoueurReversi;
import view.DrawingWindow;
import view.Plateau;

import java.util.Scanner;

import static Etat.EtatReversi.BLANC;
import static Etat.EtatReversi.NOIR;
import static joueur.JoueurReversi.PROFONDEUR;

public class Launcher {

	private static final Scanner scanner = new Scanner(System.in);
	private static int modeJeu, algo, eval;
	private static DrawingWindow dw;
	private static Plateau plateau;
	private static long timeStart;

	public static void main(String[] args) {
		EtatReversi e = new EtatReversi();
		JoueurReversi n = new JoueurReversi(e, NOIR);    // Noir commencent
		JoueurReversi b = new JoueurReversi(e, BLANC);

		System.out.println("Reversi - BASILE_BONNAL\n");

		System.out.println("Choisissez un mode de jeu");
		System.out.println("1 - Humain vs Humain");
		System.out.println("2 - Humain vs IA");
		System.out.println("3 - IA vs Humain");
		System.out.println("4 - IA vs IA");
		System.out.println("5 - IA vs IA (comparaisons de performance) sans IG");
		modeJeu = scanner.nextInt();
		while (modeJeu < 1 || modeJeu > 5) {
			System.out.println("Saisie incorrecte... Recommencez :");
			modeJeu = scanner.nextInt();
		}

		if (modeJeu != 1) {
			System.out.println("Choisissez un algorithme");
			System.out.println("1 - MinMax");
			System.out.println("2 - MinMax Alpha Beta");
			algo = scanner.nextInt();
			while (algo < 1 || algo > 2) {
				System.out.println("Saisie incorrecte... Recommencez :");
				algo = scanner.nextInt();
			}

			System.out.println("Choisissez une fonction d'évaluation");
			System.out.println("1 - Eval0 : En fonction du nombre de pièces sur le plateau");
			System.out.println("2 - Eval1 : En fonction du nombre de coups possibles");
			System.out.println("3 - Eval2 : En fonction de l'importance des cases");
			System.out.println("4 - Eval3 : Méthode Monte-Carlo");
			eval = scanner.nextInt();
			while (eval < 1 || eval > 4) {
				System.out.println("Saisie incorrecte... Recommencez :");
				eval = scanner.nextInt();
			}
		}

		if (modeJeu != 5) {
			dw = new DrawingWindow("Reversi - BASILE_BONNAL", 500, 500);
			plateau = new Plateau(dw, e);
			//b.debug(plateau);
			plateau.dessinerPlateau();
		} else {
			System.out.println("Démarrage du comptage");
			timeStart = System.nanoTime();
		}

		int colorPlay = NOIR;
		while (e.isCoupPossible()) {
			if (modeJeu != 5) {
				plateau.dessinerPlateau();
				plateau.dessinerAide(e.coupPossibles(colorPlay), colorPlay);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} else if (modeJeu == 5) {
				System.out.print(".");
			}

			if (modeJeu == 1) {
				humainJoue(e, colorPlay);
			} else if (modeJeu == 2) {
				if (colorPlay == NOIR) {
					humainJoue(e, colorPlay);
				} else {
					int[] res = b.DecisionMinimax(e, PROFONDEUR, eval - 1, algo == 2);
					if (res.length != 1) {
						e.setCase(res[0], res[1], b.getColorPlayer());
						e.setCoup(res[0], res[1], b.getColorPlayer());
					}
				}
			} else if (modeJeu == 3) {
				if (colorPlay == NOIR) {
					int[] res = n.DecisionMinimax(e, PROFONDEUR, eval - 1, algo == 2);
					if (res.length != 1) {
						e.setCase(res[0], res[1], n.getColorPlayer());
						e.setCoup(res[0], res[1], n.getColorPlayer());
					}
				} else {
					humainJoue(e, colorPlay);
				}
			} else if (modeJeu == 4 || modeJeu == 5) {
				if (colorPlay == NOIR) {
					int[] res = n.DecisionMinimax(e, PROFONDEUR, eval - 1, algo == 2);
					if (res.length != 1) {
						e.setCase(res[0], res[1], n.getColorPlayer());
						e.setCoup(res[0], res[1], n.getColorPlayer());
					}
				} else {
					int[] res = b.DecisionMinimax(e, PROFONDEUR, eval - 1, algo == 2);
					if (res.length != 1) {
						e.setCase(res[0], res[1], b.getColorPlayer());
						e.setCoup(res[0], res[1], b.getColorPlayer());
					}
				}
			}
			colorPlay = e.couleurAdverse(colorPlay);
		}
		if (modeJeu == 5) {
			System.out.println("\nLe temps d'execution est de " + (System.nanoTime() - timeStart) / 1000000000.0 + "s");
		}
		System.out.println("Le jeu est terminé");
		System.out.println("Les " + (e.getWinner()==NOIR?"NOIR":"BLANC") + " gagnent");
	}

	private static void humainJoue(EtatReversi e, int colorPlay) {
		int[] coordCaseClic = plateau.clicCase();
		while (!e.coupPossibles(colorPlay).contains(e.getNumCase(coordCaseClic[0], coordCaseClic[1]))) {
			System.out.println("Coup impossible");
			System.out.println("Cliquez sur une autre case");
			coordCaseClic = plateau.clicCase();
		}
		e.setCase(coordCaseClic[0], coordCaseClic[1], colorPlay);
		e.setCoup(coordCaseClic[0], coordCaseClic[1], colorPlay);
	}

	private static void play(int couleur) {

	}
}
