package at.htl_klu.cardgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.util.ArrayList;
import java.util.Collections;

import static at.htl_klu.cardgame.MainThread.canvas;

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
    public static boolean healerScale = false;

    private Bitmap rndl;
    private Bitmap healer;
    private Bitmap healerBig;
    private Bitmap heretic;
    private Bitmap attacker;
    private Bitmap playerHero;
    private Bitmap kasernebg;
    private Bitmap exitBarracks;
    private Bitmap barracks;
    private Bitmap plus;
    private Bitmap minus;
    private Bitmap pieceBlank;
    private Bitmap transparentBG;
    private Bitmap priceBlank;
    private boolean connected = false;
    private Bitmap create;
    private Bitmap join;
    private Bitmap spiegel;
    private Bitmap blitz;
    private Bitmap assasine;
    private Bitmap smallscaledSpiegel;
    private Bitmap smallscaledBlitz;
    private Bitmap smallscaledAssasine;
    private Bitmap scaledPriceWanted;
    private Bitmap scaledWanted;
    private Bitmap kasernenscaledhealer;
    private Bitmap kasenenscaledattacker;
    private Bitmap kasernenscaledheretic;
    private Bitmap endturnbutton;
    private Bitmap enemyturnpicture;
    private boolean server;
    private boolean client;
    boolean part;
    boolean threadstarted;
    private CommunicationThread ct;
    private Paint paintShade;
    private boolean playerhasturn;
    private boolean endofturn;
    private boolean enemyhasturn;
    private int firstturn;
    private FieldCard fieldCards[] = new FieldCard[7];
    private HandCard handCardsName[] = new HandCard[7];
    private ArrayList<String> deckCards = new ArrayList<>();
    private int playermoneycounter = 5;
    private int enemymoneycounter = 5;
    private int deckremovecounter = 19;
    private boolean touch = false;
    private boolean touch1 = false;
    private boolean touch2 = false;
    private boolean touch3 = false;
    private boolean touch4 = false;
    private boolean touch5 = false;
    private boolean touch6 = false;





    private Bitmap priceWanted;
    private Bitmap wanted;
    public Paint paint;
    public Shader shader;





    boolean kaserne = false;
    boolean extendCard = false;

    private CardOffers healers = new CardOffers("Healer", 100, 1);
    private CardOffers attackers = new CardOffers("Attacker", 100, 1);
    private CardOffers heretics = new CardOffers("Heretiker", 200, 1);
    private CardOffers ghosts = new CardOffers("Geist", 400, 1);
    private CardOffers druids = new CardOffers("Heretiker", 300, 1);
    private CardOffers pioneers = new CardOffers("Pionier", 200, 1);
    private CardOffers bannerCarriers = new CardOffers("Bannerträger", 500, 1);


    private ArrayList<Handpositions> handpositionsPlayer = new ArrayList<Handpositions>();
    private ArrayList<Handpositions> handpositionsOpponent = new ArrayList<Handpositions>();
    private ArrayList<Handpositions> heroPosition = new ArrayList<>();
    private ArrayList<priceBlank> pricePositions = new ArrayList<>();
    private ArrayList<Barracks> barrackCardPosition = new ArrayList<Barracks>();
    private ArrayList<Wanted> wantedPosition = new ArrayList<>();
    private ArrayList<priceWanted> priceWantedPosition = new ArrayList<>();


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


        for (int i = 0; i <= 6; i++) {
            barrackCardPosition.add(new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight() / 3, getWidth() / 12, getHeight() / 5));
            //pricePositions.add(new priceBlank(getWidth()/12+getWidth()/12*i+getWidth()/24*i,getHeight()*9/16,getWidth()/12,getHeight()/16));
            wantedPosition.add(new Wanted(getWidth() / 16 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight() * 5 / 16, getWidth() / 8, getHeight() / 4));
            priceWantedPosition.add(new priceWanted(getWidth() / 16 + getWidth() / 12 * i + getWidth() / 22 * i, getHeight() * 9 / 16, getWidth() / 8, getHeight() / 4));

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
        barracks = BitmapFactory.decodeResource(getResources(), R.drawable.barracks);
        wanted = BitmapFactory.decodeResource(getResources(), R.drawable.steckbrief);
        priceWanted = BitmapFactory.decodeResource(getResources(), R.drawable.pricewanted);
        transparentBG = BitmapFactory.decodeResource(getResources(), R.drawable.transparent);
        healerBig = BitmapFactory.decodeResource(getResources(), R.drawable.healer);
        healerBig = Bitmap.createScaledBitmap(healer,getWidth() / 3, getHeight()*3/4,false);
        pieceBlank = BitmapFactory.decodeResource(getResources(), R.drawable.pieceblank);
        pieceBlank = Bitmap.createScaledBitmap(pieceBlank,getWidth()*4/16, getHeight()*2/16,false);
        plus = BitmapFactory.decodeResource(getResources(), R.drawable.plus);
        plus = Bitmap.createScaledBitmap(plus,getWidth()*2/16,getHeight()*2/16,false);
        minus = BitmapFactory.decodeResource(getResources(), R.drawable.minus);
        minus = Bitmap.createScaledBitmap(minus,getWidth()*2/16,getHeight()*2/16,false);
        priceBlank = BitmapFactory.decodeResource(getResources(), R.drawable.priceblank);
        priceBlank = Bitmap.createScaledBitmap(priceBlank,getWidth()*4/16, getHeight()*2/16,false);
        blitz = BitmapFactory.decodeResource(getResources(), R.drawable.lightningneu);
        spiegel = BitmapFactory.decodeResource(getResources(), R.drawable.mirrorneu);
        assasine = BitmapFactory.decodeResource(getResources(), R.drawable.assasineneu);
        endturnbutton = BitmapFactory.decodeResource(getResources(),R.drawable.endturnbutton);
        enemyturnpicture = BitmapFactory.decodeResource(getResources(), R.drawable.opponentsturnpicture);
        endturnbutton = Bitmap.createScaledBitmap(endturnbutton, getWidth()/20, getHeight()/9, false);
        enemyturnpicture = Bitmap.createScaledBitmap(enemyturnpicture, getWidth()/20, getHeight()/9, false);
        smallscaledAssasine = Bitmap.createScaledBitmap(assasine, handpositionsPlayer.get(0).getLength(),
                handpositionsPlayer.get(0).getVast(), false);
        smallscaledSpiegel = Bitmap.createScaledBitmap(spiegel, handpositionsPlayer.get(0).getLength(),
                handpositionsPlayer.get(0).getVast(), false);
        smallscaledBlitz = Bitmap.createScaledBitmap(blitz, handpositionsPlayer.get(0).getLength(),
                handpositionsPlayer.get(0).getVast(), false);
        scaledPriceWanted = Bitmap.createScaledBitmap(priceWanted, priceWantedPosition.get(0).getLength(), priceWantedPosition.get(0).getVast(), false);
        scaledWanted = Bitmap.createScaledBitmap(wanted, wantedPosition.get(0).getLength(), wantedPosition.get(0).getVast(), false);
        kasernenscaledhealer = Bitmap.createScaledBitmap(healer, barrackCardPosition.get(0).getLength(), barrackCardPosition.get(0).getVast(), false);
        kasenenscaledattacker = Bitmap.createScaledBitmap(attacker, barrackCardPosition.get(1).getLength(), barrackCardPosition.get(1).getVast(), false);
        kasernenscaledheretic = Bitmap.createScaledBitmap(heretic, barrackCardPosition.get(2).getLength(), barrackCardPosition.get(2).getVast(), false);



        playerHero = BitmapFactory.decodeResource(getResources(), R.drawable.testherowall);

        create = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cra), getWidth()/2, getHeight()/5, false);
        join = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.join), getWidth()/2, getHeight()/5, false);
        connected = false;
        server = false;
        client = false;
        part = true;
        threadstarted = false;

        paint = new Paint();
        paint.setColor(Color.BLACK);


        endofturn = false;


        for(int i = 0; i <8; i++){
            deckCards.add("Blitz");

            }
        for(int i = 0; i <8; i++){
            deckCards.add("Assasine");

        }

        for(int i = 0; i <4; i++){
            deckCards.add("Spiegel");

        }

        Collections.shuffle(deckCards);

        for(int i = 0; i < 7; i++){
            handCardsName[i] = new HandCard(deckCards.get(deckremovecounter),true);
            deckCards.remove(deckremovecounter);

            deckremovecounter--;
        }



        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();


        ct = new CommunicationThread("10.66.11.18", 8888);

        ct = new CommunicationThread("10.66.12.159", 8888, this);



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //super.onTouchEvent(event);
        touchX = event.getX();
        touchY = event.getY();

        if(connected == false && touchX < getWidth()/4*3 && touchX > getWidth()/4 && touchY < getHeight()/5*2 && touchY >  getHeight()/5){

            part = false;
            ct.setPart(part);
            if(threadstarted == false) {
                ct.start();
                threadstarted = true;
            }
            Log.d("touch", "touchevent");
            connected = ct.isConnected();
            playerhasturn = true;
            enemyhasturn = false;
            ct.setPlayerhasturn(playerhasturn);
            ct.setEnemyhasturn(enemyhasturn);
        }
        if(connected == false && touchX < getWidth()/4*3 && touchX > getWidth()/4 && touchY < getHeight()/5*4 && touchY >  getHeight()/5*3){

            part = true;
            ct.setPart(part);
            if(threadstarted == false) {
                ct.start();
                threadstarted = true;
            }
            connected = ct.isConnected();
            playerhasturn = false;
            enemyhasturn = true;
            ct.setPlayerhasturn(playerhasturn);
            ct.setEnemyhasturn(enemyhasturn);
        }


        if (connected == true && touchX > 1 && touchX < getWidth() / 12 && touchY > getHeight() / 3 && touchY < getHeight() / 3 * 2) {
            kaserne = true;


        }
        if (connected == true && touchX > getWidth() / 8 * 7 && touchX < getWidth() - 1 && touchY < getHeight() / 8 && touchY > 1) {
            kaserne = false;


        }


      //  if(connected = true && kaserne == false && playerhasturn == true)


            //if (healerScale == false &&touchX > 1 && touchX < getWidth() / 12 && touchY < getHeight() / 2 && touchY > getHeight() / 4) {

            if (connected == true && healerScale == false &&touchX > 1 && touchX < getWidth() / 12 && touchY < getHeight() / 2 && touchY > getHeight() / 4
                    ) {

                //barrackCardPosition.add(new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight() / 3, getWidth() / 12, getHeight() / 5));
                //handpositionsPlayer.add(new Handpositions(getWidth() / 70 + getWidth() / 12 * i + getWidth() / 60 * i, getHeight() - 1 - (getHeight() / 5), getWidth() / 12, getHeight() / 5));
                //healerScale = true;


       // }

        if(connected == true && playerhasturn == true && touchX < getWidth() && touchX > getWidth()/20*19 && touchY < getHeight()/9*5
                && touchY > getHeight()/9*4){


           // ct.sendCommand("endturn");
            ct.setCommand(1);
            enemyhasturn = true;
            playerhasturn = false;
            ct.setEnemyhasturn(enemyhasturn);
                ct.setPlayerhasturn(playerhasturn);




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

  /*      if(enemyhasturn == true){
            String temp = ct.getIncomigData();
            if(temp.equals("endturn")){
                playerhasturn = true;
                enemyhasturn = false;
                ct.setPlayerhasturn(playerhasturn);
                ct.setEnemyhasturn(enemyhasturn);
            }
        }
        */

        bg.draw(canvas);

        if(connected == false){


            canvas.drawBitmap(create, getWidth()/4, getHeight()/5, null);
            canvas.drawBitmap(join, getWidth()/4, getHeight()/5*3, null);

            if(server){
                canvas.drawText("Peta", 50, 50, paint);
            }
            if(client){
                canvas.drawText("rendl", 50, 50, paint);
            }

        }else {

            if (kaserne == false) {





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

                for (int i = 0; i < 7; i++) {
                    if (handCardsName[i].isFilled()) {
                        if (handCardsName[i].getName().equals("Spiegel")) {
                          //  canvas.drawBitmap(Bitmap.createScaledBitmap(spiegel, handpositionsPlayer.get(i).getLength(),
                           //         handpositionsPlayer.get(i).getVast(), false),
                           //         handpositionsPlayer.get(i).getPositionX(), handpositionsPlayer.get(i).getPositionY(), null);
                            canvas.drawBitmap(smallscaledSpiegel,
                                    handpositionsPlayer.get(i).getPositionX(), handpositionsPlayer.get(i).getPositionY(), null);
                        } else if (handCardsName[i].getName().equals("Blitz")) {
                            //canvas.drawBitmap(Bitmap.createScaledBitmap(blitz, handpositionsPlayer.get(i).getLength(),
                              //      handpositionsPlayer.get(i).getVast(), false),
                            //handpositionsPlayer.get(i).getPositionX(), handpositionsPlayer.get(i).getPositionY(), null);
                            canvas.drawBitmap(smallscaledBlitz,
                                    handpositionsPlayer.get(i).getPositionX(), handpositionsPlayer.get(i).getPositionY(), null);
                        } else if (handCardsName[i].getName().equals("Assasine")) {
                           // canvas.drawBitmap(Bitmap.createScaledBitmap(assasine, handpositionsPlayer.get(i).getLength(),
                             //       handpositionsPlayer.get(i).getVast(), false),
                               //     handpositionsPlayer.get(i).getPositionX(), handpositionsPlayer.get(i).getPositionY(), null);
                            canvas.drawBitmap(smallscaledAssasine,
                                    handpositionsPlayer.get(i).getPositionX(), handpositionsPlayer.get(i).getPositionY(), null);
                        }

                    }

                }

                    //  canvas.drawBitmap(Bitmap.createScaledBitmap(healer, handpositionsPlayer.get(4).getLength(), handpositionsPlayer.get(4).getVast(), false), handpositionsPlayer.get(4).getPositionX(), handpositionsPlayer.get(4).getPositionY(), null);
                    //canvas.drawBitmap(Bitmap.createScaledBitmap(healer, handpositionsOpponent.get(4).getLength(), handpositionsOpponent.get(4).getVast(), false), handpositionsOpponent.get(4).getPositionX(), handpositionsOpponent.get(4).getPositionY(), null);
                    //canvas.drawBitmap(Bitmap.createScaledBitmap(healer, handpositionsPlayer.get(6).getLength(), handpositionsPlayer.get(6).getVast(), false), handpositionsPlayer.get(6).getPositionX(), handpositionsPlayer.get(6).getPositionY(), null);
                    //  canvas.drawBitmap(Bitmap.createScaledBitmap(playerHero, heroPosition.get(0).getLength(), heroPosition.get(0).getVast(),false),heroPosition.get(0).getPositionX(),heroPosition.get(0).getPositionY(), null);
                    //  canvas.drawBitmap(Bitmap.createScaledBitmap(playerHero, heroPosition.get(1).getLength(), heroPosition.get(1).getVast(),false),heroPosition.get(1).getPositionX(),heroPosition.get(1).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(barracks, getWidth() / 6, getHeight() / 2, false), getWidth() / 100, getHeight() / 4, null);


                if(playerhasturn){
                    canvas.drawBitmap(endturnbutton, getWidth()/20*19, getHeight()/9*4, null);
                }
                if(enemyhasturn){
                    canvas.drawBitmap(enemyturnpicture, getWidth()/20*19, getHeight()/9*4, null);

                }
                }

                if (kaserne == true) {

                    if (touchX > getWidth() / 12 && touchX < getWidth() / 12 + barrackCardPosition.get(0).getLength() && touchY > getHeight() / 3 && touchY < getHeight()/3+ barrackCardPosition.get(0).getVast()) {
                        // barrackCardPosition.add(new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight() / 3, getWidth() / 12, getHeight() / 5));
                        touch = true;
                        Log.d("Koordinaten", "touchX" + "touchY");
                        // Log.d("touch", "touchevent");



                    }

                    if (touchX > getWidth() / 6 && touchX < getWidth() / 6 + barrackCardPosition.get(1).getLength() && touchY > getHeight() / 3 && touchY < getHeight()/3+ barrackCardPosition.get(1).getVast()) {
                        //barrackCardPosition.add(new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight() / 3, getWidth() / 12, getHeight() / 5));
                        touch1= true;
                    }

                    if (touchX > getWidth() / 3 && touchX < getWidth() / 3 + barrackCardPosition.get(2).getLength() && touchY > getHeight() / 3 && touchY < getHeight()/3+ barrackCardPosition.get(2).getVast()) {

                        //barrackCardPosition.add(new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight() / 3, getWidth() / 12, getHeight() / 5));
                        touch2= true;
                    }
                    if (touchX > getWidth() / 2 && touchX < getWidth() / 2 + barrackCardPosition.get(3).getLength() && touchY > getHeight() / 3 && touchY < getHeight()/3+ barrackCardPosition.get(3).getVast()) {

                        //barrackCardPosition.add(new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight() / 3, getWidth() / 12, getHeight() / 5));
                        touch3= true;
                    }

                    if (touchX > getWidth()*7/12 && touchX < getWidth()*7/12 + barrackCardPosition.get(4).getLength() && touchY > getHeight() / 3 && touchY < getHeight()/3+ barrackCardPosition.get(4).getVast()) {

                        //barrackCardPosition.add(new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight() / 3, getWidth() / 12, getHeight() / 5));
                        touch4= true;
                    }

                    if (touchX > getWidth()*3/4 && touchX < getWidth()*3/4 + barrackCardPosition.get(5).getLength() && touchY > getHeight() / 3 && touchY < getHeight()/3+ barrackCardPosition.get(5).getVast()) {

                        //barrackCardPosition.add(new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight() / 3, getWidth() / 12, getHeight() / 5));
                        touch5= true;
                    }

                    if (touchX > getWidth()*5/6&& touchX < getWidth()*5/6 + barrackCardPosition.get(6).getLength() && touchY > getHeight() / 3 && touchY < getHeight()/3+ barrackCardPosition.get(6).getVast()) {

                        //barrackCardPosition.add(new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight() / 3, getWidth() / 12, getHeight() / 5));
                        touch6= true;
                    }





                    paintShade = new Paint();
                    ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);
                    paintShade.setColorFilter(filter);


                    canvas.drawBitmap(kasernebg, 0, 0, null);

/*
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(0).getLength(), wantedPosition.get(0).getVast(), false), wantedPosition.get(0).getPositionX(), wantedPosition.get(0).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(1).getLength(), wantedPosition.get(1).getVast(), false), wantedPosition.get(1).getPositionX(), wantedPosition.get(1).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(2).getLength(), wantedPosition.get(2).getVast(), false), wantedPosition.get(2).getPositionX(), wantedPosition.get(2).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(3).getLength(), wantedPosition.get(3).getVast(), false), wantedPosition.get(3).getPositionX(), wantedPosition.get(3).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(4).getLength(), wantedPosition.get(4).getVast(), false), wantedPosition.get(4).getPositionX(), wantedPosition.get(4).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(5).getLength(), wantedPosition.get(5).getVast(), false), wantedPosition.get(5).getPositionX(), wantedPosition.get(5).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(6).getLength(), wantedPosition.get(6).getVast(), false), wantedPosition.get(6).getPositionX(), wantedPosition.get(6).getPositionY(), null);


                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(0).getLength(), wantedPosition.get(0).getVast(), false), wantedPosition.get(0).getPositionX(), wantedPosition.get(0).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(1).getLength(), wantedPosition.get(1).getVast(), false), wantedPosition.get(1).getPositionX(), wantedPosition.get(1).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(2).getLength(), wantedPosition.get(2).getVast(), false), wantedPosition.get(2).getPositionX(), wantedPosition.get(2).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(3).getLength(), wantedPosition.get(3).getVast(), false), wantedPosition.get(3).getPositionX(), wantedPosition.get(3).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(4).getLength(), wantedPosition.get(4).getVast(), false), wantedPosition.get(4).getPositionX(), wantedPosition.get(4).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(5).getLength(), wantedPosition.get(5).getVast(), false), wantedPosition.get(5).getPositionX(), wantedPosition.get(5).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wanted, wantedPosition.get(6).getLength(), wantedPosition.get(6).getVast(), false), wantedPosition.get(6).getPositionX(), wantedPosition.get(6).getPositionY(), null);
*/

                    canvas.drawBitmap(scaledWanted, wantedPosition.get(0).getPositionX(), wantedPosition.get(0).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(1).getPositionX(), wantedPosition.get(1).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(2).getPositionX(), wantedPosition.get(2).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(3).getPositionX(), wantedPosition.get(3).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(4).getPositionX(), wantedPosition.get(4).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(5).getPositionX(), wantedPosition.get(5).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(6).getPositionX(), wantedPosition.get(6).getPositionY(), null);


                    canvas.drawBitmap(scaledWanted, wantedPosition.get(0).getPositionX(), wantedPosition.get(0).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(1).getPositionX(), wantedPosition.get(1).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(2).getPositionX(), wantedPosition.get(2).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(3).getPositionX(), wantedPosition.get(3).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(4).getPositionX(), wantedPosition.get(4).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(5).getPositionX(), wantedPosition.get(5).getPositionY(), null);
                    canvas.drawBitmap(scaledWanted, wantedPosition.get(6).getPositionX(), wantedPosition.get(6).getPositionY(), null);

                    /*
                    canvas.drawBitmap(Bitmap.createScaledBitmap(healer, barrackCardPosition.get(0).getLength(), barrackCardPosition.get(0).getVast(), false), barrackCardPosition.get(0).getPositionX(), barrackCardPosition.get(0).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(attacker, barrackCardPosition.get(1).getLength(), barrackCardPosition.get(1).getVast(), false), barrackCardPosition.get(1).getPositionX(), barrackCardPosition.get(1).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(heretic, barrackCardPosition.get(2).getLength(), barrackCardPosition.get(2).getVast(), false), barrackCardPosition.get(2).getPositionX(), barrackCardPosition.get(2).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(healer, barrackCardPosition.get(3).getLength(), barrackCardPosition.get(3).getVast(), false), barrackCardPosition.get(3).getPositionX(), barrackCardPosition.get(3).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(healer, barrackCardPosition.get(4).getLength(), barrackCardPosition.get(4).getVast(), false), barrackCardPosition.get(4).getPositionX(), barrackCardPosition.get(3).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(healer, barrackCardPosition.get(5).getLength(), barrackCardPosition.get(5).getVast(), false), barrackCardPosition.get(5).getPositionX(), barrackCardPosition.get(5).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(healer, barrackCardPosition.get(6).getLength(), barrackCardPosition.get(6).getVast(), false), barrackCardPosition.get(6).getPositionX(), barrackCardPosition.get(6).getPositionY(), null);
                    */
                    canvas.drawBitmap(kasernenscaledhealer, barrackCardPosition.get(0).getPositionX(), barrackCardPosition.get(0).getPositionY(), null);
                    canvas.drawBitmap(kasenenscaledattacker, barrackCardPosition.get(1).getPositionX(), barrackCardPosition.get(1).getPositionY(), null);
                    canvas.drawBitmap(kasernenscaledheretic, barrackCardPosition.get(2).getPositionX(), barrackCardPosition.get(2).getPositionY(), null);
                    canvas.drawBitmap(kasernenscaledhealer, barrackCardPosition.get(3).getPositionX(), barrackCardPosition.get(3).getPositionY(), null);
                    canvas.drawBitmap(kasernenscaledhealer, barrackCardPosition.get(4).getPositionX(), barrackCardPosition.get(3).getPositionY(), null);
                    canvas.drawBitmap(kasernenscaledhealer, barrackCardPosition.get(5).getPositionX(), barrackCardPosition.get(5).getPositionY(), null);
                    canvas.drawBitmap(kasernenscaledhealer, barrackCardPosition.get(6).getPositionX(), barrackCardPosition.get(6).getPositionY(), null);
/*
/*
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(0).getLength(),pricePositions.get(0).getVast(), false),pricePositions.get(0).getPositionX(),pricePositions.get(0).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(1).getLength(),pricePositions.get(1).getVast(), false),pricePositions.get(1).getPositionX(),pricePositions.get(1).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(2).getLength(),pricePositions.get(2).getVast(), false),pricePositions.get(2).getPositionX(),pricePositions.get(2).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(3).getLength(),pricePositions.get(3).getVast(), false),pricePositions.get(3).getPositionX(),pricePositions.get(3).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(4).getLength(),pricePositions.get(4).getVast(), false),pricePositions.get(4).getPositionX(),pricePositions.get(4).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(5).getLength(),pricePositions.get(5).getVast(), false),pricePositions.get(5).getPositionX(),pricePositions.get(5).getPositionY(),null);
    canvas.drawBitmap(Bitmap.createScaledBitmap(priceBlank, pricePositions.get(6).getLength(),pricePositions.get(6).getVast(), false),pricePositions.get(6).getPositionX(),pricePositions.get(6).getPositionY(),null);
    */
             /*       canvas.drawBitmap(Bitmap.createScaledBitmap(priceWanted, priceWantedPosition.get(0).getLength(), priceWantedPosition.get(0).getVast(), false), priceWantedPosition.get(0).getPositionX(), priceWantedPosition.get(0).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(priceWanted, priceWantedPosition.get(1).getLength(), priceWantedPosition.get(1).getVast(), false), priceWantedPosition.get(1).getPositionX(), priceWantedPosition.get(1).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(priceWanted, priceWantedPosition.get(2).getLength(), priceWantedPosition.get(2).getVast(), false), priceWantedPosition.get(2).getPositionX(), priceWantedPosition.get(2).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(priceWanted, priceWantedPosition.get(3).getLength(), priceWantedPosition.get(3).getVast(), false), priceWantedPosition.get(3).getPositionX(), priceWantedPosition.get(3).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(priceWanted, priceWantedPosition.get(4).getLength(), priceWantedPosition.get(4).getVast(), false), priceWantedPosition.get(4).getPositionX(), priceWantedPosition.get(4).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(priceWanted, priceWantedPosition.get(5).getLength(), priceWantedPosition.get(5).getVast(), false), priceWantedPosition.get(5).getPositionX(), priceWantedPosition.get(5).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(priceWanted, priceWantedPosition.get(6).getLength(), priceWantedPosition.get(6).getVast(), false), priceWantedPosition.get(6).getPositionX(), priceWantedPosition.get(6).getPositionY(), null);
            */        //  if (touchX > getWidth() / 8 * 7 && touchX < getWidth() - 1 && touchY < getHeight() / 8 && touchY > 1) {

                    canvas.drawBitmap(scaledPriceWanted, priceWantedPosition.get(0).getPositionX(), priceWantedPosition.get(0).getPositionY(), null);
                    canvas.drawBitmap(scaledPriceWanted, priceWantedPosition.get(1).getPositionX(), priceWantedPosition.get(1).getPositionY(), null);
                    canvas.drawBitmap(scaledPriceWanted, priceWantedPosition.get(2).getPositionX(), priceWantedPosition.get(2).getPositionY(), null);
                    canvas.drawBitmap(scaledPriceWanted, priceWantedPosition.get(3).getPositionX(), priceWantedPosition.get(3).getPositionY(), null);
                    canvas.drawBitmap(scaledPriceWanted, priceWantedPosition.get(4).getPositionX(), priceWantedPosition.get(4).getPositionY(), null);
                    canvas.drawBitmap(scaledPriceWanted, priceWantedPosition.get(5).getPositionX(), priceWantedPosition.get(5).getPositionY(), null);
                    canvas.drawBitmap(scaledPriceWanted, priceWantedPosition.get(6).getPositionX(), priceWantedPosition.get(6).getPositionY(), null);
                    canvas.drawBitmap(Bitmap.createScaledBitmap(exitBarracks, getWidth() / 8, getHeight() / 6, false), getWidth() * 7 / 8, getHeight() / 35, null);


                    paint = new Paint();
                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setTextSize(30);


                    //new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight()/3, getWidth() / 12, getHeight() / 5


                    paint = new Paint();
                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setTextSize(30);



          //  priceWantedPosition.add(new priceWanted(getWidth() / 16 + getWidth() / 12 * i + getWidth() / 22 * i, getHeight() * 9 / 16, getWidth() / 8, getHeight() / 4));


                    //new Barracks(getWidth() / 12 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight()/3, getWidth() / 12, getHeight() / 5


                    //String healerPrice = String.valueOf(healers.getPrice());
                    canvas.drawText(String.valueOf(healers.getPrice()) + "Gold", (getWidth() / 11), (getHeight() * 20 / 32), paint);
                    canvas.drawText(String.valueOf(attackers.getPrice()) + "Gold", (getWidth() / 11) + getWidth() / 8, (getHeight() * 20 / 32), paint);
                    canvas.drawText(String.valueOf(heretics.getPrice()) + "Gold", (getWidth() / 11) + getWidth() * 2 / 8, (getHeight() * 20 / 32), paint);
                    canvas.drawText(String.valueOf(ghosts.getPrice()) + "Gold", (getWidth() / 11) + getWidth() * 3 / 8, (getHeight() * 20 / 32), paint);
                    canvas.drawText(String.valueOf(druids.getPrice()) + "Gold", (getWidth() / 11) + getWidth() * 4 / 8, (getHeight() * 20 / 32), paint);
                    canvas.drawText(String.valueOf(pioneers.getPrice()) + "Gold", (getWidth() / 11) + getWidth() * 5 / 8, (getHeight() * 20 / 32), paint);
                    canvas.drawText(String.valueOf(bannerCarriers.getPrice()) + "Gold", (getWidth() / 11) + getWidth() * 6 / 8, (getHeight() * 20 / 32), paint);


                    paint.setColor(Color.TRANSPARENT);
                    paint.setAlpha(200);


                    if (touch == true){
                        canvas.drawBitmap(transparentBG, 0, 0, paint);

                        canvas.drawBitmap(healerBig, getWidth() * 4 / 12, getHeight() / 16, null);
                        canvas.drawBitmap(pieceBlank, getWidth() * 3 / 12, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(plus, getWidth() * 2 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(minus, getWidth() * 8 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(priceBlank, getWidth() * 10 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(Bitmap.createScaledBitmap(exitBarracks, getWidth() / 8, getHeight() / 6, false), getWidth() /16, getHeight() / 35, null);

                        if (connected == true && touchX > getWidth() /16 && touchX < getWidth()/16 + getWidth()/8 && touchY < getHeight() / 8 && touchY > 1) {
                            touch = false;


                        }

                    }
                    if (touch1 == true){
                        canvas.drawBitmap(transparentBG, 0, 0, paint);

                        canvas.drawBitmap(healerBig, getWidth() * 4 / 12, getHeight() / 16, null);
                        canvas.drawBitmap(pieceBlank, getWidth() * 3 / 12, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(plus, getWidth() * 2 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(minus, getWidth() * 8 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(priceBlank, getWidth() * 10 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(Bitmap.createScaledBitmap(exitBarracks, getWidth() / 8, getHeight() / 6, false), getWidth() * 7 / 8, getHeight() / 35, null);

                    }
                    if (touch2 == true){
                        canvas.drawBitmap(transparentBG, 0, 0, paint);

                        canvas.drawBitmap(healerBig, getWidth() * 4 / 12, getHeight() / 16, null);
                        canvas.drawBitmap(pieceBlank, getWidth() * 3 / 12, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(plus, getWidth() * 2 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(minus, getWidth() * 8 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(priceBlank, getWidth() * 10 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(Bitmap.createScaledBitmap(exitBarracks, getWidth() / 8, getHeight() / 6, false), getWidth() * 7 / 8, getHeight() / 35, null);

                    }
                    if (touch3 == true){
                        canvas.drawBitmap(transparentBG, 0, 0, paint);

                        canvas.drawBitmap(healerBig, getWidth() * 4 / 12, getHeight() / 16, null);
                        canvas.drawBitmap(pieceBlank, getWidth() * 3 / 12, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(plus, getWidth() * 2 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(minus, getWidth() * 8 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(priceBlank, getWidth() * 10 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(Bitmap.createScaledBitmap(exitBarracks, getWidth() / 8, getHeight() / 6, false), getWidth() * 7 / 8, getHeight() / 35, null);

                    }
                    if (touch4 == true){
                        canvas.drawBitmap(transparentBG, 0, 0, paint);

                        canvas.drawBitmap(healerBig, getWidth() * 4 / 12, getHeight() / 16, null);
                        canvas.drawBitmap(pieceBlank, getWidth() * 3 / 12, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(plus, getWidth() * 2 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(minus, getWidth() * 8 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(priceBlank, getWidth() * 10 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(Bitmap.createScaledBitmap(exitBarracks, getWidth() / 8, getHeight() / 6, false), getWidth() * 7 / 8, getHeight() / 35, null);

                    }
                    if (touch5 == true){
                        canvas.drawBitmap(transparentBG, 0, 0, paint);

                        canvas.drawBitmap(healerBig, getWidth() * 4 / 12, getHeight() / 16, null);
                        canvas.drawBitmap(pieceBlank, getWidth() * 3 / 12, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(plus, getWidth() * 2 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(minus, getWidth() * 8 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(priceBlank, getWidth() * 10 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(Bitmap.createScaledBitmap(exitBarracks, getWidth() / 8, getHeight() / 6, false), getWidth() * 7 / 8, getHeight() / 35, null);

                    }
                    if (touch6 == true){
                        canvas.drawBitmap(transparentBG, 0, 0, paint);

                        canvas.drawBitmap(healerBig, getWidth() * 4 / 12, getHeight() / 16, null);
                        canvas.drawBitmap(pieceBlank, getWidth() * 3 / 12, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(plus, getWidth() * 2 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(minus, getWidth() * 8 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(priceBlank, getWidth() * 10 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(Bitmap.createScaledBitmap(exitBarracks, getWidth() / 8, getHeight() / 6, false), getWidth() * 7 / 8, getHeight() / 35, null);

                    }




                    if (extendCard == true) {
                        canvas.drawBitmap(transparentBG, 0, 0, paint);

                        canvas.drawBitmap(healerBig, getWidth() * 4 / 12, getHeight() / 16, null);
                        canvas.drawBitmap(pieceBlank, getWidth() * 3 / 12, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(plus, getWidth() * 2 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(minus, getWidth() * 8 / 16, getHeight() * 15 / 18, null);
                        canvas.drawBitmap(priceBlank, getWidth() * 10 / 16, getHeight() * 15 / 18, null);
                    }


                    //wantedPosition.add(new Wanted(getWidth() / 16 + getWidth() / 12 * i + getWidth() / 24 * i, getHeight()*5/16, getWidth() / 8, getHeight() / 4));




                }

            }
        }

    public void saveInt(String key, int val) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(key, val);
        editor.commit();
    }

    public void setPlayerhasturn(boolean playerhasturn) {
        this.playerhasturn = playerhasturn;
    }

    public void setEnemyhasturn(boolean enemyhasturn) {
        this.enemyhasturn = enemyhasturn;
    }

    public int loadInt(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getInt(key, 0);
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}