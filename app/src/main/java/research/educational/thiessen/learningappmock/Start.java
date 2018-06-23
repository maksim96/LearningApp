package research.educational.thiessen.learningappmock;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class Start extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.squirrel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Task1.class);
                startActivity(intent);
            }
        });

    }

}
