package research.educational.thiessen.learningappmock;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import research.educational.thiessen.learningappmock.helpers.SpeechBubble;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Task3 extends Activity {

    private int currentSubTask = 0;
    private RelativeLayout plates[];
    private ImageView food[];
    private TextView linesOnBoard[];
    private EditText editTextLeft;
    private EditText editTextRight;
    private EditText editTextProduct;
    private int[] tasksLeft =  {3,5,10,4,6,2,  5,10,4,6,3,  4,8,5};
    private int[] tasksRight = {2,2, 2,2,2,2,  3, 3,3,3,3,  4,4,4};
    private int[][] transitions = {{1},{2,3},{6},{4,5},{6},{6}, {7,8}, {11},{9,10}, {11},{11}, {12,13},{},{}};
    private RelativeLayout rootLayout;
    private ImageView bear;
    private ImageView bunny;
    private ImageView squirrel;
    private SpeechBubble bearBubble;
    private SpeechBubble squirrelBubble;
    private SpeechBubble bunnyBubble;
    private boolean firstTimeAutoFocus = true;
    private ImageView rightFood;
    private boolean completelyDone = false;
    private Handler handler = new Handler();
    private Runnable squirrelShaker;
    private Runnable bearShaker;
    private Runnable bunnyShaker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3);
        //initally hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        rootLayout = findViewById(R.id.task3_root);
        rootLayout.setOnTouchListener(new GlobalOnTouchListener());

        int[] plateIds = {R.id.plate1, R.id.plate2, R.id.plate3, R.id.plate4, R.id.plate5, R.id.plate6, R.id.plate7, R.id.plate8, R.id.plate9, R.id.plate10};
        int[] foodIds = {R.id.food1, R.id.food2, R.id.food3, R.id.food4, R.id.food5,R.id.food6, R.id.food7, R.id.food8, R.id.food9, R.id.food10};

        plates = new RelativeLayout[10];
        food = new ImageView[10];
        for (int i = 0; i < plateIds.length; i++) {
            plates[i] = findViewById(plateIds[i]);
            food[i] = findViewById(foodIds[i]);
        }
        linesOnBoard = new TextView[7];
        int[] lineIds = {R.id.line1, R.id.line2, R.id.line3, R.id.line4, R.id.line5, R.id.line6, R.id.line7};

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/finger_paint.ttf");
        for (int i = 0; i < lineIds.length; i++) {
            linesOnBoard[i] = findViewById(lineIds[i]);
            linesOnBoard[i].setTypeface(custom_font);

        }

        bear = findViewById(R.id.bear);
        squirrel = findViewById(R.id.squirrel);
        bunny = findViewById(R.id.bunny);
        bearBubble = findViewById(R.id.bearBubble);
        bunnyBubble = findViewById(R.id.bunnyBubble);

        editTextLeft = findViewById(R.id.editTextLeft);
        editTextRight = findViewById(R.id.editTextRight);
        editTextProduct = findViewById(R.id.editTextProduct);

        editTextLeft.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        editTextRight.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        editTextProduct.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        editTextLeft.setOnEditorActionListener(new OnDoneListener());
        editTextLeft.addTextChangedListener(new OnNumberMoveOnListener(editTextLeft));
        editTextLeft.setOnFocusChangeListener(new OnLeaveListener());
        editTextRight.setOnEditorActionListener(new OnDoneListener());
        editTextRight.setOnFocusChangeListener(new OnLeaveListener());
        editTextRight.addTextChangedListener(new OnNumberMoveOnListener(editTextRight));
        editTextProduct.setOnEditorActionListener(new OnDoneListener());
        editTextProduct.setOnFocusChangeListener(new OnLeaveListener());

        squirrelBubble = findViewById(R.id.squirrelBubble);

        rightFood = findViewById(R.id.rightImage);

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
        bunnyShaker = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(rootLayout.getContext(), R.anim.shake);
                bunny.startAnimation(shake);
                handler.postDelayed(this, 3000);
            }
        };


        handler.postDelayed(squirrelShaker, 3000);

        squirrelBubble.setAnimateDots(true);
        squirrelBubble.makeDotsSmaller();
    }

    private final class OnLeaveListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            System.out.println(hasFocus);
            if (!hasFocus) {
               /* if (rootLayout.hasFocus()) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }*/
               // checkResult();
            } else {
                if (firstTimeAutoFocus) {
                    firstTimeAutoFocus = false;
                    return;
                }
                squirrelBubble.setVisibility(View.INVISIBLE);
                bunnyBubble.setVisibility(View.INVISIBLE);
                bearBubble.setVisibility(View.INVISIBLE);
                handler.removeCallbacks(squirrelShaker);
                handler.removeCallbacks(bearShaker);
                handler.removeCallbacks(bunnyShaker);

            }
        }
    }

    private final class OnDoneListener implements TextView.OnEditorActionListener {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(editTextProduct.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextProduct.getWindowToken(), 0);

                    checkResult();
                    rootLayout.requestFocus();
                    return true; // consume.

                }
                return true; // pass on to other listeners.
            }

    }

    /*
    private final class OnDoneAndMoveOnListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            System.out.println(actionId);
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager)getSystemService(editTextProduct.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextProduct.getWindowToken(), 0);

                checkResult();
                return true; // consume.

            } else if (actionId == EditorInfo.TYPE_CLASS_NUMBER) {
                View nextView = v.focusSearch(View.FOCUS_RIGHT);
                nextView.requestFocus();
            }
            return false; // pass on to other listeners.
        }

    }*/

   /* private final class OnNumberMoveOnListener implements View.OnKeyListener {

        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() != KeyEvent.KEYCODE_DEL && keyEvent.getKeyCode() != KeyEvent.KEYCODE_ENTER) {
                View nextView = view.focusSearch(View.FOCUS_RIGHT);
                nextView.requestFocus();
            }
        }
    }*/

    private final class OnNumberMoveOnListener implements TextWatcher {

        private View view;

        public OnNumberMoveOnListener(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            handler.removeCallbacks(squirrelShaker);
            handler.removeCallbacks(bearShaker);
            handler.removeCallbacks(bunnyShaker);
            if ((tasksLeft[currentSubTask] < 10 && charSequence.length() > before && charSequence.length() == 1) ||
                    (view.equals(editTextRight)&& charSequence.length() > before)) {
                View nextView = this.view.focusSearch(View.FOCUS_RIGHT);
                nextView.requestFocus();
            } else if (tasksLeft[currentSubTask] >= 10 && charSequence.length() > before && charSequence.length() == 2) {
                View nextView = this.view.focusSearch(View.FOCUS_RIGHT);
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private int totalTasksSolved = 0;
    private long timeOfSubTaskStart;
    private final void checkResult() {
        int left, right, product;
        String leftString = editTextLeft.getText().toString();
        String rightString = editTextRight.getText().toString();
        String productString = editTextProduct.getText().toString();



        if (leftString.isEmpty() || rightString.isEmpty() || productString.isEmpty()) {
            return;
        }

        left = Integer.parseInt(leftString);
        right = Integer.parseInt(rightString);
        product = Integer.parseInt(productString);





        if (tasksLeft[currentSubTask] == left && tasksRight[currentSubTask] == right && product == left*right) {
            long timeDiff = (System.currentTimeMillis() - timeOfSubTaskStart)/1000;
            squirrelBubble.setVisibility(View.INVISIBLE);
            linesOnBoard[totalTasksSolved].setText(left + " \u00B7 " + right + " = " + (left*right));
            if (transitions[currentSubTask].length == 0 || totalTasksSolved >= 6) {
                bearBubble.setVisibility(View.VISIBLE);
                bearBubble.setText("Wow, danke!");
                handler.postDelayed(bearShaker, 3000);
                completelyDone = true;
                return;
            } else if (transitions[currentSubTask].length == 1) {
                currentSubTask = transitions[currentSubTask][0];
            } else if (transitions[currentSubTask].length == 2) {
                if (timeDiff <= 30) {
                    currentSubTask = transitions[currentSubTask][0];
                } else {
                    currentSubTask = transitions[currentSubTask][1];
                }
            }
            totalTasksSolved++;
            updateTable();
           // rootLayout.requestFocus();

          //  rootLayout.invalidate();
        } else {
            if (currentSubTask < 6) {
                squirrelBubble.setVisibility(View.VISIBLE);
                squirrelBubble.setText("Das ist noch nicht ganz richtig.");
            } else if (currentSubTask < 11) {
                bearBubble.setVisibility(View.VISIBLE);
                bearBubble.setText("Das ist noch nicht ganz richtig.");
            } else {
                bunnyBubble.setVisibility(View.VISIBLE);
                bunnyBubble.setText("Das ist noch nicht ganz richtig.");
            }

            editTextLeft.setText("");
            editTextRight.setText("");
            editTextProduct.setText("");


        }
    }

    private final void updateTable() {
        editTextProduct.setText("");
        editTextLeft.setText("");
        editTextRight.setText("");
        timeOfSubTaskStart = System.currentTimeMillis();
        if (currentSubTask < 6) {
            squirrelBubble.setText("Ja richtig! Und jetzt?");
            bubbleSetVisible(0, true);
            handler.postDelayed(squirrelShaker, 3000);
        } else if (currentSubTask == 6) {
            bearBubble.setText(Html.fromHtml("Ja richtig! Und <b>wie viele Waben</b> sind es jetzt?"));
            bubbleSetVisible(1, true);
            bear.setVisibility(View.VISIBLE);
            for (ImageView food: food) {
                food.setImageResource(R.drawable.honey);
            }
            rightFood.setImageResource(R.drawable.honey);
            handler.postDelayed(bearShaker, 3000);
        } else if (currentSubTask < 11) {
            bearBubble.setText("Ja richtig! Und jetzt?");
            bubbleSetVisible(1, true);
            for (ImageView food: food) {
                food.setImageResource(R.drawable.honey);
            }
            handler.postDelayed(bearShaker, 3000);
        } else if (currentSubTask == 11) {
            bunnyBubble.setText(Html.fromHtml("Ja richtig! Und <b>wie viele Möhren</b> sind es jetzt?"));
            bubbleSetVisible(2, true);
            bunny.setVisibility(View.VISIBLE);
            for (ImageView food: food) {
                food.setImageResource(R.drawable.carrots);
            }
            rightFood.setImageResource(R.drawable.carrots);
            handler.postDelayed(bunnyShaker, 3000);
        } else if (currentSubTask > 11) {
            bunnyBubble.setText("Ja richtig! Und jetzt?");
            bubbleSetVisible(2, true);
            for (ImageView food: food) {
                food.setImageResource(R.drawable.carrots);
            }
            handler.postDelayed(bunnyShaker, 3000);
        }

        for (int i = 0; i < plates.length; i++) {
            if (i < tasksLeft[currentSubTask]) {
                plates[i].setVisibility(View.VISIBLE);
            } else {
                plates[i].setVisibility(View.INVISIBLE);
            }
        }



    }

    private final class GlobalOnTouchListener implements View.OnTouchListener {
        private boolean firstTime = true;
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                handler.removeCallbacks(squirrelShaker);
                handler.removeCallbacks(bearShaker);
                handler.removeCallbacks(bunnyShaker);
                if (completelyDone) {
                    Intent intent = new Intent(bunnyBubble.getContext(), Ending.class);
                    startActivity(intent);
                }
                if (firstTime) {
                    squirrelBubble.setText(Html.fromHtml("Mit welcher <b>Aufgabe</b> kann ich errechnen, <b>wie viel</b> auf den Tellern liegt?"));
                    firstTime = false;
                } else {
                    squirrelBubble.setVisibility(View.INVISIBLE);
                    bunnyBubble.setVisibility(View.INVISIBLE);
                    bearBubble.setVisibility(View.INVISIBLE);



                    InputMethodManager imm = (InputMethodManager)getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    checkResult();
                }
            }




            return true;
        }
    }

    /*
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


    }*/
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

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }



}
