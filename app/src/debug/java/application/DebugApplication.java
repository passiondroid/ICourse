package application;

import com.facebook.stetho.Stetho;
import com.icloud.cronin.peter.application.ICourseApp;

public class DebugApplication extends ICourseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}