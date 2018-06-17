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
    private int subTask = 0;
    private ImageView bear;
    private ImageView[] food = new ImageView[7];
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
    private final int[] bearSubTasks = {2,3,4,6,8};
    private final int[] bunnySubTasks = {2,3,4,5,7};
    private int currentSubTask = 0;
    private boolean madeMistakes = false;
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

        int[] foodIds = {R.id.honey1, R.id.honey2, R.id.honey3, R.id.honey4, R.id.honey5, R.id.honey6, R.id.honey7};
        for (int i = 0; i < 7; i++) {
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
                honeyEditText.requestFocus();
                handler.removeCallbacks(signShaker);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });

       /* honeyEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus){
                System.out.println("muh!!!: " + hasFocus);
                if (!hasFocus) {
                    if (honeyEditText.getText().equals("6")) {
                        bearText.setText("Ja, richtig!");
                    } else {
                        bearText.setText("Bist du sicher?");
                    }
                }
            }
        });*/

        squirrelShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                squirrel.startAnimation(shake);
            }
        };

        bearShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                bear.startAnimation(shake);
            }
        };
        signShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                sign.startAnimation(shake);
            }
        };
        bunnyShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                bunny.startAnimation(shake);
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
                    } else if (introSubTask == 1) {
                        bearBubble.setVisibility(View.INVISIBLE);
                        bearThoughtBubble.setVisibility(View.VISIBLE);
                    } else if (introSubTask == 2) {
                        bearThoughtBubble.setVisibility(View.INVISIBLE);
                        bearBubble.setText("Es kleben immer 3 Waben aneinander.");
                        bearBubble.setVisibility(View.VISIBLE);
                    } else if (introSubTask == 3){
                        bearBubble.setVisibility(View.VISIBLE);
                        bearBubble.setText("Ich habe 2 · 3 Waben gesammelt und in meinen Beutel getan.");
                    } else if (introSubTask == 4) {
                        bearBubble.setVisibility(View.VISIBLE);
                        bearBubble.setText("Tipp mal den Beutel an!");
                        bag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int currentFoodCount;
                                if (situation == Situation.TASK1) {
                                    currentFoodCount= bearSubTasks[currentSubTask];
                                } else {
                                    currentFoodCount = bunnySubTasks[currentSubTask];
                                }
                                for (int i = 0; i < 7; i++) {
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
                                        for (int i = 0; i < 7; i++) {
                                                food[i].setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }, 1500);
                            }
                        });
                    } else if (introSubTask == 5) {
                        bearBubble.setVisibility(View.VISIBLE);
                        bearBubble.setText("Kannst du mir auf das Schild schreiben, wie viele Waben ich habe?");
                        handler.postDelayed(signShaker, 5000);
                        situation = Situation.TASK1;
                        honeyEditText.setOnEditorActionListener(new OnSignEdit());
                        timeOfSubTaskStart = System.currentTimeMillis();
                    }
                    introSubTask++;
                } else if (situation == Situation.TASK1) {
                    if (bearDone) {
                        bubbleSetVisible(1, true);
                        bearBubble.setText("Schau mal, wer im Wald noch sammeln war.");
                        bunny.setVisibility(View.VISIBLE);
                        bunny.setOnClickListener(new BunnyOnClickListener());
                        situation = Situation.TASK2;
                        handler.postDelayed(bunnyShaker, 5000);

                    }
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
                handler.postDelayed(bearShaker, 5000);
            }
        });




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
                        if (madeMistakes || currentSubTask > 0) {
                            currentSubTask++;
                        } else {
                            currentSubTask += 2;
                        }

                        long timeDiff = (System.currentTimeMillis() - timeOfSubTaskStart)/1000;

                        if (currentSubTask < bearSubTasks.length-2) {
                            bearBubble.setText("Ja, richtig! Wie viele Waben habe ich bei " + bearSubTasks[currentSubTask] +
                                    " · 3?");
                            bubbleSetVisible(1, true);
                        //letzte Aufgabe nur für schnelle Kinder die keine Fehler gemacht haben
                        } else if (currentSubTask == bearSubTasks.length-2 && timeDiff <= 30 && !madeMistakes){
                            bearBubble.setText("Ja, richtig! Wie viele Waben habe ich bei " + bearSubTasks[currentSubTask] +
                                    " · 3?");
                            bubbleSetVisible(1, true);
                        } else {
                            bearBubble.setText("Ja, richtig!");
                            bubbleSetVisible(1, true);
                            handler.postDelayed(bearShaker, 5000);
                            bearDone = true;
                        }



                    } else {
                        bearBubble.setText("Das ist noch nicht ganz richtig. Tipp den Beutel an, falls du Hilfe brauchst.");
                        bubbleSetVisible(1, true);
                        madeMistakes = true;
                    }
                } else if (situation == Situation.TASK2) {
                    if (honeyEditText.getText().toString().equals(Integer.toString((bunnySubTasks[currentSubTask])*4))) {
                        honeyEditText.setText("");
                        if (madeMistakes || currentSubTask != 1) {
                            currentSubTask++;
                        } else {
                            currentSubTask += 2;
                        }

                        long timeDiff = (System.currentTimeMillis() - timeOfSubTaskStart)/1000;

                        if (currentSubTask < bunnySubTasks.length-3) {
                            bunnyBubble.setText("Ja, richtig! Wie viele Möhren habe ich bei " + bunnySubTasks[currentSubTask] +
                                    " · 4?");
                            bubbleSetVisible(2, true);
                            //letzte Aufgabe nur für schnelle Kinder die keine Fehler gemacht haben
                        } else if (currentSubTask >= bunnySubTasks.length-3 && timeDiff <= 30 && !madeMistakes && currentSubTask <= bunnySubTasks.length-1){
                            bunnyBubble.setText("Ja, richtig! Wie viele Möhren habe ich bei " + bunnySubTasks[currentSubTask] +
                                    " · 4?");
                            bubbleSetVisible(2, true);
                        } else {
                            bunnyBubble.setText("Ja, richtig!");
                            bubbleSetVisible(2, true);
                            handler.postDelayed(bunnyShaker, 5000);
                            situation = Situation.DONE;
                        }



                    } else {
                        bunnyBubble.setText("Das ist noch nicht ganz richtig. Tipp den Beutel an, falls du Hilfe brauchst.");
                        bubbleSetVisible(1, true);
                        madeMistakes = true;

                    }
                }

                InputMethodManager imm = (InputMethodManager)getSystemService(honeyEditText.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(honeyEditText.getWindowToken(), 0);
                return true; // consume.

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
                bubbleSetVisible(2, true);
                for (int i = 0; i < 7; i++) {
                    food[i].setImageResource(R.drawable.carrots);
                    food[i].setVisibility(View.INVISIBLE);
                }

            } else if (introSubTask == 1) {
                bubbleSetVisible(2, true);
                bunnyBubble.setText("Ich liebe Möhren in Bündeln.");
            } else if (introSubTask == 2) {
                bubbleSetVisible(2, true);
                bunnyBubble.setText("So sehen sie aus:");
            } else if (introSubTask == 3) {
                bubbleSetVisible(2, false);
                bunnyThoughtBubble.setVisibility(View.VISIBLE);
            } else if (introSubTask == 4){
                bubbleSetVisible(2, true);
                bunnyThoughtBubble.setVisibility(View.INVISIBLE);
                bunnyBubble.setText("An einem Bund hängen immer 4 Möhren");
            } else if (introSubTask == 5) {
                bubbleSetVisible(2, true);
                bunnyBubble.setText("Ich habe 2 · 4 Möhren gesammelt. Wie viele sind im Beutel?");
                handler.postDelayed(signShaker, 5000);
                timeOfSubTaskStart = System.currentTimeMillis();
                madeMistakes = false;
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
