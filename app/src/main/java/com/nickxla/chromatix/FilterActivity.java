package com.nickxla.chromatix;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FilterActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 40;

    private Bitmap originalPicture;
    private Bitmap currentPicture;

    private ImageView imageViewF;

    Button colorDialog;
    boolean stockageAccessible;

    int previewWidth;
    int previewHeight;
    int[] rgbInt;

    final String [] colors = { "Black", "Black", "Black", "Blue", "Blue", "Blue", "Blue","Blue",
            "Black","Black","Black", "Blue","Blue","Blue","Blue","Blue",
            "Green","Green","Green","Blue","Blue","Blue","Blue","Blue",
            "Green","Green","Green","Green","Blue","Blue","Blue","Blue",
            "Green","Green","Green","Green","Green","Blue","Blue","Blue",
            "Green","Green","Green","Green","Green","Blue","Blue","Blue",
            "Green","Green","Green","Green","Green","Green","Blue","Blue",
            "Green","Green","Green","Green","Green","Green","Green","Blue",

            "Black","Black","Black","Black","Purple","Purple","Blue","Blue",
            "Black","Black","Black","Black","Blue","Blue","Blue","Blue",
            "Green","Green","Green","Blue","Blue","Blue","Blue","Blue",
            "Green","Green","Green","Green","Blue","Blue","Blue","Blue",
            "Green","Green","Green","Green","Green","Blue","Blue","Blue",
            "Green","Green","Green","Green","Green","Green","Blue","Blue",
            "Green","Green","Green","Green","Green","Green","Blue","Blue",
            "Green","Green","Green","Green","Green","Green","Green","Blue",

            "Brown","Brown","Brown","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Purple","Purple","Purple","Purple","Purple",
            "Green","Green","Gray","Gray","Purple","Purple","Purple","Purple",
            "Green","Green","Green","Gray","Gray","Purple","Blue","Blue",
            "Green","Green","Green","Green","Green","Blue","Blue","Blue",
            "Green","Green","Green","Green","Green","Green","Blue","Blue",
            "Green","Green","Green","Green","Green","Green","Blue","Blue",
            "Green","Green","Green","Green","Green","Green","Green","Blue",

            "Brown","Brown","Brown","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Gray","Purple","Purple","Purple","Purple",
            "Green","Green","Gray","Gray","Gray","Gray","Purple","Purple",
            "Green","Green","Green","Gray","Gray","Gray","Blue","Blue",
            "Green","Green","Green","Green","Gray","Gray","Blue","Blue",
            "Green","Green","Green","Green","Green","Green","Green","Blue",
            "Green","Green","Green","Green","Green","Green","Green","Blue",

            "Red","Brown","Purple","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Gray","Gray","Purple","Purple","Purple",
            "Green","Green","Green","Gray","Gray","Gray","Gray","Purple",
            "Green","Green","Green","Green","Gray","Gray","Gray","Gray",
            "Green","Green","Green","Green","Green","Gray","Gray","Purple",
            "Green","Green","Green","Green","Green","Green","Green","Blue",

            "Red","Red","Red","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Gray","Gray","Purple","Purple","Purple",
            "Brown","Brown","Brown","Brown","Gray","Gray","Purple","Purple",
            "Green","Green","Green","Gray","Gray","Gray","Gray","Purple",
            "Green","Green","Green","Green","Gray","Gray","Gray","Gray",
            "Green","Green","Green","Green","Green","Green","Green","Blue",

            "Red","Red","Red","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Purple","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown", "Brown","Purple","Purple","Purple","Purple",
            "Brown","Brown","Brown","Brown","Gray","Purple","Purple","Purple",
            "Yellow","Yellow","Yellow","Yellow","Gray","Gray","Gray","Purple",
            "Yellow","Yellow","Yellow","Yellow","Gray","Gray","Gray","Gray",
            "Green","Green","Green","Green","Gray","Gray","Gray","Gray",

            "Red","Red","Red","Red","Pink","Pink","Pink","Pink",
            "Red","Red","Red","Red","Pink","Pink","Pink","Pink",
            "Orange","Orange","Red","Red","Pink","Pink","Pink","Pink",
            "Orange","Orange","Orange","Red","Pink","Pink","Pink","Pink",
            "Orange","Orange","Orange","Orange","Pink","Pink","Pink","Pink",
            "Orange","Orange","Orange","Orange","Orange","Pink","Pink","Pink",
            "Orange","Orange","Orange","Orange","Orange","Gray","Pink","Pink",
            "Yellow","Yellow","Yellow","Yellow","Yellow","Gray","Gray","Gray"};

    boolean[] checkedItems = {false, false, false, false, false, false, false, false, false};

    int couleurRemplissage = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbarFilterActivity));
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        setTitle("Filtrer");

        colorDialog = findViewById(R.id.selectColorsButton);

        ConstraintLayout cl = findViewById(R.id.includeFilterLayout);
        imageViewF = cl.findViewById(R.id.imageViewFiltrage);

        try {
            Intent intent = getIntent();
            Uri uri = Uri.parse(intent.getStringExtra("image"));

            try
            {
                originalPicture = MediaStore.Images.Media.getBitmap(getContentResolver() , uri);

                final int maxSize = 960;
                int inWidth = originalPicture.getWidth();
                int inHeight = originalPicture.getHeight();
                if(inWidth > inHeight){
                    previewWidth = maxSize;
                    previewHeight = (inHeight * maxSize) / inWidth;
                } else {
                    previewHeight = maxSize;
                    previewWidth = (inWidth * maxSize) / inHeight;
                }

                rgbInt = new int[previewHeight*previewWidth];

                currentPicture = Bitmap.createScaledBitmap(originalPicture,previewWidth,previewHeight,true);
                currentPicture.getPixels(rgbInt, 0, previewWidth, 0, 0, previewWidth, previewHeight);

                for (int y = 0; y < previewHeight; y++){
                    for (int x = 0; x < previewWidth; x++)
                    {
                        int index = y * previewWidth + x;
                        int R = (rgbInt[index] >> 16) & 0xff;     //bitwise shifting
                        int G = (rgbInt[index] >> 8) & 0xff;
                        int B = rgbInt[index] & 0xff;

                        //R,G.B - Red, Green, Blue
                        //to restore the values after RGB modification, use
                        //next statement
                        rgbInt[index] = 0xff000000 | (R << 16) | (G << 8) | B;
                    }}

                //bitmap = Bitmap.createBitmap(rgbInt, previewWidth , previewHeight, Bitmap.Config.ARGB_8888);

                Drawable d = new BitmapDrawable(getResources(), currentPicture);
                imageViewF.setImageDrawable(d);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            Glide.with(FilterActivity.this).load(uri).into(imageViewF);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            if(imageViewF != null) {
                //Log.e("SET IMAGE", e.toString() + imageViewF.toString());
            } else {
                //Log.e("SET IMAGE", e.toString() + imageViewF);
            }
        }

        colorDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] multiChoiceItems = getResources().getStringArray(R.array.colors_array);
                new AlertDialog.Builder(FilterActivity.this)
                        .setTitle(getString(R.string.main_dialog_multi_choice))
                        .setMultiChoiceItems(multiChoiceItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                // If the user checked the item, add it to the selected items
                                // Else, if the item is already in the array, remove it
                                checkedItems[which] = isChecked;
                            }
                        })
                        .setPositiveButton(getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BackgroundTask task = new BackgroundTask(FilterActivity.this);
                                task.execute();
                                //imageViewF.setImageBitmap(currentPicture);
                                //Glide.with(FilterActivity.this).load(getImageUri(FilterActivity.this, currentPicture)).into(imageViewF);
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_cancel), null)
                        .show();
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void changeImage(){
        String colors2Change = boxesChecked();
            int[] nRGB = new int[rgbInt.length];
            for (int i = 0; i < rgbInt.length; i++) {
                if (colors2Change.contains(getCouleur(rgbInt[i]))) {
                    nRGB[i] = couleurRemplissage;
                } else
                    nRGB[i] = (((rgbInt[i])) | 0xff000000);
            }
            currentPicture = Bitmap.createBitmap(nRGB, previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
            //TODO: rotation
    }

    public String getCouleur(int midColor) {
        int rRed = getRed(midColor) / 32;
        int rBlue = getBlue(midColor) / 32;
        int rGreen = getGreen(midColor) / 32;
        int reducedColor = ((rRed*64) + (rGreen * 8) + (rBlue));
        if ((getRed(midColor) + getBlue(midColor) + getGreen(midColor)) == 765)
            return "White";
        for (int i = 0; i < colors.length; i++) {
            if (i == reducedColor)
                return colors[i];
        }
        return "Not recognizable";
    }

    public int getRed(long color){
        return ((int) (((color & 0xFF0000) >>16) & 0xFF));
    }
    //return 0-255
    public int getGreen(long color){
        return  ((int) (((color & 0xFF00) >> 8) & 0xFF));
    }
    //return 0-255
    public int getBlue(long color){
        return ((int) (color & 0xFF));
    }

    private String boxesChecked(){
        String boxes = "";
        if(checkedItems[0])
            boxes += "Brown ";
        if(checkedItems[1])
            boxes += "Blue ";
        if(checkedItems[2])
            boxes += "Red ";
        if(checkedItems[3])
            boxes += "Yellow ";
        if(checkedItems[4])
            boxes += "Gray ";
        if(checkedItems[5])
            boxes += "Pink ";
        if(checkedItems[6])
            boxes += "Orange ";
        if(checkedItems[7])
            boxes += "Green ";
        if(checkedItems[8])
            boxes += "Purple ";

        return boxes;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filtre_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            checkStoragePermission();
            if(stockageAccessible) {
                SavePicture();
                Toast.makeText(this, "Image sauvegardée !", Toast.LENGTH_SHORT).show();
            } else {
                requestWriteStoragePermission();
            }
            return true;
        } else if(id == R.id.action_reset) {
            ResetColors();
            Toast.makeText(this, "Couleurs réinitialisées !", Toast.LENGTH_SHORT).show();
        } else if(id == R.id.action_color) {
            ChooseColor();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean noneSelected() {
        boolean Isselected = false;
        for (boolean checkedItem : checkedItems) {
            if (checkedItem) {
                Isselected = true;
            }
        }
        return Isselected;
    }

    private void ChooseColor() {
        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this);
        builder.setTitle("Choisissez la couleur de remplissage");
        builder.setPreferenceName("MyColorPickerDialog");
        builder.setPositiveButton(getString(R.string.dialog_ok), new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                couleurRemplissage = colorEnvelope.getColor();
                if(noneSelected()) {
                    BackgroundTask task = new BackgroundTask(FilterActivity.this);
                    task.execute();
                    //imageViewF.setImageBitmap(currentPicture);
                    //Glide.with(FilterActivity.this).load(getImageUri(FilterActivity.this, currentPicture)).into(imageViewF);
                }
            }
        });
        builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void checkStoragePermission() {
        stockageAccessible = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestWriteStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    stockageAccessible = true;
                    SavePicture();

                } else {
                    stockageAccessible = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    private void SavePicture() {
        String first_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File imagesFolder = new File(first_path, "Chromatix");
        imagesFolder.mkdir();
        String path = imagesFolder.getAbsolutePath();
        OutputStream fOut;
        File file = new File(path, System.currentTimeMillis() + "_chromatix.jpeg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
        try {
            fOut = new FileOutputStream(file);

            Bitmap pictureBitmap = currentPicture;
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            MediaScannerConnection.scanFile(this, new String[] { file.getPath() }, new String[] { "image/jpeg" }, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ResetColors() {
        for (int i = 0; i < checkedItems.length; i++) {
            checkedItems[i] = false;
        }
        try {
            currentPicture = originalPicture;
            imageViewF.setImageBitmap(currentPicture);
        } catch (Exception e) {
            //Log.d("Réinialisation filtres", e.toString());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        //overridePendingTransition();
        return false;
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public BackgroundTask(FilterActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Filtrage en cours, veuillez patienter...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                //imageViewF.setImageBitmap(currentPicture);
                Glide.with(FilterActivity.this).load(getImageUri(FilterActivity.this, currentPicture)).into(imageViewF);
                dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                changeImage();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}
