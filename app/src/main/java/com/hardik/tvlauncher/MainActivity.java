
package com.hardik.tvlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.HorizontalScrollView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private final int BG = Color.rgb(5, 12, 32);
    private final int BLUE = Color.rgb(40, 130, 255);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(180, 200, 225);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 24, 40, 24);
        root.setBackgroundColor(BG);

        setContentView(root);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);

        TextView logo = makeText("HARDIK TV", 28, BLUE, true);
        top.addView(logo, new LinearLayout.LayoutParams(0, -2, 1));

        String date = new SimpleDateFormat(
            "EEE, dd MMM  |  hh:mm a", Locale.getDefault()
        ).format(new Date());

        top.addView(makeText(date, 16, WHITE, false));
        root.addView(top);

        TextView hero = makeText(
            "YOUR CINEMA. YOUR MUSIC. YOUR WORLD.", 26, WHITE, true
        );
        hero.setPadding(0, 28, 0, 4);
        root.addView(hero);

        root.addView(makeText(
            "Welcome to your personal big-screen home.", 16, MUTED, false
        ));

        addHeading(root, "YOUR APPS");

        LinearLayout apps = new LinearLayout(this);
        apps.setOrientation(LinearLayout.HORIZONTAL);

        String[] appNames = {
            "YouTube", "Netflix", "Prime Video", "Settings", "Play Store"
        };

        int[] appColors = {
            0xffd62828, 0xffb91d35, 0xff1769aa,
            0xff455a75, 0xff16865b
        };

        for (int i = 0; i < appNames.length; i++) {
            final String appName = appNames[i];
            TextView tile = makeTile(appName, appColors[i]);

            LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(0, 76, 1);
            params.setMargins(4, 4, 4, 4);

            apps.addView(tile, params);

            tile.setOnClickListener(v ->
                Toast.makeText(
                    MainActivity.this,
                    appName + " shortcut selected",
                    Toast.LENGTH_SHORT
                ).show()
            );
        }

        root.addView(apps);

        addHeading(root, "MUSIC FAVOURITES");

        HorizontalScrollView scroll = new HorizontalScrollView(this);
        scroll.setHorizontalScrollBarEnabled(false);

        LinearLayout artists = new LinearLayout(this);
        artists.setOrientation(LinearLayout.HORIZONTAL);

        String[] names = {
            "Sunidhi Chauhan",
            "Atif Aslam",
            "Rahat Fateh Ali Khan",
            "Pritam",
            "Yo Yo Honey Singh",
            "Jasmine Sandlas"
        };

        for (String name : names) {
            TextView artist = makeTile(name, 0xff16365f);

            LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(190, 64);
            params.setMargins(4, 4, 8, 4);

            artists.addView(artist, params);

            artist.setOnClickListener(v ->
                Toast.makeText(
                    MainActivity.this,
                    name,
                    Toast.LENGTH_SHORT
                ).show()
            );
        }

        scroll.addView(artists);
        root.addView(scroll);

        TextView footer = makeText(
            "Use your remote to select a tile", 14, MUTED, false
        );
        footer.setPadding(0, 22, 0, 0);
        root.addView(footer);
    }

    private void addHeading(LinearLayout root, String title) {
        TextView heading = makeText(title, 17, BLUE, true);
        heading.setPadding(0, 24, 0, 8);
        root.addView(heading);
    }

    private TextView makeText(
        String value, float size, int color, boolean bold
    ) {
        TextView text = new TextView(this);
        text.setText(value);
        text.setTextSize(size);
        text.setTextColor(color);

        if (bold) {
            text.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }

        return text;
    }

    private TextView makeTile(String value, int normalColor) {
        TextView tile = makeText(value, 16, WHITE, true);
        tile.setGravity(Gravity.CENTER);
        tile.setPadding(8, 8, 8, 8);
        tile.setBackgroundColor(normalColor);
        tile.setFocusable(true);
        tile.setClickable(true);

        tile.setOnFocusChangeListener((view, focused) -> {
            view.setBackgroundColor(focused ? BLUE : normalColor);
            view.setScaleX(focused ? 1.04f : 1.0f);
            view.setScaleY(focused ? 1.04f : 1.0f);
        });

        return tile;
    }
                                              }
