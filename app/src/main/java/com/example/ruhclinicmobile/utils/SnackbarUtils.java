package com.example.ruhclinicmobile.utils;

import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtils {
    public static void show(Activity activity, String message, boolean isError) {
        if (activity == null) return;

        View view = activity.findViewById(android.R.id.content);
        if (view == null) return;

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        if (isError) {
            snackbar.setBackgroundTint(
                    ContextCompat.getColor(activity, android.R.color.holo_red_dark)
            );
        } else {
            snackbar.setBackgroundTint(
                    ContextCompat.getColor(activity, android.R.color.holo_green_light)
            );
        }

        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
        int marginInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                90,
                activity.getResources().getDisplayMetrics()
        );
        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, marginInPx);
        snackbarView.setLayoutParams(params);

        snackbar.show();
    }
}
