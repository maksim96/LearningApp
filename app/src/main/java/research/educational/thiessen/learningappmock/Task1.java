package research.educational.thiessen.learningappmock;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;

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
    private float defaultNutPos[][] = new float[11][2];
    private int defaultNutSize[] = new int[2];
    ImageView squirrel;
    Handler handler = new Handler();
    Runnable nutShaker;
    Runnable squirrelShaker;
    private Situation situation = Situation.INTRODUCTION;
    private ThoughtBubble thoughtBubble;
    private int subTask = 0;
    private SpeechBubble choiceBubble2;
    private SpeechBubble choiceBubble4;
    private SpeechBubble choiceBubble8;
    private MediaPlayer mediaPlayer;

    private StringBuilder statsString = new StringBuilder();



     /*   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getPx(nut.getWidth()), getPx(nut.getHeight()));
        layoutParams.leftMargin = getPx(nut.getX());
        layoutParams.topMargin = getPx(nut.getY());

        nut.setLayoutParams(layoutParams);*/

    private int elaIds[] = {R.raw.e1,
                       R.raw.e2,
                       R.raw.e3,
                       R.raw.e4,
                       R.raw.e5,
                       R.raw.e6,
                       R.raw.e7,
                       R.raw.e8,
                       R.raw.e9,
                       R.raw.e10,
                       R.raw.e11,
                       R.raw.e12,
                       R.raw.e13,
                       R.raw.e14,
                       R.raw.e15,
                       R.raw.e16,
                       R.raw.e17,
                       R.raw.e18,
                       R.raw.e19,
                       R.raw.e20,
                       R.raw.e21,
                       R.raw.e22,
                       R.raw.e23,
                       R.raw.e24,
                       R.raw.e25,
                       R.raw.e26,
                       R.raw.e27,
                       R.raw.e28,
                       R.raw.e29,
                       R.raw.e30,
                       R.raw.e31};

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

        rootLayout.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              if (!nutDragActivated) {
                                                  return;
                                              }
                                              if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                                  return;
                                              }
                                              bubble.setVisibility(View.INVISIBLE);
                                          }
                                      });

                squirrel = findViewById(R.id.squirrel);
        bubble = findViewById(R.id.speechbubble);
        thoughtBubble = findViewById(R.id.thoughtbubble);
        squirrel.setOnClickListener(new TextTouchListener());

        squirrelShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                squirrel.startAnimation(shake);
                handler.postDelayed(this, 3000);
            }
        };

        nutShaker = new Runnable() {
            @Override
            public void run() {
                if (!nutPlaceOccupied[0]) {
                    Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                    nuts[0].startAnimation(shake);
                    handler.postDelayed(this, 3000);
                }

            }
        };

        handler.postDelayed(squirrelShaker, 3000);

        choiceBubble2 = findViewById(R.id.choice_bubble2);
        choiceBubble4 = findViewById(R.id.choice_bubble4);
        choiceBubble8 = findViewById(R.id.choice_bubble8);

        statsString.append("========1. Aufgabe=========\n");



    }

    private void initMediaPlayer(int sample) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(this, elaIds[sample]);
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
            String htmlText = "Hilfst du mir <br><b>2 Nüsse</b> in meinen Beutel zu packen?";
            bubble.setText(Html.fromHtml(htmlText));
            bubble.setAnimateDots(false);
            initMediaPlayer(3);
            mediaPlayer.start();
            durationUntilShaking = mediaPlayer.getDuration();

            nutDragActivated = true;
        }
        handler.postDelayed(nutShaker, durationUntilShaking);
    }

    private final java.util.List<Integer> nutTasks = java.util.Arrays.asList(5,7,6,9,8,7,2);
    private final int[][] transitions = {{1,2},{3,4},{4,5}};
    private int thirdSubTaskPart = 0;
    private boolean startOfSubTask = true;
    private boolean done = false;
    private boolean veryFirstTime = true;
    private long startTime;
    private int durationUntilShaking = 3000; //default value if no playback


    private final void thirdSubTask() {
        bubbleSetVisible(true);
        if (startOfSubTask) {
            if (nutTasks.get(thirdSubTaskPart) != 7 && nutTasks.get(thirdSubTaskPart) != 2) {
                initMediaPlayer(10+thirdSubTaskPart);
                mediaPlayer.start();
                durationUntilShaking = mediaPlayer.getDuration();
            } else if (nutTasks.get(thirdSubTaskPart) == 7) {
                initMediaPlayer(11);
                mediaPlayer.start();
                durationUntilShaking = mediaPlayer.getDuration();
            } else {
                initMediaPlayer(15);
                mediaPlayer.start();
                durationUntilShaking = mediaPlayer.getDuration();
            }
            nutDragActivated = true;
            bubble.setText(Html.fromHtml("Könntest du mir als nächstes <br><b>"
                    + nutTasks.get(thirdSubTaskPart) + " \u00B7 2 Nüsse</b> in meinen Beutel packen?"));
            statsString.append("Aufgabe: " + nutTasks.get(thirdSubTaskPart) + " x 2 Nuesse\n");
            if (veryFirstTime) {
                startTime = System.currentTimeMillis();

                startOfSubTask = true;
                waitForOneClick = true;
                bubble.setAnimateDots(true);
                handler.postDelayed(squirrelShaker, durationUntilShaking);
            } else {

                startTime = System.currentTimeMillis();
                startOfSubTask = false;
                bubble.setAnimateDots(false);
                handler.postDelayed(nutShaker, durationUntilShaking);

            }


        } else {
            if (nutCount != nutTasks.get(thirdSubTaskPart)*2) {
                initMediaPlayer(16);
                mediaPlayer.start();
                durationUntilShaking = mediaPlayer.getDuration();
                bubble.setText("Das ist noch nicht ganz richtig.");
                long tempTimeDiff = (System.currentTimeMillis() - startTime)/1000;
                statsString.append("    Fehler gemacht: " + nutCount + " Nuesse im Beutel. Zeit gebrauchrt: " + tempTimeDiff + "s\n");
                bubble.setAnimateDots(false);
            } else {
                if (nutTasks.get(thirdSubTaskPart) != 7 && nutTasks.get(thirdSubTaskPart) != 2) {
                    initMediaPlayer(17+thirdSubTaskPart);
                    mediaPlayer.start();
                    durationUntilShaking = mediaPlayer.getDuration();
                } else if (nutTasks.get(thirdSubTaskPart) == 7) {
                    initMediaPlayer(18);
                    mediaPlayer.start();
                    durationUntilShaking = mediaPlayer.getDuration();
                } else {
                    initMediaPlayer(22);
                    mediaPlayer.start();
                    durationUntilShaking = mediaPlayer.getDuration();
                }
                bubble.setText(Html.fromHtml("Richtig! Du hast <br><b>" + nutTasks.get(thirdSubTaskPart)*2 + " Nüsse</b> in meinen Beutel gepackt!"));
                startOfSubTask = true;
                bubble.setAnimateDots(true);
                long timeDiff = (System.currentTimeMillis() - startTime)/1000;
                statsString.append("    Zeit gebraucht: " + timeDiff + "s\n");
                startTime = System.currentTimeMillis();
                if (thirdSubTaskPart == 0 && timeDiff >= 40){
                     //Special easy task for slow kids
                     thirdSubTaskPart = nutTasks.size() - 1;
                } else if (thirdSubTaskPart >= 3) {
                    done = true;
                } else {
                    if (timeDiff <= 20) {
                        thirdSubTaskPart = transitions[thirdSubTaskPart][0];
                    } else {
                        thirdSubTaskPart = transitions[thirdSubTaskPart][1];
                    }
                }
                /*if (thirdSubTaskPart >= nutTasks.size() -4 && timeDiff > 60) {
                    done = true;
                } else if (thirdSubTaskPart == nutTasks.size()- 1) {
                    done = true;
                } else if (madeMistake && thirdSubTaskPart >= nutTasks.size() -4 ) {
                    done = true;
                } else if (madeMistake) {
                    thirdSubTaskPart++;
                } else {
                    if (nutTasks[thirdSubTaskPart] == 5) {
                        thirdSubTaskPart += 3;
                    } else {
                            thirdSubTaskPart++;
                    }
                }*/


                handler.postDelayed(squirrelShaker, 3000);
                if (!done) {
                    restoreNuts();
                }


            }
        }
        if (done) {
            situation = Situation.TASK3;
            nutDragActivated = false;
            waitForOneClick = true;
        }
    }

    private final class TextTouchListener implements View.OnClickListener {
        boolean firstClicked = true;
        boolean choiceSolved = false;

        @Override
        public void onClick(View view) {
            handler.removeCallbacks(squirrelShaker);
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return;
            }
            if (situation == Situation.INTRODUCTION) {
                intro();
            } else if (situation == Situation.TASK1) {
                firstSubTask(view, firstClicked);
                firstClicked = false;
                if (waitForOneClick) {
                    waitForOneClick = false;
                    if (subTask == 1) {
                        bubble.setText(Html.fromHtml("Hilfst du mir ein <b>zweites mal</b> 2 Nüsse in meinen Beutel zu packen?"));
                        bubble.setAnimateDots(false);
                        initMediaPlayer(5);
                        mediaPlayer.start();
                        durationUntilShaking = mediaPlayer.getDuration();
                    } else if (subTask == 2) {
                        bubble.setText(Html.fromHtml("Hilfst du mir ein <b>drittes mal</b> 2 Nüsse in meinen Beutel zu packen?"));
                        bubble.setAnimateDots(false);
                        initMediaPlayer(7);
                        mediaPlayer.start();
                        durationUntilShaking = mediaPlayer.getDuration();
                    } else if (subTask == 3) {
                        validateFirstSubTask();
                    }

                }
            } else if(situation == Situation.TASK2) {

                if (veryFirstTime && waitForOneClick) {
                    bubble.setText("Tipp mich an, wenn du fertig bist.");
                    initMediaPlayer(30);
                    mediaPlayer.start();
                    durationUntilShaking = mediaPlayer.getDuration();
                    bubble.setAnimateDots(false);
                    veryFirstTime = false;
                    handler.postDelayed(nutShaker, durationUntilShaking);
                    waitForOneClick = false;
                    restoreNuts();
                    startOfSubTask = false;
                } else if (waitForOneClick) {
                    waitForOneClick = false;
                    restoreNuts();
                    thirdSubTask();
                } else {
                    thirdSubTask();
                }
            } else if (situation == Situation.TASK3){


                if (waitForOneClick) {
                    bubble.setText(Html.fromHtml("Schau dir den Beutel an. <b>Wie oft</b> habe ich schon gepflückt?"));
                    bubble.setAnimateDots(true);
                    initMediaPlayer(23);
                    mediaPlayer.start();
                    durationUntilShaking = mediaPlayer.getDuration();
                    handler.postDelayed(squirrelShaker, durationUntilShaking);

                    restoreNuts();
                    for (int i = 0; i < 4; i++) {
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) nuts[i].getLayoutParams();
                        lParams.height /= 2;
                        lParams.width /= 2;
                        lParams.leftMargin = (int) getPx(870 + (i)%4*50);
                        lParams.topMargin = (int) getPx(520 + (i)/4*50);
                        nuts[i].setLayoutParams(lParams);
                    }
                    waitForOneClick = false;
                    firstClicked = true;
                } else if (firstClicked) {
                    firstClicked = false;
                    bubble.setVisibility(View.INVISIBLE);
                    Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                    shake.setRepeatCount(Integer.MAX_VALUE);
                    shake.setDuration(1000);
                    choiceBubble2.startAnimation(shake);
                    choiceBubble2.setVisibility(View.VISIBLE);
                    choiceBubble4.startAnimation(shake);
                    choiceBubble4.setVisibility(View.VISIBLE);
                    choiceBubble8.startAnimation(shake);
                    choiceBubble8.setVisibility(View.VISIBLE);
                    statsString.append("Aufgabe: Sprechblase aussuchen\n");

                    choiceBubble2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bubble.setVisibility(View.VISIBLE);
                            bubble.setText("Das ist noch nicht ganz richtig.");
                            initMediaPlayer(16);
                            mediaPlayer.start();
                            durationUntilShaking = mediaPlayer.getDuration();
                            statsString.append("    Fehler gemacht: 2 gedrueckt\n");
                        }
                    });
                    choiceBubble8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bubble.setVisibility(View.VISIBLE);
                            bubble.setText("Das ist noch nicht ganz richtig.");
                            initMediaPlayer(16);
                            mediaPlayer.start();
                            durationUntilShaking = mediaPlayer.getDuration();
                            statsString.append("    Fehler gemacht: 8 gedrueckt\n");
                        }
                    });
                    choiceBubble4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bubble.setVisibility(View.VISIBLE);
                            bubble.setText("Richtig!");
                            bubble.setAnimateDots(true);
                            initMediaPlayer(24);
                            mediaPlayer.start();
                            durationUntilShaking = mediaPlayer.getDuration();
                            choiceBubble2.setVisibility(View.GONE);
                            choiceBubble4.setVisibility(View.GONE);
                            choiceBubble8.setVisibility(View.GONE);
                            choiceBubble2.clearAnimation();
                            choiceBubble4.clearAnimation();
                            choiceBubble8.clearAnimation();
                            situation = Situation.DONE;
                            waitForOneClick = false;
                            bubble.setAnimateDots(true);
                            handler.postDelayed(squirrelShaker,durationUntilShaking);
                            statsString.append("    Absolviert\n");
                        }
                    });

                } else if (choiceSolved) {


                }

            } else {
                try {
                    Start.outputStream.write(statsString.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(view.getContext(), Task2.class);
                startActivity(intent);
            }



       }


    }

    int introPart = 0;
    private void intro() {
        bubbleSetVisible(true);

        if (introPart == 0) {
            bubble.setText("Hallo, ich bin Ela das Eichhörnchen und ich möchte heute im Wald Nüsse sammeln. Kannst du mir helfen?");
            initMediaPlayer(0);
            mediaPlayer.start();
            durationUntilShaking = mediaPlayer.getDuration();
            bubble.setAnimateDots(true);
            bubbleSetVisible(true);
        } else if (introPart == 1) {
            bubble.setText(Html.fromHtml("Von meinen Lieblingsnüssen hängen immer <b>2 zusammen</b>."));
            initMediaPlayer(1);
            mediaPlayer.start();
            durationUntilShaking = mediaPlayer.getDuration();
            bubble.setAnimateDots(true);
            bubble.setAnimateDots(true);
            bubbleSetVisible(true);
        } else if (introPart == 2) {
            bubble.setText("So sehen sie aus:");
            initMediaPlayer(2);
            mediaPlayer.start();
            durationUntilShaking = mediaPlayer.getDuration();
            bubble.setAnimateDots(true);
            bubble.setAnimateDots(true);
            bubbleSetVisible(true);
        } else {
            thoughtBubble.setVisibility(View.VISIBLE);
            bubble.setVisibility(View.INVISIBLE);
            situation = Situation.TASK1;
            waitForOneClick = true;
        }


        handler.postDelayed(squirrelShaker, durationUntilShaking);
        introPart++;
    }

    private boolean nutDragActivated = false;
    private final class ChoiceTouchListener implements View.OnTouchListener {

        private boolean inBag = false;
        private boolean changed = false;
        private int myNutPosition;
        int xDelta = 0, yDelta = 0;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            handler.removeCallbacks(nutShaker);
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return true;
            }
            if (waitForOneClick || !nutDragActivated) {
                return true;
            }
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

                        if (situation == Situation.TASK1) {
                            validateFirstSubTask();
                        } else {
                            handler.removeCallbacks(squirrelShaker);
                            handler.postDelayed(squirrelShaker, durationUntilShaking);
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

    boolean waitForOneClick = false;
    private void validateFirstSubTask() {
        if (subTask == 3) {
            bubble.setText(Html.fromHtml("Du hast <b>3 \u00B7 2 Nüsse</b> für mich gesammelt."));
            bubble.setAnimateDots(true);
            initMediaPlayer(9);
            mediaPlayer.start();
            durationUntilShaking = mediaPlayer.getDuration();
            nutDragActivated = false;
            bubble.setAnimateDots(true);
            waitForOneClick = false;
            situation = Situation.TASK2;
            restoreNuts();
            //waitForOneClick = false;
        } else if (nutCount == (subTask+1)*2) {
            bubbleSetVisible(true);
            bubble.setText(Html.fromHtml("Super, das waren jetzt <b>" + (subTask+1)*2 + " Nüsse</b>!"));
            initMediaPlayer(4+subTask*2);
            mediaPlayer.start();
            durationUntilShaking = mediaPlayer.getDuration();
            bubble.setAnimateDots(true);

            waitForOneClick = true;
            subTask++;


        }
        handler.postDelayed(squirrelShaker, durationUntilShaking);
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
