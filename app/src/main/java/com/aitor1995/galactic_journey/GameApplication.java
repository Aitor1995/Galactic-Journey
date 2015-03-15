package com.aitor1995.galactic_journey;

import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

@ReportsCrashes(
        formUri = "https://aitor1995.cloudant.com/acra-galactic-journey/_design/acra-storage/_update/report",
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.PUT,
        formUriBasicAuthLogin = "nsittonsisiduchoserescre",
        formUriBasicAuthPassword = "0JiH1K5kqEsnAaGuCgpuNf64"
)
public class GameApplication extends Application {
    @Override
    public void onCreate() {
        ACRA.init(this);
    }
}
