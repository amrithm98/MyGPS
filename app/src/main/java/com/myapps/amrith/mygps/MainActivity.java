package com.myapps.amrith.mygps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button button;
    private TextView textView;
    DBhelper mydb;
    Button viewb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new DBhelper(this);
        button = (Button) findViewById(R.id.button);
        viewb = (Button) findViewById(R.id.button3);
        textView = (TextView) findViewById(R.id.textView3);
        viewdat();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                textView.setText("");
                textView.append("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                TextView add = (TextView) findViewById(R.id.textView4);

                try {

                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    final String address = addresses.get(0).getAddressLine(0);
                    final String city = addresses.get(0).getLocality();
                    final String state = addresses.get(0).getAdminArea();
                    final String country = addresses.get(0).getCountryName();
                    final String postalCode = addresses.get(0).getPostalCode();
                    final String knownName = addresses.get(0).getFeatureName();
                    final String spa = addresses.get(0).getSubLocality();
                    final String subtf = addresses.get(0).getSubThoroughfare();
                    final String subadmin = addresses.get(0).getSubAdminArea();
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    final String formattedDate = df.format(c.getTime());
                    add.setText("");
                    add.setText("Address: " + address + "\nCity: " + city + "\nState:" + state + "\nCountry:" + country + "\nPostal Code:" + postalCode + "\nSub Locality:" + spa + "\nSub Admin Area:" + subadmin + "\nLoc:" + subtf);
                    Button save = (Button) findViewById(R.id.button2);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isins = mydb.insertData(formattedDate, address, city, state, country, postalCode, spa);
                            if (isins = true)
                                Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(MainActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }


            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 1);
        } else {
            getloc();
        }
    }

    public void viewdat() {
        viewb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = mydb.getdata();
                if (cursor.getCount() == 0) {
                    showmsg("Error", "Nothing Found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (cursor.moveToNext()) {
                    buffer.append("Time:" + cursor.getString(0) + "\n");
                    buffer.append("Address:" + cursor.getString(1) + "\n");
                    buffer.append("City:" + cursor.getString(2) + "\n");
                    buffer.append("State:" + cursor.getString(3) + "\n");
                    buffer.append("Country:" + cursor.getString(4) + "\n");
                    buffer.append("PostalCode" + cursor.getString(5) + "\n");
                    buffer.append("SubLocality:" + cursor.getString(6) + "\n\n");
                }
                showmsg("Data", buffer.toString());
            }
        });
    }

    public void showmsg(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getloc();
                }
        }
    }

    public void getloc() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, locationListener);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
