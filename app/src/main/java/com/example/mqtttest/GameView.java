package com.example.mqtttest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class GameView extends View {

    private Bitmap bmTiles1, bmTiles2, bmCharacter;
    public static int sizeOfMap = 80*Constants.SCREEN_WIDTH/1150;
    private int h = 8, w = 24;
    private ArrayList<Tiles> arrayTiles = new ArrayList<>();
    public int x, y;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bmTiles1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.ground_01);
        bmTiles1 = Bitmap.createScaledBitmap(bmTiles1, sizeOfMap, sizeOfMap, true);
        bmTiles2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.ground_04);
        bmTiles2 = Bitmap.createScaledBitmap(bmTiles2, sizeOfMap, sizeOfMap, true);
        bmCharacter = BitmapFactory.decodeResource(this.getResources(), R.drawable.character);
        bmCharacter = Bitmap.createScaledBitmap(bmCharacter, sizeOfMap, sizeOfMap, true);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if ((i+j)%2==0) {
                    arrayTiles.add(new Tiles(bmTiles1, j*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap,
                            i*sizeOfMap+100*Constants.SCREEN_HEIGHT/900, sizeOfMap, sizeOfMap));
                } else {
                    arrayTiles.add(new Tiles(bmTiles2, j*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap,
                            i*sizeOfMap+100*Constants.SCREEN_HEIGHT/900, sizeOfMap, sizeOfMap));
                }
            }
        }



    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(0xFF000000);
        for (int i = 0; i < arrayTiles.size(); i++) {
            canvas.drawBitmap(arrayTiles.get(i).getBm(), arrayTiles.get(i).getX(), arrayTiles.get(i).getY(), null);
        }
        canvas.drawBitmap(bmCharacter,  x*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap, y*sizeOfMap+100*Constants.SCREEN_HEIGHT/900, null);
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
        invalidate();
    }
}
