package com.nickxla.chromatix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizzActivity extends AppCompatActivity {

    private static final int NORMAL = 0;
    private static final int TRITANOPE = 1;
    private static final int PROTANOPE = 2;
    private static final int DEUTERANOPE = 3;

    FirebaseAuth auth;
    FirebaseAnalytics mFirebaseAnalytics;

    private ListeQuestions listeQ = new ListeQuestions();
    private List<Integer> questionsDejaPosees = new ArrayList<>();

    private Button backHome;
    TextView resultat;

    private int deficienceRougeVert = 0;

    private int deficienceRouge = 0; // Protanomal
    private int deficienceBleu = 0; // Tritanomal
    private int deficienceVert = 0; // Deutéranomal
    private int noDeficience = 0;

    SharedPreferences prefs = null;
    private boolean firstQuizz = false;

    Random rand = new Random();

    private EditText mInputReponse;
    private String reponseInput;
    private ImageView mImageIshihara;
    Button mValiderBouton;
    Button mIdkBouton;

    private String reponseQuestion;

    private int mNumeroQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        setTitle("Test d'Ishihara");

        auth = FirebaseAuth.getInstance();

        prefs = PreferenceManager.getDefaultSharedPreferences(QuizzActivity.this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        reponseInput = "";
        mInputReponse = findViewById(R.id.inputReponse);
        mImageIshihara = findViewById(R.id.questionImage);
        mValiderBouton = findViewById(R.id.validate_button);
        mIdkBouton = findViewById(R.id.idk_button);
        backHome = findViewById(R.id.backHomeButton);

        if (prefs.getBoolean("firstquizz", true)) {
            firstQuizz = true;
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            //prefs.edit().putBoolean("firstquizz", false).apply();
        }

        if(!firstQuizz) {
            new AlertDialog.Builder(QuizzActivity.this)
                    .setTitle(getString(R.string.main_dialog_simple_title))
                    .setMessage(getString(R.string.main_dialog_simple_message))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(QuizzActivity.this, "Bonne chance !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(QuizzActivity.this, MainActivity.class));
                            // Back at home
                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(QuizzActivity.this)
                    .setTitle(getString(R.string.main_dialog_welcome_title))
                    .setMessage(getString(R.string.welcome_dialog_message))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.dialog_ok), null)
                    .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            prefs.edit().putBoolean("firstquizz", true).apply();
                            startActivity(new Intent(QuizzActivity.this, MainActivity.class));
                            finish();
                        }
                    })
                    .show();
        }

        updateQuestion();

        mInputReponse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!mInputReponse.getText().toString().equals("")) {
                    reponseInput = mInputReponse.getText().toString();
                } else {
                    reponseInput = "";
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(QuizzActivity.this)
                .setTitle("Attention")
                .setMessage("Vous ne pouvez pas quitter le quizz maintenant.")
                .setCancelable(true)
                .show();
    }

    private void updateQuestion() {
        mNumeroQuestion = rand.nextInt(21);

        if(questionsDejaPosees.size() != 21) {
            while(questionsDejaPosees.contains(mNumeroQuestion)){
                mNumeroQuestion = rand.nextInt(21);
            }
            Drawable d;// = getResources().getDrawable(listeQ.getImageQuestion(mNumeroQuestion));
            d = ContextCompat.getDrawable(QuizzActivity.this, listeQ.getImageQuestion(mNumeroQuestion));
            mImageIshihara.setImageDrawable(d);
            reponseQuestion = listeQ.getBonneReponse(mNumeroQuestion);
            mInputReponse.setText("");
            questionsDejaPosees.add(mNumeroQuestion);
        } else {
            setContentView(R.layout.quizz_finished);
            resultat = findViewById(R.id.finalite);
            backHome = findViewById(R.id.backHomeButton);
            backHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Animation animScale = AnimationUtils.loadAnimation(QuizzActivity.this, R.anim.scale);
                    v.startAnimation(animScale);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            int def = getDeficience();
            switch(def) {
                case 0:
                    resultat.setText(R.string.quizz_normal);
                    break;
                case 1:
                    resultat.setText(R.string.quizz_tritano);
                    break;
                case 2:
                    resultat.setText(R.string.quizz_protanope);
                    break;
                case 3:
                    resultat.setText(R.string.quizz_deutera);
                    break;
                default:
                    break;
            }
            saveDeficience(def);
        }
    }

    private void saveDeficience(int deficience) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(QuizzActivity.this);
        sharedPref.edit().putInt("deficience", deficience).apply();
        sharedPref.edit().putBoolean("firstquizz", false).apply();
    }

    public void onButtonClick(View view) {
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        view.startAnimation(animScale);
        int idBouton = view.getId();
        if(mNumeroQuestion >= 1 && mNumeroQuestion <= 12) {
            from0to12(idBouton);
        } else if(mNumeroQuestion >= 13 && mNumeroQuestion <= 16) {
            from13to16(idBouton);
        } else if(mNumeroQuestion >= 17 && mNumeroQuestion <= 20) {
            from17to20(idBouton);
        } else {
            updateQuestion();
        }
    }

    private void from0to12(int id) {
        if(reponseInput.equals(reponseQuestion) && id == R.id.validate_button) {
            updateQuestion();
            noDeficience++;
        } else {
            deficienceRougeVert++;
            updateQuestion();
        }
    }

    private void from13to16(int id) {
        if(reponseInput.equals(reponseQuestion) && id == R.id.validate_button) {
            updateQuestion();
            noDeficience++;
        } else {
            deficienceBleu++;
            updateQuestion();
        }
    }

    private void from17to20(int id) {
        if(reponseInput.equals(reponseQuestion) && id == R.id.validate_button) {
            updateQuestion();
            noDeficience++;
        } else {
            switch(mNumeroQuestion) {
                case 17: // numéro : 26
                    if(reponseInput.equals("2")) {
                        deficienceVert++;
                    } else if(reponseInput.equals("6")){
                        deficienceRouge++;
                    }
                    break;
                case 18: // numéro : 42
                    if(reponseInput.equals("4")) {
                        deficienceVert++;
                    } else if(reponseInput.equals("2")){
                        deficienceRouge++;
                    }
                    break;
                case 19: // numéro : 35
                    if(reponseInput.equals("3")) {
                        deficienceVert++;
                    } else if(reponseInput.equals("5")){
                        deficienceRouge++;
                    }
                    break;
                case 20: // numéro : 96
                    if(reponseInput.equals("9")) {
                        deficienceVert++;
                    } else if(reponseInput.equals("6")){
                        deficienceRouge++;
                    }
                    break;
                default:
                    break;
            }
            updateQuestion();
        }
    }

    private int getDeficience(){
        int maxRVNoDef = Math.max(deficienceRougeVert, noDeficience); // max entre pas de déficience et déf rouge & vert
        int maxRV;
        int maxRB;
        int maxNV;

        int def;
        if(maxRVNoDef == deficienceRougeVert) {
            // départager rouge et vert
            maxRV = Math.max(deficienceRouge, deficienceVert);
            def = maxRV;
        } else {
            // pas de déficience ou autre déficience : max des 4
            maxRB = Math.max(deficienceRouge, deficienceBleu);
            maxNV = Math.max(deficienceVert, noDeficience);
            def = Math.max(maxRB, maxNV);
        }

        if(def == deficienceBleu) {
            return TRITANOPE;
        } else if(def == deficienceRouge) {
            return PROTANOPE;
        } else if(def == deficienceVert) {
            return DEUTERANOPE;
        } else if(def == noDeficience) {
            return NORMAL;
        } else {
            return -1;
        }
    }
}
