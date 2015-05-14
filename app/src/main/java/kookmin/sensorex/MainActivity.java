package kookmin.sensorex;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends Activity {

    LocationManager locationManager;
    SensorManager sensorManager;
    Listener listener;

    TextView Location, Acceler, Gyro, Magnetic, Light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Location = (TextView) findViewById(R.id.Location);
        Acceler = (TextView) findViewById(R.id.Acceler);
        Gyro = (TextView) findViewById(R.id.Gyro);
        Magnetic = (TextView) findViewById(R.id.Magnetic);
        Light = (TextView) findViewById(R.id.Light);

        listener = new Listener();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor acceler = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(acceler != null)
            sensorManager.registerListener(listener, acceler, SensorManager.SENSOR_DELAY_UI);
        Sensor gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(gyro != null)
            sensorManager.registerListener(listener, gyro, SensorManager.SENSOR_DELAY_UI);
        Sensor magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(magnetic != null)
            sensorManager.registerListener(listener, magnetic, SensorManager.SENSOR_DELAY_UI);
        Sensor light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(light != null)
            sensorManager.registerListener(listener, light, SensorManager.SENSOR_DELAY_UI);
    }

    class Listener implements LocationListener, SensorEventListener {

        @Override
        public void onLocationChanged(Location location) {
            double Lat = location.getLatitude();
            double Lng = location.getLongitude();

            Location.setText("Location : " + Lat + ", " + Lng);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;

            switch(event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    Acceler.setText("Acceler : X-" + (int)values[0] + ", Y-" + (int)values[1] + ", Z-" + (int)values[2]);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    Gyro.setText("Gyroscope : X-" + (int)values[0] + ", Y-" + (int)values[1] + ", Z-" + (int)values[2]);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    Magnetic.setText("Magnetic : X-" + (int)values[0] + ", Y-" + (int)values[1] + ", Z-" + (int)values[2]);
                    break;
                case Sensor.TYPE_LIGHT:
                    Light.setText("Light : " + (int)values[0]);
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }
        @Override
        public void onProviderEnabled(String provider) { }
        @Override
        public void onProviderDisabled(String provider) { }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        locationManager.removeUpdates(listener);
        sensorManager.unregisterListener(listener);
    }
}
