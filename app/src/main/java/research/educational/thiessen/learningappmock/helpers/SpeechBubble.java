package research.educational.thiessen.learningappmock.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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

    public void setText(CharSequence cs) {
        text.setText(cs);
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
    }
}