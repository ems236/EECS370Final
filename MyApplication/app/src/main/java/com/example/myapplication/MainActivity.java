package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Log.d("hitnav", "Navigation item selected()");

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    //mTextMessage.setText("Welf Saupe");
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    protected Drawable getSliderShape(float width, int[] colors ) {
        LinearGradient test;
        test = new LinearGradient(0.f, 0.f, width, 0.0f, colors,
                null, Shader.TileMode.CLAMP);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(test);
        return (Drawable)shape;
    }

    protected Drawable getSliderThumb(int color, int width) {
        ShapeDrawable th;
        th = new ShapeDrawable(new OvalShape());
        th.setIntrinsicWidth(width);
        th.setIntrinsicHeight(width);
        th.setColorFilter(color, PorterDuff.Mode.SRC_OVER);
        return th;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        mTextMessage.setText("Connection Type: Not Connected");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Click this button to pass data to target activity and
        // then wait for target activity to return result data back.
        Button passDataReturnResultSourceButton = (Button)findViewById(R.id.buttonSelectDevice);
        passDataReturnResultSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                intent.putExtra("message", "This message comes from PassingDataSourceActivity's second button");
                mTextMessage = (TextView) findViewById(R.id.message);
                intent.putExtra(CONNECTION_TYPE, mTextMessage.getText().toString());
                mTextMessage = (TextView) findViewById(R.id.deviceId);
                intent.putExtra("DeviceId", mTextMessage.getText().toString());
                //mTextMessage = (TextView) findViewById(R.id.message);

                startActivityForResult(intent, REQUEST_CODE_1);
            }
        });

        SeekBar seekBarHue = (SeekBar)findViewById(R.id.seekbarHue);
        SeekBar seekBarSat = (SeekBar)findViewById(R.id.seekbarSat);
        SeekBar seekBarVal = (SeekBar)findViewById(R.id.seekbarVal);
        //int  x1 = seekBarHue.getMeasuredWidth(); //returns 0
        int[] hueColors = new int[] { 0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF,
                0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF};
        seekBarHue.setProgressDrawable(getSliderShape(900, hueColors));
        seekBarVal.setProgressDrawable(getSliderShape(900, new int[] { 0xFFFFFFFF, 0xFFFFFF00}));
        seekBarSat.setProgressDrawable(getSliderShape(900, new int[] { 0xFF000000, 0xFFFFFFFF}));

        seekBarHue.setThumb(getSliderThumb(Color.GREEN, 100));
        seekBarSat.setThumb(getSliderThumb(Color.RED, 100));
        seekBarVal.setThumb(getSliderThumb(Color.BLUE, 100));
        final View view= new View(this);

        view.post(new Runnable() {
            @Override
            public void run() {
                int i = view.getHeight(); //height is ready
            }
        });
    }

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String CONNECTION_TYPE = "";

//    /** Called when the user taps the Send button */
//    public void sendMessage(View view) {
//        Log.d("hitsend", "sendMessage()");
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        mTextMessage = (TextView) findViewById(R.id.message);
//        mTextMessage.setText("Connection Type: Not Connected");
//        intent.putExtra(CONNECTION_TYPE, mTextMessage.getText().toString());
//        startActivity(intent);
//    }
    private final static int REQUEST_CODE_1 = 1;
    // This method is invoked when target activity return result data back.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);

        // The returned result data is identified by requestCode.
        // The request code is specified in startActivityForResult(intent, REQUEST_CODE_1); method.
        switch (requestCode)
        {
            // This request code is set by startActivityForResult(intent, REQUEST_CODE_1) method.
            case REQUEST_CODE_1:
                //TextView textView = (TextView)findViewById(R.id.textView);
                if(resultCode == RESULT_OK)
                {
                    String deviceId = dataIntent.getStringExtra("deviceId");
                    String connectionType = dataIntent.getStringExtra("connectionType");
                    String messageReturn = dataIntent.getStringExtra("message_return");
                    mTextMessage = (TextView) findViewById(R.id.deviceId);
                    mTextMessage.setText(deviceId);

                    mTextMessage = (TextView) findViewById(R.id.message);
                    mTextMessage.setText(connectionType);
                    //mTextMessage = (TextView) findViewById(R.id.message);
                    //textView.setText(messageReturn);
                }
        }
    }
}

