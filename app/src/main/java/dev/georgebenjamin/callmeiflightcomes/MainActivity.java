package dev.georgebenjamin.callmeiflightcomes;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private PackageManager mPackageManager;
    private ComponentName mComponentName;

    //This holds a reference to the main activity
    private static MainActivity instance;

    public static MainActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPackageManager = getPackageManager();
        mComponentName = new ComponentName(this, PowerReceiver.class);

        instance = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //This activates the Power receiver only when the app is running
        mPackageManager.setComponentEnabledSetting(mComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //This deactivates the power receiver when the app is no longer visible
        mPackageManager.setComponentEnabledSetting(mComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void updateIndicator(final int value){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Value returned from Power receiver => 0 = not charging, 1 = charging
                if(value == 0){
                    Toast.makeText(MainActivity.this, "They have taken light", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this, "They have brought light", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
