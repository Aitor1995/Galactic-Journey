package com.aitor1995.galactic_journey.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.aitor1995.galactic_journey.R;

public class AcercaDePreference extends DialogPreference {
    public AcercaDePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
    }

    @Override
    protected View onCreateDialogView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.acerca_de_dialog, null);
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/acercade.html");
        return view;
    }
}
