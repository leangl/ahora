package com.lanacion.ahora.util;

/**
 * Created by ignacios on 1/16/15.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Fonts {

    private static Map<String, Typeface> fonts = new HashMap<String, Typeface>();

    public static Typeface getFont(Context context, String fontName) {
        Typeface typeface = fonts.get(fontName);
        if (typeface != null) {
            return typeface;
        } else {
            try {
                Typeface newFont = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
                fonts.put(fontName, newFont);
                return newFont;
            } catch (Exception e) {
                Log.e("Fonts", "Font not found.", e);
                return null;
            }
        }
    }

}

