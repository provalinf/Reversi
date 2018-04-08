package joueur;

import Etat.EtatReversi;
import view.Plateau;

import java.util.List;

import static Etat.EtatReversi.NOIR;

public class JoueurReversi extends Joueur {

	private int tourJoueur;
	public static final int PROFONDEUR = 6;
	private Plateau plateau;


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
		return etat.importance(couleurJoueur);
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

	public int[] DecisionMinimax(EtatReversi etat, int profondeur, int numEval, boolean elagage) {
		List<Integer> coupsPossibles = etat.coupPossibles(colorPlayer);
		int[] bestCoup = new int[0];
		int scoreMax = -10000;
		if (coupsPossibles.size() == 0) return new int[]{666};

		for (Integer coupPossible : coupsPossibles) {
			EtatReversi tmp = etat.duplicateEtatReversi();
			int[] coordCoup = etat.getCoordCase(coupPossible);
			tmp.setCase(coordCoup[0], coordCoup[1], colorPlayer);
			tmp.setCoup(coordCoup[0], coordCoup[1], colorPlayer);

			int score;
			if (elagage)
				score = evaluationAlphaBeta(tmp, profondeur, tmp.couleurAdverse(colorPlayer), numEval, -10000, 10000);
			else score = evaluation(tmp, profondeur, tmp.couleurAdverse(colorPlayer), numEval);

			if (score >= scoreMax) {
				bestCoup = coordCoup.clone();
				scoreMax = score;
				/*System.out.println("PAF -->>>>>>>>:" + score + " :" + bestCoup[0] + "," + bestCoup[1]);
				if (colorPlayer == NOIR) {
					System.out.println();
					for (int i = 0; i < etat.tabPoids.length; i++) {
						for (int j = 0; j < etat.tabPoids[0].length; j++) {
							System.out.print(etat.tabPoids[i][j] + "\t");
						}
						System.out.println();
					}
					System.out.println();
					System.out.println();
				}*/
			}
		}

		return bestCoup;
	}

	private int evaluation(EtatReversi etat, int profondeur, int couleurJoueur, int numEval) {
		if (etat.coupPossibles(couleurJoueur).size() == 0) {
			if (etat.getWinner() == couleurJoueur) return 10000;
			if (etat.getWinner() == etat.couleurAdverse(couleurJoueur)) return -10000;
			if (etat.getWinner() == -1) return 0;
		}
		if (profondeur == 0) {
			if (numEval == 0) return eval0(etat, couleurJoueur);
			if (numEval == 1) return eval1(etat, couleurJoueur);
			if (numEval == 2) return eval2(etat, couleurJoueur);
		}
		List<Integer> coupsPossibles = etat.coupPossibles(couleurJoueur);
		if (couleurJoueur != colorPlayer) {
			int scoreMax = -10000;
			for (Integer coupPossible : coupsPossibles) {
				EtatReversi tmp = etat.duplicateEtatReversi();
				int[] coordCoup = etat.getCoordCase(coupPossible);
				tmp.setCase(coordCoup[0], coordCoup[1], couleurJoueur);
				tmp.setCoup(coordCoup[0], coordCoup[1], couleurJoueur);
				scoreMax = Math.max(scoreMax, evaluation(tmp, profondeur - 1, tmp.couleurAdverse(couleurJoueur), numEval));
			}
			return scoreMax;
		} else {
			int scoreMin = 10000;
			for (Integer coupPossible : coupsPossibles) {
				EtatReversi tmp = etat.duplicateEtatReversi();
				int[] coordCoup = etat.getCoordCase(coupPossible);
				tmp.setCase(coordCoup[0], coordCoup[1], couleurJoueur);
				tmp.setCoup(coordCoup[0], coordCoup[1], couleurJoueur);
				scoreMin = Math.min(scoreMin, evaluation(tmp, profondeur - 1, tmp.couleurAdverse(couleurJoueur), numEval));
			}
			return scoreMin;
		}
	}

	private int evaluationAlphaBeta(EtatReversi etat, int profondeur, int couleurJoueur, int alpha, int beta, int numEval) {
		if (etat.coupPossibles(couleurJoueur).size() == 0) {
			if (etat.getWinner() == couleurJoueur) return 10000;
			if (etat.getWinner() == etat.couleurAdverse(couleurJoueur)) return -10000;
			if (etat.getWinner() == -1) return 0;
		}
		if (profondeur == 0) {
			if (numEval == 0) return eval0(etat, couleurJoueur);
			if (numEval == 1) return eval1(etat, couleurJoueur);
			if (numEval == 2) return eval2(etat, couleurJoueur);
		}
		List<Integer> coupsPossibles = etat.coupPossibles(couleurJoueur);
		if (couleurJoueur != colorPlayer) {
			int scoreMax = -10000;
			for (Integer coupPossible : coupsPossibles) {
				EtatReversi tmp = etat.duplicateEtatReversi();
				int[] coordCoup = etat.getCoordCase(coupPossible);
				tmp.setCase(coordCoup[0], coordCoup[1], couleurJoueur);
				tmp.setCoup(coordCoup[0], coordCoup[1], couleurJoueur);
				scoreMax = Math.max(scoreMax, evaluationAlphaBeta(tmp, profondeur - 1, tmp.couleurAdverse(couleurJoueur), alpha, beta, numEval));
				if (scoreMax >= beta) return scoreMax;    // coupure bêta
				alpha = Math.max(alpha, scoreMax);
			}
			return scoreMax;
		} else {
			int scoreMin = 10000;
			for (Integer coupPossible : coupsPossibles) {
				EtatReversi tmp = etat.duplicateEtatReversi();
				int[] coordCoup = etat.getCoordCase(coupPossible);
				tmp.setCase(coordCoup[0], coordCoup[1], couleurJoueur);
				tmp.setCoup(coordCoup[0], coordCoup[1], couleurJoueur);
				scoreMin = Math.min(scoreMin, evaluationAlphaBeta(tmp, profondeur - 1, tmp.couleurAdverse(couleurJoueur), alpha, beta, numEval));
				if (scoreMin <= alpha) return scoreMin;    // coupure alpha
				beta = Math.min(beta, scoreMin);
			}
			return scoreMin;
		}
	}

	public int max(EtatReversi etat, int couleurJoueur, int profondeur, int numEval) {
		if (profondeur == 0 || !etat.isCoupPossible()) {
			if (numEval == 0) return eval0(etat, couleurJoueur);
			if (numEval == 1) return eval1(etat, couleurJoueur);
			if (numEval == 2) return eval2(etat, couleurJoueur);
		}

		List<Integer> coupsPossibles = etat.coupPossibles(couleurJoueur);
		int max = -800;
		int[] bestCoup = null;

		for (Integer coupPossible : coupsPossibles) {
			EtatReversi tmp = etat.duplicateEtatReversi();
			int[] coordCoup = etat.getCoordCase(coupPossible);
			tmp.setCase(coordCoup[0], coordCoup[1], couleurJoueur);
			tmp.setCoup(coordCoup[0], coordCoup[1], couleurJoueur);
			int val = min(tmp, etat.couleurAdverse(couleurJoueur), profondeur - 1, numEval);

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


	public int min(EtatReversi etat, int couleurJoueur, int profondeur, int numEval) {
		if (profondeur == 0 || !etat.isCoupPossible()) {
			if (numEval == 0) return eval0(etat, couleurJoueur);
			if (numEval == 1) return eval1(etat, couleurJoueur);
			if (numEval == 2) return eval2(etat, couleurJoueur);
		}

		List<Integer> coupsPossibles = etat.coupPossibles(couleurJoueur);
		int min = etat.getSize()[0] >= etat.getSize()[1] ? etat.getSize()[0] : etat.getSize()[1];
		int[] coordCoup = null;

		for (Integer coupPossible : coupsPossibles) {
			EtatReversi tmp = etat.duplicateEtatReversi();
			coordCoup = etat.getCoordCase(coupPossible);
			tmp.setCase(coordCoup[0], coordCoup[1], couleurJoueur);
			tmp.setCoup(coordCoup[0], coordCoup[1], couleurJoueur);
			int val = max(tmp, etat.couleurAdverse(couleurJoueur), profondeur - 1, numEval);

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

	public void debug(Plateau plateau) {
		this.plateau = plateau;
	}

/*    public void setMeilleurCoup() {
        max((EtatReversi) etat, colorPlayer, PROFONDEUR, );
    }*/
}
