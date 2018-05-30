package com.nickxla.chromatix;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle(getString(R.string.about_activity_name));

        Toolbar toolbar = findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getWindow().setNavigationBarColor(ContextCompat.getColor(AboutActivity.this, R.color.colorPrimary));

        initView();
    }

    public void initView() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show);
        ScrollView scroll_about = findViewById(R.id.scroll_about);
        scroll_about.startAnimation(animation);

        LinearLayout ll_card_about_2_shop = findViewById(R.id.ll_card_about_2_shop);
        LinearLayout ll_card_about_2_email = findViewById(R.id.ll_card_about_2_email);
        LinearLayout ll_card_about_source_licenses = findViewById(R.id.ll_card_about_source_licenses);
        ll_card_about_2_shop.setOnClickListener(this);
        ll_card_about_2_email.setOnClickListener(this);
        ll_card_about_source_licenses.setOnClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab_about_share);
        fab.setOnClickListener(this);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setStartOffset(600);

        TextView tv_about_version = findViewById(R.id.tv_about_version);

        String versionName = BuildConfig.VERSION_NAME;
        tv_about_version.setText(versionName);

        tv_about_version.startAnimation(alphaAnimation);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_card_about_2_shop:
                Intent playStore = new Intent(Intent.ACTION_VIEW);
                playStore.setData(Uri.parse("market://details?id=com.nickxla.chromatix"));
                startActivity(playStore);
                break;

            case R.id.ll_card_about_2_email:
                String uriText =
                        "mailto:chromatixapp@gmail.com" +
                                "?subject=" + Uri.encode("Feedback pour Chromatix (Android)") +
                                "&body=" + Uri.encode("Bonjour !");

                Uri uri = Uri.parse(uriText);

                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                startActivity(Intent.createChooser(sendIntent, "Envoyer le feedback"));
                break;
            /*
            case R.id.ll_card_about_source_licenses:
                final Dialog dialog = new Dialog(this, R.style.DialogFullscreenWithTitle);
                dialog.setTitle(getString(R.string.about_source_licenses));
                dialog.setContentView(R.layout.dialog_source_licenses);

                final WebView webView = dialog.findViewById(R.id.web_source_licenses);
                webView.loadUrl("file:///android_asset/source_licenses.html");

                Button btn_source_licenses_close = dialog.findViewById(R.id.btn_source_licenses_close);
                btn_source_licenses_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            */
            case R.id.fab_about_share:

                String shareBody = "Venez tester l'application Chromatix, crée spécialement pour les daltoniens";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mon coup de coeur");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_with)));
                break;
        }
    }
}
