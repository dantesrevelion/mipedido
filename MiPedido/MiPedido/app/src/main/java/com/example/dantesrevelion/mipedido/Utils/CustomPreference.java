package com.example.dantesrevelion.mipedido.Utils;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class CustomPreference extends PreferenceCategory {
    public CustomPreference (Context context) {
        super(context);
    }

    public CustomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreference(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTextColor(Color.BLACK);
    }
}