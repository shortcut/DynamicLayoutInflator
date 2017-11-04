package com.nickandjerry.dynamiclayoutinflator.lib.util;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nick on 8/10/15.
 * <p>
 * Taken from http://stackoverflow.com/questions/8343971/how-to-parse-a-dimension-string-and-convert-it-to-a-dimension-value
 */
public class Dimensions {

    private static final ValueMapper<Integer> UNITS = new ValueMapper<Integer>("unit")
            .map("px", TypedValue.COMPLEX_UNIT_PX)
            .map("dip", TypedValue.COMPLEX_UNIT_DIP)
            .map("dp", TypedValue.COMPLEX_UNIT_DIP)
            .map("sp", TypedValue.COMPLEX_UNIT_SP)
            .map("pt", TypedValue.COMPLEX_UNIT_PT)
            .map("in", TypedValue.COMPLEX_UNIT_IN)
            .map("mm", TypedValue.COMPLEX_UNIT_MM);


    private static final Pattern DIMENSION_PATTERN = Pattern.compile("([0-9.]+)([a-zA-Z]*)");

    public static int parseToPixel(String dimension, DisplayMetrics metrics, ViewGroup parent, boolean horizontal) {
        if (dimension.endsWith("%")) {
            float pct = Float.parseFloat(dimension.substring(0, dimension.length() - 1)) / 100.0f;
            return (int) (pct * (horizontal ? parent.getMeasuredWidth() : parent.getMeasuredHeight()));
        }
        return parseToIntPixel(dimension, metrics);
    }

    public static float parseToPixel(String dimension, View view) {
        return parseToPixel(dimension, view.getResources().getDisplayMetrics());
    }

    public static float parseToPixel(String dimension, DisplayMetrics metrics) {
        Matcher m = DIMENSION_PATTERN.matcher(dimension);
        if (!m.matches()) {
            throw new InflateException("dimension cannot be resolved: " + dimension);
        }
        int unit = m.groupCount() == 2 ? UNITS.get(m.group(2)) : TypedValue.COMPLEX_UNIT_PX;
        float value = Integer.valueOf(m.group(1));
        return TypedValue.applyDimension(unit, value, metrics);
    }

    public static int parseToIntPixel(String value, View view) {
        return Math.round(parseToPixel(value, view));
    }

    public static int parseToIntPixel(String value, DisplayMetrics metrics) {
        return Math.round(parseToPixel(value, metrics));
    }
}




