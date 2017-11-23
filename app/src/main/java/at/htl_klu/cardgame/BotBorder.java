package at.htl_klu.cardgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Felix on 20/03/16.
 */
public class BotBorder extends GameObject{

    private Bitmap image;
    public BotBorder(Bitmap res, int x, int y)
    {
        height = 200;
        width = 20;

        this.x = x;
        this.y = y;
        dx = GamePanel.MOVESPEED;

        image = Bitmap.createBitmap(res, 0, 0, width, height);

    }
    public void update()
    {
        x +=dx;

    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y, null);

    }
}