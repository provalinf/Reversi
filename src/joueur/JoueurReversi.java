package joueur;

import Etat.EtatReversi;

import java.util.List;

public class JoueurReversi extends Joueur {

	private int tourJoueur;
	public static final int PROFONDEUR = 4;


	public JoueurReversi(EtatReversi e, int colorPlayer) {
		super(e, colorPlayer);
		tourJoueur = 1; //joueur qui joue
	}

	public int getTourJoueur() {
		return tourJoueur;
	}

	public void successeurs() {
		tourJoueur = tourJoueur + 1 % 2; // permet de changer de joueur automatiquement
	}

	/**
	 * Evaluation en fonction du nb de pièce du joueur
	 *
	 * @param etat
	 * @param couleurJoueur
	 * @return
	 */
	public int eval0(EtatReversi etat, int couleurJoueur) {
		return etat.getNbPion(couleurJoueur);
	}

	/**
	 * Evaluation en fonction des coups possibles restant au joueur
	 *
	 * @param etat
	 * @param couleurJoueur
	 * @return
	 */
	public int eval1(EtatReversi etat, int couleurJoueur) {
		return etat.coupPossibles(couleurJoueur).size();
	}

	/**
	 * Evaluation en fonction de l'importance des cases capturées par le joueur
	 *
	 * @param etat
	 * @param couleurJoueur
	 * @return
	 */
	public int eval2(EtatReversi etat, int couleurJoueur) {
		return 0;
	}

	/**
	 * Comparaison des performances des fonctions évaluation
	 * en comparant le nombre de coup pour chaque joueurs.
	 * Plus la partie est longue, plus intéressante est la partie
	 *
	 * @return
	 */
	public int compareEvaluations() {
		return 0;
	}

	public int max(EtatReversi etat, int couleurJoueur, int profondeur) {
		if (profondeur == 0 || !etat.isCoupPossible()) return eval0(etat, couleurJoueur);

		List<Integer> coupsPossibles = etat.coupPossibles(couleurJoueur);
		if (profondeur == PROFONDEUR) {
			for (Integer cp : coupsPossibles) {
				System.out.println(etat.getCoordCase(cp)[0] + "," + etat.getCoordCase(cp)[1]);
			}
		}
		int max = 0;
		int[] bestCoup = null;

		for (Integer coupPossible : coupsPossibles) {
			EtatReversi tmp = etat.duplicateEtatReversi();
			int[] coordCoup = etat.getCoordCase(coupPossible);
			tmp.setCase(coordCoup[0], coordCoup[1], couleurJoueur);
			tmp.setCoup(coordCoup[0], coordCoup[1], couleurJoueur);
			int val = min(tmp, etat.couleurAdverse(couleurJoueur), profondeur - 1);

			//int nbCapture = etat.getNbCaptures(coordCoup[0], coordCoup[1], couleurJoueur);
			if (max < val) {
				max = val;
				bestCoup = coordCoup.clone();
				if (profondeur == PROFONDEUR) {
					System.out.println("best coup " + couleurJoueur + " " + bestCoup[0] + "," + bestCoup[1]);
				}
			}
		}

		if (coupsPossibles.size() != 0 && bestCoup != null) {
			if (profondeur == PROFONDEUR) {
				System.out.println("best coup---> " + couleurJoueur + " " + bestCoup[0] + "," + bestCoup[1]);
			}
			etat.setCase(bestCoup[0], bestCoup[1], couleurJoueur);
			etat.setCoup(bestCoup[0], bestCoup[1], couleurJoueur);

		}

		return max;
	}


	public int min(EtatReversi etat, int couleurJoueur, int profondeur) {
		if (profondeur == 0 || !etat.isCoupPossible()) return eval0(etat, couleurJoueur);

		List<Integer> coupsPossibles = etat.coupPossibles(couleurJoueur);
		int min = etat.getSize()[0] >= etat.getSize()[1] ? etat.getSize()[0] : etat.getSize()[1];
		int[] coordCoup = null;

		for (Integer coupPossible : coupsPossibles) {
			EtatReversi tmp = etat.duplicateEtatReversi();
			coordCoup = etat.getCoordCase(coupPossible);
			tmp.setCase(coordCoup[0], coordCoup[1], couleurJoueur);
			tmp.setCoup(coordCoup[0], coordCoup[1], couleurJoueur);
			int val = max(tmp, etat.couleurAdverse(couleurJoueur), profondeur - 1);

			//int nbCapture = etat.getNbCaptures(coordCoup[0], coordCoup[1], couleurJoueur);
			if (min > val) min = val;
		}

		if (coupsPossibles.size() == 0) {
			min = 0;
		} else {
			etat.setCase(coordCoup[0], coordCoup[1], couleurJoueur);
			etat.setCoup(coordCoup[0], coordCoup[1], couleurJoueur);
		}


		return min;
	}

	public void setMeilleurCoup() {
		max((EtatReversi) etat, colorPlayer, PROFONDEUR);
	}
}
