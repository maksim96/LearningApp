package research.educational.thiessen.learningappmock;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
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
    private ImageView bubble;
    private TextView bubbleText;
    private TextView squirrelText;
    private ImageView squirrelBubble;
    private boolean firstTimeAutoFocus = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3);
        //initally hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        rootLayout = findViewById(R.id.task3_root);

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

        for (int i = 0; i < plateIds.length; i++) {
            linesOnBoard[i] = findViewById(lineIds[i]);
        }

        bear = findViewById(R.id.bear);
        squirrel = findViewById(R.id.squirrel);
        bunny = findViewById(R.id.bunny);
        bubble = findViewById(R.id.bubble);
        bubbleText = findViewById(R.id.bubbleText);

        editTextLeft = findViewById(R.id.editTextLeft);
        editTextRight = findViewById(R.id.editTextRight);
        editTextProduct = findViewById(R.id.editTextProduct);

        editTextLeft.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        editTextRight.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        editTextProduct.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        editTextLeft.setOnEditorActionListener(new OnDoneListener());
        editTextLeft.setOnFocusChangeListener(new OnLeaveListener());
        editTextRight.setOnEditorActionListener(new OnDoneListener());
        editTextRight.setOnFocusChangeListener(new OnLeaveListener());
        editTextProduct.setOnEditorActionListener(new OnDoneListener());
        editTextProduct.setOnFocusChangeListener(new OnLeaveListener());

        squirrelBubble = findViewById(R.id.squirrelBubble);
        squirrelText = findViewById(R.id.squirrelText);

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

            if (currentSubTask == 6) {
                bubble.setVisibility(View.VISIBLE);
                bubbleText.setVisibility(View.VISIBLE);
                return;
            } else {
                linesOnBoard[i].setText(left + " x " + right + " = " + (left*right));
                currentSubTask++;
                updateTable();
            }

          //  rootLayout.invalidate();
        } else {
            squirrelBubble.setVisibility(View.VISIBLE);
            squirrelText.setVisibility(View.VISIBLE);
            squirrelText.setText("Leider verzählt!");
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
                bear.setVisibility(View.VISIBLE);

                break;
            case 4:
                plates[3].setVisibility(View.VISIBLE);
                break;
            case 5:
                plates[2].setVisibility(View.INVISIBLE);
                plates[3].setVisibility(View.INVISIBLE);
                plates[4].setVisibility(View.INVISIBLE);
                for (ImageView food: food) {
                    food.setImageResource(R.drawable.carrots);
                }
                bunny.setVisibility(View.VISIBLE);
                break;
            case 6:
                plates[2].setVisibility(View.VISIBLE);
                plates[3].setVisibility(View.VISIBLE);
                plates[4].setVisibility(View.VISIBLE);
        }
    }
    /*
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
                textViewBear.setText("Kannst du mir sagen wie viele Honigwaben ich habe, " +
                        "wenn ich 2 x 3 Waben habe?");
                situation++;
                return;
            } else {
                InputMethodManager imm = (InputMethodManager)getSystemService(honeyEditText.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(honeyEditText.getWindowToken(), 0);

            }

        }
    }*/
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



    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }



}
