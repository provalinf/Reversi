package view;

import Etat.EtatReversi;

import java.awt.*;

/**
 * Created by Valentin.
 */
public class Plateau {

	private DrawingWindow dw;
	private EtatReversi etat;

	private static final int POS_PLATEAU = 10;
	private static final int TAILLE_CASE = 60;
	private static final int TAILLE_PION = TAILLE_CASE - 10;

	private static final Color BACKGROUND_WINDOW = Color.DARK_GRAY;
	private static final Color PLATEAU_COLOR = new Color(44, 115, 1);
	private static final Color BORDER_CASE = new Color(27, 55, 1);

	private static final Color PION_BLACK = Color.BLACK;
	private static final Color PION_WHITE = Color.WHITE;

	public Plateau(DrawingWindow dw, EtatReversi etat) {
		this.dw = dw;
		this.etat = etat;
	}

	public void dessinerPlateau() {
		dw.setBgColor(BACKGROUND_WINDOW);
		dw.clearGraph();
		for (int row = 0; row < etat.getSize()[0]; row++) {
			for (int col = 0; col < etat.getSize()[1]; col++) {
				dessinerCases(row, col);
				if (etat.getCase(row, col) == EtatReversi.NOIR) {
					dessinerPion(row, col, PION_BLACK);
				} else if (etat.getCase(row, col) == EtatReversi.BLANC) {
					dessinerPion(row, col, PION_WHITE);
				} else {
					dessinerNumCase(row, col);
				}
			}
		}
	}

	public void dessinerCases(int posX, int posY) {
		dw.setColor(PLATEAU_COLOR);
		dw.fillRect(POS_PLATEAU + posY * TAILLE_CASE, POS_PLATEAU + posX * TAILLE_CASE, POS_PLATEAU + posY * TAILLE_CASE + TAILLE_CASE, POS_PLATEAU + posX * TAILLE_CASE + TAILLE_CASE);
		dw.setColor(BORDER_CASE);
		dw.drawLine(POS_PLATEAU + posY * TAILLE_CASE, POS_PLATEAU + posX * TAILLE_CASE, POS_PLATEAU + posY * TAILLE_CASE + TAILLE_CASE, POS_PLATEAU + posX * TAILLE_CASE);//haut
		dw.drawLine(POS_PLATEAU + posY * TAILLE_CASE, POS_PLATEAU + posX * TAILLE_CASE, POS_PLATEAU + posY * TAILLE_CASE, POS_PLATEAU + posX * TAILLE_CASE + TAILLE_CASE);//gauche
		dw.drawLine(POS_PLATEAU + posY * TAILLE_CASE, POS_PLATEAU + posX * TAILLE_CASE + TAILLE_CASE, POS_PLATEAU + posY * TAILLE_CASE + TAILLE_CASE, POS_PLATEAU + posX * TAILLE_CASE + TAILLE_CASE);//bas
		dw.drawLine(POS_PLATEAU + posY * TAILLE_CASE + TAILLE_CASE, POS_PLATEAU + posX * TAILLE_CASE, POS_PLATEAU + posY * TAILLE_CASE + TAILLE_CASE, POS_PLATEAU + posX * TAILLE_CASE + TAILLE_CASE);//droite
	}

	public void dessinerPion(int posX, int posY, Color color) {
		dw.setColor(color);
		System.out.println((POS_PLATEAU + posY * TAILLE_CASE + TAILLE_PION / 2 + (TAILLE_CASE - TAILLE_PION) / 2) + " " + (POS_PLATEAU + posX * TAILLE_CASE + TAILLE_PION / 2 + (TAILLE_CASE - TAILLE_PION) / 2));
		dw.fillCircle(POS_PLATEAU + posY * TAILLE_CASE + TAILLE_PION / 2 + (TAILLE_CASE - TAILLE_PION) / 2, POS_PLATEAU + posX * TAILLE_CASE + TAILLE_PION / 2 + (TAILLE_CASE - TAILLE_PION) / 2, TAILLE_PION / 2);
	}

	public void dessinerNumCase(int posX, int posY) {
		dw.setColor(Color.WHITE);
		dw.drawText(POS_PLATEAU + posY * TAILLE_CASE + TAILLE_CASE / 2, POS_PLATEAU + posX * TAILLE_CASE + TAILLE_CASE / 2, Integer.toString(etat.getNumCase(posX, posY)));
	}

	public int[] clicCase() {
		dw.waitMousePress();
		int x = dw.getMouseX();
		int y = dw.getMouseY();
		System.out.println(x + " " + y);
		if (x > POS_PLATEAU && x < TAILLE_CASE * etat.getSize()[0]) {
			if (y > POS_PLATEAU && y < TAILLE_CASE * etat.getSize()[1]) {
				int caseX = (x - POS_PLATEAU) / TAILLE_CASE;
				int caseY = (y - POS_PLATEAU) / TAILLE_CASE;
				System.out.println("Clic case " + caseX + " " + caseY);
				return new int[]{caseY, caseX};
			}
		}
		System.out.println("à coté");
		return clicCase();
	}

}
