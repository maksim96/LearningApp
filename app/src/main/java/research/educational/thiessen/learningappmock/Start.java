package research.educational.thiessen.learningappmock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.File;
import java.io.FileOutputStream;

public class Start extends Activity {

    private Handler handler;
    private Runnable shaker;
    private Animation bounce;
    public static FileOutputStream outputStream;

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


        int i = 1;
        String fileName;
        File file;
        do {
            fileName = "Kind"+i+".txt";
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            System.out.println(file.getAbsolutePath());
            i++;
        } while (file.exists());
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            //outputStream.write("test".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }





    }

}
