package at.htl_klu.cardgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;

    private MainThread thread;
    private Background bg;
    private Background bg2;

    public double touchX = 1;
    public double touchY = 1;

    public static boolean start = true;

    private Bitmap rndl;
    private Bitmap healer;
    private Bitmap heretic;
    private Bitmap attacker;
    private Bitmap playerHero;
    private Bitmap kasernebg;
    private Bitmap exitBarracks;
    private Bitmap barracks;
    private Bitmap plus;
    private Bitmap minus;
    private Bitmap priceBlank;
    public Paint paint = new Paint();

    boolean kaserne = false;


    private ArrayList<Handpositions> handpositionsPlayer = new ArrayList<Handpositions>();
    private ArrayList<Handpositions> handpositionsOpponent = new ArrayList<Handpositions>();
    private ArrayList<Handpositions> heroPosition = new ArrayList<>();
    private ArrayList<priceBlank> pricePositions = new ArrayList<>();

    private ArrayList<Barracks> barrackCardPosition = new ArrayList<Barracks>();















    public GamePanel(Context context) {
        super(context);


        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);


        //make gamePanel focusable so it can handle events
        setFocusable(true);


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        kaserne = false;
        for (int i = 0; i <= 6; i++) {


            handpositionsPlayer.add(new Handpositions(getWidth() / 70 + getWidth() / 12 * i + getWidth() / 60 * i, getHeight() - 1 - (getHeight() / 5), getWidth() / 12, getHeight() / 5));

            handpositionsOpponent.add(new Handpositions(getWidth() / 70 + getWidth() / 12 * i + getWidth() / 60 * i, 0, getWidth() / 12, getHeight() / 5));


        }

        //  heroPosition.add(new Handpositions(getWidth() / 70 + getWidth()/12*7 + getWidth()/60*7 , getHeight() - 1 - (getHeight() / 4), getWidth() / 10, getHeight() / 4));
        //  heroPosition.add(new Handpositions(getWidth() / 70 + getWidth()/12*7 + getWidth()/60*7 , -1, getWidth() / 10, getHeight() / 4));
        heroPosition.add(new Handpositions(-1, -1, getWidth() - 1, getHeight() / 4));
        heroPosition.add(new Handpositions(-1, getHeight() - 1 - getHeight() / 4, getWidth() - 1, getHeight() / 4));


        for(int i= 0; i<=6; i++) {
            barrackCardPosition.add(new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight()/3, getWidth() / 12, getHeight() / 5));
            pricePositions.add(new priceBlank(getWidth()/12+getWidth()/12*i+getWidth()/24*i,getHeight()*9/16,getWidth()/12,getHeight()/16));
                    //priceBlank, getWidth() /12 , getHeight() *1/16, false), getWidth()/12, getHeight()*9/16, null
        }




        bg = new Background(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg2), getWidth() - 1, getHeight() - 1, false));
        //bg2 = new Background(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.healer), getWidth() - 1, getHeight() - 1, false));2 = new Background(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.healer), getWidth() - 1, getHeight() - 1, false));
        kasernebg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kasernebg), getWidth() - 1, getHeight() - 1, false);

        rndl = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.peta), 200, 200, false);
        // healer = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.healer), getWidth()/12, getHeight()/5, false);

        healer = BitmapFactory.decodeResource(getResources(), R.drawable.healer);
        attacker = BitmapFactory.decodeResource(getResources(), R.drawable.attacker);
        heretic = BitmapFactory.decodeResource(getResources(), R.drawable.heretiker);
        exitBarracks = BitmapFactory.decodeResource(getResources(), R.drawable.exitbutton);
        barracks = BitmapFactory.decodeResource(getResources(),R.drawable.barracks);
        plus = BitmapFactory.decodeResource(getResources(),R.drawable.plus);
        minus = BitmapFactory.decodeResource(getResources(),R.drawable.minus);
        priceBlank = BitmapFactory.decodeResource(getResources(),R.drawable.priceblank);

        playerHero = BitmapFactory.decodeResource(getResources(), R.drawable.testherowall);


        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //super.onTouchEvent(event);
        touchX = event.getX();
        touchY = event.getY();

        if (touchX > 1 && touchX < getWidth() / 12 && touchY > getHeight() / 3 && touchY < getHeight() / 3 * 2) {
            kaserne = true;


        }
        if (touchX > getWidth() / 8 * 7 && touchX < getWidth() - 1 && touchY < getHeight() / 8 && touchY > 1) {
            kaserne = false;


        }


        //return super.onTouchEvent(event);
        return true;
    }

    public void update() {
        bg.update();
        //bg2.update();
    }

    public boolean collision(GameObject a, GameObject b) {
        return Rect.intersects(a.getRectangle(), b.getRectangle());
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        bg.draw(canvas);

        if (kaserne ==false) {



     /*   canvas.drawBitmap(rndl, handpositions.get(0).getPositionX(), 200, null);
        canvas.drawBitmap(healer,getWidth()/70, getHeight()-1-(getHeight()/5), null);
        canvas.drawBitmap(healer,getWidth()/70 + getWidth()/12 + getWidth()/60, getHeight()-1-(getHeight()/5), null);
        canvas.drawBitmap(healer,getWidth()/70 + getWidth()/12*2 + getWidth()/60*2, getHeight()-1-(getHeight()/5), null);
        canvas.drawBitmap(healer,getWidth()/70 + getWidth()/12*3 + getWidth()/60*3, getHeight()-1-(getHeight()/5), null);
        canvas.drawBitmap(healer,handpositionsPlayer.get(4).getPositionX(),handpositionsPlayer.get(4).getPositionY(), null);
        canvas.drawBitmap(healer,getWidth()/70 + getWidth()/12*5 + getWidth()/60*5, getHeight()-1-(getHeight()/5), null);
        canvas.drawBitmap(healer,getWidth()/70 + getWidth()/12*6 + getWidth()/60*6, getHeight()-1-(getHeight()/5), null);

*/
            canvas.drawBitmap(Bitmap.createScaledBitmap(playerHero, heroPosition.get(0).getLength(), heroPosition.get(0).getVast(), false), heroPosition.get(0).getPositionX(), heroPosition.get(0).getPositionY(), null);
            canvas.drawBitmap(Bitmap.createScaledBitmap(playerHero, heroPosition.get(1).getLength(), heroPosition.get(1).getVast(), false), heroPosition.get(1).getPositionX(), heroPosition.get(1).getPositionY(), null);


            canvas.drawBitmap(Bitmap.createScaledBitmap(healer, handpositionsPlayer.get(4).getLength(), handpositionsPlayer.get(4).getVast(), false), handpositionsPlayer.get(4).getPositionX(), handpositionsPlayer.get(4).getPositionY(), null);
            canvas.drawBitmap(Bitmap.createScaledBitmap(healer, handpositionsOpponent.get(4).getLength(), handpositionsOpponent.get(4).getVast(), false), handpositionsOpponent.get(4).getPositionX(), handpositionsOpponent.get(4).getPositionY(), null);
            canvas.drawBitmap(Bitmap.createScaledBitmap(healer, handpositionsPlayer.get(6).getLength(), handpositionsPlayer.get(6).getVast(), false), handpositionsPlayer.get(6).getPositionX(), handpositionsPlayer.get(6).getPositionY(), null);
            //  canvas.drawBitmap(Bitmap.createScaledBitmap(playerHero, heroPosition.get(0).getLength(), heroPosition.get(0).getVast(),false),heroPosition.get(0).getPositionX(),heroPosition.get(0).getPositionY(), null);
            //  canvas.drawBitmap(Bitmap.createScaledBitmap(playerHero, heroPosition.get(1).getLength(), heroPosition.get(1).getVast(),false),heroPosition.get(1).getPositionX(),heroPosition.get(1).getPositionY(), null);
            canvas.drawBitmap(Bitmap.createScaledBitmap(barracks,getWidth()/6,getHeight()/2,false),getWidth()/100, getHeight()/4,null);
        }

if(kaserne == true){

    canvas.drawBitmap(kasernebg, 0,0, null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(healer, barrackCardPosition.get(0).getLength(),barrackCardPosition.get(0).getVast(), false),barrackCardPosition.get(0).getPositionX(),barrackCardPosition.get(0).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(attacker, barrackCardPosition.get(1).getLength(),barrackCardPosition.get(1).getVast(), false),barrackCardPosition.get(1).getPositionX(),barrackCardPosition.get(1).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(heretic, barrackCardPosition.get(2).getLength(),barrackCardPosition.get(2).getVast(), false),barrackCardPosition.get(2).getPositionX(),barrackCardPosition.get(2).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(healer, barrackCardPosition.get(3).getLength(),barrackCardPosition.get(3).getVast(), false),barrackCardPosition.get(3).getPositionX(),barrackCardPosition.get(3).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(healer, barrackCardPosition.get(4).getLength(),barrackCardPosition.get(4).getVast(), false),barrackCardPosition.get(4).getPositionX(),barrackCardPosition.get(3).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(healer, barrackCardPosition.get(5).getLength(),barrackCardPosition.get(5).getVast(), false),barrackCardPosition.get(5).getPositionX(),barrackCardPosition.get(5).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(healer, barrackCardPosition.get(6).getLength(),barrackCardPosition.get(6).getVast(), false),barrackCardPosition.get(6).getPositionX(),barrackCardPosition.get(6).getPositionY(),null);

    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(0).getLength(),pricePositions.get(0).getVast(), false),pricePositions.get(0).getPositionX(),pricePositions.get(0).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(1).getLength(),pricePositions.get(1).getVast(), false),pricePositions.get(1).getPositionX(),pricePositions.get(1).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(2).getLength(),pricePositions.get(2).getVast(), false),pricePositions.get(2).getPositionX(),pricePositions.get(2).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(3).getLength(),pricePositions.get(3).getVast(), false),pricePositions.get(3).getPositionX(),pricePositions.get(3).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(4).getLength(),pricePositions.get(4).getVast(), false),pricePositions.get(4).getPositionX(),pricePositions.get(4).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(5).getLength(),pricePositions.get(5).getVast(), false),pricePositions.get(5).getPositionX(),pricePositions.get(5).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(6).getLength(),pricePositions.get(6).getVast(), false),pricePositions.get(6).getPositionX(),pricePositions.get(6).getPositionY(),null);
    //  if (touchX > getWidth() / 8 * 7 && touchX < getWidth() - 1 && touchY < getHeight() / 8 && touchY > 1) {
    canvas.drawBitmap(Bitmap.createScaledBitmap(exitBarracks,getWidth()/8,getHeight()/6,false),getWidth()*7/8, getHeight()/35,null);
    for(int i= 0; i<=6; i++) {
        canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, getWidth() /12 , getHeight() *1/16, false), getWidth()/12, getHeight()*9/16, null);

        canvas.drawText("100 Gold",(getWidth() /12) , (getHeight() *1/16),paint);
        //new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight()/3, getWidth() / 12, getHeight() / 5
    }

}



    }

    public void saveInt(String key, int val) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(key, val);
        editor.commit();
    }

    public int loadInt(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getInt(key, 0);
    }


}