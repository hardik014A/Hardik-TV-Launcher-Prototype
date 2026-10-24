
package com.hardik.tvlauncher;

import android.app.Activity;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    int bg = Color.rgb(9, 10, 15);
    int cardBg = Color.rgb(27, 29, 38);
    int white = Color.WHITE;
    int muted = Color.rgb(165, 169, 181);
    int blue = Color.rgb(112, 180, 255);

    LinearLayout root;
    LinearLayout content;
    TextView status;
    final List<ResolveInfo> installedApps = new ArrayList<>();

    String[] artists = {
        "Sunidhi Chauhan",
        "Atif Aslam",
        "Rahat Fateh Ali Khan",
        "Pritam",
        "Yo Yo Honey Singh",
        "Jasmine Sandlas"
    };

    String[] initials = {
        "SC", "AA", "RF", "P", "YY", "JS"
    };

    int dp(float n) {
        return (int) (n * getResources()
            .getDisplayMetrics().density + 0.5f);
    }

    GradientDrawable shape(int color, int border) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(16));
        d.setStroke(dp(2), border);
        return d;
    }

    TextView label(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        return t;
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        buildHome();
        discoverApps();
    }

    void buildHome() {
        ScrollView page = new ScrollView(this);
        page.setFillViewport(true);
        page.setBackgroundColor(bg);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(32), dp(22), dp(32), dp(28));
        page.addView(root);

        setContentView(page);

        TextView brand = label("HARDIK PREMIUM+", 27, white);
        brand.setTypeface(Typeface.DEFAULT_BOLD);
        root.addView(brand, new LinearLayout.LayoutParams(
            -1, dp(42)));

        TextView tagline = label(
            "CINEMA  •  ENTERTAINMENT  •  MUSIC",
            11, muted);
        root.addView(tagline, new LinearLayout.LayoutParams(
            -1, dp(27)));

        TextView welcome = label(
            "Your screen. Your world.", 24, white);
        welcome.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams wp =
            new LinearLayout.LayoutParams(-1, dp(48));
        wp.topMargin = dp(15);
        root.addView(welcome, wp);

        TextView subtitle = label(
            "Everything you love, in one place.",
            14, muted);
        root.addView(subtitle, new LinearLayout.LayoutParams(
            -1, dp(30)));

        LinearLayout nav = new LinearLayout(this);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setGravity(Gravity.CENTER_VERTICAL);

        String[] tabs = {
            "Home", "Movies", "TV Shows", "Music", "Apps"
        };

        for (String tab : tabs) {
            TextView t = label(tab, 14,
                tab.equals("Home") ? white : muted);
            t.setGravity(Gravity.CENTER);
            t.setPadding(dp(12), dp(7), dp(12), dp(7));
            t.setFocusable(true);
            t.setBackground(shape(bg, Color.TRANSPARENT));

            t.setOnFocusChangeListener((v, focused) -> {
                t.setBackground(shape(
                    focused ? Color.rgb(49, 55, 70) : bg,
                    focused ? blue : Color.TRANSPARENT
                ));
                t.setTextColor(focused ? white : muted);
            });

            if (tab.equals("Music")) {
                t.setOnClickListener(v ->
                    findViewById(3003).requestFocus());
            } else if (tab.equals("Apps")) {
                t.setOnClickListener(v ->
                    findViewById(3002).requestFocus());
            } else if (tab.equals("Movies")
                    || tab.equals("TV Shows")) {
                t.setOnClickListener(v ->
                    Toast.makeText(this,
                        "Open a streaming app below",
                        Toast.LENGTH_SHORT).show());
            } else {
                t.setOnClickListener(v ->
                    findViewById(3001).requestFocus());
            }

            nav.addView(t, new LinearLayout.LayoutParams(
                -2, dp(43)));
        }

        LinearLayout.LayoutParams np =
            new LinearLayout.LayoutParams(-1, dp(48));
        np.topMargin = dp(12);
        root.addView(nav, np);

        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setGravity(Gravity.CENTER_VERTICAL);
        hero.setPadding(dp(24), dp(15), dp(24), dp(15));
        hero.setBackground(shape(
            Color.rgb(23, 30, 46),
            Color.rgb(42, 54, 76)));

        TextView eyebrow = label(
            "THE CINEMATIC EXPERIENCE", 11, blue);
        hero.addView(eyebrow);

        TextView heroTitle = label(
            "Make it a movie night.", 25, white);
        heroTitle.setTypeface(Typeface.DEFAULT_BOLD);
        hero.addView(heroTitle);

        TextView heroDesc = label(
            "Choose your favourite entertainment app.",
            14, muted);
        hero.addView(heroDesc);

        hero.setFocusable(true);
        hero.setOnFocusChangeListener((v, focused) ->
            hero.setBackground(shape(
                focused ? Color.rgb(39, 54, 78)
                        : Color.rgb(23, 30, 46),
                focused ? blue : Color.rgb(42, 54, 76)
            )));

        hero.setOnClickListener(v -> {
            if (!installedApps.isEmpty()) {
                launch(installedApps.get(0));
            }
        });

        LinearLayout.LayoutParams hp =
            new LinearLayout.LayoutParams(-1, dp(145));
        hp.topMargin = dp(12);
        root.addView(hero, hp);

        status = label("YOUR APPS", 20, white);
        status.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams sp =
            new LinearLayout.LayoutParams(-1, dp(44));
        sp.topMargin = dp(12);
        root.addView(status, sp);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        root.addView(content);

        content.setFocusable(false);

        TextView footer = label(
            "HARDIK PREMIUM+    •    Use remote to navigate",
            11, muted);
        LinearLayout.LayoutParams fp =
            new LinearLayout.LayoutParams(-1, dp(42));
        fp.topMargin = dp(15);
        root.addView(footer, fp);

        addRow("YOUR INSTALLED APPS", 3002);
        addRow("MUSIC LIBRARY", 3003);
    }

    void addRow(String title, int id) {
        TextView heading = label(title, 18, white);
        heading.setTypeface(Typeface.DEFAULT_BOLD);
        heading.setId(id);

        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(-1, dp(42));
        p.topMargin = dp(12);
        content.addView(heading, p);

        HorizontalScrollView horizontal =
            new HorizontalScrollView(this);
        horizontal.setHorizontalScrollBarEnabled(false);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(dp(3), dp(5), dp(3), dp(12));

        horizontal.addView(row);
        content.addView(horizontal, new LinearLayout.LayoutParams(
            -1, dp(112)));

        if (id == 3002) {
            for (ResolveInfo info : installedApps) {
                addAppCard(row, info);
            }

            if (installedApps.isEmpty()) {
                row.addView(label(
                    "No compatible apps found",
                    14, muted));
            }
        } else {
            for (int i = 0; i < artists.length; i++) {
                addArtistCard(row, i);
            }
        }
    }

    void addAppCard(LinearLayout row, ResolveInfo info) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(8), dp(8), dp(8), dp(8));
        card.setBackground(shape(cardBg, Color.TRANSPARENT));
        card.setFocusable(true);

        TextView icon = label("▶", 27, blue);
        icon.setGravity(Gravity.CENTER);
        card.addView(icon, new LinearLayout.LayoutParams(
            -1, dp(42)));

        TextView name = label(
            info.loadLabel(getPackageManager()).toString(),
            13, white);
        name.setGravity(Gravity.CENTER);
        name.setMaxLines(2);
        card.addView(name, new LinearLayout.LayoutParams(
            -1, dp(35)));

        card.setOnFocusChangeListener((v, focused) ->
            card.setBackground(shape(
                focused ? Color.rgb(47, 58, 77) : cardBg,
                focused ? blue : Color.TRANSPARENT
            )));

        card.setOnClickListener(v -> launch(info));

        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(dp(155), dp(92));
        p.rightMargin = dp(12);
        row.addView(card, p);
    }

    void addArtistCard(LinearLayout row, int index) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(8), dp(8), dp(8), dp(8));
        card.setFocusable(true);
        card.setBackground(shape(cardBg, Color.TRANSPARENT));

        TextView monogram = label(initials[index], 25, blue);
        monogram.setTypeface(Typeface.DEFAULT_BOLD);
        monogram.setGravity(Gravity.CENTER);
        card.addView(monogram, new LinearLayout.LayoutParams(
            -1, dp(43)));

        TextView name = label(artists[index], 12, white);
        name.setGravity(Gravity.CENTER);
        name.setMaxLines(2);
        card.addView(name, new LinearLayout.LayoutParams(
            -1, dp(32)));

        card.setOnFocusChangeListener((v, focused) ->
            card.setBackground(shape(
                focused ? Color.rgb(47, 58, 77) : cardBg,
                focused ? blue : Color.TRANSPARENT
            )));

        card.setOnClickListener(v -> {
            String url = "https://music.youtube.com/search?q="
                + Uri.encode(artists[index]);
            Intent intent = new Intent(
                Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this,
                    "Music app unavailable",
                    Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(dp(155), dp(92));
        p.rightMargin = dp(12);
        row.addView(card, p);
    }

    void discoverApps() {
        installedApps.clear();
        PackageManager pm = getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);

        List<ResolveInfo> results =
            pm.queryIntentActivities(intent, 0);

        if (results.isEmpty()) {
            intent.removeCategory(
                Intent.CATEGORY_LEANBACK_LAUNCHER);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            results = pm.queryIntentActivities(intent, 0);
        }

        Collections.sort(results, (a, b) ->
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(pm).toString(),
                b.loadLabel(pm).toString()));

        installedApps.addAll(results);
        content.removeAllViews();
        addRow("YOUR INSTALLED APPS", 3002);
        addRow("MUSIC LIBRARY", 3003);
    }

    void launch(ResolveInfo info) {
        Intent intent = getPackageManager()
            .getLaunchIntentForPackage(
                info.activityInfo.packageName);

        if (intent != null) {
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this,
                    "Unable to open app",
                    Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (root != null) {
            discoverApps();
        }
    }
            }
