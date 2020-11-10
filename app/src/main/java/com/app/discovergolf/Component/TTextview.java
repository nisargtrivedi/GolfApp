package com.app.discovergolf.Component;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by admin on 7/5/2017.
 */

public class TTextview extends AppCompatTextView {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public TTextview(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public TTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, textStyle);
        setTypeface(customFont);
    }

    private Typeface selectTypeface(Context context, int textStyle) {
        /*
        * information about the TextView textStyle:
        * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
        */
        switch (textStyle) {
            case Typeface.BOLD: // bold
                return FontCache.getTypeface("arial_bold.ttf", context);

            case Typeface.ITALIC: // italic
                return FontCache.getTypeface("arial.ttf", context);

            case Typeface.BOLD_ITALIC: // bold italic
                return FontCache.getTypeface("arial.ttf", context);

            case Typeface.NORMAL: // regular
            default:
                return FontCache.getTypeface("arial.ttf", context);
        }
    }
}
