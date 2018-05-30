package com.nickxla.chromatix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    // Booléen de vérification si l'utilisateur est en train de s'enregistrer
    private boolean isSigningUp;

    // Champs de texte (email et mot de passe)
    private EditText emailEditText;
    private EditText passwordEditText;

    private TextView registerTextView;
    TextView forgotPasswordTextView;

    // Boutons
    private Button signInBtn;
    SignInButton googleSignInBtn;

    SharedPreferences prefs = null;
    boolean firstRun = false;

    // Connexion avec Firebase et Google
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleSignInClient googleSignInClient;

    private final static int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        isSigningUp = false;

        prefs = getSharedPreferences("com.nickxla.chromatix", MODE_PRIVATE);

        registerTextView = findViewById(R.id.createAccountTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInBtn = findViewById(R.id.signInbtn);
        googleSignInBtn = findViewById(R.id.google_sign_in_button);

        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        auth = FirebaseAuth.getInstance();

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                signInWithGoogle();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                if(isSigningUp) {
                    register(emailEditText.getText().toString(), passwordEditText.getText().toString());
                } else {
                    signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSigningUp) {
                    isSigningUp = false;
                    signInBtn.setText(R.string.connexion);
                    registerTextView.setText(R.string.create_account);
                } else {
                    isSigningUp = true;
                    signInBtn.setText(R.string.register);
                    registerTextView.setText(R.string.connect);
                }
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Entrez l'email associé à votre compte pour réinitialiser votre mot de passe", Toast.LENGTH_SHORT).show();
                    return;
                }

                //progressBar.setVisibility(View.VISIBLE);

                auth.sendPasswordResetEmail(email)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Les instructions pour réinitialiser votre mot de passe vous ont été envoyées par email", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Aucun compte existant trouvé à cette adresse", Toast.LENGTH_SHORT).show();
                                }

                                //progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });

    }

    private boolean checkCredentials(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Entrez une adresse mail!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Entrer un mot de passe!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Votre mot de passe est trop court, 6 caractères minimum !", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInWithEmailAndPassword(String email, String password) {

        if(checkCredentials(email, password)) {
            final ProgressDialog dialog = RegisterOrLoginDialog("Connexion", "Veuillez patienter...", false);
            dialog.show();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d("Firebase Login", "signInWithEmail:success");
                                //FirebaseUser user = auth.getCurrentUser();
                                dialog.dismiss();
                            } else {
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    passwordEditText.setError("Votre mot de passe est trop faible");
                                    passwordEditText.requestFocus();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    emailEditText.setError("Email / Mot de passe erronés");
                                    emailEditText.requestFocus();
                                } catch(FirebaseAuthUserCollisionException e) {
                                    emailEditText.setError("Un utilisateur est déjà enregistré à cette adresse");
                                    emailEditText.requestFocus();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    emailEditText.setError("L'utilisateur entré n'existe pas, merci de bien vouloir créer un compte");
                                    emailEditText.requestFocus();
                                } catch(Exception e) {
                                    //Log.e(TAG, e.getMessage());
                                }
                                dialog.dismiss();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            firstRun = true;
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            //prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w("", "Google sign in failed", e);
                //Log.w("Firebase", e.toString());
                showToast("L'authentification a échoué");
                // ...
            }
        }
    }

    private void register(String email, String password) {
        if(checkCredentials(email, password)) {
            final ProgressDialog dialog = RegisterOrLoginDialog("Création du compte", "Veuillez patienter...", false);
            dialog.show();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d("", "createUserWithEmail:success");
                        //FirebaseUser user = auth.getCurrentUser();
                        dialog.dismiss();
                    } else {
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {
                            passwordEditText.setError("Votre mot de passe est trop faible");
                            passwordEditText.requestFocus();
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            emailEditText.setError("Email / Mot de passe erronés");
                            emailEditText.requestFocus();
                        } catch(FirebaseAuthUserCollisionException e) {
                            emailEditText.setError("Un utilisateur est déjà enregistré à cette adresse");
                            emailEditText.requestFocus();
                        } catch(Exception e) {
                            //Log.e(TAG, e.getMessage());
                        }
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d("", "firebaseAuthWithGoogle:" + acct.getId());

        final ProgressDialog dialog = RegisterOrLoginDialog("Connexion", "Veuillez patienter...", false);
        dialog.show();
        final String name = acct.getGivenName();
        final String email = acct.getEmail();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d("", "signInWithCredential:success");
                            if(!firstRun) {
                                showToast("Rebonjour " + name);
                            } else {
                                showToast("Bienvenue " + name + " !");
                            }
                            //FirebaseUser firebaseUser = auth.getCurrentUser();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                            String userId = mDatabase.push().getKey();
                            User user = new User(name, email, "");
                            mDatabase.child(userId).setValue(user);
                            dialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w("", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "L'authentification a échoué", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        // ...
                    }
                });
    }

    private ProgressDialog RegisterOrLoginDialog(String title, String message, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        return progressDialog;
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
