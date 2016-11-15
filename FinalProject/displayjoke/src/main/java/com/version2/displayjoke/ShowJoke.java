package com.version2.displayjoke;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ShowJoke extends AppCompatActivity {

    public static String EXTRA_JOKE = "JOKE";
    TextView jokeTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_joke);

        showJoke();
    }

    private void showJoke() {
        jokeTV = (TextView) findViewById(R.id.joke);
        String joke = getIntent().getStringExtra(EXTRA_JOKE);
        if (joke != null) {
            jokeTV.setText(joke);
        }
    }
}
