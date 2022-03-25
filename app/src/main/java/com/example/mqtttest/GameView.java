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

    private Bitmap bmTiles1, bmTiles2;
    public static int sizeOfMap = 75*Constants.SCREEN_WIDTH/1550;
    private int h = 10, w = 18;
    private ArrayList<Tiles> arrayTiles = new ArrayList<>();

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bmTiles1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.tiles1);
        bmTiles1 = Bitmap.createScaledBitmap(bmTiles1, sizeOfMap, sizeOfMap, true);
        bmTiles2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.tiles2);
        bmTiles2 = Bitmap.createScaledBitmap(bmTiles2, sizeOfMap, sizeOfMap, true);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if ((i+j)%2==0) {
                    arrayTiles.add(new Tiles(bmTiles1, j*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap,
                            i*sizeOfMap+100*Constants.SCREEN_HEIGHT/1920, sizeOfMap, sizeOfMap));
                } else {
                    arrayTiles.add(new Tiles(bmTiles2, j*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap,
                            i*sizeOfMap+100*Constants.SCREEN_HEIGHT/1920, sizeOfMap, sizeOfMap));
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(0xFF1A6100);
        for (int i = 0; i < arrayTiles.size(); i++) {
            canvas.drawBitmap(arrayTiles.get(i).getBm(), arrayTiles.get(i).getX(), arrayTiles.get(i).getY(), null);
        }
    }
}
