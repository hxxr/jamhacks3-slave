package com.example.littlebro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MessageListener {
    Button b1;
    Button b3;
    Button b5;
    Button b6;
    Button blocation;
    EditText et2;
    EditText et3;
    class_info[] parent_list;
    TextView status_update;
    public int startmin=0, starthour=12, endmin=0, endhour=13;
    public boolean excepted = false;
    final Context c = this;
    private SmsManager smsManager;
    private String telephone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        child_info();
        /*
        b1=findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child_info();            }
        });*/
    }

    private void child_info() {
        setContentView(R.layout.child_info);

        et2=findViewById(R.id.editText2);
        et3=findViewById(R.id.editText3);
        /*et4=(EditText)findViewById(R.id.editText4);*/

        b3 = findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                telephone = "+1"+et2.getText().toString();
                child_status();
            }
        });
        /*
        b4=(Button)findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.child_status);
            }
        });
        b5=(Button)findViewById(R.id.button5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.child_chat);
            }
        });
        b6=(Button)findViewById(R.id.button6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.number_of_kids);
            }
        });*/
    }

    private void child_status() {
        setContentView(R.layout.child_status);

        smsManager = SmsManager.getDefault();


        Accel.initialize(c);
        Accel.getInstance().setOnReceive(onAccel);
        Accel.getInstance().begin();
    }

    // Length of each accelerometer sample in milliseconds.
    private static final int ACCUMULATOR_THRESHOLD = 2506;
    // Deviation of accelerometer sample must be < this to count as not moving.
    private static final double STILL_THRESHOLD = 0.45;
    // Deviation of accelerometer sample must be >= this to count as walking.
    private static final double WALKING_THRESHOLD_LOWER = 0.45;
    // Deviation of accelerometer sample must be < this to count as walking.
    private static final double WALKING_THRESHOLD_UPPER = 1;
    // Deviation of accelerometer sample must be >= this to count as running.
    private static final double RUNNING_THRESHOLD_LOWER = 1.4;
    // Deviation of accelerometer sample must be < this to count as running.
    private static final double RUNNING_THRESHOLD_UPPER = 2.2;

    private List<Double> sample = new ArrayList<>();
    private long accumulator = System.currentTimeMillis() + ACCUMULATOR_THRESHOLD;
    private double deviation = 0;

    private Runnable onAccel = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() < accumulator)
                sample.add(Accel.getInstance().getLinearAcceleration());
            else {
                deviation = Cruncher.deviation(sample);
                sample = new ArrayList<>();
                accumulator = System.currentTimeMillis() + ACCUMULATOR_THRESHOLD;
                if (deviation < STILL_THRESHOLD)
                    smsManager.sendTextMessage(telephone, null, "*****STATIONARY*****", null, null);
                else if (deviation >= WALKING_THRESHOLD_LOWER && deviation < WALKING_THRESHOLD_UPPER)
                    smsManager.sendTextMessage(telephone, null, "*****WALKING*****", null, null);
                else if (deviation >= RUNNING_THRESHOLD_LOWER && deviation < RUNNING_THRESHOLD_UPPER)
                    smsManager.sendTextMessage(telephone, null, "*****RUNNING*****", null, null);
                else
                    smsManager.sendTextMessage(telephone, null, "*****OTHER*****", null, null);
            }
        }
    };

    @Override
    public void messageReceived(String message) {
        if (message.substring(0,5).equals("*****")) {
            Tracker.initialize(c);
            Tracker.getInstance().setOnReceive(onTrack);
            Tracker.getInstance().begin();
        }
    }

    private Runnable onTrack = new Runnable() {
        @Override
        public void run() {
            smsManager.sendTextMessage(telephone, null, "*****"+Tracker.getInstance().getLatitude()+" | "+Tracker.getInstance().getLongitude()+"*****", null, null);
        }
    };
}
