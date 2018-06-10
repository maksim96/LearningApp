package research.educational.thiessen.learningappmock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import research.educational.thiessen.learningappmock.helpers.NumericKeyBoardTransformationMethod;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Task2 extends Activity {

    private ViewGroup rootLayout;
    private TextView bearText;
    private TextView squirrelText;
    private ImageView bearBubble;
    private ImageView squirrelBubble;
    private EditText honeyEditText;
    private int situation = 0;
    private ImageView bear;
    private ImageView food1;
    private ImageView food2;
    private ImageView food3;
    private ImageView bunny;
    private ImageView bunnyBubble;
    private TextView bunnyText;
    private ImageView squirrel;
    private Runnable squirrelShaker;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initally hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        setContentView(R.layout.activity_task2);

        rootLayout = (ViewGroup) findViewById(R.id.task2_root);
        rootLayout.setOnClickListener(new GlobalOnTouchListener());

        bearText = findViewById(R.id.textViewBear);
        squirrelText = findViewById(R.id.textViewSquirrel);
        bearBubble = findViewById(R.id.bubbleBear);
        squirrelBubble = findViewById(R.id.bubbleSquirrel);
        bear = findViewById(R.id.bear);
        food1 = findViewById(R.id.honey1);
        food2 = findViewById(R.id.honey2);
        food3 = findViewById(R.id.honey3);
        bunny = findViewById(R.id.bunny);
        bunnyBubble = findViewById(R.id.bunnyBubble);
        bunnyText = findViewById(R.id.textViewBunny);

        squirrel = findViewById(R.id.squirrel);

        honeyEditText = findViewById(R.id.honeyEdit);
        honeyEditText.setTransformationMethod(new NumericKeyBoardTransformationMethod());


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

        honeyEditText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        System.out.println("Actionid: " +  actionId);
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE) {
                                // the user is done typing.
                            if (situation < 4) {
                                if (honeyEditText.getText().toString().equals(Integer.toString((situation)*3))) {
                                    honeyEditText.setText("");
                                    if (situation == 2) {
                                        bearText.setText("Ja, richtig! Wie viele Waben habe ich bei 3 x 3?");
                                        bubbleSetVisible(1, true);

                                    } else {
                                        bearText.setText("Ja, richtig! Danke!");
                                        bubbleSetVisible(1, true);


                                        handler.postDelayed(squirrelShaker, 3000);
                                    }


                                   situation++;
                                } else {
                                    squirrelText.setText("Leider falsch. Tipp den Bären an, wenn du Hilfe brauchst.");
                                    bubbleSetVisible(0, true);
                                }
                            } else {
                                if (honeyEditText.getText().toString().equals(Integer.toString((situation-2)*4))) {
                                    honeyEditText.setText("");
                                    if (situation == 4) {
                                        bunnyText.setText("Ja, super! Und wie viele Möhren habe ich bei 3 x 4?");
                                        bubbleSetVisible(2, true);
                                    } else {
                                        bunnyText.setText("Ja, super! Vielen Dank");
                                        bubbleSetVisible(2, true);
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                                                squirrel.startAnimation(shake);
                                            }
                                        }, 3000);
                                    }

                                   situation++;
                                } else {
                                    squirrelText.setText("Leider falsch. Tipp den Hasen an, wenn du Hilfe brauchst.");
                                    bubbleSetVisible(0, true);
                                }
                            }

                                InputMethodManager imm = (InputMethodManager)getSystemService(honeyEditText.getContext().INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(honeyEditText.getWindowToken(), 0);
                                return true; // consume.

                        }
                        return false; // pass on to other listeners.
                    }
                }
        );

        bear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    food1.setVisibility(View.VISIBLE);
                    food2.setVisibility(View.VISIBLE);
                    if (situation == 3) {
                        food3.setVisibility(View.VISIBLE);
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            food1.setVisibility(View.INVISIBLE);
                            food2.setVisibility(View.INVISIBLE);
                            food3.setVisibility(View.INVISIBLE);
                        }
                    }, 1500);
                }
                return true;
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


    }

    private final class GlobalOnTouchListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            handler.removeCallbacks(squirrelShaker);
            if (situation == 0) {
                bubbleSetVisible(0, true);
                situation++;
                return;
            } else if (situation == 1) {
                bubbleSetVisible(1, true);
                bearText.setText("Ich habe 2 x 3 Waben gesammelt. Wie viele sind im Beutel?");
                situation++;
                return;
            } else if (situation == 4)  {
                bunny.setVisibility(View.VISIBLE);

                bunny.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                            food1.setVisibility(View.VISIBLE);
                            food2.setVisibility(View.VISIBLE);
                            if (situation == 5) {
                                food3.setVisibility(View.VISIBLE);
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    food1.setVisibility(View.INVISIBLE);
                                    food2.setVisibility(View.INVISIBLE);
                                    food3.setVisibility(View.INVISIBLE);
                                }
                            }, 1500);
                        }
                        return true;
                    }
                });
                food1.setImageResource(R.drawable.carrots);
                food2.setImageResource(R.drawable.carrots);
                food3.setImageResource(R.drawable.carrots);

                bubbleSetVisible(2, true);
            } else if (situation == 6) {
                Intent intent = new Intent(bunnyText.getContext(), Task3.class);
                startActivity(intent);
            }
            InputMethodManager imm = (InputMethodManager)getSystemService(honeyEditText.getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(honeyEditText.getWindowToken(), 0);

        }
    }

    private void bubbleSetVisible(int animal, boolean visible) {
        if (animal == 0) {
            if (visible) {
                squirrelBubble.setVisibility(View.VISIBLE);
                squirrelText.setVisibility(View.VISIBLE);
                bunnyBubble.setVisibility(View.INVISIBLE);
                bunnyText.setVisibility(View.INVISIBLE);
                bearBubble.setVisibility(View.INVISIBLE);
                bearText.setVisibility(View.INVISIBLE);
            } else {
                squirrelBubble.setVisibility(View.INVISIBLE);
                squirrelText.setVisibility(View.INVISIBLE);
            }

        } else if (animal == 1) {
            if (visible) {
                bearBubble.setVisibility(View.VISIBLE);
                bearText.setVisibility(View.VISIBLE);
                squirrelBubble.setVisibility(View.INVISIBLE);
                squirrelText.setVisibility(View.INVISIBLE);
                bunnyBubble.setVisibility(View.INVISIBLE);
                bunnyText.setVisibility(View.INVISIBLE);
            } else {
                bearBubble.setVisibility(View.INVISIBLE);
                bearText.setVisibility(View.INVISIBLE);
            }
        } else if (animal == 2) {
            if (visible) {
                bunnyBubble.setVisibility(View.VISIBLE);
                bunnyText.setVisibility(View.VISIBLE);
                squirrelBubble.setVisibility(View.INVISIBLE);
                squirrelText.setVisibility(View.INVISIBLE);
                bearBubble.setVisibility(View.INVISIBLE);
                bearText.setVisibility(View.INVISIBLE);
            } else {
                bunnyBubble.setVisibility(View.INVISIBLE);
                bunnyText.setVisibility(View.INVISIBLE);
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
