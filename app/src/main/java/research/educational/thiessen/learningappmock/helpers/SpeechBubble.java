package research.educational.thiessen.learningappmock.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import research.educational.thiessen.learningappmock.R;

import static android.content.ContentValues.TAG;

/**
 * TODO: document your custom view class.
 */
public class SpeechBubble extends RelativeLayout {
    private TextView text;
    private ImageView bubble;
    private TextView dots;
    private TextView bottom;
    private boolean animateDots = false;

    public boolean isAnimateDots() {
        return animateDots;
    }

    private Handler handler = new Handler();
    private Runnable dotAnimator;
    public void setAnimateDots(boolean animateDots) {
        this.animateDots = animateDots;
        if (animateDots) {
            //dots.setTextSize(text.getTextSize());
            handler.postDelayed(dotAnimator, 500);
            dots.setVisibility(VISIBLE);
            if (this.getScaleY() == -1.0) {
                findViewById(R.id.textviewUpper).setVisibility(GONE);
            }
        } else {
            handler.removeCallbacks(dotAnimator);
            dots.setVisibility(GONE);
            if (this.getScaleY() == -1.0) {
                findViewById(R.id.textviewUpper).setVisibility(VISIBLE);
            }
        }
    }

    public void setText(CharSequence cs) {
        text.setText(cs);
        setAnimateDots(false);
    }

    public CharSequence getText() {
        return text.getText();
    }

    public SpeechBubble(Context context) {
        super(context);
        init();
    }

    public SpeechBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        int[] set = {
                android.R.attr.background, // idx 0
                android.R.attr.text        // idx 1
        };
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, set);
        Drawable d = a.getDrawable(0);
        CharSequence t = a.getText(1);
        if (t!= null) {
            text.setText(t);
        }

        if (this.getScaleX() == -1.0) {
            text.setScaleX(-1);
            dots.setScaleX(-1);
        }
        if (this.getScaleY() == -1.0) {
            text.setScaleY(-1);
            dots.setVisibility(GONE);
            dots = findViewById(R.id.dotsinbubble_upper);
            dots.setVisibility(VISIBLE);
            findViewById(R.id.textviewUpper).setVisibility(GONE);
        }

        setAnimateDots(false);


        Log.d(TAG, "attrs " + d + " " + t);
        a.recycle();
    }

    public SpeechBubble(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.speechbubble, this);
        this.bubble = findViewById(R.id.actualbubble);
        this.text = findViewById(R.id.bubbleText);
        this.dots = findViewById(R.id.dotsinbubble);
        this.bottom = findViewById(R.id.textViewBottom);

        dotAnimator = new Runnable() {
            @Override
            public void run() {
                if (dots.getText().toString().equals("...")) {
                    dots.setText("   ");
                } else if (dots.getText().toString().equals(".. ")) {
                    dots.setText("...");
                } else if (dots.getText().toString().equals(".  ")) {
                    dots.setText(".. ");
                }else if (dots.getText().toString().equals("   ")) {
                    dots.setText(".  ");
                }
                handler.removeCallbacks(this);
                handler.postDelayed(this, 500);
            }
        };
    }
}