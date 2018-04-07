package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Fenêtre de dessin
 * <p>
 * <p>Cette classe permet d'écrire des applications graphiques simples
 * en dessinant dans une fenêtre.
 * <p>
 * <p><b>NB.</b> Pour toutes les méthodes de dessin, le coin en haut à
 * gauche de la fenêtre a les coordonnées (0, 0).  Le coin en bas à
 * droite de la fenêtre a les coordonnées (largeur - 1, hauteur - 1),
 * si la fenêtre est de dimension largeur × hauteur.
 * <p>
 * <p>Un appui sur la touche &lt;Esc&gt; provoque la fermeture de la
 * fenêtre.  Comme pour la plupart des applications, il est également
 * possible de fermer la fenêtre via le gestionnaire de fenêtres.
 *
 * @author Arnaud Giersch &lt;arnaud.giersch@univ-fcomte.fr&gt;
 * @version 20141104
 */
public class DrawingWindow {

	/**
	 * Largeur de la fenêtre
	 */
	public final int width;

	/**
	 * Hauteur de la fenêtre
	 */
	public final int height;

	/**
	 * Construit une nouvelle fenêtre de dessin avec le titre et les dimensions
	 * passés en paramètres.
	 *
	 * @param title  titre de la fenêtre
	 * @param width  largeur de la fenêtre
	 * @param height hauteur de la fenêtre
	 * @see JPanel
	 */
	public DrawingWindow(String title, int width, int height) {

		this.title = new String(title);
		this.width = width;
		this.height = height;

		mouseLock = new Object();
		mouseEvent = null;
		mousePos = new Point(0, 0);
		mouseButton = 0;

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		graphics = image.createGraphics();

		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createGUI();
				}
			});
		} catch (Exception e) {
			System.err.println("Error: interrupted while creating GUI");
			System.err.println("Got exception: " + e);
			System.exit(1);
		}

		setColor(Color.BLACK);
		setBgColor(Color.WHITE);
		clearGraph();
		sync();
	}

	/**
	 * Change la couleur de dessin.
	 *
	 * @param color couleur
	 * @see Color
	 * @see #setColor(int)
	 * @see #setColor(String)
	 * @see #setColor(float, float, float)
	 * @see #setBgColor(Color)
	 */
	public void setColor(Color color) {
		graphics.setColor(color);
	}

	/**
	 * Change la couleur de dessin.
	 * <p>
	 * La couleur est un entier, tel que retourné par {@link #getPointColor}.
	 * Normalement de la forme #00RRGGBB.
	 *
	 * @param rgb couleur
	 * @see #setColor(String)
	 * @see #setColor(float, float, float)
	 * @see #setBgColor(int)
	 * @see #getPointColor
	 */
	public void setColor(int rgb) {
		setColor(new Color(rgb));
	}

	/**
	 * Change la couleur de dessin.
	 *
	 * @param name nom de couleur
	 * @see #setColor(int)
	 * @see #setColor(float, float, float)
	 * @see #setBgColor(String)
	 * @see <a href="http://www.w3.org/TR/SVG/types.html#ColorKeywords">liste des noms de couleurs</a>
	 */
	public void setColor(String name) {
		Color color = colorMap.get(name);
		if (color != null)
			setColor(color);
		else
			System.err.println("Warning: color not found: " + name);
	}

	/**
	 * Change la couleur de dessin.
	 * <p>
	 * Les composantes de rouge, vert et bleu de la couleur doivent être
	 * compris entre 0 et 1.  Si le trois composantes sont à 0, on obtient
	 * du noir; si les trois composantes sont à 1, on obtient du blanc.
	 *
	 * @param red   composante de rouge
	 * @param green composante de vert
	 * @param blue  composante de bleu
	 * @see #setColor(int)
	 * @see #setColor(String)
	 * @see #setBgColor(float, float, float)
	 */
	public void setColor(float red, float green, float blue) {
		setColor(new Color(red, green, blue));
	}

	/**
	 * Change la couleur de fond.
	 *
	 * @param color couleur
	 * @see #setBgColor(int)
	 * @see #setBgColor(String)
	 * @see #setBgColor(float, float, float)
	 * @see #setColor(Color)
	 * @see #clearGraph
	 */
	public void setBgColor(Color color) {
		bgColor = color;
	}

	/**
	 * Change la couleur de fond.
	 *
	 * @param rgb couleur
	 * @see #setBgColor(String)
	 * @see #setBgColor(float, float, float)
	 * @see #setColor(int)
	 * @see #getPointColor
	 * @see #clearGraph
	 */
	public void setBgColor(int rgb) {
		setBgColor(new Color(rgb));
	}

	/**
	 * Change la couleur de fond.
	 *
	 * @param name nom de couleur
	 * @see #setBgColor(int)
	 * @see #setBgColor(float, float, float)
	 * @see #setColor(String)
	 * @see #clearGraph
	 * @see <a href="http://www.w3.org/TR/SVG/types.html#ColorKeywords">liste des noms de couleurs</a>
	 */
	public void setBgColor(String name) {
		Color color = colorMap.get(name);
		if (color != null)
			setBgColor(color);
		else
			System.err.println("Warning: color not found: " + name);
	}

	/**
	 * Change la couleur de fond.
	 *
	 * @param red   composante de rouge
	 * @param green composante de vert
	 * @param blue  composante de bleu
	 * @see #setBgColor(int)
	 * @see #setBgColor(String)
	 * @see #setColor(float, float, float)
	 * @see #clearGraph
	 */
	public void setBgColor(float red, float green, float blue) {
		setBgColor(new Color(red, green, blue));
	}

	/**
	 * Efface la fenêtre.
	 * <p>
	 * La fenêtre est effacée avec la couleur de fond courante.
	 *
	 * @see #setBgColor
	 */
	public void clearGraph() {
		synchronized (image) {
			Color c = graphics.getColor();
			graphics.setColor(bgColor);
			graphics.fillRect(0, 0, width, height);
			graphics.setColor(c);
		}
		panel.repaint();
	}

	/**
	 * Dessine un point.
	 * <p>
	 * Dessine un point (pixel) aux coordonnées (x, y), avec la couleur de
	 * dessin courante.
	 *
	 * @see #setColor
	 */
	public void drawPoint(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return;
		synchronized (image) {
			image.setRGB(x, y, graphics.getColor().getRGB());
		}
		panel.repaint(x, y, 1, 1);
	}

	/**
	 * Dessine un segment.
	 * <p>
	 * Dessine un segement de droite entre les coordonnées (x1, y1) et
	 * (x2, y2), avec la couleur de dessin courante.
	 *
	 * @see #setColor
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		synchronized (image) {
			graphics.drawLine(x1, y1, x2, y2);
		}
		panel.repaint(Math.min(x1, x2), Math.min(y1, y2),
				Math.abs(x1 - x2) + 1, Math.abs(y1 - y2) + 1);
	}

	/**
	 * Dessine un rectangle.
	 * <p>
	 * Dessine le rectangle parallèle aux axes et défini par les
	 * coordonnées de deux sommets opposés (x1, y1) et (x2, y2).  Utilise
	 * la couleur de dessin courante.
	 *
	 * @see #fillRect
	 * @see #setColor
	 */
	public void drawRect(int x1, int y1, int x2, int y2) {
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);
		int w = Math.abs(x1 - x2);
		int h = Math.abs(y1 - y2);
		synchronized (image) {
			graphics.drawRect(x, y, w, h);
		}
		panel.repaint(x, y, w + 1, h + 1);
	}

	/**
	 * Dessine un rectangle plein.
	 * <p>
	 * Dessine le rectangle plein parallèle aux axes et défini par les
	 * coordonnées de deux sommets opposés (x1, y1) et (x2, y2).  Utilise
	 * la couleur de dessin courante.
	 *
	 * @see #drawRect
	 * @see #setColor
	 */
	public void fillRect(int x1, int y1, int x2, int y2) {
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);
		int w = Math.abs(x1 - x2) + 1;
		int h = Math.abs(y1 - y2) + 1;
		synchronized (image) {
			graphics.fillRect(x, y, w, h);
		}
		panel.repaint(x, y, w, h);
	}

	/**
	 * Dessine un cercle.
	 * <p>
	 * Dessine un cercle de centre (x, y) et de rayon r.  Utilise la
	 * couleur de dessin courante.
	 *
	 * @see #fillCircle
	 * @see #setColor
	 */
	public void drawCircle(int x, int y, int r) {
		synchronized (image) {
			graphics.drawOval(x - r, y - r, 2 * r, 2 * r);
		}
		panel.repaint(x - r, y - r, 2 * r + 1, 2 * r + 1);
	}

	/**
	 * Dessine un disque.
	 * <p>
	 * Dessine un disque (cercle plein) de centre (x, y) et de rayon r.
	 * Utilise la couleur de dessin courante.
	 *
	 * @see #drawCircle
	 * @see #setColor
	 */
	public void fillCircle(int x, int y, int r) {
		synchronized (image) {
			graphics.drawOval(x - r, y - r, 2 * r, 2 * r);
			graphics.fillOval(x - r, y - r, 2 * r, 2 * r);
		}
		panel.repaint(x - r, y - r, 2 * r + 1, 2 * r + 1);
	}

	/**
	 * Dessine un triangle.
	 * <p>
	 * Dessine un triangle défini par les coordonnées de ses sommets:
	 * (x1, y1), (x2, y2) et (x3, y3).  Utilise la couleur de dessin
	 * courante.
	 *
	 * @see #fillTriangle
	 * @see #setColor
	 */

	public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		Polygon poly = new Polygon();
		poly.addPoint(x1, y1);
		poly.addPoint(x2, y2);
		poly.addPoint(x3, y3);
		synchronized (image) {
			graphics.drawPolygon(poly);
		}
		Rectangle bounds = poly.getBounds();
		bounds.setSize(bounds.width + 1, bounds.height + 1);
		panel.repaint(bounds);
	}

	/**
	 * Dessine un triangle plein.
	 * <p>
	 * Dessine un triangle plein défini par les coordonnées de ses
	 * sommets: (x1, y1), (x2, y2) et (x3, y3).  Utilise la couleur de
	 * dessin courante.
	 *
	 * @see #drawTriangle
	 * @see #setColor
	 */
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		Polygon poly = new Polygon();
		poly.addPoint(x1, y1);
		poly.addPoint(x2, y2);
		poly.addPoint(x3, y3);
		synchronized (image) {
			graphics.drawPolygon(poly);
			graphics.fillPolygon(poly);
		}
		Rectangle bounds = poly.getBounds();
		bounds.setSize(bounds.width + 1, bounds.height + 1);
		panel.repaint(poly.getBounds());
	}

	/**
	 * Écrit du texte.
	 * <p>
	 * Écrit le texte text, aux coordonnées (x, y).
	 */
	public void drawText(int x, int y, String text) {
		synchronized (image) {
			graphics.drawString(text, x, y);
		}
		panel.repaint(); // don't know how to calculate tighter bounding box
	}

	/**
	 * Retourne la couleur d'un pixel.
	 * <p>
	 * Retourne la couleur du pixel de coordonnées (x, y).
	 *
	 * @return couleur du pixel
	 * @see #setColor(int)
	 * @see #setBgColor(int)
	 */
	public int getPointColor(int x, int y) {
		return (x < 0 || y < 0 || x >= width || y >= height) ?
				0 : image.getRGB(x, y) & 0x00ffffff;
	}

	/**
	 * Attend l'appui sur un des boutons de la souris.
	 *
	 * @return vrai (true) si un bouton a été pressé
	 * @see #waitMousePress(long)
	 * @see #getMouseX
	 * @see #getMouseY
	 * @see #getMouseButton
	 */
	public boolean waitMousePress() {
		return waitMousePress(-1);
	}

	/**
	 * Attend l'appui sur un des boutons de la souris.
	 *
	 * @param timeout temps maximal d'attente (millisecondes)
	 * @return vrai (true) si un bouton a été pressé
	 * @see #waitMousePress()
	 * @see #getMouseX
	 * @see #getMouseY
	 * @see #getMouseButton
	 */
	public boolean waitMousePress(long timeout) {
		boolean result = false;
		synchronized (mouseLock) {
			if (timeout != 0) {
				mouseEvent = null;
				try {
					if (timeout > 0)
						mouseLock.wait(timeout);
					else // (timeout < 0)
						mouseLock.wait();
				} catch (InterruptedException e) {
				}
			}
			if (mouseEvent != null) {
				mousePos.setLocation(mouseEvent.getPoint());
				mouseButton = mouseEvent.getButton();
				mouseEvent = null;
				result = true;
			}
		}
		return result;
	}

	/**
	 * Retourne la position (x) de la souris la dernière fois qu'un
	 * bouton a été pressé pendant l'appel à {@link #waitMousePress()}.
	 *
	 * @return position (x)
	 */
	public int getMouseX() {
		return mousePos.x;
	}

	/**
	 * Retourne la position (y) de la souris la dernière fois qu'un
	 * bouton a été pressé pendant l'appel à {@link #waitMousePress()}.
	 *
	 * @return position (y)
	 */
	public int getMouseY() {
		return mousePos.y;
	}

	/**
	 * Retourne le numéro du bouton de la souris pressé pendant
	 * le dernier appel à {@link #waitMousePress()}.
	 *
	 * @return numéro de bouton (1: gauche, 2: milieu, 3: droit)
	 */
	public int getMouseButton() {
		return mouseButton;
	}

	/**
	 * Synchronise le contenu de la fenêtre.
	 * <p>
	 * Pour des raisons d'efficacités, le résultat des fonctions de dessin
	 * n'est pas affiché immédiatement.  L'appel à sync permet de
	 * synchroniser le contenu de la fenêtre.  Autrement dit, cela bloque
	 * l'exécution du programme jusqu'à ce que le contenu de la fenêtre
	 * soit à jour.
	 */
	public void sync() {
		// put an empty action on the event queue, and  wait for its completion
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
				}
			});
		} catch (Exception e) {
		}
	}

	/**
	 * Ferme la fenêtre graphique.
	 */
	public void closeGraph() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				WindowEvent ev =
						new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
				Toolkit.getDefaultToolkit()
						.getSystemEventQueue().postEvent(ev);
			}
		});
	}


	/**
	 * Suspend l'exécution pendant un certain temps.
	 *
	 * @param secs temps d'attente en seconde
	 * @see #msleep
	 * @see #usleep
	 */
	public static void sleep(long secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (Exception e) {
		}
	}

	/**
	 * Suspend l'exécution pendant un certain temps.
	 *
	 * @param msecs temps d'attente en millisecondes
	 * @see #sleep
	 * @see #usleep
	 */
	public static void msleep(long msecs) {
		try {
			Thread.sleep(msecs);
		} catch (Exception e) {
		}
	}

	/**
	 * Suspend l'exécution pendant un certain temps.
	 *
	 * @param usecs temps d'attente en microsecondes
	 * @see #sleep
	 * @see #msleep
	 */
	public static void usleep(long usecs) {
		try {
			Thread.sleep(usecs / 1000, (int) (usecs % 1000) * 1000);
		} catch (Exception e) {
		}
	}

	/* PRIVATE STUFF FOLLOWS */

	private static final Map<String, Color> colorMap;

	static {
		Map<String, Color> m = new HashMap<String, Color>();
		// From http://www.w3.org/TR/SVG/types.html#ColorKeywords
		m.put("aliceblue", new Color(0x00f0f8ff));
		m.put("antiquewhite", new Color(0x00faebd7));
		m.put("aqua", new Color(0x0000ffff));
		m.put("aquamarine", new Color(0x007fffd4));
		m.put("azure", new Color(0x00f0ffff));
		m.put("beige", new Color(0x00f5f5dc));
		m.put("bisque", new Color(0x00ffe4c4));
		m.put("black", new Color(0000000000));
		m.put("blanchedalmond", new Color(0x00ffebcd));
		m.put("blue", new Color(0x000000ff));
		m.put("blueviolet", new Color(0x008a2be2));
		m.put("brown", new Color(0x00a52a2a));
		m.put("burlywood", new Color(0x00deb887));
		m.put("cadetblue", new Color(0x005f9ea0));
		m.put("chartreuse", new Color(0x007fff00));
		m.put("chocolate", new Color(0x00d2691e));
		m.put("coral", new Color(0x00ff7f50));
		m.put("cornflowerblue", new Color(0x006495ed));
		m.put("cornsilk", new Color(0x00fff8dc));
		m.put("crimson", new Color(0x00dc143c));
		m.put("cyan", new Color(0x0000ffff));
		m.put("darkblue", new Color(0x0000008b));
		m.put("darkcyan", new Color(0x00008b8b));
		m.put("darkgoldenrod", new Color(0x00b8860b));
		m.put("darkgray", new Color(0x00a9a9a9));
		m.put("darkgreen", new Color(0x00006400));
		m.put("darkgrey", new Color(0x00a9a9a9));
		m.put("darkkhaki", new Color(0x00bdb76b));
		m.put("darkmagenta", new Color(0x008b008b));
		m.put("darkolivegreen", new Color(0x00556b2f));
		m.put("darkorange", new Color(0x00ff8c00));
		m.put("darkorchid", new Color(0x009932cc));
		m.put("darkred", new Color(0x008b0000));
		m.put("darksalmon", new Color(0x00e9967a));
		m.put("darkseagreen", new Color(0x008fbc8f));
		m.put("darkslateblue", new Color(0x00483d8b));
		m.put("darkslategray", new Color(0x002f4f4f));
		m.put("darkslategrey", new Color(0x002f4f4f));
		m.put("darkturquoise", new Color(0x0000ced1));
		m.put("darkviolet", new Color(0x009400d3));
		m.put("deeppink", new Color(0x00ff1493));
		m.put("deepskyblue", new Color(0x0000bfff));
		m.put("dimgray", new Color(0x00696969));
		m.put("dimgrey", new Color(0x00696969));
		m.put("dodgerblue", new Color(0x001e90ff));
		m.put("firebrick", new Color(0x00b22222));
		m.put("floralwhite", new Color(0x00fffaf0));
		m.put("forestgreen", new Color(0x00228b22));
		m.put("fuchsia", new Color(0x00ff00ff));
		m.put("gainsboro", new Color(0x00dcdcdc));
		m.put("ghostwhite", new Color(0x00f8f8ff));
		m.put("gold", new Color(0x00ffd700));
		m.put("goldenrod", new Color(0x00daa520));
		m.put("gray", new Color(0x00808080));
		m.put("grey", new Color(0x00808080));
		m.put("green", new Color(0x00008000));
		m.put("greenyellow", new Color(0x00adff2f));
		m.put("honeydew", new Color(0x00f0fff0));
		m.put("hotpink", new Color(0x00ff69b4));
		m.put("indianred", new Color(0x00cd5c5c));
		m.put("indigo", new Color(0x004b0082));
		m.put("ivory", new Color(0x00fffff0));
		m.put("khaki", new Color(0x00f0e68c));
		m.put("lavender", new Color(0x00e6e6fa));
		m.put("lavenderblush", new Color(0x00fff0f5));
		m.put("lawngreen", new Color(0x007cfc00));
		m.put("lemonchiffon", new Color(0x00fffacd));
		m.put("lightblue", new Color(0x00add8e6));
		m.put("lightcoral", new Color(0x00f08080));
		m.put("lightcyan", new Color(0x00e0ffff));
		m.put("lightgoldenrodyellow", new Color(0x00fafad2));
		m.put("lightgray", new Color(0x00d3d3d3));
		m.put("lightgreen", new Color(0x0090ee90));
		m.put("lightgrey", new Color(0x00d3d3d3));
		m.put("lightpink", new Color(0x00ffb6c1));
		m.put("lightsalmon", new Color(0x00ffa07a));
		m.put("lightseagreen", new Color(0x0020b2aa));
		m.put("lightskyblue", new Color(0x0087cefa));
		m.put("lightslategray", new Color(0x00778899));
		m.put("lightslategrey", new Color(0x00778899));
		m.put("lightsteelblue", new Color(0x00b0c4de));
		m.put("lightyellow", new Color(0x00ffffe0));
		m.put("lime", new Color(0x0000ff00));
		m.put("limegreen", new Color(0x0032cd32));
		m.put("linen", new Color(0x00faf0e6));
		m.put("magenta", new Color(0x00ff00ff));
		m.put("maroon", new Color(0x00800000));
		m.put("mediumaquamarine", new Color(0x0066cdaa));
		m.put("mediumblue", new Color(0x000000cd));
		m.put("mediumorchid", new Color(0x00ba55d3));
		m.put("mediumpurple", new Color(0x009370db));
		m.put("mediumseagreen", new Color(0x003cb371));
		m.put("mediumslateblue", new Color(0x007b68ee));
		m.put("mediumspringgreen", new Color(0x0000fa9a));
		m.put("mediumturquoise", new Color(0x0048d1cc));
		m.put("mediumvioletred", new Color(0x00c71585));
		m.put("midnightblue", new Color(0x00191970));
		m.put("mintcream", new Color(0x00f5fffa));
		m.put("mistyrose", new Color(0x00ffe4e1));
		m.put("moccasin", new Color(0x00ffe4b5));
		m.put("navajowhite", new Color(0x00ffdead));
		m.put("navy", new Color(0x00000080));
		m.put("oldlace", new Color(0x00fdf5e6));
		m.put("olive", new Color(0x00808000));
		m.put("olivedrab", new Color(0x006b8e23));
		m.put("orange", new Color(0x00ffa500));
		m.put("orangered", new Color(0x00ff4500));
		m.put("orchid", new Color(0x00da70d6));
		m.put("palegoldenrod", new Color(0x00eee8aa));
		m.put("palegreen", new Color(0x0098fb98));
		m.put("paleturquoise", new Color(0x00afeeee));
		m.put("palevioletred", new Color(0x00db7093));
		m.put("papayawhip", new Color(0x00ffefd5));
		m.put("peachpuff", new Color(0x00ffdab9));
		m.put("peru", new Color(0x00cd853f));
		m.put("pink", new Color(0x00ffc0cb));
		m.put("plum", new Color(0x00dda0dd));
		m.put("powderblue", new Color(0x00b0e0e6));
		m.put("purple", new Color(0x00800080));
		m.put("red", new Color(0x00ff0000));
		m.put("rosybrown", new Color(0x00bc8f8f));
		m.put("royalblue", new Color(0x004169e1));
		m.put("saddlebrown", new Color(0x008b4513));
		m.put("salmon", new Color(0x00fa8072));
		m.put("sandybrown", new Color(0x00f4a460));
		m.put("seagreen", new Color(0x002e8b57));
		m.put("seashell", new Color(0x00fff5ee));
		m.put("sienna", new Color(0x00a0522d));
		m.put("silver", new Color(0x00c0c0c0));
		m.put("skyblue", new Color(0x0087ceeb));
		m.put("slateblue", new Color(0x006a5acd));
		m.put("slategray", new Color(0x00708090));
		m.put("slategrey", new Color(0x00708090));
		m.put("snow", new Color(0x00fffafa));
		m.put("springgreen", new Color(0x0000ff7f));
		m.put("steelblue", new Color(0x004682b4));
		m.put("tan", new Color(0x00d2b48c));
		m.put("teal", new Color(0x00008080));
		m.put("thistle", new Color(0x00d8bfd8));
		m.put("tomato", new Color(0x00ff6347));
		m.put("turquoise", new Color(0x0040e0d0));
		m.put("violet", new Color(0x00ee82ee));
		m.put("wheat", new Color(0x00f5deb3));
		m.put("white", new Color(0x00ffffff));
		m.put("whitesmoke", new Color(0x00f5f5f5));
		m.put("yellow", new Color(0x00ffff00));
		m.put("yellowgreen", new Color(0x009acd32));
		colorMap = Collections.unmodifiableMap(m);
	}

	private static int instances = 0;

	private final String title; // window's title
	private JFrame frame;       // the frame (window)
	private DWPanel panel;      // the panel showing the image
	private BufferedImage image; // the image we draw into
	private Graphics2D graphics; // graphics associated with image
	private Color bgColor;       // background color, for clearGraph()
	private boolean isClosed;    // is the window closed ?
	private Object mouseLock;       // lock for mouse events
	private MouseEvent mouseEvent;  // last mouse event
	private Point mousePos;         // last mouse position
	private int mouseButton;        // last mouse click

	// To be run on the Event Dispatching Thread
	void createGUI() {
		panel = new DWPanel();
		frame = new JFrame(title);
		frame.add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new DWWindowHandler());
		frame.addKeyListener(new DWKeyHandler());
		panel.addMouseListener(new DWMouseHandler());
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	private class DWWindowHandler extends WindowAdapter {
		public void windowOpened(WindowEvent ev) {
			DrawingWindow w = DrawingWindow.this;
			DrawingWindow.instances++;
			w.isClosed = false;
		}

		public void windowClosed(WindowEvent ev) {
			DrawingWindow w = DrawingWindow.this;
			if (!w.isClosed) {
				w.isClosed = true;
				if (DrawingWindow.instances <= 0)
					throw new AssertionError("Bad instance counter: " +
							DrawingWindow.instances);
				DrawingWindow.instances--;
				if (DrawingWindow.instances == 0)
					System.exit(0);
			}
		}
	}

	private class DWKeyHandler extends KeyAdapter {
		public void keyPressed(KeyEvent ev) {
			DrawingWindow w = DrawingWindow.this;
			if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
				w.closeGraph();
			}
		}
	}

	private class DWMouseHandler extends MouseAdapter {
		public void mousePressed(MouseEvent ev) {
			DrawingWindow w = DrawingWindow.this;
			synchronized (w.mouseLock) {
				w.mouseEvent = ev;
				mouseLock.notifyAll();
			}
		}
	}

	private class DWPanel extends JPanel {
		DWPanel() {
			DrawingWindow w = DrawingWindow.this;
			Dimension dimension = new Dimension(w.width, w.height);
			super.setMinimumSize(dimension);
			super.setMaximumSize(dimension);
			super.setPreferredSize(dimension);
		}

		public void paint(Graphics g) {
			DrawingWindow w = DrawingWindow.this;
			synchronized (w.image) {
				g.drawImage(w.image, 0, 0, null);
			}
		}

		private static final long serialVersionUID = 0;
	}
}
