package research.educational.thiessen.learningappmock;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import research.educational.thiessen.learningappmock.helpers.NumericKeyBoardTransformationMethod;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Task2 extends Activity {

    private ViewGroup rootLayout;
    private TextView textViewBear;
    private TextView textViewSquirrel;
    private ImageView bubbleBear;
    private ImageView bubbleSquirrel;
    private EditText honeyEditText;
    private int situation = 0;
    private ImageView bear;
    private ImageView honey1;
    private ImageView honey2;
    private ImageView bunny;
    private ImageView bubbleBunny;
    private TextView textViewBunny;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initally hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        setContentView(R.layout.activity_task2);

        rootLayout = (ViewGroup) findViewById(R.id.task2_root);
        rootLayout.setOnClickListener(new GlobalOnTouchListener());

        textViewBear = findViewById(R.id.textViewBear);
        textViewSquirrel = findViewById(R.id.textViewSquirrel);
        bubbleBear = findViewById(R.id.bubbleBear);
        bubbleSquirrel = findViewById(R.id.bubbleSquirrel);
        bear = findViewById(R.id.bear);
        honey1 = findViewById(R.id.honey1);
        honey2 = findViewById(R.id.honey2);
        bunny = findViewById(R.id.bunny);
        bubbleBunny = findViewById(R.id.bunnyBubble);
        textViewBunny = findViewById(R.id.textViewBunny);

        honeyEditText = findViewById(R.id.honeyEdit);
        honeyEditText.setTransformationMethod(new NumericKeyBoardTransformationMethod());


       /* honeyEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus){
                System.out.println("muh!!!: " + hasFocus);
                if (!hasFocus) {
                    if (honeyEditText.getText().equals("6")) {
                        textViewBear.setText("Ja, richtig!");
                    } else {
                        textViewBear.setText("Bist du sicher?");
                    }
                }
            }
        });*/

        honeyEditText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        System.out.println("Actionid: " +  actionId);
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE) {
                                // the user is done typing.
                            if (situation < 3) {
                                if (honeyEditText.getText().toString().equals("6")) {
                                    textViewBear.setText("Ja, richtig!");
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //do this after 3 seconds first
                                            textViewBear.setVisibility(View.INVISIBLE);
                                            bubbleBear.setVisibility(View.INVISIBLE);
                                            textViewBunny.setVisibility(View.VISIBLE);
                                            bunny.setVisibility(View.VISIBLE);
                                            bubbleBunny.setVisibility(View.VISIBLE);
                                            situation = 3;
                                        }
                                    }, 3000);
                                } else {
                                    textViewBear.setText("Bist du sicher?");
                                }
                            } else {
                                if (honeyEditText.getText().toString().equals("8")) {
                                    textViewBunny.setText("Ja, super!");
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(textViewBunny.getContext(), Task3.class);
                                            startActivity(intent);
                                        }
                                    }, 3000);
                                } else {
                                    textViewBunny.setText("Bist du sicher?");
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

                    honey1.setVisibility(View.VISIBLE);
                    honey2.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            honey1.setVisibility(View.INVISIBLE);
                            honey2.setVisibility(View.INVISIBLE);
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
            if (situation == 0) {
                bubbleSetVisible(true, false);
                bubbleSetVisible(false, true);
                situation++;
                return;
            } else if (situation == 1) {
                bubbleSetVisible(true, true);
                bubbleSetVisible(false, false);
                textViewBear.setText("Ich habe 2 x 3 Waben gesammelt. Wie viele sind im Beutel?");
                situation++;
                return;
            } else {
                InputMethodManager imm = (InputMethodManager)getSystemService(honeyEditText.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(honeyEditText.getWindowToken(), 0);

            }

        }
    }

    private void bubbleSetVisible(boolean bear, boolean visible) {
        if (bear) {
            if (visible) {
                bubbleBear.setVisibility(View.VISIBLE);
                textViewBear.setVisibility(View.VISIBLE);
            } else {
                bubbleBear.setVisibility(View.INVISIBLE);
                textViewBear.setVisibility(View.INVISIBLE);
            }
        } else {
            if (visible) {
                bubbleSquirrel.setVisibility(View.VISIBLE);
                textViewSquirrel.setVisibility(View.VISIBLE);
            } else {
                bubbleSquirrel.setVisibility(View.INVISIBLE);
                textViewSquirrel.setVisibility(View.INVISIBLE);
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
