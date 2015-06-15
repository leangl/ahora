package com.lanacion.ahora.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.lanacion.ahora.R;
import com.lanacion.ahora.util.Fonts;


/**
 * Created by ignacios on 1/22/15.
 */
public class CustomFontEditText extends EditText {

    private String fontName;

    public CustomFontEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFontText);
        fontName = a.getString(R.styleable.CustomFontText_customFont);
        a.recycle();

        if (fontName != null) {
            if (!isInEditMode()) {
                setTypeface(Fonts.getFont(context, fontName));
            }
        }

    }

    public CustomFontEditText(Context context) {
        super(context);
    }
}
