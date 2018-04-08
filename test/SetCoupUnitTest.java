import Etat.EtatReversi;
import joueur.JoueurReversi;
import org.junit.jupiter.api.Test;
import view.DrawingWindow;
import view.Plateau;

import static Etat.EtatReversi.BLANC;
import static Etat.EtatReversi.NOIR;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Valentin.
 */
public class SetCoupUnitTest {

	@Test
	public void testSetCoup() {
		EtatReversi e = new EtatReversi();
		JoueurReversi b = new JoueurReversi(e, BLANC);
		JoueurReversi n = new JoueurReversi(e, NOIR);
		DrawingWindow dw = new DrawingWindow("Reversi", 1200, 700);
		Plateau plateau = new Plateau(dw, e);
		e.setCase(3, 3, NOIR);
		e.setCase(3, 4, BLANC);
		e.setCase(4, 3, BLANC);
		e.setCase(4, 4, BLANC);
		e.setCase(4, 5, BLANC);
		//e.setCase(5, 4, BLANC);
		/*e.setCase(5, 5, BLANC);
		e.setCase(5, 6, BLANC);*/
		e.setCase(6, 4, NOIR);
		e.setCase(6, 5, NOIR);
		e.setCase(6, 6, NOIR);
		e.setCase(7, 5, NOIR);
		e.setCase(7, 6, NOIR);
		e.setCase(7, 7, NOIR);
		System.out.println(e.toString());
		plateau.dessinerPlateau();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		e.setCase(7, 4, BLANC);
		e.setCoup(7,4,BLANC);
		plateau.dessinerPlateau();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		assertEquals(e.getCase(6, 4), NOIR, "Erreur 6,4 Blanc");
		assertEquals(e.getCase(7, 5), NOIR, "Erreur 7,5 Blanc");
		assertEquals(e.getCase(7, 6), NOIR,"Erreur 7,6 Blanc");
		assertEquals(e.getCase(7, 7), NOIR,"Erreur 7,7 Blanc");
		System.out.println(e.toString());


	}
}
