package com.game.tim.theverthingame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class startScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }
    public void startGame(View view){
        Intent intent = new Intent(this, GameScreen.class);

        EditText t1name = (EditText) findViewById(R.id.team1Name);
        String message = t1name.getText().toString();
        intent.putExtra("team1Name", message);

        if(TextUtils.isEmpty(message)) {
            t1name.setError("This field can't be blank");
            return;
        }

        EditText t2name = (EditText) findViewById(R.id.team2Name);
        String message2 = t2name.getText().toString();
        intent.putExtra("team2Name", message2);

        if(TextUtils.isEmpty(message2)) {
            t2name.setError("This field can't be blank");
            return;
        }

        EditText numOfCards = (EditText) findViewById(R.id.totalCards);
        String message3 = numOfCards.getText().toString();
        intent.putExtra("totalCards", message3);

        if(TextUtils.isEmpty(message3)) {
            numOfCards.setError("This field can't be blank");
            return;
        }

        startActivity(intent);
    }
}
