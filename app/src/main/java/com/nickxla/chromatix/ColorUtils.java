package com.nickxla.chromatix;

import java.util.ArrayList;


class ColorUtils {

	private ArrayList<ColorName> initColorList() {
		ArrayList<ColorName> colorList = new ArrayList<>();
		colorList.add(new ColorName("Bleu alice", 0xF0, 0xF8, 0xFF));
		colorList.add(new ColorName("Blanc antique", 0xFA, 0xEB, 0xD7));
		colorList.add(new ColorName("Bleu eau", 0x00, 0xFF, 0xFF));
		colorList.add(new ColorName("Aigue-marine", 0x7F, 0xFF, 0xD4));
		colorList.add(new ColorName("Azur", 0xF0, 0xFF, 0xFF));
		colorList.add(new ColorName("Beige", 0xF5, 0xF5, 0xDC));
		colorList.add(new ColorName("Écrevisse", 0xFF, 0xE4, 0xC4));
		colorList.add(new ColorName("Noir", 0x00, 0x00, 0x00));
		colorList.add(new ColorName("Amande blême", 0xFF, 0xEB, 0xCD));
		colorList.add(new ColorName("Bleu", 0x00, 0x00, 0xFF));
		colorList.add(new ColorName("Bleu violet", 0x8A, 0x2B, 0xE2));
		colorList.add(new ColorName("Marron", 0xA5, 0x2A, 0x2A));
		colorList.add(new ColorName("Bois massif", 0xDE, 0xB8, 0x87));
		colorList.add(new ColorName("Bleu cadet", 0x5F, 0x9E, 0xA0));
		colorList.add(new ColorName("Vert chartreuse", 0x7F, 0xFF, 0x00));
		colorList.add(new ColorName("Chocolat", 0xD2, 0x69, 0x1E));
		colorList.add(new ColorName("Corail", 0xFF, 0x7F, 0x50));
		colorList.add(new ColorName("Bleu bleuet", 0x64, 0x95, 0xED));
		colorList.add(new ColorName("Soie de maïs", 0xFF, 0xF8, 0xDC));
		colorList.add(new ColorName("Cramoisi", 0xDC, 0x14, 0x3C));
		colorList.add(new ColorName("Cyan", 0x00, 0xFF, 0xFF));
		colorList.add(new ColorName("Bleu foncé", 0x00, 0x00, 0x8B));
		colorList.add(new ColorName("Cyan foncé", 0x00, 0x8B, 0x8B));
		colorList.add(new ColorName("Solidage foncé", 0xB8, 0x86, 0x0B));
		colorList.add(new ColorName("Gris foncé", 0xA9, 0xA9, 0xA9));
		colorList.add(new ColorName("Vert foncé", 0x00, 0x64, 0x00));
		colorList.add(new ColorName("Kaki foncé", 0xBD, 0xB7, 0x6B));
		colorList.add(new ColorName("Magenta foncé", 0x8B, 0x00, 0x8B));
		colorList.add(new ColorName("Vert olive foncé", 0x55, 0x6B, 0x2F));
		colorList.add(new ColorName("Orange foncé", 0xFF, 0x8C, 0x00));
		colorList.add(new ColorName("Rouge orchidée foncé", 0x99, 0x32, 0xCC));
		colorList.add(new ColorName("Rouge foncé", 0x8B, 0x00, 0x00));
		colorList.add(new ColorName("Saumon foncé", 0xE9, 0x96, 0x7A));
		colorList.add(new ColorName("Vert mer foncé", 0x8F, 0xBC, 0x8F));
		colorList.add(new ColorName("Bleu ardoise foncé", 0x48, 0x3D, 0x8B));
		colorList.add(new ColorName("Gris ardoise foncé", 0x2F, 0x4F, 0x4F));
		colorList.add(new ColorName("Turquoise foncé", 0x00, 0xCE, 0xD1));
		colorList.add(new ColorName("Violet foncé", 0x94, 0x00, 0xD3));
		colorList.add(new ColorName("Rose profond", 0xFF, 0x14, 0x93));
		colorList.add(new ColorName("Bleu ciel profond", 0x00, 0xBF, 0xFF));
		colorList.add(new ColorName("Gris mat", 0x69, 0x69, 0x69));
		colorList.add(new ColorName("Bleu Dodger", 0x1E, 0x90, 0xFF));
		colorList.add(new ColorName("Brique", 0xB2, 0x22, 0x22));
		colorList.add(new ColorName("Blanc floral", 0xFF, 0xFA, 0xF0));
		colorList.add(new ColorName("Vert forêt", 0x22, 0x8B, 0x22));
		colorList.add(new ColorName("Rouge fuchsia", 0xFF, 0x00, 0xFF));
		colorList.add(new ColorName("Gris étain", 0xDC, 0xDC, 0xDC));
		colorList.add(new ColorName("Blanc fantôme", 0xF8, 0xF8, 0xFF));
		colorList.add(new ColorName("Doré", 0xFF, 0xD7, 0x00));
		colorList.add(new ColorName("Solidage", 0xDA, 0xA5, 0x20));
		colorList.add(new ColorName("Gris", 0x80, 0x80, 0x80));
		colorList.add(new ColorName("Vert", 0x00, 0x80, 0x00));
		colorList.add(new ColorName("Vert jaune", 0xAD, 0xFF, 0x2F));
		colorList.add(new ColorName("Miellat", 0xF0, 0xFF, 0xF0));
		colorList.add(new ColorName("Rose vif", 0xFF, 0x69, 0xB4));
		colorList.add(new ColorName("Rouge indien", 0xCD, 0x5C, 0x5C));
		colorList.add(new ColorName("Indigo", 0x4B, 0x00, 0x82));
		colorList.add(new ColorName("Ivoire", 0xFF, 0xFF, 0xF0));
		colorList.add(new ColorName("Kaki", 0xF0, 0xE6, 0x8C));
		colorList.add(new ColorName("Lavande", 0xE6, 0xE6, 0xFA));
		colorList.add(new ColorName("Lavande rosée", 0xFF, 0xF0, 0xF5));
		colorList.add(new ColorName("Vert pelouse", 0x7C, 0xFC, 0x00));
		colorList.add(new ColorName("Citron clair", 0xFF, 0xFA, 0xCD));
		colorList.add(new ColorName("Bleu clair", 0xAD, 0xD8, 0xE6));
		colorList.add(new ColorName("Corail clair", 0xF0, 0x80, 0x80));
		colorList.add(new ColorName("Cyan clair", 0xE0, 0xFF, 0xFF));
		colorList.add(new ColorName("Solidage clair", 0xFA, 0xFA, 0xD2));
		colorList.add(new ColorName("Gris clair", 0xD3, 0xD3, 0xD3));
		colorList.add(new ColorName("Vert clair", 0x90, 0xEE, 0x90));
		colorList.add(new ColorName("Rose clair", 0xFF, 0xB6, 0xC1));
		colorList.add(new ColorName("Saumon clair", 0xFF, 0xA0, 0x7A));
		colorList.add(new ColorName("Vert mer clair", 0x20, 0xB2, 0xAA));
		colorList.add(new ColorName("Bleu ciel clair", 0x87, 0xCE, 0xFA));
		colorList.add(new ColorName("Gris ardoise clair", 0x77, 0x88, 0x99));
		colorList.add(new ColorName("Bleu acier clair", 0xB0, 0xC4, 0xDE));
		colorList.add(new ColorName("Jaune clair", 0xFF, 0xFF, 0xE0));
		colorList.add(new ColorName("Citron vert", 0x00, 0xFF, 0x00));
		colorList.add(new ColorName("Vert citron", 0x32, 0xCD, 0x32));
		colorList.add(new ColorName("Blanc lin", 0xFA, 0xF0, 0xE6));
		colorList.add(new ColorName("Magenta", 0xFF, 0x00, 0xFF));
		colorList.add(new ColorName("Bordeaux", 0x80, 0x00, 0x00));
		colorList.add(new ColorName("Aigue-marine moyen", 0x66, 0xCD, 0xAA));
		colorList.add(new ColorName("Bleu moyen", 0x00, 0x00, 0xCD));
		colorList.add(new ColorName("Rouge orchidée moyen", 0xBA, 0x55, 0xD3));
		colorList.add(new ColorName("Violet moyen", 0x93, 0x70, 0xDB));
		colorList.add(new ColorName("Vert mer moyen", 0x3C, 0xB3, 0x71));
		colorList.add(new ColorName("Bleu ardoise moyen", 0x7B, 0x68, 0xEE));
		colorList.add(new ColorName("Vert printanier moyen", 0x00, 0xFA, 0x9A));
		colorList.add(new ColorName("Turquoise moyen", 0x48, 0xD1, 0xCC));
		colorList.add(new ColorName("Violet moyen rouge", 0xC7, 0x15, 0x85));
		colorList.add(new ColorName("Bleu nuit", 0x19, 0x19, 0x70));
		colorList.add(new ColorName("Menthe clair", 0xF5, 0xFF, 0xFA));
		colorList.add(new ColorName("Misty Rose", 0xFF, 0xE4, 0xE1));
		colorList.add(new ColorName("Mocassin", 0xFF, 0xE4, 0xB5));
		colorList.add(new ColorName("Blanc navajo", 0xFF, 0xDE, 0xAD));
		colorList.add(new ColorName("Bleu marine", 0x00, 0x00, 0x80));
		colorList.add(new ColorName("Vieux blanc dentelle ", 0xFD, 0xF5, 0xE6));
		colorList.add(new ColorName("Olive", 0x80, 0x80, 0x00));
		colorList.add(new ColorName("Olivâtre", 0x6B, 0x8E, 0x23));
		colorList.add(new ColorName("Orange", 0xFF, 0xA5, 0x00));
		colorList.add(new ColorName("Rouge orangé", 0xFF, 0x45, 0x00));
		colorList.add(new ColorName("Rouge orchidée", 0xDA, 0x70, 0xD6));
		colorList.add(new ColorName("Solidage pâle", 0xEE, 0xE8, 0xAA));
		colorList.add(new ColorName("Vert pâle", 0x98, 0xFB, 0x98));
		colorList.add(new ColorName("Turquoise pâle", 0xAF, 0xEE, 0xEE));
		colorList.add(new ColorName("Rouge violet pâle", 0xDB, 0x70, 0x93));
		colorList.add(new ColorName("Papaye", 0xFF, 0xEF, 0xD5));
		colorList.add(new ColorName("Pêche", 0xFF, 0xDA, 0xB9));
		colorList.add(new ColorName("Marron clair", 0xCD, 0x85, 0x3F));
		colorList.add(new ColorName("Rose", 0xFF, 0xC0, 0xCB));
		colorList.add(new ColorName("Prune", 0xDD, 0xA0, 0xDD));
		colorList.add(new ColorName("Bleu poudre", 0xB0, 0xE0, 0xE6));
		colorList.add(new ColorName("Violet", 0x80, 0x00, 0x80));
		colorList.add(new ColorName("Rouge", 0xFF, 0x00, 0x00));
		colorList.add(new ColorName("Rose marroné", 0xBC, 0x8F, 0x8F));
		colorList.add(new ColorName("Bleu roi", 0x41, 0x69, 0xE1));
		colorList.add(new ColorName("Marron foncé", 0x8B, 0x45, 0x13));
		colorList.add(new ColorName("Saumon", 0xFA, 0x80, 0x72));
		colorList.add(new ColorName("Marron sablé", 0xF4, 0xA4, 0x60));
		colorList.add(new ColorName("Vert mer", 0x2E, 0x8B, 0x57));
		colorList.add(new ColorName("Blanc coquillage", 0xFF, 0xF5, 0xEE));
		colorList.add(new ColorName("Sienna", 0xA0, 0x52, 0x2D));
		colorList.add(new ColorName("Argenté", 0xC0, 0xC0, 0xC0));
		colorList.add(new ColorName("Bleu ciel", 0x87, 0xCE, 0xEB));
		colorList.add(new ColorName("Bleu ardoise", 0x6A, 0x5A, 0xCD));
		colorList.add(new ColorName("Gris ardoise", 0x70, 0x80, 0x90));
		colorList.add(new ColorName("Neige", 0xFF, 0xFA, 0xFA));
		colorList.add(new ColorName("Vert printanier", 0x00, 0xFF, 0x7F));
		colorList.add(new ColorName("Bleu acier", 0x46, 0x82, 0xB4));
		colorList.add(new ColorName("Ocre", 0xD2, 0xB4, 0x8C));
		colorList.add(new ColorName("Bleu sarcelle", 0x00, 0x80, 0x80));
		colorList.add(new ColorName("Rouge chardon", 0xD8, 0xBF, 0xD8));
		colorList.add(new ColorName("Tomate", 0xFF, 0x63, 0x47));
		colorList.add(new ColorName("Turquoise", 0x40, 0xE0, 0xD0));
		colorList.add(new ColorName("Violet", 0xEE, 0x82, 0xEE));
		colorList.add(new ColorName("Blé", 0xF5, 0xDE, 0xB3));
		colorList.add(new ColorName("Blanc", 0xFF, 0xFF, 0xFF));
		colorList.add(new ColorName("Blanc fumé", 0xF5, 0xF5, 0xF5));
		colorList.add(new ColorName("Jaune", 0xFF, 0xFF, 0x00));
		colorList.add(new ColorName("Jaune vert", 0x9A, 0xCD, 0x32));
		return colorList;
	}

	/**
	 *
	 * @param r Valeur du rouge
	 * @param g Valeur du vert
	 * @param b Valeur du bleu
	 * @return Nom de couleur correspondante
	 */
	String getColorNameFromRgb(int r, int g, int b) {
		ArrayList<ColorName> colorList = initColorList();
		ColorName closestMatch = null;
		int minMSE = Integer.MAX_VALUE;
		int mse;
		for (ColorName c : colorList) {
			mse = c.computeMSE(r, g, b);
			if (mse < minMSE) {
				minMSE = mse;
				closestMatch = c;
			}
		}

		if (closestMatch != null) {
			return closestMatch.getName();
		} else {
			return "No matched color name.";
		}
	}

	public class ColorName {
		int r, g, b;
		public String name;

		ColorName(String name, int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.name = name;
		}

		int computeMSE(int pixR, int pixG, int pixB) {
			return (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b)
					* (pixB - b)) / 3);
		}

		public int getR() {
			return r;
		}

		public int getG() {
			return g;
		}

		public int getB() {
			return b;
		}

		public String getName() {
			return name;
		}
	}
}


