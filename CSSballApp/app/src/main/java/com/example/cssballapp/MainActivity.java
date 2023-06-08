package com.example.cssballapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements SensorEventListener2 {

        private float xPos, xAccel, xVel = 0.0f;
        private float yPos, yAccel, yVel = 0.0f;
        private float xMax, yMax;
        private float frameTime = 0.666f;
        private Bitmap ball;
        private SensorManager sensorManager;
        // TextView textview1 = (TextView)findViewById(R.id.textView1);
        //Button button1 = (Button)findViewById(R.id.button1);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // keep portrait mode-> no changes
            BallView ballView = new BallView(this);
            setContentView(ballView);

            //Calculate Boundries
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            xMax = (float)width - 100;
            yMax = (float)height - 100;

            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // use sensorManager for gyro

           /* button1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    updateBall();
                }
            });
            */

        }

        // register listener for sensor
        @Override
        protected void onStart() {
            super.onStart();
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        }

        // unregister listener
        @Override
        protected void onStop() {
            sensorManager.unregisterListener(this);
            super.onStop();
        }

        // not used
        @Override
        public void onFlushCompleted(Sensor sensor) {

        }

        // update entities per frame
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                xAccel = sensorEvent.values[0];
                yAccel = -sensorEvent.values[1];
                updateBall();
            }
        }

        // update ball function to calc new positions
        private void updateBall() {

            // new speed
            xVel += ((xAccel * frameTime) * 0.5);
            yVel += ((yAccel * frameTime) * 0.5);
            // calc dist
            float xS = (xVel / 2) * frameTime;
            float yS = (yVel / 2) * frameTime;
            xPos -= xS;
            yPos -= yS;
            // border x
            if (xPos > xMax) {
                xPos = xMax;
            } else if (xPos < 0) {
                xPos = 0;
            }
            // border y
            if (yPos > yMax) {
                yPos = yMax;
            } else if (yPos < 0) {
                yPos = 0;
            }
        }

        //not used
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

        private class BallView extends View {

            // position ball on custom view
            public BallView(Context context) {
                super(context);
                Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
                final int dstWidth = 100;
                final int dstHeight = 100;
                ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
                setBackgroundColor(0xFF00FFFF);
                //textview1.setText("goal");
            }

            // repeat drawing
            @Override
            protected void onDraw(Canvas canvas) {
                canvas.drawBitmap(ball, xPos, yPos, null);
                invalidate();
            }
        }

    }
