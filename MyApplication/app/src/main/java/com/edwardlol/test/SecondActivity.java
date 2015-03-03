package com.edwardlol.test;

import android.app.Activity;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by edwardlol on 15/2/18.
 */
public class SecondActivity extends Activity {

    private RatingBar rtbMyScore;
    private SeekBar skbChinaScore;
    private TextView tvChinaScore;

    private ImageButton ibTimeGoing;
    private ProgressBar pbTimeWaiting;

    private DatePicker dpPicker;
    private TimePicker tpPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        rtbMyScore = (RatingBar) findViewById(R.id.rtbMyScore);
        skbChinaScore = (SeekBar) findViewById(R.id.skbChinaScore);
        tvChinaScore = (TextView) findViewById(R.id.tvChinaScore);

        ibTimeGoing = (ImageButton) findViewById(R.id.ibTimeGoing);
        pbTimeWaiting = (ProgressBar) findViewById(R.id.pbTimeWaiting);

        dpPicker = (DatePicker) findViewById(R.id.dpPicker);
        tpPicker = (TimePicker) findViewById(R.id.tpPicker);

        rtbMyScore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float score, boolean b) {
                Toast.makeText(SecondActivity.this, "your self score is:" + score, Toast.LENGTH_SHORT).show();
            }
        });

        skbChinaScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int score, boolean b) {
                tvChinaScore.setText("the progress of the GREAT revivification of China is " + score + "%");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        ibTimeGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbTimeWaiting.setVisibility(View.VISIBLE);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                Toast.makeText(SecondActivity.this, "hehehe", Toast.LENGTH_SHORT).show();
            }
        });

        dpPicker.setCalendarViewShown(false);
        dpPicker.init(2015, 1, 18, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                Toast.makeText(SecondActivity.this, "the date u pick is " + year + "-" + (month+1) + "-" + day, Toast.LENGTH_SHORT).show();
            }
        });

        tpPicker.setIs24HourView(true);
        tpPicker.setCurrentHour(22);
        tpPicker.setCurrentMinute(38);
        tpPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker timePicker, int i, int i2) {
                Toast.makeText(SecondActivity.this, "your time: " + i + "-" + i2, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
