package com.nickxla.chromatix;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class OverlayService extends Service {
    WindowManager wm;
    private LinearLayout ll;

    final int NO_DEF = 0; // Aucune déficience
    final int DEF_ROUGE = 1; // Protanomalie
    final int DEF_VERT = 2; // Deutéranomalie
    final int DEF_BLEU = 3; // Tritanomalie

    int DEFICIENCE_USER;

    private int alpha;
    private int red;
    private int green;
    private int blue;

    @Override
    public IBinder onBind(Intent i) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RetrieveDeficience();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        ll = new LinearLayout(this);

        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setBackgroundColor(Color.argb(alpha, red, green, blue));
        ll.setLayoutParams(llParam);

        WindowManager.LayoutParams parametres = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        parametres.x = 0;
        parametres.y = 0;
        parametres.gravity = Gravity.CENTER;

        wm.addView(ll, parametres);
    }

    private void RetrieveDeficience() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(OverlayService.this);
        DEFICIENCE_USER = sp.getInt("deficience", DEF_ROUGE);
        switch(DEFICIENCE_USER) {
            case NO_DEF:
                alpha = 0;
                red = 0;
                green = 0;
                blue = 0;
                break;
            case DEF_ROUGE:
                alpha = 66;
                red = 255;
                green = 0;
                blue = 0;
                break;
            case DEF_VERT:
                alpha = 66;
                red = 0;
                green = 255;
                blue = 0;
                break;
            case DEF_BLEU:
                alpha = 66;
                red = 0;
                green = 0;
                blue = 255;
                break;
            default:
                alpha = 0;
                red = 0;
                green = 0;
                blue = 0;
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ll != null){
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            if (wm != null) {
                wm.removeView(ll);
            }
        }
    }
}
