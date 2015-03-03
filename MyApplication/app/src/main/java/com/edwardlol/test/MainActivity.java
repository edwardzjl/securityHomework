package com.edwardlol.test;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.edwardlol.test.CustomContactsHandler;
import java.lang.System;
/*
import android.view.Menu;
import android.view.MenuItem;
*/

public class MainActivity extends ActionBarActivity {

    private Button btnGetMoney, btnLoseMoney, btnSwitch, btnAddContact1, btnAddContact2;
    private TextView tvGetMoney;
    private EditText etGoldMoney;
    private int money = 0;

    private RadioGroup rgCCAVSurvey;
    private CheckBox cbButton1, cbButton2, cbButton3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetMoney = (Button)findViewById(R.id.btnGetMoney);
        btnLoseMoney = (Button)findViewById(R.id.btnLoseMoney);
        tvGetMoney = (TextView)findViewById(R.id.tvGetMoney);
        etGoldMoney = (EditText)findViewById(R.id.etGoldMoney);

        rgCCAVSurvey = (RadioGroup)findViewById(R.id.rgCCAVSurvey);
        cbButton1 = (CheckBox)findViewById(R.id.cbButton1);
        cbButton2 = (CheckBox)findViewById(R.id.cbButton2);
        cbButton3 = (CheckBox)findViewById(R.id.cbButton3);

        btnSwitch = (Button)findViewById(R.id.btnSwitch);

        btnAddContact1 = (Button)findViewById(R.id.btnAddContact1);
        btnAddContact2 = (Button)findViewById(R.id.btnAddContact2);


        CustomPhoneStateListener phoneListener = new CustomPhoneStateListener();
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        btnAddContact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomContactsHandler handler = new CustomContactsHandler();
                handler.AddContacts1();
            }
        });

        btnAddContact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomContactsHandler handler = new CustomContactsHandler();
                handler.AddContacts2();
            }
        });


        btnGetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String strInputMoney = etGoldMoney.getText().toString().trim();
            int iMoney = 0;
            Log.i("edwardlol","clicked");
            if (!strInputMoney.equals("")) {
                iMoney = Integer.parseInt(strInputMoney);
            }
            if (iMoney == money) {
                Toast.makeText(MainActivity.this, "u've achieved ur goal", Toast.LENGTH_SHORT).show();
            } else {
                money++;
                tvGetMoney.setText("haha, ive earned $"+money+" by clicking the btn");
            }
            }
        });

        btnLoseMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (money == 0) {
                Toast.makeText(MainActivity.this, "poor enough, stop clicking!", Toast.LENGTH_SHORT).show();
            } else {
                money--;
                tvGetMoney.setText("haha, ive earned $"+money+" by clicking the btn");
            }

            }
        });


        rgCCAVSurvey.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch (checkedID) {
                    case R.id.rbButton1:
                        Toast.makeText(MainActivity.this, "yoo", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbButton2:
                        Toast.makeText(MainActivity.this, "yoooo", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbButton3:
                        Toast.makeText(MainActivity.this, "YOOOOOO", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        cbButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked){
                if (checked) {
                    Toast.makeText(MainActivity.this, "heheda", Toast.LENGTH_SHORT).show();
                }
            }

        });
        cbButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    Toast.makeText(MainActivity.this, "heheda again", Toast.LENGTH_SHORT).show();
                }
            }

        });
        cbButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    Toast.makeText(MainActivity.this, "heheheheheheda", Toast.LENGTH_SHORT).show();
                }
            }

        });

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

}
