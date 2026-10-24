
package com.hardik.tvlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private LinearLayout root;
    private LinearLayout appsRow;
    private final List<ResolveInfo> apps = new ArrayList<>();
    private int focusedIndex = 0;

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(10, 14, 25));
        getWindow().setNavigationBarColor(Color.rgb(10, 14, 25));
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        buildLauncher();
        loadApps();
    }

    private GradientDrawable background(int color, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(16));
        drawable.setStroke(dp(2), strokeColor);
        return drawable;
    }

    private TextView text(String value, int size, int color) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
        return view;
    }

    private void buildLauncher() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(38), dp(24), dp(38), dp(20));
        root.setBackgroundColor(Color.rgb(10, 14, 25));

        setContentView(root);

        TextView brand = text("HARDIK", 30, Color.WHITE);
        brand.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(brand, new LinearLayout.LayoutParams(
                -1, dp(45)));

        TextView subtitle = text(
                "TV LAUNCHER  •  YOUR ENTERTAINMENT",
                13, Color.LTGRAY);
        root.addView(subtitle, new LinearLayout.LayoutParams(
                -1, dp(35)));

        TextView heading = text("Your Apps", 23, Color.WHITE);
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        LinearLayout.LayoutParams headingParams =
                new LinearLayout.LayoutParams(-1, dp(55));
        headingParams.topMargin = dp(20);
        root.addView(heading, headingParams);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);

        appsRow = new LinearLayout(this);
        appsRow.setOrientation(LinearLayout.VERTICAL);
        appsRow.setFocusable(false);

        scroll.addView(appsRow);
        root.addView(scroll, new LinearLayout.LayoutParams(
                -1, 0, 1f));

        TextView footer = text(
                "Use your TV remote to navigate and select apps",
                12, Color.GRAY);
        root.addView(footer, new LinearLayout.LayoutParams(
                -1, dp(35)));
    }

    private void loadApps() {
        apps.clear();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> results = pm.queryIntentActivities(
                intent, 0);

        if (results.isEmpty()) {
            intent.removeCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            results = pm.queryIntentActivities(intent, 0);
        }

        Collections.sort(results, (a, b) ->
                String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(pm).toString(),
                        b.loadLabel(pm).toString()));

        apps.addAll(results);
        appsRow.removeAllViews();

        if (apps.isEmpty()) {
            TextView empty = text(
                    "No launchable apps found.",
                    18, Color.WHITE);
            appsRow.addView(empty);
            return;
        }

        for (int i = 0; i < apps.size(); i++) {
            final int index = i;
            ResolveInfo info = apps.get(i);

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setGravity(Gravity.CENTER_VERTICAL);
            card.setPadding(dp(20), dp(10), dp(20), dp(10));
            card.setFocusable(true);
            card.setFocusableInTouchMode(true);
            card.setBackground(background(
                    Color.rgb(25, 32, 49),
                    Color.TRANSPARENT));

            TextView icon = text("▶", 22, Color.rgb(100, 180, 255));
            card.addView(icon, new LinearLayout.LayoutParams(
                    dp(50), dp(48)));

            TextView name = text(
                    info.loadLabel(pm).toString(),
                    18, Color.WHITE);
            card.addView(name, new LinearLayout.LayoutParams(
                    0, dp(48), 1f));

            LinearLayout.LayoutParams cardParams =
                    new LinearLayout.LayoutParams(-1, dp(76));
            cardParams.bottomMargin = dp(10);
            appsRow.addView(card, cardParams);

            card.setOnFocusChangeListener((v, hasFocus) -> {
                v.setBackground(background(
                        hasFocus
                                ? Color.rgb(40, 70, 110)
                                : Color.rgb(25, 32, 49),
                        hasFocus
                                ? Color.rgb(100, 180, 255)
                                : Color.TRANSPARENT));

                if (hasFocus) {
                    focusedIndex = index;
                }
            });

            card.setOnClickListener(v -> launchApp(index));
        }

        appsRow.post(() -> {
            View first = appsRow.getChildAt(0);
            if (first != null) {
                first.requestFocus();
            }
        });
    }

    private void launchApp(int index) {
        if (index < 0 || index >= apps.size()) return;

        ResolveInfo info = apps.get(index);
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
        } else {
            Toast.makeText(this,
                    "App cannot be launched",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (appsRow != null && appsRow.getChildCount() > 0) {
            appsRow.post(() -> {
                View focused = appsRow.getChildAt(focusedIndex);
                if (focused != null) focused.requestFocus();
            });
        }
    }
    }
