package research.educational.thiessen.learningappmock;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Task3 extends Activity {

    private int currentSubTask = 1;
    private RelativeLayout plates[];
    private ImageView food[];
    private TextView linesOnBoard[];
    private EditText editTextLeft;
    private EditText editTextRight;
    private EditText editTextProduct;
    private int[] tasksLeft = {5,2,3,4,2,5};
    private int[] tasksRight = {2,2,3,3,4,4};
    private RelativeLayout rootLayout;
    private ImageView bear;
    private ImageView bunny;
    private ImageView squirrel;
    private ImageView bearBubble;
    private TextView bearText;
    private TextView squirrelText;
    private ImageView squirrelBubble;
    private ImageView bunnyBubble;
    private TextView bunnyText;
    private boolean firstTimeAutoFocus = true;
    private ImageView rightFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3);
        //initally hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        rootLayout = findViewById(R.id.task3_root);
        rootLayout.setOnClickListener(new GlobalOnTouchListener());

        int[] plateIds = {R.id.plate1, R.id.plate2, R.id.plate3, R.id.plate4, R.id.plate5};
        int[] foodIds = {R.id.food1, R.id.food2, R.id.food3, R.id.food4, R.id.food5};

        plates = new RelativeLayout[5];
        food = new ImageView[5];
        for (int i = 0; i < plateIds.length; i++) {
            plates[i] = findViewById(plateIds[i]);
            food[i] = findViewById(foodIds[i]);
        }
        linesOnBoard = new TextView[6];
        int[] lineIds = {R.id.line1, R.id.line2, R.id.line3, R.id.line4, R.id.line5, R.id.line6};

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/finger_paint.ttf");
        for (int i = 0; i < lineIds.length; i++) {
            linesOnBoard[i] = findViewById(lineIds[i]);
            linesOnBoard[i].setTypeface(custom_font);

        }

        bear = findViewById(R.id.bear);
        squirrel = findViewById(R.id.squirrel);
        bunny = findViewById(R.id.bunny);
        bearBubble = findViewById(R.id.bearBubble);
        bearText = findViewById(R.id.bearText);
        bunnyBubble = findViewById(R.id.bunnyBubble);
        bunnyText = findViewById(R.id.bunnyText);

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
        squirrelText = findViewById(R.id.squirrelText);

        rightFood = findViewById(R.id.rightImage);

    }

    private final class OnLeaveListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            System.out.println(hasFocus);
            if (!hasFocus) {
                checkResult();
            } else {
                if (firstTimeAutoFocus) {
                    firstTimeAutoFocus = false;
                    return;
                }
                squirrelBubble.setVisibility(View.INVISIBLE);
                squirrelText.setVisibility(View.INVISIBLE);
                bunnyBubble.setVisibility(View.INVISIBLE);
                bunnyText.setVisibility(View.INVISIBLE);
                bearBubble.setVisibility(View.INVISIBLE);
                bearText.setVisibility(View.INVISIBLE);
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
                    return true; // consume.

                }
                return false; // pass on to other listeners.
            }

    }

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

    }

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
            if (charSequence.length() > before) {
                View nextView = this.view.focusSearch(View.FOCUS_RIGHT);
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

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





        int i = currentSubTask-1;

        if (tasksLeft[i] == left && tasksRight[i] == right && product == left*right) {
            squirrelBubble.setVisibility(View.INVISIBLE);
            squirrelText.setVisibility(View.INVISIBLE);
            linesOnBoard[i].setText(left + " x " + right + " = " + (left*right));
            if (currentSubTask == 6) {
                bearBubble.setVisibility(View.VISIBLE);
                bearText.setVisibility(View.VISIBLE);
                bearText.setText("Wow! Danke");
                return;
            } else {
                currentSubTask++;
                updateTable();
            }

          //  rootLayout.invalidate();
        } else {
            squirrelBubble.setVisibility(View.VISIBLE);
            squirrelText.setVisibility(View.VISIBLE);
            squirrelText.setText("Bist du sicher?");
        }
    }

    private final void updateTable() {
        System.out.println("+++" + currentSubTask);
        editTextProduct.setText("");
        editTextLeft.setText("");
        editTextRight.setText("");
        switch (currentSubTask) {
            case 2:
                for (int i = 2; i < plates.length; i++) {
                    plates[i].setVisibility(View.INVISIBLE);
                }
                squirrelText.setText("Richtig! Wie viele Nüsse sind es nun?");
                bubbleSetVisible(0, true);
                break;
            case 3:
                plates[0].setVisibility(View.VISIBLE);
                plates[1].setVisibility(View.VISIBLE);
                plates[2].setVisibility(View.VISIBLE);
                plates[3].setVisibility(View.INVISIBLE);
                plates[4].setVisibility(View.INVISIBLE);

                for (ImageView food: food) {
                    food.setImageResource(R.drawable.honey);
                }
                rightFood.setImageResource(R.drawable.honey);
                bear.setVisibility(View.VISIBLE);
                bearBubble.setVisibility(View.VISIBLE);
                bearText.setVisibility(View.VISIBLE);

                break;
            case 4:
                plates[3].setVisibility(View.VISIBLE);
                bearText.setText("Genau! Und jetzt?");
                bubbleSetVisible(1, true);
                break;
            case 5:
                plates[2].setVisibility(View.INVISIBLE);
                plates[3].setVisibility(View.INVISIBLE);
                plates[4].setVisibility(View.INVISIBLE);
                for (ImageView food: food) {
                    food.setImageResource(R.drawable.carrots);
                }
                rightFood.setImageResource(R.drawable.carrots);
                bunny.setVisibility(View.VISIBLE);
                bunnyBubble.setVisibility(View.VISIBLE);
                bunnyText.setVisibility(View.VISIBLE);
                break;
            case 6:
                plates[2].setVisibility(View.VISIBLE);
                plates[3].setVisibility(View.VISIBLE);
                plates[4].setVisibility(View.VISIBLE);
                bunnyText.setText("Klasse! Wie viele Möhren gibt es zum Nachtisch?");
                bubbleSetVisible(2, true);
                break;
        }
    }

    private final class GlobalOnTouchListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            squirrelBubble.setVisibility(View.INVISIBLE);
            squirrelText.setVisibility(View.INVISIBLE);
            bunnyBubble.setVisibility(View.INVISIBLE);
            bunnyText.setVisibility(View.INVISIBLE);
            bearBubble.setVisibility(View.INVISIBLE);
            bearText.setVisibility(View.INVISIBLE);

            InputMethodManager imm = (InputMethodManager)getSystemService(view.getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            checkResult();

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

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }



}
