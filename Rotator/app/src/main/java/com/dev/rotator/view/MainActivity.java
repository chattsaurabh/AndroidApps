package com.dev.rotator.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dev.rotator.HttpModule;
import com.dev.rotator.R;
import com.dev.rotator.model.TimeRs;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnDragListener {

    private LinearLayout pickUpZone;
    private LinearLayout dropZone;
    private View rotor;
    private TextView rotorText;

    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickUpZone = (LinearLayout) findViewById(R.id.pickUpZone);
        dropZone = (LinearLayout) findViewById(R.id.dropZone);
        slideDown(dropZone);
        rotor = findViewById(R.id.rotorContainer);
        rotorText = findViewById(R.id.rotorText);
        rotor.setTag("rotor");

        Animation rotorAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotatate_clockwise);
        rotor.setAnimation(rotorAnimation);

        rotor.setOnLongClickListener(this);
        pickUpZone.setOnDragListener(this);
        dropZone.setOnDragListener(this);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                initTime();
            }
        }, 0,250);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void initTime() {
        HttpModule httpModule = new HttpModule();
        httpModule.getTime(getApplicationContext(), new Callback<TimeRs>() {

            @Override
            public void onSuccess(TimeRs response) {
                updateTime(response);
            }

            @Override
            public void onFailure(VolleyError failure) {

            }
        });
    }

    private void updateTime(final TimeRs response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rotorText.setText(response.dateTime);
            }
        });
    }

    private void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        Animation slideUpAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        view.startAnimation(slideUpAnim);
    }

    private void slideDown(View view) {
        Animation slideDownAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        view.startAnimation(slideDownAnim);
        view.setVisibility(View.GONE);
    }

    @Override
    public boolean onLongClick(View v) {
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);
        v.startDrag(data
                , dragshadow
                , v
                , 0
        );
        slideUp(dropZone);
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    return true;
                }
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                v.getBackground().clearColorFilter();
                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                ClipData.Item item = event.getClipData().getItemAt(0);
                String dragData = item.getText().toString();
                v.getBackground().clearColorFilter();
                v.invalidate();

                View vw = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) vw.getParent();
                owner.removeView(vw);
                LinearLayout container = (LinearLayout) v;
                container.addView(vw);
                vw.setVisibility(View.VISIBLE);
                owner.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                v.getBackground().clearColorFilter();
                v.invalidate();
                return true;
            default:
                break;
        }
        return false;
    }
}
