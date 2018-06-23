package research.educational.thiessen.learningappmock;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Start extends Activity {

    private Handler handler;
    private Runnable shaker;
    private Animation bounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        bounce = AnimationUtils.loadAnimation(findViewById(R.id.squirrel).getContext(), R.anim.bounce);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                shaker = new Runnable() {
                    @Override
                    public void run() {
                        Animation shake = AnimationUtils.loadAnimation(findViewById(R.id.squirrel).getContext(), R.anim.shake_helper);
                        findViewById(R.id.squirrel).startAnimation(shake);
                        findViewById(R.id.start_root).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(view.getContext(), Task1.class);
                                startActivity(intent);
                            }
                        });
                    }
                };
                handler.postDelayed(shaker, 3000);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        bounce.setAnimationListener(animationListener);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                findViewById(R.id.squirrel).startAnimation(bounce);
            }
        }, 3000);






    }

}
