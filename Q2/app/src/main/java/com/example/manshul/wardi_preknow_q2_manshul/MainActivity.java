package com.example.manshul.wardi_preknow_q2_manshul;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor,gyroscopeSensor,rotationVectorSensor,magnetometerSensor;
    private TextView textView;
    private ImageView image;
    private SensorEventListener gyroscopeSensorListener,rvListener,magnetometerSensorListener;
    private final float[] mGyroscopeReading= new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        ///////////////////////////////////////////////////////////////////////
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                /*textView = (TextView) findViewById(R.id.x);
                textView.setText(String.valueOf(sensorEvent.values[1]));*/
                float gyro_y=sensorEvent.values[1];
                textView = (TextView) findViewById(R.id.x);
                /*if(gyro_y<-0.5f || gyro_y>0.5f)
                {
                    textView.setText("See I told you!");
                }
                else
                    textView.setText("Don't you get bored of me");*/
                System.arraycopy(sensorEvent.values,0,mGyroscopeReading,0,mGyroscopeReading.length);
                Log.d("checking:",String.valueOf(mGyroscopeReading));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

// Register the listener
        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

/////////////////////////////magnetometer//////////////////////////////
        /*magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        magnetometerSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //textView = (TextView) findViewById(R.id.x);
                //textView.setText(String.valueOf(sensorEvent.values[0]));
                //System.arraycopy(sensorEvent.values,0,mGyroscopeReading,0,mGyroscopeReading.length);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

// Register the listener
        sensorManager.registerListener(magnetometerSensorListener, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);*/

        //Rotation Vector Sensor
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        rvListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, mGyroscopeReading);
                // Remap coordinate system
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, remappedRotationMatrix);

                // Convert to orientations
                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);
                for(int i = 0; i < 3; i++) {
                    orientations[i] = (float)(Math.toDegrees(orientations[i]));
                }
                textView = (TextView) findViewById(R.id.x);
                textView.setText(String.valueOf(orientations[2]));
                /*if(orientations[1] == 180 || orientations[1] == -180) {
                    textView = (TextView) findViewById(R.id.x);
                    textView.setText("See I told you");
                }*/
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register it
        sensorManager.registerListener(rvListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        textView = (TextView) findViewById(R.id.x);
        //textView.setText(String.valueOf(x));
        //textView = (TextView) findViewById(R.id.y);
        //textView.setText(String.valueOf(y));
        //textView = (TextView) findViewById(R.id.z);
        //textView.setText(String.valueOf(z));

        if (Math.abs(x) > Math.abs(y)) {
            textView.setText("See I told you");
        }
        else{
            textView.setText("Don't you get bored of me");
        }


        //            if (x < 0) {
//                image.setImageResource(R.drawable.right);
//                textView.setText("You tilt the device right");
//            }
//            if (x > 0) {
//                image.setImageResource(R.drawable.left);
//                textView.setText("You tilt the device left");
//            }
//        } else {
//            if (y < 0) {
//                image.setImageResource(R.drawable.up);
//                textView.setText("You tilt the device up");
//            }
//            if (y > 0) {
//                image.setImageResource(R.drawable.down);
//                textView.setText("You tilt the device down");
//            }
//        }
//        if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
//            image.setImageResource(R.drawable.center);
//            textView.setText("Not tilt device");
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister Sensor listener
        sensorManager.unregisterListener(this);
    }
}
