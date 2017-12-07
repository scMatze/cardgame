package at.htl_klu.cardgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new GamePanel(this));

        //set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //turn of the title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    @Override
    protected void onStop()  {
        super.onStop();
    }
}