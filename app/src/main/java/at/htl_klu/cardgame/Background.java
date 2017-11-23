package at.htl_klu.cardgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {

    private Bitmap image;
    private int x, dx;

    public Background(Bitmap res) {
        image = res;
        dx = GamePanel.MOVESPEED;
    }

    public void update() {
        if (GamePanel.start == true) {

            x += dx;
            if (x < -GamePanel.WIDTH) {
                x = 0;
            }
        }
    }

    public void draw(Canvas canvas) {
        if (GamePanel.start == true) {
            //canvas.drawBitmap(image, x, 0, null);
            canvas.drawBitmap(image, 0, 0, null);
            /*if (x < 0) {
                canvas.drawBitmap(image, x + GamePanel.WIDTH, 0, null);
            }
        */
        }else{

        }
    }
}