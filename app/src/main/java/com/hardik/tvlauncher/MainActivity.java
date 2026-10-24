
package com.hardik.tvlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    final int BG = Color.rgb(5, 9, 24);
    final int BLUE = Color.rgb(65, 155, 255);
    final int WHITE = Color.WHITE;
    final int MUTED = Color.rgb(177, 199, 230);

    LinearLayout root, appRow, musicRow;
    TextView clockText, dateText;
    Handler handler = new Handler();
    List<ResolveInfo> apps = new ArrayList<>();

    final String[] artists = {
        "Sunidhi Chauhan", "Atif Aslam",
        "Rahat Fateh Ali Khan", "Pritam",
        "Yo Yo Honey Singh", "Jasmine Sandlas"
    };

    final String[] initials = {
        "SC", "AA", "RF", "P", "YY", "JS"
    };

    final String[] songs = {
        "Sheila Ki Jawani", "Tera Hone Laga Hoon",
        "Zaroori Tha", "Kesariya",
        "Brown Rang", "Yaar Na Miley"
    };

    int dp(float n) {
        return (int) (n * getResources()
            .getDisplayMetrics().density + 0.5f);
    }

    GradientDrawable gradient(int a, int b, int border) {
        GradientDrawable d = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{a, b, BG}
        );
        d.setCornerRadius(dp(22));
        d.setStroke(dp(1), border);
        return d;
    }

    TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        return t;
    }

    LinearLayout vertical() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    LinearLayout horizontal() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL);
        return l;
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        buildScreen();
        loadApps();
        updateClock();
    }

    void buildScreen() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackground(gradient(
            Color.rgb(15, 52, 112),
            Color.rgb(7, 17, 48), Color.TRANSPARENT
        ));

        root = vertical();
        root.setPadding(dp(30), dp(20), dp(30), dp(30));
        scroll.addView(root);
        setContentView(scroll);

        // Brand and clock widgets
        LinearLayout top = horizontal();

        LinearLayout brandBox = vertical();
        TextView brand = text("Hardik TV+", 30, WHITE);
        brand.setTypeface(Typeface.DEFAULT_BOLD);
        brandBox.addView(brand);

        TextView sub = text(
            "CINEMATIC ENTERTAINMENT", 11, MUTED);
        brandBox.addView(sub);

        top.addView(brandBox,
            new LinearLayout.LayoutParams(0, -2, 1));

        LinearLayout widgets = vertical();
        widgets.setGravity(Gravity.RIGHT);

        clockText = text("--:--", 25, WHITE);
        clockText.setTypeface(Typeface.DEFAULT_BOLD);
        clockText.setGravity(Gravity.RIGHT);
        widgets.addView(clockText);

        dateText = text("--", 12, MUTED);
        dateText.setGravity(Gravity.RIGHT);
        widgets.addView(dateText);

        top.addView(widgets);
        root.addView(top);

        // Navigation
        LinearLayout nav = horizontal();
        nav.setPadding(0, dp(15), 0, dp(12));

        String[] tabs = {
            "Home", "Movies", "TV Shows",
            "Music", "Apps", "Settings"
        };

        for (String tab : tabs) {
            TextView item = text(tab, 13,
                tab.equals("Home") ? WHITE : MUTED);
            item.setGravity(Gravity.CENTER);
            item.setPadding(dp(12), dp(9), dp(12), dp(9));
            item.setFocusable(true);
            item.setBackground(gradient(
                Color.rgb(16, 40, 80),
                Color.rgb(8, 20, 44), Color.TRANSPARENT
            ));

            item.setOnFocusChangeListener((v, focused) -> {
                item.setTextColor(focused ? WHITE : MUTED);
                item.setBackground(gradient(
                    focused ? Color.rgb(35, 102, 194)
                            : Color.rgb(16, 40, 80),
                    Color.rgb(8, 20, 44),
                    focused ? BLUE : Color.TRANSPARENT
                ));
            });

            item.setOnClickListener(v -> {
                if (tab.equals("Apps")) {
                    findViewById(2001).requestFocus();
                } else if (tab.equals("Music")) {
                    findViewById(2002).requestFocus();
                } else if (tab.equals("Settings")) {
                    startActivity(new Intent(
                        android.provider.Settings.ACTION_SETTINGS));
                } else {
                    Toast.makeText(this,
                        tab + " selected",
                        Toast.LENGTH_SHORT).show();
                }
            });

            nav.addView(item);
        }
        root.addView(nav);

        // Cinematic hero banner
        LinearLayout hero = vertical();
        hero.setPadding(dp(25), dp(20), dp(20), dp(20));
        hero.setGravity(Gravity.CENTER_VERTICAL);
        hero.setBackground(gradient(
            Color.rgb(35, 111, 225),
            Color.rgb(12, 39, 95), BLUE
        ));
        hero.setFocusable(true);

        TextView eyebrow = text(
            "YOUR PERSONAL ENTERTAINMENT HUB", 11, WHITE);
        hero.addView(eyebrow);

        TextView heroTitle = text(
            "A whole world.\\nOne screen.", 30, WHITE);
        heroTitle.setTypeface(Typeface.DEFAULT_BOLD);
        heroTitle.setMaxLines(2);
        LinearLayout.LayoutParams titleP =
            new LinearLayout.LayoutParams(-1, dp(82));
        titleP.topMargin = dp(7);
        hero.addView(heroTitle, titleP);

        TextView heroSub = text(
            "Movies  •  Shows  •  Music  •  Apps",
            14, Color.rgb(218, 235, 255));
        hero.addView(heroSub);

        hero.setOnFocusChangeListener((v, focused) ->
            hero.setBackground(gradient(
                focused ? Color.rgb(49, 139, 255)
                        : Color.rgb(35, 111, 225),
                Color.rgb(12, 39, 95),
                focused ? WHITE : BLUE
            )));

        hero.setOnClickListener(v -> {
            if (!apps.isEmpty()) launch(apps.get(0));
        });

        LinearLayout.LayoutParams heroP =
            new LinearLayout.LayoutParams(-1, dp(190));
        heroP.bottomMargin = dp(15);
        root.addView(hero, heroP);

        // Widget cards
        TextView widgetTitle = text("AT A GLANCE", 17, WHITE);
        widgetTitle.setTypeface(Typeface.DEFAULT_BOLD);
        root.addView(widgetTitle);

        LinearLayout widgetRow = horizontal();
        widgetRow.setGravity(Gravity.TOP);

        String[] widgetNames = {
            "WEATHER", "QUICK ACCESS", "MUSIC"
        };
        String[] widgetValues = {
            "Check locally", "Your apps", "Your library"
        };

        for (int i = 0; i < widgetNames.length; i++) {
            final int index = i;
            LinearLayout w = vertical();
            w.setPadding(dp(12), dp(12), dp(10), dp(12));
            w.setBackground(gradient(
                Color.rgb(21, 43, 79),
                Color.rgb(10, 22, 44),
                Color.rgb(46, 83, 137)
            ));
            w.setFocusable(true);

            TextView heading = text(
                widgetNames[i], 10, BLUE);
            w.addView(heading);

            TextView value = text(
                widgetValues[i], 13, WHITE);
            w.addView(value);

            w.setOnClickListener(v -> {
                if (index == 1) {
                    findViewById(2001).requestFocus();
                } else if (index == 2) {
                    findViewById(2002).requestFocus();
                } else {
                    Toast.makeText(this,
                        "Connect a weather provider for live weather",
                        Toast.LENGTH_SHORT).show();
                }
            });

            w.setOnFocusChangeListener((v, focused) ->
                w.setBackground(gradient(
                    Color.rgb(21, 43, 79),
                    Color.rgb(10, 22, 44),
                    focused ? WHITE : Color.rgb(46, 83, 137)
                )));

            LinearLayout.LayoutParams wp =
                new LinearLayout.LayoutParams(0, dp(75), 1);
            wp.rightMargin = dp(8);
            widgetRow.addView(w, wp);
        }
        root.addView(widgetRow);

        // Installed apps row
        TextView appsTitle = text(
            "YOUR APPS", 19, WHITE);
        appsTitle.setTypeface(Typeface.DEFAULT_BOLD);
        appsTitle.setId(2001);

        LinearLayout.LayoutParams headingP =
            new LinearLayout.LayoutParams(-1, dp(45));
        headingP.topMargin = dp(15);
        root.addView(appsTitle, headingP);

        HorizontalScrollView appScroll = new HorizontalScrollView(this);
        appScroll.setHorizontalScrollBarEnabled(false);
        appRow = horizontal();
        appScroll.addView(appRow);
        root.addView(appScroll,
            new LinearLayout.LayoutParams(-1, dp(125)));

        // Music library row
        TextView musicTitle = text(
            "MUSIC LIBRARY", 19, WHITE);
        musicTitle.setTypeface(Typeface.DEFAULT_BOLD);
        musicTitle.setId(2002);

        LinearLayout.LayoutParams musicHeadingP =
            new LinearLayout.LayoutParams(-1, dp(45));
        musicHeadingP.topMargin = dp(12);
        root.addView(musicTitle, musicHeadingP);

        HorizontalScrollView musicScroll =
            new HorizontalScrollView(this);
        musicScroll.setHorizontalScrollBarEnabled(false);
        musicRow = horizontal();
        musicScroll.addView(musicRow);
        root.addView(musicScroll,
            new LinearLayout.LayoutParams(-1, dp(140)));

        for (int i = 0; i < artists.length; i++) {
            addArtist(i);
        }

        TextView footer = text(
            "HARDIK TV+  •  Made for your big screen",
            11, MUTED);
        footer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams footerP =
            new LinearLayout.LayoutParams(-1, dp(40));
        footerP.topMargin = dp(15);
        root.addView(footer, footerP);
    }

    void loadApps() {
        PackageManager pm = getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);

        List<ResolveInfo> found =
            pm.queryIntentActivities(intent, 0);

        if (found.isEmpty()) {
            intent.removeCategory(
                Intent.CATEGORY_LEANBACK_LAUNCHER);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            found = pm.queryIntentActivities(intent, 0);
        }

        apps.clear();
        apps.addAll(found);

        for (ResolveInfo info : apps) {
            addApp(info);
        }

        if (apps.isEmpty()) {
            TextView empty = text(
                "No compatible apps detected.", 14, MUTED);
            appRow.addView(empty);
        }
    }

    void addApp(ResolveInfo info) {
        LinearLayout card = vertical();
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(10), dp(8), dp(10), dp(8));
        card.setFocusable(true);
        card.setBackground(gradient(
            Color.rgb(24, 37, 61),
            Color.rgb(10, 18, 34), Color.TRANSPARENT
        ));

        ImageView icon = new ImageView(this);
        try {
            icon.setImageDrawable(info.loadIcon(getPackageManager()));
        } catch (Exception ignored) {}

        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        card.addView(icon, new LinearLayout.LayoutParams(
            dp(43), dp(43)));

        TextView name = text(
            info.loadLabel(getPackageManager()).toString(),
            12, WHITE);
        name.setGravity(Gravity.CENTER);
        name.setMaxLines(2);
        card.addView(name, new LinearLayout.LayoutParams(
            dp(105), dp(35)));

        card.setOnFocusChangeListener((v, focused) ->
            card.setBackground(gradient(
                Color.rgb(24, 53, 93),
                Color.rgb(10, 18, 34),
                focused ? WHITE : Color.TRANSPARENT
            )));

        card.setOnClickListener(v -> launch(info));

        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(dp(125), dp(110));
        p.rightMargin = dp(10);
        appRow.addView(card, p);
    }

    void addArtist(int index) {
        LinearLayout card = vertical();
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(10), dp(10), dp(10), dp(10));
        card.setFocusable(true);
        card.setBackground(gradient(
            Color.rgb(24, 53, 93),
            Color.rgb(10, 18, 34), Color.TRANSPARENT
        ));

        TextView initialsText = text(
            initials[index], 27, WHITE);
        initialsText.setTypeface(Typeface.DEFAULT_BOLD);
        initialsText.setGravity(Gravity.CENTER);
        card.addView(initialsText,
            new LinearLayout.LayoutParams(-1, dp(55)));

        TextView name = text(artists[index], 12, WHITE);
        name.setGravity(Gravity.CENTER);
        name.setMaxLines(2);
        card.addView(name,
            new LinearLayout.LayoutParams(dp(130), dp(35)));

        card.setOnFocusChangeListener((v, focused) ->
            card.setBackground(gradient(
                Color.rgb(35, 105, 192),
                Color.rgb(10, 18, 34),
                focused ? WHITE : Color.TRANSPARENT
            )));

        card.setOnClickListener(v -> {
            String query = artists[index] + " " + songs[index];
            Intent music = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://music.youtube.com/search?q="
                    + Uri.encode(query))
            );
            try {
                startActivity(music);
            } catch (Exception e) {
                Toast.makeText(this,
                    "Unable to open music search",
                    Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(dp(150), dp(120));
        p.rightMargin = dp(10);
        musicRow.addView(card, p);
    }

    void launch(ResolveInfo info) {
        Intent launch = getPackageManager()
            .getLaunchIntentForPackage(
                info.activityInfo.packageName);

        if (launch != null) {
            try {
                startActivity(launch);
            } catch (Exception e) {
                Toast.makeText(this,
                    "Unable to open app",
                    Toast.LENGTH_SHORT).show();
            }
        }
    }

    void updateClock() {
        if (clockText == null) return;

        Date now = new Date();
        clockText.setText(new SimpleDateFormat(
            "h:mm a", Locale.getDefault()).format(now));

        dateText.setText(new SimpleDateFormat(
            "EEE, d MMM", Locale.getDefault()).format(now));

        handler.postDelayed(this::updateClock, 30000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (appRow != null) {
            appRow.removeAllViews();
            loadApps();
        }
        updateClock();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
                    }
            
