package research.educational.thiessen.learningappmock;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Task1 extends Activity {

    private ImageView nuts[];
    private ViewGroup rootLayout;
    private ImageView bag;
    private TextView textView;
    private ImageView bubble;
    private int nutCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task1);

        rootLayout = findViewById(R.id.task1_root);
        bag = findViewById(R.id.bag);

        nuts = new ImageView[4];

        int[] nutIds = new int[] { R.id.nuts, R.id.nuts2, R.id.nuts3, R.id.nuts4 };

        for (int i = 0; i < 4; i++) {
            nuts[i] = findViewById(nutIds[i]);
            nuts[i].setOnTouchListener(new ChoiceTouchListener());
        }

        ImageView squirrel = findViewById(R.id.squirrel);
        textView = findViewById(R.id.textView);
        textView.setText("Hallo, kannst du mir helfen?");
        bubble = findViewById(R.id.bubble);
        squirrel.setOnClickListener(new TextTouchListener());


     /*   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getPx(nut.getWidth()), getPx(nut.getHeight()));
        layoutParams.leftMargin = getPx(nut.getX());
        layoutParams.topMargin = getPx(nut.getY());

        nut.setLayoutParams(layoutParams);*/


    }

    private void bubbleSetVisible(boolean visible) {
        if (visible) {
            bubble.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            bubble.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }

    }

    private final class TextTouchListener implements View.OnClickListener {
        boolean firstClicked = true;


        @Override
        public void onClick(View view) {
            bubbleSetVisible(true);
            if (firstClicked && nutCount != 6) {
                firstClicked = false;
                textView.setText("Könntest du mir 6 Nüsse in meinen Beutel tun?");

            } else if (firstClicked && nutCount == 6) {
                Intent intent = new Intent(view.getContext(), Task2.class);
                startActivity(intent);
            } else {
                    if (nutCount < 6) {
                        textView.setText("Ich brauche ein paar mehr Nüsse");
                    } else if (nutCount == 6) {
                        textView.setText("Wow! Vielen Dank!");
                        firstClicked = true;
                    } else {
                        textView.setText("So viele brauche ich doch gar nicht...");
                    }
            }

        }
    }

    private final class ChoiceTouchListener implements View.OnTouchListener {

        private boolean inBag = false;
        private boolean changed = false;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            bubbleSetVisible(false);
            final int x = (int) motionEvent.getRawX();
            final int y = (int) motionEvent.getRawY();
            int xDelta = 0, yDelta = 0;
            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    xDelta = x - lParams.leftMargin;
                    yDelta = y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    int[] firstPosition = new int[2];
                    int[] secondPosition = new int[2];

                    bag.getLocationOnScreen(firstPosition);
                    view.getLocationOnScreen(secondPosition);

                    // Rect constructor parameters: left, top, right, bottom
                    Rect rectFirstView = new Rect(firstPosition[0], firstPosition[1],
                            firstPosition[0] + bag.getMeasuredWidth(), firstPosition[1] + bag.getMeasuredHeight());
                    Rect rectSecondView = new Rect(secondPosition[0], secondPosition[1],
                            secondPosition[0] + view.getMeasuredWidth(), secondPosition[1] + view.getMeasuredHeight());
                    changed = false;
                    if (rectFirstView.intersect(rectSecondView)) {
                        //lParams.rightMargin = getPx(255);
                        //lParams.bottomMargin = getPx(102);
                        //lParams.leftMargin = 2000;
                        //lParams.topMargin = 1400;
                        //lParams.height /= 2;
                        //lParams.width /= 2;
                        if (!inBag) {
                            changed = true;
                        }
                        inBag = true;
                    } else {
                        if (inBag) {
                            changed = true;
                        }
                        inBag = false;

                    }
                    if (changed) {
                        if (inBag) {
                            lParams.height /= 2;
                            lParams.width /= 2;
                            nutCount += 2;
                        } else {
                            lParams.height *= 2;
                            lParams.width *= 2;
                            nutCount -= 2;
                        }
                    }
                    view.setLayoutParams(lParams);
                    break;
                    /*
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;*/
                case MotionEvent.ACTION_MOVE:
                    lParams.leftMargin = x - xDelta;
                    lParams.topMargin = y - yDelta;
                    lParams.rightMargin = -250;
                    lParams.bottomMargin = -250;
                    view.setLayoutParams(lParams);
                    break;
            }
            rootLayout.invalidate();
            return true;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }





}
