package com.bignerdranch.android.dice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Button btnRoll;
    private TextView tvResult;
    private TextView tvResult2;
    private TextView finalResult;
    private TextView firstRollTV;
    private int firstRoll;
    private int rollAgain;
    private boolean firstTurnHappened = false;

    Sensor accelerometer;
    SensorManager sm;
    TextView acceleration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRoll = (Button)findViewById(R.id.rollButton);
        tvResult = (TextView) findViewById(R.id.resultTV);
        tvResult2 = (TextView) findViewById(R.id.reultTV2);
        finalResult = (TextView) findViewById(R.id.finalResultTV);
        firstRollTV = (TextView) findViewById(R.id.firstRollTV);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        acceleration = (TextView) findViewById(R.id.acceleration);



        btnRoll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(firstTurnHappened == false) {
                    rollTheDice();
                }else if(firstTurnHappened == true){
                    rollagain();
                }
            }

        });

    }

    public void rollTheDice() {

            Random r1 = new Random();
            Random r2 = new Random();
            int result = r1.nextInt(6) + 1;
            int result2 = r2.nextInt(6) + 1;
            tvResult.setText(String.valueOf(result));
            tvResult2.setText(String.valueOf(result2));
//        hello sleep in github.  2417 examples
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    done();
//                }
//            }, 3000);
            determineFirstRoll();
            winOrLose();
        }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){


    }

    @Override
    public void onSensorChanged(SensorEvent event){

        acceleration.setText("X: " + event.values[0]+
        "\nY: " + event.values[1]+
        "\nZ: " + event.values[2]);

        if (event.values[0]>9 ){
            rollTheDice();
            //Handler handler = new Handler();
            //handler.postDelayed(new Runnable() {
                //@Override
                //public void run() {
                    //done();
               // }
            //}, 5000);
        }

    }
    private void done(){


        Toast.makeText(this, "Rolled the Dice", Toast.LENGTH_LONG).show();
    }

    private void determineFirstRoll(){

        if(firstTurnHappened == false) {
            int dice1 = Integer.parseInt(tvResult.getText().toString());
            int dice2 = Integer.parseInt(tvResult2.getText().toString());
            firstRoll = dice1 + dice2;
            finalResult.setText("Result: " + firstRoll);
            firstRollTV.setText("Target: " + firstRoll);
        }else{
            System.out.println("determine first roll (else)");
        }
    }
    private void winOrLose() {

        if (firstRoll == 2 || firstRoll == 3 || firstRoll == 12) {
            Toast.makeText(this, "You Lose", Toast.LENGTH_SHORT).show();
//            tvResult.setText("Lost");
//            tvResult2.setText("Lost");
            finalResult.setText("Lost");
            firstTurnHappened = false;
        } else if (firstRoll == 7 || firstRoll == 11) {
            Toast.makeText(this, "You Win", Toast.LENGTH_SHORT).show();
//            tvResult.setText("Won");
//            tvResult2.setText("Won");
            finalResult.setText("Won");
            firstTurnHappened = false;
        } else{
            firstTurnHappened = true;
            //while (firstRoll > 3 && firstRoll < 7 || firstRoll > 7 && firstRoll < 11) {

//                if (rollAgain == firstRoll) {
//                    finalResult.setText("You Win");
//                    firstTurnHappened = false;
//                } else if (rollAgain == 7) {
//                    finalResult.setText("You Lose");
//                    firstTurnHappened = false;
//                } else {
                    Toast.makeText(this, "Press Roll to roll again", Toast.LENGTH_SHORT).show();
                }
            }



    private void rollagain() {
        if (firstTurnHappened == true){
            Random r1 = new Random();
            Random r2 = new Random();
            int result = r1.nextInt(6) + 1;
            int result2 = r2.nextInt(6) + 1;
            tvResult.setText(String.valueOf(result));
            tvResult2.setText(String.valueOf(result2));
            rollAgain = result + result2;
            finalResult.setText("Result: " + rollAgain);

            while (firstRoll > 3 && firstRoll < 7 || firstRoll > 7 && firstRoll < 11) {

                if (rollAgain == firstRoll) {
                    finalResult.setText("You Win");
                    firstTurnHappened = false;
                } else if (rollAgain == 7) {
                    finalResult.setText("You Lose");
                    firstTurnHappened = false;
                } else {
                    Toast.makeText(this, "Press Roll to roll again", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            System.out.println("rollagain (else)");
        }
    }


}
