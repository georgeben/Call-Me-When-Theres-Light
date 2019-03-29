package dev.georgebenjamin.callmeiflightcomes;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private PackageManager mPackageManager;
    private ComponentName mComponentName;
    private static final int CALL_PHONE_PERMISSION_REQ_CODE = 1;

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

        if(checkForPermission(Manifest.permission.CALL_PHONE)){
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:09060791349")));
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION_REQ_CODE);
        }
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
                    //Make phone call
                    if(checkForPermission(Manifest.permission.CALL_PHONE)){
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:09060791349")));
                    }else{
                        Toast.makeText(MainActivity.this, "Call permission not grated", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private boolean checkForPermission(String permission){
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CALL_PHONE_PERMISSION_REQ_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(MainActivity.this, "Call permission granted", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
