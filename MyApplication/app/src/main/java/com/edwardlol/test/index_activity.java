package com.edwardlol.test;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;


public class index_activity extends ActionBarActivity {

    private Button btnMainActivity,btnSecondActivity;
    private ButtonListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_activity);

        listener = new ButtonListener();

        FindView();

        SetClickListener();

    }
    private void SetClickListener() {
        btnMainActivity.setOnClickListener(listener);
        btnSecondActivity.setOnClickListener(listener);
    }

    private void FindView() {
        btnMainActivity = (Button)findViewById(R.id.btnMainActivity);
        btnSecondActivity = (Button)findViewById(R.id.btnSecondActivity);
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnMainActivity:
                    Intent intent1 = new Intent(index_activity.this, MainActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.btnSecondActivity:
                    Intent intent2 = new Intent(index_activity.this, SecondActivity.class);
                    startActivity(intent2);
                    break;

            }
        }
    }

}
