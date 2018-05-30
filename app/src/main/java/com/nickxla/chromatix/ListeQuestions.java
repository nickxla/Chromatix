package com.nickxla.chromatix;

class ListeQuestions {

    private int[] listeImages = {
            R.drawable.ish01, R.drawable.ish02, R.drawable.ish03,
            R.drawable.ish04, R.drawable.ish05, R.drawable.ish06,
            R.drawable.ish07, R.drawable.ish08, R.drawable.ish09,
            R.drawable.ish10, R.drawable.ish11, R.drawable.ish12,
            R.drawable.ish13, R.drawable.ish14, R.drawable.ish15,
            R.drawable.ish16, R.drawable.ish17, R.drawable.ish18,
            R.drawable.ish19, R.drawable.ish20, R.drawable.ish21
    };

    // Test d'Ishihara : 32 questions

    private String bonnesReponses [] = {
            "12", "8", "6", "29", "57", "5", "3",
            "15", "74", "5", "7", "16", "73", "2",
            "6", "97", "45", "26", "42", "35", "96",
            "2", "3", "4", "1", "2", "3", "4"
    };

    int getImageQuestion(int numChoix) {
        return listeImages[numChoix];
    }

    String getBonneReponse(int numBonneRep) {
        return bonnesReponses[numBonneRep];
    }

}
