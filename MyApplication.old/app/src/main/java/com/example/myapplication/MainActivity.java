package com.example.myapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.util.Log;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {


    //private TextView mTextMessage;
    private TextView deviceIdTextView;
    private TextView messageTextView;

    private ToggleButton onOffToggle;

    private SeekBar seekBarHue;
    private SeekBar seekBarSat;
    private SeekBar seekBarVal;

    private LinearLayout colorBar;
    protected Drawable getSliderShape(float width, int[] colors ) {
        LinearGradient test;
        test = new LinearGradient(0.f, 0.f, width, 0.0f, colors,
                null, Shader.TileMode.CLAMP);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(test);
        return (Drawable)shape;
    }

    protected Drawable getSliderThumb(int color, int width) {
//        ShapeDrawable th;
//        th = new ShapeDrawable(new OvalShape());
//        th.setIntrinsicWidth(width);
//        th.setIntrinsicHeight(width);
//        th.setColorFilter(color, PorterDuff.Mode.SRC_OVER);
//
//        th.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
//        th.getPaint().setStrokeWidth(2); // in pixel
//        th.getPaint().setColor(Color.BLACK);


        Bitmap bitmap = Bitmap.createBitmap( 100, 100, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        //«bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        Paint circlePaint = new Paint();
        circlePaint.setColor(color);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        strokePaint.setColor(Color.BLACK);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int diameter = width;
        int radius = diameter/2;

        c.drawCircle(diameter / 2 , diameter / 2, radius, strokePaint);

        c.drawCircle(diameter / 2, diameter / 2, radius-4, circlePaint);
// This converts the bitmap to a drawable
        BitmapDrawable mDrawable = new BitmapDrawable(getResources(),bitmap);

        return mDrawable;
        //return th;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceIdTextView = (TextView) findViewById(R.id.deviceId);
        messageTextView = (TextView) findViewById(R.id.message);

        messageTextView.setText("Connection Type: Not Connected");

        // Click this button to pass data to target activity and
        // then wait for target activity to return result data back.
        Button passDataReturnResultSourceButton = (Button)findViewById(R.id.buttonSelectDevice);
        passDataReturnResultSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                intent.putExtra("message", "This message comes from PassingDataSourceActivity's second button");
                intent.putExtra(CONNECTION_TYPE, messageTextView.getText().toString());
                intent.putExtra("DeviceId", deviceIdTextView.getText().toString());

                startActivityForResult(intent, REQUEST_CODE_1);
            }
        });

        seekBarHue = (SeekBar)findViewById(R.id.seekbarHue);
        seekBarSat = (SeekBar)findViewById(R.id.seekbarSat);
        seekBarVal = (SeekBar)findViewById(R.id.seekbarVal);

        colorBar = (LinearLayout)findViewById(R.id.colorbarcontainer);
        //do all three bars at once they are all the same with
        seekBarSat.post(new Runnable() {
            @Override
            public void run() {
                int width = seekBarSat.getWidth(); //height is ready
                int[] hueColors = new int[] { 0xFFFF0000, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF,
                        0xFF0000FF, 0xFFFF00FF, 0xFFFF0000};
                seekBarHue.setProgressDrawable(getSliderShape(width, hueColors));
                seekBarVal.setProgressDrawable(getSliderShape(width, new int[] { 0xFF000000, 0xFFFFFFFF}));

                // update the display
                seekBarChange();
                return;
            }
        });


        seekBarHue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarChange();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("hello", "there");
            }
        });
        seekBarSat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarChange();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("hello", "there");
            }
        });
        seekBarVal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarChange();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("hello", "there");
            }
        });

        onOffToggle = (ToggleButton)findViewById(R.id.toggleOnOff);
        onOffToggle.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b) {
                    Log.d ("togglebutton", "checked");
                    onOffToggle.setTextColor(Color.YELLOW);

                }
                seekBarChange();
            }
        });

    }

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String CONNECTION_TYPE = "";

    public void seekBarChange()
    {
        boolean isOn = onOffToggle.isChecked();

        int width = seekBarSat.getWidth(); //height is ready
        int hue = seekBarHue.getProgress();
        int sat = seekBarSat.getProgress();
        int val = seekBarVal.getProgress();

        int fullcolor = Color.HSVToColor(new float[] {hue * 3.6f, 1.0f, 1.0f} );
        int satcolor = Color.HSVToColor(new float[] {hue * 3.6f, sat / 100f, 1.0f} );
        int gsColor = (255 * val) / 100;
        int valcolor =  Color.rgb(gsColor, gsColor, gsColor);

        seekBarHue.setThumb(getSliderThumb(fullcolor, 100));

        seekBarSat.setThumb(getSliderThumb(fullcolor, 50)); //prevents horizontal bar from getting really tall
        seekBarSat.setProgressDrawable(getSliderShape(width, new int[] { 0xFFFFFFFF, fullcolor}));
        seekBarSat.setThumb(getSliderThumb(satcolor, 100));

        seekBarVal.setThumb(getSliderThumb(valcolor, 100));

        colorBar.setBackgroundColor(satcolor);

        if (isOn) {
            onOffToggle.setTextColor(fullcolor);
            //onOffToggle.setHighlightColor(fullcolor);
        } else {
            onOffToggle.setTextColor(Color.BLACK);
        }
    }

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
                if(resultCode == RESULT_OK)
                {
                    String deviceId = dataIntent.getStringExtra("deviceId");
                    String connectionType = dataIntent.getStringExtra("connectionType");
                    String messageReturn = dataIntent.getStringExtra("message_return");
                    deviceIdTextView.setText(deviceId);
                    messageTextView.setText(connectionType);
                }
        }
    }
}

