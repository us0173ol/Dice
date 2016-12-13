package com.bignerdranch.android.dice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    String TAG = "Main";

    private Button btnRoll;
    private TextView tvResult;
    private TextView tvResult2;
    private TextView finalResult;
    private TextView firstRollTV;
    private int firstRoll;
    private int rollAgain;

    enum State {
        BEFORE, FIRST_ROLL, SECOND_OR_LATER_ROLL, WON, LOST;
    }

//    final int BEFORE = 0;
//    final int FIRST_ROLL = 1;
//    final int SECOND_OR_LATER_ROLL = 2;
//    final int WON = 3;


    State stateOfGame;

    int diceTotal = 0;

    private boolean firstTurnHappened = false;
    private boolean wonLost = false;

    Sensor accelerometer;
    SensorManager sm;
    TextView acceleration;

    ArrayList<Integer> initalRollWinVals;
    ArrayList<Integer> initalRollLoseVals;


    /*
    * rules
    *
    * roll dice. If 2, 3, 12 lose
    * if 7, 11 then win
    *
    * else
    * roll dice until one of the following....
    * if get original roll value then win
    * if roll 7 then lose
    *
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateOfGame = State.BEFORE;

        initalRollLoseVals = new ArrayList<>();
        initalRollLoseVals.add(2);
        initalRollLoseVals.add(3);
        initalRollLoseVals.add(12);

        initalRollWinVals = new ArrayList<>();
        initalRollWinVals.add(7);
        initalRollWinVals.add(11);



        btnRoll = (Button) findViewById(R.id.rollButton);
        tvResult = (TextView) findViewById(R.id.resultTV);
        tvResult2 = (TextView) findViewById(R.id.reultTV2);
        finalResult = (TextView) findViewById(R.id.finalResultTV);
        firstRollTV = (TextView) findViewById(R.id.firstRollTV);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        acceleration = (TextView) findViewById(R.id.acceleration);

        btnRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                roll();

                ///todo check other states

                //if (stateOfGame == )

            }
        });

    }

//called by pressing button, or (in future) shaking phone.
    private void roll() {
        if (stateOfGame == State.BEFORE) {
            Log.d(TAG, "before first roll, about to roll " + diceTotal);

            firstRoll = rollTheDice();
            stateOfGame = State.FIRST_ROLL;
            Log.d(TAG, "first roll has happened with total " + diceTotal);
            checkState();

        }

        else if (stateOfGame == State.SECOND_OR_LATER_ROLL) {
            Log.d(TAG, "before second (or later) roll with total " + diceTotal + " " + stateOfGame);

            rollTheDice();
            Log.d(TAG, "second+ roll with total " + diceTotal + " " + stateOfGame);

            checkState();

            Log.d(TAG, "second+ after check state " + diceTotal + " " + stateOfGame);

        }

    }



    private void checkState() {

        if (stateOfGame == State.FIRST_ROLL) {
            //check for 2 or 3 or 12 to win//check for 7, 11 to lose

            Log.d(TAG, "check state  - first roll" + diceTotal + " " + stateOfGame);


            if (initalRollWinVals.contains(diceTotal)) {
                //you win
                stateOfGame = State.WON;
                Log.d(TAG, "check state  you win" + stateOfGame);

            } else if (initalRollLoseVals.contains(diceTotal)) {
                //you lose
                stateOfGame = State.LOST;
                Log.d(TAG, "check state  you lose" + stateOfGame);

            } else {
                stateOfGame = State.SECOND_OR_LATER_ROLL;
                Log.d(TAG, "check state keep rolling" + stateOfGame);

            }
        }

        else if (stateOfGame == State.SECOND_OR_LATER_ROLL)  {

            Log.d(TAG, "check state second+" + stateOfGame);

            //roll the same value as firstRoll to win
            //roll the a 7 to lose

            //any other vals, keep rolling.

            Log.d(TAG, "second+ initial roll was " + firstRoll + " this roll was " + diceTotal);


            if (diceTotal == 7) {
                stateOfGame = State.LOST;
                Log.d(TAG, "check state  you lose" + stateOfGame);

            }

            else if (diceTotal == firstRoll) {
                stateOfGame = State.WON;
                Log.d(TAG, "check state second+ you win" + stateOfGame);

            }

            else {
                //carry on with game -- roll again?
                //roll();
                Log.d(TAG, "check state what to do here?" + stateOfGame);

            }
        }

        if (stateOfGame == State.WON) {
            Toast.makeText(this, "YOU win!!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "check state says you win" + stateOfGame);

            reset();
        }

        if (stateOfGame == State.LOST) {
            Log.d(TAG, "check state  you pse " + stateOfGame);
            reset();
        }

    }

    void reset(){
        stateOfGame = State.BEFORE;
        diceTotal = -1;
        firstRoll = -2;   //??

        Log.d(TAG, "reset " + stateOfGame);

    }

//generate two random numbers, update UI, save result.
    public int rollTheDice() {
        Random r1 = new Random();
        Random r2 = new Random();
        int result = r1.nextInt(6) + 1;
        int result2 = r2.nextInt(6) + 1;
        diceTotal = result + result2;
        tvResult.setText(String.valueOf(result));
        tvResult2.setText(String.valueOf(result2));
        return diceTotal;
    }




//        if (firstTurnHappened == false) {
//
////        hello sleep in github.  2417 examples
////            Handler handler = new Handler();
////            handler.postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    done();
////                }
////            }, 3000);
//            determineFirstRoll();
//            winOrLose();
//        }else{
//            rollAgain = result + result2;
//            rollagain();
//
//        }
//    }



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
            wonLost = true;
        } else if (firstRoll == 7 || firstRoll == 11) {
            Toast.makeText(this, "You Win", Toast.LENGTH_SHORT).show();
//            tvResult.setText("Won");
//            tvResult2.setText("Won");
            finalResult.setText("Won");
            firstTurnHappened = false;
            wonLost = true;
        } else {
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

            //rollagain();
        }
    }



    private void rollagain() {
        if (firstTurnHappened == true){


            while (firstRoll > 3 && firstRoll < 7 || firstRoll > 7 && firstRoll < 11) {

                if (rollAgain == firstRoll) {
                    finalResult.setText("You Win");
                    firstTurnHappened = false;
                    wonLost = true;
                } else if (rollAgain == 7) {
                    finalResult.setText("You Lose");
                    firstTurnHappened = false;
                    wonLost = true;
                } else {
                    Toast.makeText(this, "Press Roll to roll again", Toast.LENGTH_SHORT).show();

                }
            }
        }else{
            System.out.println("rollagain (else)");
        }
    }
}
