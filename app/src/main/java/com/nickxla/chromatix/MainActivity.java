package com.nickxla.chromatix;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_OVERLAY = 10;
    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 20;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 30;
    AdView mAdView;
    private TextView colorTextView;
    private TextView startBy;
    private ImageView imageView;

    TextView deficienceTextView;

    Uri photoUri;
    Bitmap photo;

    private boolean cameraAccessible;
    private boolean stockageAccessible;

    private static final int TAKE_PICTURE = 0;
    private static final int PICK_IMAGE = 1;

    int DEFICIENCE;

    private NavigationView navigationView;

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.w("", "Getting bitmap photo");
        if(imageView != null) {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            if(drawable != null) {
                outState.putParcelable("bitmap", drawable.getBitmap());
                if(photoUri != null) {
                    outState.putString("photoUri", photoUri.toString());
                }
                if(photo != null) {
                    outState.putString("startBy", "");
                } else {
                    outState.putString("startBy", getString(R.string.startBy));
                }
            }
        }
        if(colorTextView != null) {
            ColorDrawable couleurText = (ColorDrawable)colorTextView.getBackground();
            if(couleurText != null) {
                outState.putInt("text_color", couleurText.getColor());
                outState.putInt("text_color_interieur", colorTextView.getCurrentTextColor());
            }
            outState.putString("color", colorTextView.getText().toString());
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            //Log.w("", "Restoring bitmap photo");
            photo = savedInstanceState.getParcelable("bitmap");
            photoUri = Uri.parse(savedInstanceState.getString("photoUri"));
            //imageView.setImageBitmap(photo);
            Glide.with(MainActivity.this).load(photoUri).into(imageView);
            colorTextView.setText(savedInstanceState.getString("color"));
            startBy.setText(savedInstanceState.getString("startBy"));
            colorTextView.setBackgroundColor(savedInstanceState.getInt("text_color"));
            colorTextView.setTextColor(savedInstanceState.getInt("text_color_interieur"));
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
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        navigationView.getMenu().getItem(0).setChecked(true);
        //changeFABIcon(fab, checkOverlayStatus(sp));
        RetrieveDeficience();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == TAKE_PICTURE) {
            try {
                photoUri = data.getData();
                photo = (Bitmap) data.getExtras().get("data");
                if(photo != null) {
                    startBy.setVisibility(View.INVISIBLE);
                    //imageView.setImageBitmap(photo);
                    Glide.with(MainActivity.this).load(photoUri).into(imageView);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else if(resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            photoUri = data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            startBy.setVisibility(View.INVISIBLE);
            //imageView.setImageURI(photoUri);
            Glide.with(MainActivity.this).load(photoUri).into(imageView);
        } else if(resultCode == RESULT_OK && requestCode == REQUEST_OVERLAY) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            sharedPref.edit().putBoolean("switch_preference_overlay", true).apply();
            //enableOverlayFAB(fab);
        }
        navigationView.setCheckedItem(R.id.nav_colorpicker);
    }

    private void checkCameraPermission() {
        cameraAccessible = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkStoragePermission() {
        stockageAccessible = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkOverlayPermission(Context context) {
        return Settings.canDrawOverlays(context);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    stockageAccessible = true;
                    importPicture();

                } else {
                    stockageAccessible = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    cameraAccessible = true;
                    takePicture();

                } else {
                    cameraAccessible = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Détection de couleur");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RetrieveDeficience();

        MobileAds.initialize(this, getString(R.string.admob));

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_banner));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        colorTextView = findViewById(R.id.colorTextView);
        imageView = findViewById(R.id.imageViewToAnalyze);
        startBy = findViewById(R.id.commencezParTextView);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        };

        FloatingActionMenu menu = findViewById(R.id.menu);
        com.github.clans.fab.FloatingActionButton fab = findViewById(R.id.menu_item_filter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo != null) {
                    Intent filtre = new Intent(MainActivity.this, FilterActivity.class);

                    filtre.putExtra("image", photoUri.toString());
                    startActivity(filtre);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choisissez d'abord une image");
                    builder.setMessage("Merci de commencer par prendre une photo ou par importer une photo depusi votre galerie.");
                    builder.setCancelable(true);
                    builder.show();
                }
            }
        });

        SharedPreferences prefs = getSharedPreferences("com.nickxla.chromatix", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            ShowQuizz();
            prefs.edit().putBoolean("firstrun", false).apply();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        ImageView pp = navigationView.getHeaderView(0).findViewById(R.id.profilePicture);
        TextView userName = navigationView.getHeaderView(0).findViewById(R.id.usernameHeader);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.emailHeader);
        try {
            userName.setText(auth.getCurrentUser().getDisplayName());
        } catch (Exception e) {
            userName.setText(getString(R.string.invite));
            e.printStackTrace();
        }
        email.setText(auth.getCurrentUser().getEmail());
        if (auth.getCurrentUser().getPhotoUrl() != null) {
            try {
                Picasso.with(this).load(auth.getCurrentUser().getPhotoUrl().toString()).transform(new CropCircleTransformation()).into(pp);
                //Log.w("Bitmap profile picture", auth.getCurrentUser().getPhotoUrl().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Picasso.with(this).load(R.drawable.avatar).transform(new CropCircleTransformation()).into(pp);
        }


        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                imageView.setDrawingCacheEnabled(true);
                Bitmap imgbmp = Bitmap.createBitmap(imageView.getDrawingCache());
                imageView.setDrawingCacheEnabled(false);

                try {
                    int pxl = imgbmp.getPixel((int) x, (int) y);

                    int redValue = Color.red(pxl);
                    int blueValue = Color.blue(pxl);
                    int greenValue = Color.green(pxl);
                    String str = "RGB (" + redValue + ", " + greenValue + ", " + blueValue + ")";
                    //Log.e("COLOR", str);

                    colorTextView.setBackgroundColor(pxl);

                    ColorUtils utils = new ColorUtils();
                    String color = utils.getColorNameFromRgb(redValue, greenValue, blueValue);

                    String rgbText = String.format(getResources().getString(R.string.color), redValue, greenValue, blueValue, color);
                    colorTextView.setText(rgbText);
                    if (redValue > 128 && blueValue > 128 && greenValue > 128) {
                        colorTextView.setVisibility(View.VISIBLE);
                        colorTextView.setTextColor(Color.BLACK);
                    } else if(redValue == 0 && greenValue == 0 && blueValue == 0) { // Noir absolu => en dehors de l'image
                        colorTextView.setVisibility(View.INVISIBLE);
                    } else {
                        colorTextView.setVisibility(View.VISIBLE);
                        colorTextView.setTextColor(Color.WHITE);
                    }

                } catch (Exception e) {
                }
                imgbmp.recycle();

                return true;
            }
        });
    }

    private void ShowQuizz() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.welcome_quizz))
                .setMessage(getString(R.string.welcome_quizz_dialog_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        launchQuizz();
                    }
                })
                .setNegativeButton(getString(R.string.quizz_welcome_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(getString(R.string.dialog_neutral), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            auth.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_STORAGE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        checkCameraPermission();
        checkStoragePermission();

        if (id == R.id.nav_camera) {
            if(cameraAccessible) {
                takePicture();
            } else {
                requestCameraPermission();
            }
        } else if (id == R.id.nav_gallery) {
            if(stockageAccessible) {
                importPicture();
            } else {
                requestStoragePermission();
            }
        } else if (id == R.id.nav_quizz) {
            launchQuizz();
        } else if (id == R.id.nav_colorpicker) {

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } else if(id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_donation) {
            startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchQuizz() {
        startActivity(new Intent(MainActivity.this, QuizzActivity.class));
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    private void importPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void RetrieveDeficience() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        DEFICIENCE = sp.getInt("deficience", 0);
        if(DEFICIENCE != -1) {
            navigationView = findViewById(R.id.nav_view);
            final View headerLayout = navigationView.getHeaderView(0);
            deficienceTextView = headerLayout.findViewById(R.id.deficienceHeader);
            switch (DEFICIENCE) {
                case 0:
                    deficienceTextView.setText(getString(R.string.no_def));
                    break;
                case 1:
                    deficienceTextView.setText(getString(R.string.tritano_def));
                    break;
                case 2:
                    deficienceTextView.setText(getString(R.string.protano_def));
                    break;
                case 3:
                    deficienceTextView.setText(getString(R.string.deuterano_def));
                    break;
                default:
                    break;
            }
        }
    }


}
