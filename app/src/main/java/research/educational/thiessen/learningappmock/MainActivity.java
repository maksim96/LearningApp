package research.educational.thiessen.learningappmock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //specify the button that has to handle visibility
        ImageView forest = (ImageView) findViewById(R.id.imageForest);

//And the layout we want to change is visibility
        final ImageView unicorn = (ImageView) findViewById(R.id.imageUnicorn);
        final ImageView bubble = (ImageView) findViewById(R.id.bearBubble);
        final TextView bubbleText = (TextView) findViewById(R.id.bearText);

        forest.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (unicorn.getVisibility() == View.VISIBLE)
                                {
                                    unicorn.setVisibility(View.INVISIBLE);
                                    unicorn.setClickable(false);
                                    bubble.setVisibility(View.INVISIBLE);
                                    bubbleText.setVisibility(View.INVISIBLE);
                                }
                                else
                                {
                                    unicorn.setVisibility(View.VISIBLE);
                                    unicorn.setClickable(true);
                                }
                            }
                        }
                );


//And the layout we want to change is visibility


        unicorn.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (!firstTime) {
                                    int x = (int) (1+Math.random()*10);
                                    int y = (int) (1+Math.random()*10);
                                    bubbleText.setText(""+x+" x " +y + "?");

                                }
                                firstTime = false;

                                if (bubble.getVisibility() == View.VISIBLE)
                                {

                                }
                                else
                                {
                                    bubble.setVisibility(View.VISIBLE);
                                    bubbleText.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                );

        bubbleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Task1.class);
                startActivity(intent);
            }
        });
    }
}
