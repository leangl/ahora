package com.lanacion.ahora.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lanacion.ahora.R;
import com.lanacion.ahora.util.Fonts;

/**
 * Created by nano on 05/02/15.
 */
public class CustomFontTextView extends TextView {

    private String fontName;

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFontText);
        fontName = a.getString(R.styleable.CustomFontText_customFont);
        a.recycle();

        if (fontName != null) {
            if (!isInEditMode()) {
                Typeface font = Fonts.getFont(context, fontName);
                if (font != null) {
                    setTypeface(font);
                }
            }
        }

    }

    public CustomFontTextView(Context context) {
        super(context);
    }
}
