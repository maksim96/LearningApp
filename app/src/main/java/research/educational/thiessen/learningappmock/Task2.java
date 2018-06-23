package research.educational.thiessen.learningappmock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
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
    Handler handler = new Handler();
    private RelativeLayout sign;
    private Situation situation = Situation.INTRODUCTION;
    private ThoughtBubble bearThoughtBubble;
    private ThoughtBubble bunnyThoughtBubble;
    private ImageView bag;
    private final int[] bearSubTasks = {4,6,5,8,7,6,3}; //BFS
    private final int[] bunnySubTasks = {2,4,3,6,5,4,8};
    private final int[][] transitions = {{1,2},{3,4},{4,5}}; //transitions for first two layers, without any special stuff (>= 45s, <15s)
    private int currentSubTask = 0;
    private long timeOfSubTaskStart;
    private boolean bearDone = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initally hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        setContentView(R.layout.activity_task2);

        rootLayout = findViewById(R.id.task2_root);
        rootLayout.setOnClickListener(new GlobalOnTouchListener());
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
                handler.removeCallbacks(signShaker);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                honeyEditText.requestFocus();
            }
        });
        honeyEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                handler.removeCallbacks(signShaker);
                return false;
            }
        });


        honeyEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus){
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
                handler.postDelayed(this, 5000);
            }
        };

        bearShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                bear.startAnimation(shake);
                handler.postDelayed(this, 5000);
            }
        };
        signShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                sign.startAnimation(shake);
                handler.postDelayed(this, 5000);
            }
        };
        bunnyShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                bunny.startAnimation(shake);
                handler.postDelayed(this, 5000);
            }
        };





        bear.setOnClickListener(new View.OnClickListener() {
            private int introSubTask = 0;

            @Override
            public void onClick(View view) {
                handler.removeCallbacks(bearShaker);
                if (situation == Situation.INTRODUCTION) {
                    if (introSubTask == 0) {
                        bearBubble.setText("So sehen sie aus:");
                        bearBubble.setAnimateDots(true);
                    } else if (introSubTask == 1) {
                        bearBubble.setVisibility(View.INVISIBLE);
                        bearThoughtBubble.setVisibility(View.VISIBLE);
                    } else if (introSubTask == 2) {
                        bearThoughtBubble.setVisibility(View.INVISIBLE);
                        bearBubble.setText("Wie du siehst, kleben immer 3 Waben anneinander.");
                        bearBubble.setVisibility(View.VISIBLE);
                        bearBubble.setAnimateDots(true);
                    } else if (introSubTask == 3) {
                        bearBubble.setVisibility(View.VISIBLE);
                        bearBubble.setText("Tipp mal den Beutel an!");
                        bag.setOnClickListener(new BagListener());

                    } else if (introSubTask == 4) {
                        bearBubble.setVisibility(View.VISIBLE);
                        bearBubble.setText("Kannst du mir auf das Schild schreiben, wie viele Waben ich habe?");
                        bearBubble.setAnimateDots(true);
                    } else if (introSubTask == 5) {
                        bearBubble.setVisibility(View.VISIBLE);
                        bearBubble.setText("Ich habe 4 · 3 Waben gesammelt und in meinen Beutel getan.");
                        bearBubble.setAnimateDots(false);
                        handler.postDelayed(signShaker, 5000);
                        situation = Situation.TASK1;
                        honeyEditText.setOnEditorActionListener(new OnSignEdit());
                        timeOfSubTaskStart = System.currentTimeMillis();

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
                        handler.postDelayed(bunnyShaker, 5000);

                    }
                } else if (situation == Situation.DONE) {
                    Intent intent = new Intent(bunnyBubble.getContext(), Task3.class);
                    startActivity(intent);
                }

            }


        });


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

        squirrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                squirrel.setVisibility(View.INVISIBLE);
                squirrelBubble.setVisibility(View.INVISIBLE);
                bearBubble.setVisibility(View.VISIBLE);
                bearBubble.setAnimateDots(true);
                handler.postDelayed(bearShaker, 5000);
            }
        });




    }

    private final class BagListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                int currentFoodCount;
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
            }
    }

    private final class OnSignEdit implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            handler.removeCallbacks(signShaker);

            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE) {
                // the user is done typing.
                if (situation == Situation.TASK1) {
                    if (honeyEditText.getText().toString().equals(Integer.toString((bearSubTasks[currentSubTask])*3))) {
                        honeyEditText.setText("");
                        long timeDiff = (System.currentTimeMillis() - timeOfSubTaskStart)/1000;
                        timeOfSubTaskStart = System.currentTimeMillis();
                        if (currentSubTask == 0 && timeDiff >= 45){
                            //Special easy task for slow kids
                            currentSubTask = bearSubTasks.length - 1;
                        } else if (currentSubTask == bearSubTasks.length - 1) {
                            bearDone = true;
                            //no bunny subtask took so long
                            situation = Situation.DONE;
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
                        if (!bearDone) {
                            bearBubble.setText("Ja, richtig! Wie viele sind es, wenn ich " + bearSubTasks[currentSubTask] +
                                    " · 3 Waben gesammelt habe?");
                            bearBubble.setAnimateDots(false);
                            bubbleSetVisible(1, true);
                        }  else {
                            bearBubble.setText("Ja, richtig!");
                            bearBubble.setAnimateDots(true);
                            bubbleSetVisible(1, true);
                            handler.postDelayed(bearShaker, 5000);
                            bearDone = true;
                        }
                    } else {
                        bearBubble.setText("Das ist noch nicht ganz richtig. Tipp den Beutel an, falls du Hilfe brauchst.");
                        bubbleSetVisible(1, true);
                        bearBubble.setAnimateDots(false);
                    }
                } else if (situation == Situation.TASK2) {
                    if (honeyEditText.getText().toString().equals(Integer.toString((bunnySubTasks[currentSubTask])*4))) {
                        honeyEditText.setText("");
                        long timeDiff = (System.currentTimeMillis() - timeOfSubTaskStart)/1000;
                        timeOfSubTaskStart = System.currentTimeMillis();
                        if (currentSubTask == 1 && timeDiff <= 15){
                            //Special task for fast kids
                            currentSubTask = bunnySubTasks.length - 1;
                        } else if (currentSubTask >= 3) {
                            situation = Situation.DONE;
                        } else {
                            if (timeDiff <= 30) {
                                currentSubTask = transitions[currentSubTask][0];
                            } else {
                                currentSubTask = transitions[currentSubTask][1];
                            }
                        }

                        if (situation != Situation.DONE) {
                            bunnyBubble.setText("Ja, richtig! Wie viele sind es, wenn ich " + bunnySubTasks[currentSubTask] +
                                    " · 4 Möhren gesammelt habe?");
                            bunnyBubble.setAnimateDots(false);
                            bubbleSetVisible(2, true);
                            //letzte Aufgabe nur für schnelle Kinder die keine Fehler gemacht haben
                        }  else {
                            bunnyBubble.setText("Ja, richtig!");
                            bunnyBubble.setAnimateDots(false);
                            bubbleSetVisible(2, true);
                            handler.postDelayed(bunnyShaker, 5000);
                            situation = Situation.DONE;
                        }



                    } else {
                        bunnyBubble.setText("Das ist noch nicht ganz richtig. Tipp den Beutel an, falls du Hilfe brauchst.");
                        bunnyBubble.setAnimateDots(false);
                        bubbleSetVisible(2, true);

                    }
                }

                InputMethodManager imm = (InputMethodManager)getSystemService(honeyEditText.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(honeyEditText.getWindowToken(), 0);
                return false; // consume.

            }
            return false; // pass on to other listeners.
        }
    }


    private final class BunnyOnClickListener implements View.OnClickListener {
        private int introSubTask = 0;

        @Override
        public void onClick(View view) {
            handler.removeCallbacks(bunnyShaker);
            if (introSubTask == 0) {
                bunnyBubble.setText("Hallo, ich bin Hugo der Hase.");
                bunnyBubble.setAnimateDots(true);
                bubbleSetVisible(2, true);
                for (int i = 0; i < 8; i++) {
                    food[i].setImageResource(R.drawable.carrots);
                    food[i].setVisibility(View.INVISIBLE);
                }

            } else if (introSubTask == 1) {
                bubbleSetVisible(2, true);
                bunnyBubble.setText("Ich liebe Möhren in Bündeln.");
                bunnyBubble.setAnimateDots(true);
            } else if (introSubTask == 2) {
                bubbleSetVisible(2, true);
                bunnyBubble.setText("So sehen sie aus:");
                bunnyBubble.setAnimateDots(true);
            } else if (introSubTask == 3) {
                bubbleSetVisible(2, false);
                bunnyThoughtBubble.setVisibility(View.VISIBLE);
                bunnyBubble.setAnimateDots(false);
            } else if (introSubTask == 4){
                bubbleSetVisible(2, true);
                bunnyThoughtBubble.setVisibility(View.INVISIBLE);
                bunnyBubble.setText("An einem Bund hängen immer 4 Möhren");
                bunnyBubble.setAnimateDots(true);
            } else if (introSubTask == 5) {
                bubbleSetVisible(2, true);
                bunnyBubble.setText("Ich habe 2 · 4 Möhren gesammelt. Wie viele sind im Beutel?");
                bunnyBubble.setAnimateDots(false);
                handler.postDelayed(signShaker, 5000);
                timeOfSubTaskStart = System.currentTimeMillis();
                currentSubTask = 0;
            }
            introSubTask++;
            if (situation == Situation.DONE) {
                Intent intent = new Intent(bunnyBubble.getContext(), Task3.class);
                startActivity(intent);
            }
        }

    }



    private final class GlobalOnTouchListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            InputMethodManager imm = (InputMethodManager)getSystemService(honeyEditText.getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(honeyEditText.getWindowToken(), 0);

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





}
