package com.game.tim.theverthingame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;

public class GameScreen extends AppCompatActivity {
    boolean currentTeam  = true; //true is team1 false is team 2
    int     currentRound = 0;    //the rounds of the game
    int[]   team1Scores = new int[3];
    int[]   team2Scores = new int[3];
    int     team1Total, team2Total = 0;
    boolean rndTimerFlag = false;
    boolean mtCanceled   = false;
    CountDownTimer mainTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        Intent intent = getIntent();

        final Button penBtn = (Button) findViewById(R.id.penaltyBtn);
        assert penBtn != null;
        final Button addPoint = (Button) findViewById(R.id.addPointBtn);
        assert addPoint != null;
        final Button rmvPoint = (Button) findViewById(R.id.rmvPointBtn);
        assert rmvPoint != null;

        String team1Name = intent.getStringExtra("team1Name");
        TextView t1n = (TextView)findViewById(R.id.team1TV);
        t1n.setText(team1Name);

        String team2Name = intent.getStringExtra("team2Name");
        TextView t2n = (TextView)findViewById(R.id.team2TV);
        t2n.setText(team2Name);

        String tc = intent.getStringExtra("totalCards");
        final int totalCards = Integer.parseInt(tc);


        final TextView timerTV = (TextView)findViewById(R.id.timerTV);
        mainTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTV.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timerFinish();
            }
        };

        final Button button = (Button) findViewById(R.id.timerButton);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainTimer.start();
                rmvPoint.setEnabled(true);
                addPoint.setEnabled(true);
                penBtn.setEnabled(true);
            }
        });


        penBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                TextView curTime = (TextView) findViewById(R.id.timerTV);
                long currTime = Long.parseLong(curTime.getText().toString())*1000;
                mainTimer.cancel();
                if(currTime > 10000) {
                    mainTimer = new CountDownTimer((currTime - 10000), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            timerTV.setText("" + millisUntilFinished / 1000);

                        }

                        @Override
                        public void onFinish() {
                            timerFinish();
                            button.setVisibility(View.VISIBLE);
                        }
                    }.start();
                }
                else{
                    mainTimer = new CountDownTimer(1000 , 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            timerTV.setText("" + millisUntilFinished / 1000);

                        }

                        @Override
                        public void onFinish() {
                            timerFinish();
                            button.setVisibility(View.VISIBLE);
                        }
                    }.start();
                }
            }
        });



        final Button reBtn = (Button) findViewById(R.id.eesumeTime);
        assert reBtn != null;


        addPoint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(currentTeam){
                    team1Scores[currentRound] +=1;
                    int resId = getResources().getIdentifier("team1rnd" + Integer.toString(currentRound+1), "id", getPackageName());
                    TextView point = (TextView) findViewById(resId);
                    assert point != null;
                    point.setText(Integer.toString(team1Scores[currentRound]));
                    team1Total +=1;
                    TextView t1Total = (TextView) findViewById(R.id.team1tot);
                    t1Total.setText(Integer.toString(team1Total));
                }
                else {
                    team2Scores[currentRound] +=1;
                    int resId = getResources().getIdentifier("team2rnd" + Integer.toString(currentRound+1), "id", getPackageName());
                    TextView point = (TextView) findViewById(resId);
                    assert point != null;
                    point.setText(Integer.toString(team2Scores[currentRound]));
                    team2Total +=1;
                    TextView t2Total = (TextView)findViewById(R.id.team2Total);
                    t2Total.setText(Integer.toString(team2Total));
                }
                if( team1Scores[currentRound]+team2Scores[currentRound] >= totalCards){
                    if(currentRound==2){
                        gameEnd();
                    }
                    currentRound++;
                    changeRoundText();
                    TextView curTime = (TextView) findViewById(R.id.timerTV);
                    long currTime = Long.parseLong(curTime.getText().toString())*1000;
                    rmvPoint.setEnabled(false);
                    addPoint.setEnabled(false);
                    penBtn.setEnabled(false);
                    mainTimer.cancel();
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext() ,R.raw.roundend);
                    mediaPlayer.start(); // no need to call prepare(); create() does that for you

                    mainTimer = new CountDownTimer(currTime, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            timerTV.setText("" + millisUntilFinished / 1000);
                        }

                        @Override
                        public void onFinish() {
                            timerFinish();
                            button.setVisibility(View.VISIBLE);
                        }
                    };

                    button.setVisibility(View.INVISIBLE);
                    reBtn.setVisibility(View.VISIBLE);
                    reBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            mainTimer.start();
                            reBtn.setVisibility(View.INVISIBLE);
                            rmvPoint.setEnabled(true);
                            addPoint.setEnabled(true);
                            penBtn.setEnabled(true);
                        }
                    });
                }
            }
        });



        rmvPoint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(currentTeam){
                    if(team1Scores[currentRound] > 0 ) {
                        team1Scores[currentRound] -= 1;
                        int resId = getResources().getIdentifier("team1rnd" + Integer.toString(currentRound + 1), "id", getPackageName());
                        TextView point = (TextView) findViewById(resId);
                        assert point != null;
                        point.setText(Integer.toString(team1Scores[currentRound]));
                        team1Total -=1;
                        TextView t1Total = (TextView)findViewById(R.id.team1tot);
                        t1Total.setText(Integer.toString(team1Total));
                    }
                }
                else {
                    if(team2Scores[currentRound] > 0 ) {
                        team2Scores[currentRound] -= 1;
                        int resId = getResources().getIdentifier("team2rnd" + Integer.toString(currentRound + 1), "id", getPackageName());
                        TextView point = (TextView) findViewById(resId);
                        assert point != null;
                        point.setText(Integer.toString(team2Scores[currentRound]));

                        team2Total +=1;
                        TextView t2Total = (TextView)findViewById(R.id.team2Total);
                        t2Total.setText(Integer.toString(team2Total));
                    }
                }

            }
        });

        rmvPoint.setEnabled(false);
        addPoint.setEnabled(false);
        penBtn.setEnabled(false);

    }

    @Override
    public void onBackPressed() {

        doExit();
    }
    private void doExit() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                GameScreen.this);

        alertDialog.setPositiveButton("Yes", new  DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                mainTimer.cancel();
            }
        });

        alertDialog.setNegativeButton("No", null);

        alertDialog.setMessage("Are you sure you want to go back? (this will end the current game)");
        alertDialog.setTitle("The Everything Game");
        alertDialog.show();
    }
    private void timerFinish(){
        final TextView timerTV = (TextView)findViewById(R.id.timerTV);
        assert timerTV != null;
        final Button penBtn = (Button) findViewById(R.id.penaltyBtn);
        assert penBtn != null;
        final Button addPoint = (Button) findViewById(R.id.addPointBtn);
        assert addPoint != null;
        final Button rmvPoint = (Button) findViewById(R.id.rmvPointBtn);
        assert rmvPoint != null;
        rmvPoint.setEnabled(false);
        addPoint.setEnabled(false);
        penBtn.setEnabled(false);
        timerTV.setText("Time!");
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext() ,R.raw.timeup);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
        //mainTimer.cancel();
        mainTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTV.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timerFinish();
            }
        };

        if (currentTeam){
            TextView teamColor = (TextView)findViewById(R.id.team2TV);
            teamColor.setTextColor(getResources().getColor(R.color.teamGreen));
            TextView teamSwapColor = (TextView)findViewById(R.id.team1TV);
            teamSwapColor.setTextColor(getResources().getColor(R.color.teamNotGreen));
        }
        else {
            TextView teamColor = (TextView)findViewById(R.id.team1TV);
            teamColor.setTextColor(getResources().getColor(R.color.teamGreen));
            TextView teamSwapColor = (TextView)findViewById(R.id.team2TV);
            teamSwapColor.setTextColor(getResources().getColor(R.color.teamNotGreen));
        }
        currentTeam = !currentTeam;
    }

    private void gameEnd(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                GameScreen.this);

        alertDialog.setPositiveButton("OK", new  DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        mainTimer.cancel();
        String winTeam ="";
        if (team1Total > team2Total){
            TextView t1 = (TextView) findViewById(R.id.team1TV);
            winTeam = t1.getText().toString();
        }
        else if(team2Total > team1Total){
            TextView t2 = (TextView) findViewById(R.id.team2TV);
            winTeam = t2.getText().toString();
        }
        else{
            winTeam = "No one, it's a tie!";
        }

        alertDialog.setMessage("The winning team is: " + winTeam + "\n" + team1Total + " - " + team2Total);
        alertDialog.setTitle("Game Over");
        alertDialog.show();
    }
    private void changeRoundText(){
        TextView cr = (TextView) findViewById(R.id.textView);
        if (currentRound == 1){
            cr.setText("Round 2 - Acting");
        }
        else{
            cr.setText("Round 3 - One Word");
        }
    }

}


