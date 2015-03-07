package net.marcosnavarro.viewflipper.viewflipperexample;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final int SWIPE_MIN_DISTANCE         = 120;
    private static final int SWIPE_MAX_OFF_PATH         = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY   = 200;
    private GestureDetector gestureViewFlipperDetector  = null;
    View.OnTouchListener gestureViewFlipperListener     = null;
    private ViewFlipper viewFlipper                     = null;
    private GestureDetector gestureScrollViewDetector   = null;
    View.OnTouchListener gestureScrollViewListener      = null;
    private ScrollView scrollView                       = null;

    private Button previous                             = null;
    private Button start                                = null;
    private Button next                                 = null;

    private EditText editText                           = null;
    private int milisecondsDefault                      = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText1);

        gestureViewFlipperDetector = new GestureDetector(new GestureDetectorViewFlipper());
        gestureViewFlipperListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureViewFlipperDetector.onTouchEvent(event);
            }
        };
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        viewFlipper.setOnTouchListener(gestureViewFlipperListener);

        gestureScrollViewDetector = new GestureDetector(new GestureDetectorScrollView());
        gestureScrollViewListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureScrollViewDetector.onTouchEvent(event);
            }
        };
        scrollView = (ScrollView) findViewById(R.id.view_scroll);
        scrollView.setOnTouchListener(gestureScrollViewListener);

        previous = (Button) findViewById(R.id.btn_previous);
        start = (Button) findViewById(R.id.btn_start);
        next = (Button) findViewById(R.id.btn_next);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLeft(MainActivity.this, viewFlipper);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!viewFlipper.isFlipping()){
                    start.setText(getString(R.string.pause));
                    String getEditText = editText.getText().toString();
                    if(getEditText.equals("")){
                        viewFlipper.setFlipInterval(milisecondsDefault);
                    }else{
                        viewFlipper.setFlipInterval(Integer.parseInt(getEditText)*1000);
                    }

                    viewFlipper.startFlipping();
                    previous.setEnabled(false);
                    next.setEnabled(false);
                }else{
                    viewFlipper.stopFlipping();
                    start.setText(getString(R.string.start));
                    previous.setEnabled(true);
                    next.setEnabled(true);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveRight(MainActivity.this, viewFlipper);
            }
        });

    }

    class GestureDetectorViewFlipper extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                slide(e1, e2, velocityX, velocityY, viewFlipper);
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }

    class GestureDetectorScrollView extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                slide(e1, e2, velocityX, velocityY, viewFlipper);
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }

    // This method is responsible for making the animation when finger movement is captured
    public void slide(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY, ViewFlipper vfpr){
        if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            moveRight(MainActivity.this, vfpr);
        else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            moveLeft(MainActivity.this, vfpr);
    }

    // Move to the next screen
    public void moveRight(Context c, ViewFlipper vfp){
        vfp.setInAnimation(c, R.anim.in_from_right);
        vfp.setOutAnimation(c, R.anim.out_to_left);
        vfp.showNext();
    }

    // Mover a la previous anterior
    public void moveLeft(Context c, ViewFlipper vfp){
        vfp.setInAnimation(c, R.anim.in_from_left);
        vfp.setOutAnimation(c, R.anim.out_to_right);
        vfp.showPrevious();
    }

    @Override
    public void onClick(View v) {

    }
}
