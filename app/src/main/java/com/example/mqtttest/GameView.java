package com.example.mqtttest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View {

    private Bitmap bmTiles1, bmTiles2, bmCharacter, bmMur, bmEye, bmTrap, bmRedFlag, bmGreenFlag, bmOpenDoor, bmClosedDoor;
    public static int sizeOfMap = 80*Constants.SCREEN_WIDTH/1600;
    private int h = 8, w = 20;
    private ArrayList<Tiles> arrayTiles = new ArrayList<>();
    public int x, y;
    public int xm1, xm2, yS = 0, xS = 19;
    private int clem [][];
    public List<Monstre> listeM = new ArrayList<>();
    public int ym=0;
    boolean descend=true;
    boolean chowSortie = false;
    boolean isDead = false;
    public boolean isStart = false;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bmTiles1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.ground_01);
        bmTiles1 = Bitmap.createScaledBitmap(bmTiles1, sizeOfMap, sizeOfMap, true);
        bmTiles2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.ground_04);
        bmTiles2 = Bitmap.createScaledBitmap(bmTiles2, sizeOfMap, sizeOfMap, true);
        bmCharacter = BitmapFactory.decodeResource(this.getResources(), R.drawable.monk);
        bmCharacter = Bitmap.createScaledBitmap(bmCharacter, sizeOfMap, sizeOfMap, true);
        bmMur = BitmapFactory.decodeResource(this.getResources(), R.drawable.back);
        bmMur = Bitmap.createScaledBitmap(bmMur, sizeOfMap, sizeOfMap, true);
        bmEye = BitmapFactory.decodeResource(this.getResources(), R.drawable.flyingeye);
        bmEye = Bitmap.createScaledBitmap(bmEye, sizeOfMap, sizeOfMap, true);
        bmTrap = BitmapFactory.decodeResource(this.getResources(), R.drawable.impale);
        bmTrap = Bitmap.createScaledBitmap(bmTrap, sizeOfMap, sizeOfMap, true);
        bmRedFlag = BitmapFactory.decodeResource(this.getResources(), R.drawable.flag_red);
        bmRedFlag = Bitmap.createScaledBitmap(bmRedFlag, sizeOfMap, sizeOfMap, true);
        bmGreenFlag = BitmapFactory.decodeResource(this.getResources(), R.drawable.flag_green);
        bmGreenFlag = Bitmap.createScaledBitmap(bmGreenFlag, sizeOfMap, sizeOfMap, true);
        bmOpenDoor = BitmapFactory.decodeResource(this.getResources(), R.drawable.open_door);
        bmOpenDoor = Bitmap.createScaledBitmap(bmOpenDoor, sizeOfMap, sizeOfMap, true);
        bmClosedDoor = BitmapFactory.decodeResource(this.getResources(), R.drawable.open_door);
        bmClosedDoor = Bitmap.createScaledBitmap(bmClosedDoor, sizeOfMap, sizeOfMap, true);
        xm1=4;
        xm2=8;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if ((i+j)%2==0) {
                    arrayTiles.add(new Tiles(bmTiles1, j*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap,
                            i*sizeOfMap+50*Constants.SCREEN_HEIGHT/3000, sizeOfMap, sizeOfMap));
                } else {
                    arrayTiles.add(new Tiles(bmTiles2, j*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap,
                            i*sizeOfMap+50*Constants.SCREEN_HEIGHT/3000, sizeOfMap, sizeOfMap));
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

        if(isStart) {
            // Draw cqrqctere
            canvas.drawBitmap(bmCharacter, x * sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap, y * sizeOfMap + 50 * Constants.SCREEN_HEIGHT / 3000, null);
            if(listeM != null){
                for ( int ctr2=0;ctr2<listeM.size();ctr2++)
                {
                    canvas.drawBitmap(bmEye, listeM.get(ctr2).x * sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap, listeM.get(ctr2).y * sizeOfMap + 50 * Constants.SCREEN_HEIGHT / 3000, null);
                }
            }


        }

        if (clem != null)
            for (int ctr = 0; ctr < clem.length; ctr++){
                if (!(clem[ctr][0] == 0 && clem[ctr][1] == 0)) {
                    canvas.drawBitmap(bmMur, clem[ctr][0]*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap, clem[ctr][1]*sizeOfMap+50*Constants.SCREEN_HEIGHT/3000, null);
                }
            }

        if(chowSortie)
            canvas.drawBitmap(bmMur, xS*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap, yS*sizeOfMap+50*Constants.SCREEN_HEIGHT/3000, null);
    }

    public void setMur(int dave[][]) {
        clem = dave;
        invalidate();
    }

    public void setMonstres(List<Monstre> l) {
        listeM = l;
        invalidate();
    }

    public void setSortie(int x, int y) {
        xS = x;
        yS = y;
        chowSortie = true;
        invalidate();
    }

    public void setPos(int x, int y) {
        if(!isDead) {
            this.x = x;
            this.y = y;
            invalidate();
        }
    }

    public void mouvementMonstre( ){

        if(!isDead) {

            for (int ctrm = 0; ctrm<listeM.size(); ctrm++) {


                switch(listeM.get(ctrm).sens){
                    case 0 :
                        if (listeM.get(ctrm).flag) {
                            Log.d("dist",listeM.get(ctrm).distance + "" + listeM.get(ctrm).distanceMax);
                            listeM.get(ctrm).setDistance(listeM.get(ctrm).getDistance()+1);
                            listeM.get(ctrm).y++;

                            invalidate();
                            if (listeM.get(ctrm).distance== listeM.get(ctrm).distanceMax || listeM.get(ctrm).getY()==7){
                                listeM.get(ctrm).flag=false;
                                listeM.get(ctrm).distance=0;
                            }

                        } else if(!listeM.get(ctrm).flag) {
                            listeM.get(ctrm).y--;
                            listeM.get(ctrm).distance=listeM.get(ctrm).distance+1;
                            invalidate();
                            if (listeM.get(ctrm).distance== listeM.get(ctrm).distanceMax ||listeM.get(ctrm).getY()==0){
                                listeM.get(ctrm).flag=true;
                                listeM.get(ctrm).distance=0;
                            }
                        }
                        break;
                }
            }

        }
    }
}
