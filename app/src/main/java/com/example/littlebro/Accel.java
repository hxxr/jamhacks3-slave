package com.example.littlebro;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/*

Sets up the accelerometer to detect linear acceleration.

USAGE OF THIS CLASS:

// First run this to initialize the class:
Accel.initialize(this);

// Set the Runnable to run when Accel receives new data:
Accel.getInstance().setOnReceive(onAccel);

// Begin receiving data:
Accel.getInstance().begin();

// From within the Runnable query the linear acceleration (m/sec/sec)
private Runnable onAccel = new Runnable() { public void run() {
    ...
    double linear_acceleration = Accel.getInstance().getLinearAcceleration();
    ...
} };

 */

public final class Accel implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;

    // Acceleration due to gravity (vector)
    private double gravity_v[] = new double[3];
    // Linear acceleration (vector)
    private double linear_acceleration_v[] = new double[3];
    // Linear acceleration (overall)
    private double linear_acceleration = 0;
    // Runs on receiving accelerometer data
    private Runnable onReceive;
    // Whether we are receiving accelerometer data or not
    private boolean receiving = false;

    private Accel(Context c) {
        sensorManager = (SensorManager)c.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        onReceive = new Runnable() { public void run() { } };
    }

    // Senses changes in accelerometer data.
    @Override public void onSensorChanged(SensorEvent e) {
        if (receiving) {
            final double alpha = 0.8;
            // Compute acceleration due to gravity.
            gravity_v[0] = alpha*gravity_v[0]+(1-alpha)*e.values[0];
            gravity_v[1] = alpha*gravity_v[1]+(1-alpha)*e.values[1];
            gravity_v[2] = alpha*gravity_v[2]+(1-alpha)*e.values[2];

            // Compute linear acceleration by subtracting above values.
            linear_acceleration_v[0] = e.values[0]-gravity_v[0];
            linear_acceleration_v[1] = e.values[1]-gravity_v[1];
            linear_acceleration_v[2] = e.values[2]-gravity_v[2];

            // Pythagorean theorem calculation of acceleration vector magnitude.
            linear_acceleration = Math.sqrt(
                    Math.pow(linear_acceleration_v[0], 2)
                            + Math.pow(linear_acceleration_v[1], 2)
                            + Math.pow(linear_acceleration_v[2], 2)
            );

            onReceive.run();
        }
    }




    // Get current linear acceleration.
    public double getLinearAcceleration() { return linear_acceleration; }

    // Enable.
    public void begin() { receiving = true; }

    // Disable.
    public void cease() { receiving = false; }

    // Set onReceive.
    public void setOnReceive(Runnable R) { onReceive = R; }




    // Singleton stuff.
    private static Accel i = null;
    public static synchronized Accel getInstance() { return i; }
    public static synchronized void initialize(Context c) { i = new Accel(c); }

    // Doesn't do anything.
    @Override public void onAccuracyChanged(Sensor s, int accuracy) { }
}