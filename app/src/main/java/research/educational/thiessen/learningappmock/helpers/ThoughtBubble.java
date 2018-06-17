package research.educational.thiessen.learningappmock.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
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
public class ThoughtBubble extends RelativeLayout {
    private ImageView bubble;
    private ImageView food;

    public ThoughtBubble(Context context) {
        super(context);
        init();
    }

    public ThoughtBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        int src_resource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
        if (src_resource != 0) {
            food.setImageDrawable(ResourcesCompat.getDrawable(getResources(), src_resource, null));
        }

        if (this.getScaleX() == -1.0) {
            food.setScaleX(-1);
        }
        if (this.getScaleY() == -1.0) {
            food.setScaleY(-1);
        }



    }

    public ThoughtBubble(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.thoughtbubble, this);
        this.bubble = findViewById(R.id.actualthoughtbubble);
        this.food = findViewById(R.id.foodthought);
    }
}