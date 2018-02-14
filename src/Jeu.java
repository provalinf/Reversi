import joueur.Joueur;

public class Jeu extends Joueur {
    private Integer grille[][];
    private int tourJoueur;

    public Jeu() {
        grille = new Integer[8][8]; //0 = noir, 1 = blanc, -1 = vide
        tourJoueur = 0; //joueur qui joue
    }

    public Integer[][] getGrille() {
        return grille;
    }

    public int getTourJoueur() {
        return tourJoueur;
    }


}
