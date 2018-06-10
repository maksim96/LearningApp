package research.educational.thiessen.learningappmock;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Stack;

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
    private boolean[] nutPlaceOccupied = new boolean[6];
    private int subTask = 0;
    private float defaultNutPos[][] = new float[6][2];
    private int defaultNutSize[] = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task1);

        rootLayout = findViewById(R.id.task1_root);
        bag = findViewById(R.id.bag);

        nuts = new ImageView[6];

        final int[] nutIds = new int[] { R.id.nuts, R.id.nuts2, R.id.nuts3, R.id.nuts4, R.id.nuts5, R.id.nuts6 };
        for (int i = 0; i < nutIds.length; i++) {
            nuts[i] = findViewById(nutIds[i]);
            nuts[i].setOnTouchListener(new ChoiceTouchListener());
        }
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Layout has happened here.
                        for (int i = 0; i < nutIds.length; i++) {
                            nuts[i] = findViewById(nutIds[i]);
                            nuts[i].setOnTouchListener(new ChoiceTouchListener());
                            defaultNutPos[i][0] = nuts[i].getX();
                            defaultNutPos[i][1] = nuts[i].getY();
                        }
                        defaultNutSize[0] = nuts[0].getWidth();
                        defaultNutSize[1] = nuts[1].getHeight();
                        // Don't forget to remove your listener when you are done with it.
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });



        ImageView squirrel = findViewById(R.id.squirrel);
        textView = findViewById(R.id.textView);
        textView.setText("Hallo, kannst du mir helfen?");
        bubble = findViewById(R.id.bearBubble);
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

    private final void firstSubTask(View view, boolean firstClicked) {
        bubbleSetVisible(true);
        if (firstClicked) {
            textView.setText("Hilfst du mir 2 Nüsse zu sammeln?");

        }
    }

    private final void secondSubTask(View view, boolean firstClicked) {
        bubbleSetVisible(true);
        if (firstClicked && nutCount != 8) {
            textView.setText("Danke, das waren jetzt 3 x 2 Nüsse! Könntest du mir als nächstes 4 x 2 Nüsse sammeln?");

        } else if (firstClicked && nutCount == 8) {
            Intent intent = new Intent(view.getContext(), Task2.class);
            startActivity(intent);
        } else {
            if (nutCount < 8) {
                textView.setText("Ich brauche ein paar mehr Nüsse");
            } else if (nutCount == 8) {
                textView.setText("Wow! Vielen Dank!");
            } else {
                textView.setText("So viele brauche ich doch gar nicht...");
            }
        }
    }

    private final class TextTouchListener implements View.OnClickListener {
        boolean firstClicked = true;


        @Override
        public void onClick(View view) {
            if (subTask == 0) {
                firstSubTask(view, firstClicked);
            } else {
                secondSubTask(view, firstClicked);
            }


            firstClicked = false;
        }
    }
    private boolean firstTimeSecondSubTask = true;
    private final class ChoiceTouchListener implements View.OnTouchListener {

        private boolean inBag = false;
        private boolean changed = false;
        private int myNutPosition;


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
                            int nutPosition = 0;
                            while(nutPlaceOccupied[nutPosition]) {
                                nutPosition++;
                            }
                            myNutPosition = nutPosition;
                            nutPlaceOccupied[nutPosition] = true;
                            lParams.leftMargin = (int) getPx(870 + (nutPosition)%3*60);
                            lParams.topMargin = (int) getPx(520 + (nutPosition)/3*60);
                            nutCount += 2;

                        } else {
                            lParams.height *= 2;
                            lParams.width *= 2;
                            nutPlaceOccupied[myNutPosition] = false;
                            nutCount -= 2;
                        }
                        System.out.println(subTask);
                        if (subTask < 3) {
                            validateFirstSubTask();
                        } else {
                            secondSubTask(textView, false);
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

    private void restoreNuts() {
        for (int i = 0; i < nuts.length; i++) {

            nuts[i].setOnTouchListener(new ChoiceTouchListener());

         //   nuts[i].setLayoutParams(new RelativeLayout.LayoutParams().LayoutParams(defaultNutSize[0], defaultNutSize[1], ));



            //nuts[i].setLeft((int) defaultNutPos[i][0]);
            //nuts[i].setTop((int) defaultNutPos[i][1]);

            ((RelativeLayout.LayoutParams) nuts[i].getLayoutParams()).width = defaultNutSize[0];
            ((RelativeLayout.LayoutParams) nuts[i].getLayoutParams()).height = defaultNutSize[1];
            ((RelativeLayout.LayoutParams) nuts[i].getLayoutParams()).leftMargin = (int) defaultNutPos[i][0];
            ((RelativeLayout.LayoutParams) nuts[i].getLayoutParams()).topMargin = (int) defaultNutPos[i][1];

            System.out.println(defaultNutPos[i][0] + " " + defaultNutPos[i][1] + " " + defaultNutSize[0] + " " + defaultNutSize[1]);

        }
        nutCount = 0;
        nutPlaceOccupied = new boolean[6];
    }

    private void validateFirstSubTask() {
        if (nutCount == (subTask+1)*2) {
            bubbleSetVisible(true);
            textView.setText("Super! Hilfst du mir nochmal 2 Nüsse zu sammeln?");
            subTask++;
            if (subTask == 3) {
                restoreNuts();
                secondSubTask(textView, firstTimeSecondSubTask);
                firstTimeSecondSubTask = false;
            }

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }

    private float getPx(float dp) {
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }





}
