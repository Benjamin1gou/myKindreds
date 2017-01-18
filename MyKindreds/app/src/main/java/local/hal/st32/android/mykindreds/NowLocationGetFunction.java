package local.hal.st32.android.mykindreds;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Tester on 2017/01/17.
 */

public class NowLocationGetFunction   {

    private LocationManager locationManager;

    private MainActivity main;

    public NowLocationGetFunction(MainActivity main) {
        this.main = main;
    }



}
