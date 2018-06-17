package research.educational.thiessen.learningappmock;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import research.educational.thiessen.learningappmock.helpers.Situation;
import research.educational.thiessen.learningappmock.helpers.SpeechBubble;
import research.educational.thiessen.learningappmock.helpers.ThoughtBubble;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Task1 extends Activity {

    private ImageView nuts[];
    private ViewGroup rootLayout;
    private ImageView bag;
    private SpeechBubble bubble;
    private int nutCount = 0;
    private boolean[] nutPlaceOccupied = new boolean[11];
    private int subTask = 0;
    private float defaultNutPos[][] = new float[11][2];
    private int defaultNutSize[] = new int[2];
    ImageView squirrel;
    Handler handler = new Handler();
    Runnable nutShaker;
    Runnable squirrelShaker;
    private Situation situation = Situation.INTRODUCTION;
    private ThoughtBubble thoughtBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task1);

        rootLayout = findViewById(R.id.task1_root);
        bag = findViewById(R.id.bag);

        nuts = new ImageView[11];

        final int[] nutIds = new int[] { R.id.nuts, R.id.nuts2, R.id.nuts3, R.id.nuts4, R.id.nuts5, R.id.nuts6, R.id.nuts7, R.id.nuts8, R.id.nuts9, R.id.nuts10, R.id.nuts11 };
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
                            System.out.println(nuts[i].getX());
                        }
                        defaultNutSize[0] = nuts[0].getWidth();
                        defaultNutSize[1] = nuts[1].getHeight();
                        // Don't forget to remove your listener when you are done with it.
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

       // rootLayout.setOnClickListener( new TextTouchListener());

        squirrel = findViewById(R.id.squirrel);
        bubble = findViewById(R.id.speechbubble);
        thoughtBubble = findViewById(R.id.thoughtbubble);
        squirrel.setOnClickListener(new TextTouchListener());

        squirrelShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                squirrel.startAnimation(shake);
            }
        };

        nutShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                nuts[0].startAnimation(shake);
            }
        };

        handler.postDelayed(squirrelShaker, 5000);


     /*   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getPx(nut.getWidth()), getPx(nut.getHeight()));
        layoutParams.leftMargin = getPx(nut.getX());
        layoutParams.topMargin = getPx(nut.getY());

        nut.setLayoutParams(layoutParams);*/


    }

    private void bubbleSetVisible(boolean visible) {
        if (visible) {
            bubble.setVisibility(View.VISIBLE);
        } else {
            bubble.setVisibility(View.INVISIBLE);
        }

    }

    private final void firstSubTask(View view, boolean firstClicked) {
        bubbleSetVisible(true);
        thoughtBubble.setVisibility(View.INVISIBLE);
        if (firstClicked) {
            bubble.setText("Hilfst du mir 2 Nüsse zu sammeln?");

            handler.postDelayed(nutShaker, 5000);

        }
    }

    private final int[] nutTasks = {5,4,6,7,8,9,10};
    private int secondSubTaskPart = 0;
    private boolean madeMistake = false;
    private boolean startOfSubtask = true;
    private boolean done = false;
    private final void secondSubTask() {
        bubbleSetVisible(true);
        if (done) {
            Intent intent = new Intent(this, Task2.class);
            startActivity(intent);
        } else if (startOfSubtask) {
            bubble.setText("Könntest du mir als nächstes "
                    + nutTasks[secondSubTaskPart] + " \u00B7 2 Nüsse sammeln?");
            startOfSubtask = false;
            handler.postDelayed(nutShaker, 5000);

        } else {
            if (nutCount != nutTasks[secondSubTaskPart]*2) {
                bubble.setText("Das ist noch nicht ganz richtig.");
                madeMistake = true;
            } else {
                bubble.setText("Richtig! Du hast " + nutTasks[secondSubTaskPart]*2 + " Nüsse in den Beutel getan!" );
                startOfSubtask = true;
                if (nutTasks[secondSubTaskPart] == 10) {
                    done = true;
                } else if (madeMistake) {
                    secondSubTaskPart++;
                } else {
                    if (nutTasks[secondSubTaskPart] == 5) {
                        secondSubTaskPart += 3;
                    } else if (nutTasks[secondSubTaskPart] == 7) {
                        done = true;
                    } else {
                            secondSubTaskPart++;
                    }
                }

                handler.postDelayed(squirrelShaker, 5000);
                restoreNuts();
                madeMistake = false;

            }
        }
    }

    private final class TextTouchListener implements View.OnClickListener {
        boolean firstClicked = true;


        @Override
        public void onClick(View view) {
            handler.removeCallbacks(squirrelShaker);
            if (situation == Situation.INTRODUCTION) {
                intro();
            } else if (situation == Situation.TASK1) {
                    firstSubTask(view, firstClicked);
                firstClicked = false;
            } else {
                secondSubTask();
            }



        }


    }

    int introPart = 0;
    private void intro() {
        bubbleSetVisible(true);
        if (introPart == 0) {
            bubble.setText("Hallo, ich bin Ela das Eichhörnchen und ich möchte heute im Wald Nüsse sammeln gehen. Ich brauche deine Hilfe!");
            bubbleSetVisible(true);
        } else if (introPart == 1) {
            bubble.setText("Meine Lieblingsnüsse hängen immer zu zweit am Baum. So sehen sie aus:");
            bubbleSetVisible(true);
        } else {
            thoughtBubble.setVisibility(View.VISIBLE);
            bubble.setVisibility(View.INVISIBLE);
            situation = Situation.TASK1;
        }


        handler.postDelayed(squirrelShaker, 5000);
        introPart++;
    }

    private boolean firstTimeSecondSubTask = true;
    private final class ChoiceTouchListener implements View.OnTouchListener {

        private boolean inBag = false;
        private boolean changed = false;
        private int myNutPosition;
        int xDelta = 0, yDelta = 0;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            handler.removeCallbacks(nutShaker);
            bubbleSetVisible(false);
            final int x = (int) motionEvent.getRawX();
            final int y = (int) motionEvent.getRawY();

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
                            lParams.leftMargin = (int) getPx(870 + (nutPosition)%4*50);
                            lParams.topMargin = (int) getPx(520 + (nutPosition)/4*50);
                            nutCount += 2;

                        } else {
                            lParams.height *= 2;
                            lParams.width *= 2;
                            nutPlaceOccupied[myNutPosition] = false;
                            nutCount -= 2;
                        }



                        System.out.println(subTask);
                        if (situation == Situation.TASK1) {
                            validateFirstSubTask();
                        } else {
                            handler.removeCallbacks(squirrelShaker);
                            handler.postDelayed(squirrelShaker, 5000);
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
        rootLayout.requestLayout();
        nutCount = 0;
        nutPlaceOccupied = new boolean[11];
    }

    private void validateFirstSubTask() {
        if (nutCount == (subTask+1)*2) {
            bubbleSetVisible(true);
            bubble.setText("Super, das waren jetzt " + (subTask+1)*2 + " Nüsse! Hilfst du mir nochmal 2 Nüsse zu sammeln?");
            subTask++;
            if (subTask == 3) {
                restoreNuts();
                secondSubTask();
                firstTimeSecondSubTask = false;
                situation = Situation.TASK2;
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
