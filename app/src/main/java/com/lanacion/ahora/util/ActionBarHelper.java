package com.lanacion.ahora.util;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.lanacion.ahora.R;

import java.lang.reflect.Field;

/**
 * Created by Ignacio Saslavsky on 05/02/15.
 * correonano@gmail.com
 */
public class ActionBarHelper {

    public static void setActionBarCustomSettings(Context context, Toolbar toolbar, String font) {
        TextView titleView = getActionBarTextView(toolbar);
        titleView.setTypeface(Fonts.getFont(context, font));
        titleView.setTextColor(context.getResources().getColor(android.R.color.white));
    }

    public static TextView getActionBarTextView(Toolbar toolbar) {
        TextView titleTextView = null;

        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        return titleTextView;
    }

    public static void changeToolbarIcon(Toolbar toolbar) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        }
    }
}
