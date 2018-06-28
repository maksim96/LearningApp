package research.educational.thiessen.learningappmock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import research.educational.thiessen.learningappmock.helpers.NumericKeyBoardTransformationMethod;
import research.educational.thiessen.learningappmock.helpers.Situation;
import research.educational.thiessen.learningappmock.helpers.SpeechBubble;
import research.educational.thiessen.learningappmock.helpers.ThoughtBubble;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Task2 extends Activity {

    private ViewGroup rootLayout;
    private SpeechBubble bearBubble;
    private SpeechBubble squirrelBubble;
    private EditText honeyEditText;
    private ImageView bear;
    private ImageView[] food = new ImageView[8];
    private ImageView bunny;
    private SpeechBubble bunnyBubble;
    private ImageView squirrel;
    private Runnable squirrelShaker;
    private Runnable bearShaker;
    private Runnable signShaker;
    private Runnable bunnyShaker;
    private Runnable bagHelperMessageDisplayer;
    Handler handler = new Handler();
    private RelativeLayout sign;
    private Situation situation = Situation.INTRODUCTION;
    private ThoughtBubble bearThoughtBubble;
    private ThoughtBubble bunnyThoughtBubble;
    private ImageView bag;
    private final int[] bearSubTasks = {4,6,5,8,7,6,3}; //BFS
    private final int[] bunnySubTasks = {2,4,3,6,5,4,8,3};
    private final int[][] transitions = {{1,2},{3,4},{4,5}}; //transitions for first two layers, without any special stuff (>= 45s, <15s)
    private int currentSubTask = 0;
    private long timeOfSubTaskStart;
    private boolean bearDone = false;

    private StringBuilder statsString = new StringBuilder();
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

    private int berthaIds[] = {R.raw.b1,
            R.raw.b2,
            R.raw.b3,
            R.raw.b4,
            R.raw.b5,
            R.raw.b6,
            R.raw.b7,
            R.raw.b8,
            R.raw.b9,
            R.raw.b10,
            R.raw.b11,
            R.raw.b12,
            R.raw.b13,
            R.raw.b14,
            R.raw.b15,
            R.raw.b16,
            R.raw.b17,
            R.raw.b18,
            R.raw.b19};

    private int hugoIds[] = {R.raw.h1,
            R.raw.h2,
            R.raw.h3,
            R.raw.h4,
            R.raw.h5,
            R.raw.h6,
            R.raw.h7,
            R.raw.h8,
            R.raw.h9,
            R.raw.h10,
            R.raw.h11,
            R.raw.h12,
            R.raw.h13,
            R.raw.h14,
            R.raw.h15};

    private MediaPlayer mediaPlayer;
    private int durationUntilShaking = 3000; //default value if no playback



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initally hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        setContentView(R.layout.activity_task2);

        rootLayout = findViewById(R.id.task2_root);
        rootLayout.setOnTouchListener(new GlobalOnTouchListener());
        bearBubble = findViewById(R.id.bubbleBear);
        bearThoughtBubble = findViewById(R.id.thoughtBubbleBear);
        bunnyThoughtBubble = findViewById(R.id.thoughtBubbleBunny);
        squirrelBubble = findViewById(R.id.bubbleSquirrel);
        bear = findViewById(R.id.bear);

        int[] foodIds = {R.id.honey1, R.id.honey2, R.id.honey3, R.id.honey4, R.id.honey5, R.id.honey6, R.id.honey7, R.id.honey8};
        for (int i = 0; i < 8; i++) {
            food[i] = findViewById(foodIds[i]);
        }
        bunny = findViewById(R.id.bunny);
        bunnyBubble = findViewById(R.id.bunnyBubble);
        bag = findViewById(R.id.bag);
        squirrel = findViewById(R.id.squirrel);

        honeyEditText = findViewById(R.id.honeyEdit);
        honeyEditText.setTransformationMethod(new NumericKeyBoardTransformationMethod());


        sign = findViewById(R.id.sign);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    return;
                }
                if (honeyEditText.isEnabled()) {
                    handler.removeCallbacks(signShaker);
                    handler.removeCallbacks(bagHelperMessageDisplayer);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    honeyEditText.requestFocus();
                    bubbleSetVisible(0, false);
                    bubbleSetVisible(1, false);
                    bubbleSetVisible(2, false);
                }

            }
        });
        honeyEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    return false;
                }
                handler.removeCallbacks(signShaker);
                bubbleSetVisible(0, false);
                bubbleSetVisible(1, false);
                bubbleSetVisible(2, false);
                return false;
            }
        });


        honeyEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus){
                handler.removeCallbacks(bagHelperMessageDisplayer);
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        squirrelShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                squirrel.startAnimation(shake);
                handler.postDelayed(this, 3000);
            }
        };

        bearShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                bear.startAnimation(shake);
                handler.postDelayed(this, 3000);
            }
        };
        signShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                sign.startAnimation(shake);
                handler.postDelayed(this, 3000);
            }
        };
        bunnyShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                bunny.startAnimation(shake);
                handler.postDelayed(this, 3000);
            }
        };


        bagHelperMessageDisplayer = new Runnable() {
            @Override
            public void run() {
                bubbleSetVisible(1, true);
                bearBubble.setText("Tipp den Beutel an, falls du Hilfe brauchst.");
                bearBubble.setAnimateDots(false);
                initMediaPlayer(1,18);
                mediaPlayer.start();

            }
        };





        /*
        rootLayout = (ViewGroup) findViewById(R.id.drag_root);
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
        squirrel.setOnClickListener(new TextTouchListener());*/


     /*   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getPx(nut.getWidth()), getPx(nut.getHeight()));
        layoutParams.leftMargin = getPx(nut.getX());
        layoutParams.topMargin = getPx(nut.getY());

        nut.setLayoutParams(layoutParams);*/
    initMediaPlayer(0,25);
    mediaPlayer.start();
        honeyEditText.setEnabled(false);
    durationUntilShaking = mediaPlayer.getDuration();
     handler.postDelayed(squirrelShaker, durationUntilShaking);

    squirrel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return;
            }
            squirrel.setVisibility(View.INVISIBLE);
            squirrelBubble.setVisibility(View.INVISIBLE);
            bearBubble.setVisibility(View.VISIBLE);

            handler.removeCallbacks(squirrelShaker);
            initMediaPlayer(1,0);
            mediaPlayer.start();
            honeyEditText.setEnabled(false);
            durationUntilShaking = mediaPlayer.getDuration();
            handler.postDelayed(bearShaker, durationUntilShaking);
            bearBubble.setText(Html.fromHtml("Hallo, ich bin Bertha der Bär. Ich esse am liebsten <b>Honigwaben</b>."));
            bearBubble.setAnimateDots(true);
            if (!bear.hasOnClickListeners()) {
                bear.setOnClickListener(new BearOnClickListener());
            }

        }
    });

    honeyEditText.setEnabled(false);
    statsString.append("========2. Aufgabe=========\n");

    }

    private final class BearOnClickListener implements  View.OnClickListener {
        private int introSubTask = 0;

        @Override
        public void onClick(View view) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return;
            }
            handler.removeCallbacks(bearShaker);
            if (situation == Situation.INTRODUCTION) {
                if (introSubTask == 0) {
                    bearThoughtBubble.setVisibility(View.INVISIBLE);
                    bearBubble.setText(Html.fromHtml("Es kleben immer <br> <b>3 Waben aneinander</b>."));
                    bearBubble.setVisibility(View.VISIBLE);
                    bearBubble.setAnimateDots(true);
                    initMediaPlayer(1,1);
                    mediaPlayer.start();
                    durationUntilShaking = mediaPlayer.getDuration();
                    honeyEditText.setEnabled(false);
                    handler.postDelayed(bearShaker, durationUntilShaking);
                } else if (introSubTask == 1) {
                    bearBubble.setText("So sehen sie aus:");
                    bearBubble.setAnimateDots(true);
                    initMediaPlayer(1,2);
                    mediaPlayer.start();
                    durationUntilShaking = mediaPlayer.getDuration();
                    honeyEditText.setEnabled(false);
                    handler.postDelayed(bearShaker, durationUntilShaking);
                } else if (introSubTask == 2) {
                    bearBubble.setVisibility(View.INVISIBLE);
                    bearThoughtBubble.setVisibility(View.VISIBLE);
                    handler.postDelayed(bearShaker, 3000);
                }  else if (introSubTask == 3) {
                    bearBubble.setVisibility(View.VISIBLE);
                    bearBubble.setText("Tipp mal den Beutel an!");
                    timeOfSubTaskStart = System.currentTimeMillis();
                    bearThoughtBubble.setVisibility(View.INVISIBLE);
                    bearBubble.setAnimateDots(true);
                    bag.setOnClickListener(new BagListener());
                    initMediaPlayer(1,3);
                    honeyEditText.setEnabled(false);
                    mediaPlayer.start();
                    durationUntilShaking = mediaPlayer.getDuration();
                    handler.postDelayed(bearShaker, durationUntilShaking);
                } else if (introSubTask == 4) {
                    bearBubble.setVisibility(View.VISIBLE);
                    bearBubble.setText(Html.fromHtml("Ich habe <br><b>4 · 3 Waben</b> gesammelt und in meinen Beutel getan."));
                    bearBubble.setAnimateDots(true);
                    initMediaPlayer(1,5);
                    mediaPlayer.start();
                    honeyEditText.setEnabled(false);
                    durationUntilShaking = mediaPlayer.getDuration();
                    handler.postDelayed(bearShaker, durationUntilShaking);
                } else if (introSubTask == 5) {
                    bearBubble.setVisibility(View.VISIBLE);
                    bearBubble.setText(Html.fromHtml("Kannst du mir auf das Schild schreiben, <b>wie viele Waben</b> ich habe?"));
                    bearBubble.setAnimateDots(false);
                    handler.postDelayed(signShaker, durationUntilShaking);
                    situation = Situation.TASK1;
                    honeyEditText.setOnEditorActionListener(new OnSignEdit());
                    timeOfSubTaskStart = System.currentTimeMillis();
                    initMediaPlayer(1,4);
                    mediaPlayer.start();
                    honeyEditText.setEnabled(false);
                    durationUntilShaking = mediaPlayer.getDuration();
                    honeyEditText.setEnabled(true);
                    statsString.append("Aufgabe: 4 x 3\n");
                }
                introSubTask++;
            } else if (situation == Situation.TASK1) {
                if (bearDone) {
                    bubbleSetVisible(1, true);
                    bearBubble.setText("Schau mal, wer auch noch im Wald gesammelt hat.");
                    bearBubble.setAnimateDots(false);
                    bunny.setVisibility(View.VISIBLE);
                    bunny.setOnClickListener(new BunnyOnClickListener());
                    situation = Situation.TASK2;
                    initMediaPlayer(1,13);
                    mediaPlayer.start();
                    honeyEditText.setEnabled(false);
                    durationUntilShaking = mediaPlayer.getDuration();
                    handler.postDelayed(bunnyShaker, durationUntilShaking);

                }
            } else if (situation == Situation.DONE) {
                try {
                    Start.outputStream.write(statsString.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(bunnyBubble.getContext(), Task3.class);
                startActivity(intent);
            }

        }

    }

    private final class BagListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                int currentFoodCount;
                bubbleSetVisible(0, false);
                handler.removeCallbacks(bagHelperMessageDisplayer);
                if (bearDone && situation == Situation.TASK1) {
                    return; //this is the small pause before the bunny appears.
                }
                if (situation == Situation.TASK1 ||situation == Situation.INTRODUCTION) {
                    currentFoodCount= bearSubTasks[currentSubTask];
                } else {
                    currentFoodCount = bunnySubTasks[currentSubTask];
                }
                if (situation == Situation.DONE) {
                    currentFoodCount = 0;
                }
                for (int i = 0; i < 8 ; i++) {
                    if (i < currentFoodCount) {
                        food[i].setVisibility(View.VISIBLE);
                    } else {
                        food[i].setVisibility(View.INVISIBLE);
                    }


                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 8; i++) {
                            food[i].setVisibility(View.INVISIBLE);
                        }
                    }
                }, 1500);
                statsString.append("    Beutel benutzt zum Zeitpunkt: " + (System.currentTimeMillis() - timeOfSubTaskStart)/1000 +"s\n");
            }
    }

    private final class OnSignEdit implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            handler.removeCallbacks(signShaker);

            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE) {
                validateTask();
                return false; // consume.

            }
            return false; // pass on to other listeners.
        }
    }

    private void validateTask() {
        handler.removeCallbacks(bagHelperMessageDisplayer);
        if (situation == Situation.TASK1) {
            if (honeyEditText.getText().toString().equals(Integer.toString((bearSubTasks[currentSubTask])*3))) {

                long timeDiff = (System.currentTimeMillis() - timeOfSubTaskStart)/1000;
                statsString.append("    Zeit gebraucht: " + timeDiff + "s\n");
                timeOfSubTaskStart = System.currentTimeMillis();
                handler.postDelayed(bagHelperMessageDisplayer, 30000);
                if (currentSubTask == 0 && timeDiff >= 45){
                    //Special easy task for slow kids
                    currentSubTask = bearSubTasks.length - 1;
                    initMediaPlayer(1,10);
                    mediaPlayer.start();
                    honeyEditText.setEnabled(false);
                    durationUntilShaking = mediaPlayer.getDuration();
                } else if (currentSubTask == bearSubTasks.length - 1) {
                    bearDone = true;
                } else if (currentSubTask >= 3) {
                    bearDone = true;
                    currentSubTask = 0;
                } else {
                    if (timeDiff <= 30) {
                        currentSubTask = transitions[currentSubTask][0];
                    } else {
                        currentSubTask = transitions[currentSubTask][1];
                    }
                }
                if (bearSubTasks[currentSubTask] != 6 && bearSubTasks[currentSubTask] != 3) {
                    initMediaPlayer(1,5+currentSubTask);
                    mediaPlayer.start();
                    durationUntilShaking = mediaPlayer.getDuration();
                    honeyEditText.setEnabled(false);
                } else if (bearSubTasks[currentSubTask] == 6) {
                    initMediaPlayer(1,6);
                    mediaPlayer.start();
                    honeyEditText.setEnabled(false);
                    durationUntilShaking = mediaPlayer.getDuration();
                }

                if (!bearDone) {
                    bearBubble.setText(Html.fromHtml("Ja, richtig! <b>Wie viele</b> sind es, wenn ich<b> " + bearSubTasks[currentSubTask] +
                            " · 3 Waben</b> gesammelt habe?"));
                    bearBubble.setAnimateDots(false);
                    bubbleSetVisible(1, true);
                    statsString.append("Aufgabe: " + bearSubTasks[currentSubTask] + " x 3\n");
                }  else {
                    bearBubble.setText("Ja, richtig!");
                    bearBubble.setAnimateDots(true);
                    bubbleSetVisible(1, true);
                    initMediaPlayer(1,12);
                    mediaPlayer.start();
                    honeyEditText.setEnabled(false);
                    durationUntilShaking = mediaPlayer.getDuration();
                    handler.postDelayed(bearShaker, durationUntilShaking);
                    bearDone = true;
                }
            } else {
                bearBubble.setText(Html.fromHtml("Das ist noch nicht ganz richtig. Tipp den <b>Beutel</b> an, falls du <b>Hilfe</b> brauchst."));
                bubbleSetVisible(1, true);
                bearBubble.setAnimateDots(false);
                initMediaPlayer(1,11);
                mediaPlayer.start();
                honeyEditText.setEnabled(false);
                durationUntilShaking = mediaPlayer.getDuration();
                statsString.append("    Fehler gemacht. Eingabe war " + honeyEditText.getText().toString() + "\n");
            }

        } else if (situation == Situation.TASK2) {
            if (honeyEditText.getText().toString().equals(Integer.toString((bunnySubTasks[currentSubTask])*4))) {
                long timeDiff = (System.currentTimeMillis() - timeOfSubTaskStart)/1000;
                statsString.append("    Zeit gebraucht: " + timeDiff + "s\n");
                timeOfSubTaskStart = System.currentTimeMillis();
                if (currentSubTask == 0 && timeDiff >= 45){
                    //Special easy task for slow kids
                    currentSubTask = bunnySubTasks.length - 1;
                } else if (currentSubTask == 1 && timeDiff <= 15){
                    //Special task for fast kids
                    currentSubTask = bunnySubTasks.length - 2;
                } else if (currentSubTask >= 3) {
                    situation = Situation.DONE;
                } else {
                    if (timeDiff <= 30) {
                        currentSubTask = transitions[currentSubTask][0];
                    } else {
                        currentSubTask = transitions[currentSubTask][1];
                    }
                }

                if (bunnySubTasks[currentSubTask] != 4 && bunnySubTasks[currentSubTask] != 3 && bunnySubTasks[currentSubTask] != 8 ) {
                    initMediaPlayer(2,5+currentSubTask);
                    mediaPlayer.start();
                    honeyEditText.setEnabled(false);
                    durationUntilShaking = mediaPlayer.getDuration();
                } else if (bunnySubTasks[currentSubTask] == 4) {
                    initMediaPlayer(2,6);
                    mediaPlayer.start();
                    honeyEditText.setEnabled(false);
                    durationUntilShaking = mediaPlayer.getDuration();
                } else if (bunnySubTasks[currentSubTask] == 3) {
                    initMediaPlayer(2,7);
                    mediaPlayer.start();
                    honeyEditText.setEnabled(false);
                    durationUntilShaking = mediaPlayer.getDuration();
                } else if (bunnySubTasks[currentSubTask] == 8) {
                    initMediaPlayer(2,10);
                    mediaPlayer.start();
                    honeyEditText.setEnabled(false);
                    durationUntilShaking = mediaPlayer.getDuration();
                }

                if (situation != Situation.DONE) {
                    bunnyBubble.setText(Html.fromHtml("Ja, richtig! <b>Wie viele</b> sind es, wenn ich <b>" + bunnySubTasks[currentSubTask] +
                            " · 4 Möhren</b> gesammelt habe?"));
                    statsString.append("Aufgabe: " + bunnySubTasks[currentSubTask] + " x 4\n");
                    bunnyBubble.setAnimateDots(false);
                    bubbleSetVisible(2, true);
                    //letzte Aufgabe nur für schnelle Kinder die keine Fehler gemacht haben
                }  else {
                    bunnyBubble.setText("Ja, richtig!");
                    bunnyBubble.setAnimateDots(false);
                    bubbleSetVisible(2, true);

                    situation = Situation.DONE;
                    initMediaPlayer(2,11);
                    mediaPlayer.start();
                    honeyEditText.setEnabled(false);
                    durationUntilShaking = mediaPlayer.getDuration();
                    handler.postDelayed(bunnyShaker, durationUntilShaking);
                }



            } else {
                bunnyBubble.setText(Html.fromHtml("Das ist noch nicht ganz richtig. Tipp den <b>Beutel</b> an, wenn du <b>Hilfe</b> brauchst."));
                bunnyBubble.setAnimateDots(false);
                bubbleSetVisible(2, true);
                initMediaPlayer(2,5);
                mediaPlayer.start();
                honeyEditText.setEnabled(false);
                durationUntilShaking = mediaPlayer.getDuration();
                statsString.append("    Fehler gemacht. Eingabe war " + honeyEditText.getText().toString() + "\n");

            }
        }
        honeyEditText.setText("");

        InputMethodManager imm = (InputMethodManager)getSystemService(honeyEditText.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(honeyEditText.getWindowToken(), 0);
    }

    private int bunnyIntroSubTask = 0;
    private final class BunnyOnClickListener implements View.OnClickListener {


        @Override
        public void onClick(View view) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return;
            }
            handler.removeCallbacks(bunnyShaker);
            if (bunnyIntroSubTask == 0) {
                bunnyBubble.setText("Hallo, ich bin Hugo der Hase.");
                bunnyBubble.setAnimateDots(true);
                bubbleSetVisible(2, true);
                initMediaPlayer(2,0);
                mediaPlayer.start();
                honeyEditText.setEnabled(false);
                durationUntilShaking = mediaPlayer.getDuration();
                for (int i = 0; i < 8; i++) {
                    food[i].setImageResource(R.drawable.carrots);
                    food[i].setVisibility(View.INVISIBLE);
                }
                handler.postDelayed(bunnyShaker, durationUntilShaking);
            } else if (bunnyIntroSubTask == 1) {
                bubbleSetVisible(2, true);
                bunnyBubble.setText(Html.fromHtml("Ich liebe <b>Möhren</b> in Bündeln."));
                bunnyBubble.setAnimateDots(true);
                initMediaPlayer(2,1);
                mediaPlayer.start();
                honeyEditText.setEnabled(false);
                durationUntilShaking = mediaPlayer.getDuration();
                handler.postDelayed(bunnyShaker, durationUntilShaking);
            }else if (bunnyIntroSubTask == 2) {
                    bubbleSetVisible(2, true);
                    bunnyBubble.setText(Html.fromHtml("Es hängen immer <b>4 zusammen</b>."));
                    bunnyBubble.setAnimateDots(true);
                initMediaPlayer(2,2);
                mediaPlayer.start();
                honeyEditText.setEnabled(false);
                durationUntilShaking = mediaPlayer.getDuration();
                handler.postDelayed(bunnyShaker, durationUntilShaking);
            } else if (bunnyIntroSubTask == 3) {
                bubbleSetVisible(2, true);
                bunnyBubble.setText("So sehen sie aus:");
                bunnyBubble.setAnimateDots(true);
                initMediaPlayer(2,3);
                mediaPlayer.start();
                honeyEditText.setEnabled(false);
                durationUntilShaking = mediaPlayer.getDuration();
                handler.postDelayed(bunnyShaker, durationUntilShaking);
            } else if (bunnyIntroSubTask == 4) {
                bubbleSetVisible(2, false);
                bunnyThoughtBubble.setVisibility(View.VISIBLE);
                bunnyBubble.setAnimateDots(false);
                handler.postDelayed(bunnyShaker, 3000);
            } else if (bunnyIntroSubTask == 5) {
                bubbleSetVisible(2, true);
                bunnyBubble.setText(Html.fromHtml("Ich habe <br><b>2 · 4 Möhren</b> gesammelt. <b>Wie viele</b> sind im Beutel?"));
                bunnyBubble.setAnimateDots(false);
                bunnyThoughtBubble.setVisibility(View.INVISIBLE);
                timeOfSubTaskStart = System.currentTimeMillis();
                currentSubTask = 0;
                initMediaPlayer(2,4);
                mediaPlayer.start();
                honeyEditText.setEnabled(false);
                durationUntilShaking = mediaPlayer.getDuration();
                handler.postDelayed(signShaker, durationUntilShaking);
                statsString.append("Aufgabe: 2 x 4\n");
            }
            bunnyIntroSubTask++;
            if (situation == Situation.DONE) {
                try {
                    Start.outputStream.write(statsString.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(bunnyBubble.getContext(), Task3.class);
                startActivity(intent);
            }
        }

    }



    private final class GlobalOnTouchListener implements View.OnTouchListener {

        private String text = "";

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return true;
            }

            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                InputMethodManager imm = (InputMethodManager)getSystemService(view.getContext().INPUT_METHOD_SERVICE);

                if (!honeyEditText.getText().toString().equals(text) && !honeyEditText.getText().toString().isEmpty()) {

                    validateTask();
                    text  =honeyEditText.getText().toString();
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (situation == Situation.TASK2) {
                    bubbleSetVisible(0, false);
                    bubbleSetVisible(1, false);
                    bubbleSetVisible(2, false);
                }



            }

            return true;
        }
    }

    private void bubbleSetVisible(int animal, boolean visible) {
        if (animal == 0) {
            if (visible) {
                squirrelBubble.setVisibility(View.VISIBLE);
                bunnyBubble.setVisibility(View.INVISIBLE);
                bearBubble.setVisibility(View.INVISIBLE);
            } else {
                squirrelBubble.setVisibility(View.INVISIBLE);
            }

        } else if (animal == 1) {
            if (visible) {
                bearBubble.setVisibility(View.VISIBLE);
                squirrelBubble.setVisibility(View.INVISIBLE);
                bunnyBubble.setVisibility(View.INVISIBLE);
            } else {
                bearBubble.setVisibility(View.INVISIBLE);
            }
        } else if (animal == 2) {
            if (visible) {
                bunnyBubble.setVisibility(View.VISIBLE);
                squirrelBubble.setVisibility(View.INVISIBLE);
                bearBubble.setVisibility(View.INVISIBLE);
            } else {
                bunnyBubble.setVisibility(View.INVISIBLE);
            }
        }


    }
/*
    private final class TextTouchListener implements View.OnClickListener {
        boolean firstClicked = true;


        @Override
        public void onClick(View view) {
            bubbleSetVisible(true);
            if (firstClicked) {
                firstClicked = false;
                textView.setText("Könntest du mir 6 Nüsse in meinen Beutel tun?");

            } else {
                if (nutCount < 6) {
                    textView.setText("Ich brauche ein paar mehr Nüsse");
                } else if (nutCount == 6) {
                    textView.setText("Wow! Vielen Dank!");
                } else {
                    textView.setText("So viele brauche ich doch gar nicht...");
                }
            }

        }
    }
*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }

    private void initMediaPlayer(int animal, int sample) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (animal == 0) {
            mediaPlayer = MediaPlayer.create(this, elaIds[sample]);
        } else if (animal == 1) {
            mediaPlayer = MediaPlayer.create(this, berthaIds[sample]);
        } else {
            mediaPlayer = MediaPlayer.create(this, hugoIds[sample]);
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (situation == Situation.TASK1 || (
                        situation == Situation.TASK2 && bunnyIntroSubTask >= 5)) {
                    honeyEditText.setEnabled(true);
                }

            }
        });

    }






}
