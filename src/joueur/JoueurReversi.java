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

    public int DecisionMinimax(EtatReversi etat, int profondeur, int numEval) {
        List<Integer> coupsPossibles = etat.coupPossibles(colorPlayer);
        int[] bestCoup;
        int scoreMax = -10000;
        if (coupsPossibles.size() == 0) return 666;

        for (Integer coupPossible : coupsPossibles) {
            EtatReversi tmp = etat.duplicateEtatReversi();
            int[] coordCoup = etat.getCoordCase(coupPossible);
            tmp.setCase(coordCoup[0], coordCoup[1], colorPlayer);
            tmp.setCoup(coordCoup[0], coordCoup[1], colorPlayer);
            int score = evaluation(tmp, profondeur, numEval);
        }
    }

    private int evaluation(EtatReversi etat, int profondeur, int numEval) {
        int scoreMax;
        if (etat.coupPossibles(etat.couleurAdverse(colorPlayer)).size() == 0) {
            if (etat.getWinner() == colorPlayer) return 10000;
            if (etat.getWinner() == etat.couleurAdverse(colorPlayer)) return -10000;
            if (etat.getWinner() == -1) return 0;
        }
        if (profondeur == 0) {
            if (numEval == 0) return eval0(etat, colorPlayer);
            if (numEval == 1) return eval1(etat, colorPlayer);
            if (numEval == 2) return eval2(etat, colorPlayer);
        }
        if () {
            scoreMax = -10000;
            for (Integer coupPossible : coupsPossibles) {
                scoreMax = Math.max(scoreMax, evaluation(etat, profondeur-1, numEval));
            }
            return scoreMax;
        }
        else{
            int scoreMin = 10000;
            for (Integer coupPossible : coupsPossibles) {
                scoreMin = Math.min(scoreMin, evaluation(etat, profondeur-1, numEval));
            }
            return scoreMin;
        }
        return 0;
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

/*    public void setMeilleurCoup() {
        max((EtatReversi) etat, colorPlayer, PROFONDEUR, );
    }*/
}
