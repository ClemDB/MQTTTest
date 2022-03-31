package com.example.mqtttest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View {

    private Bitmap bmTiles1, bmTiles2, bmCharacter, bmMur, bmEye, bmTrap1, bmTrap2, bmTrap3, bmRedFlag, bmGreenFlag, bmOpenDoor, bmClosedDoor;
    public static int sizeOfMap = 80*Constants.SCREEN_WIDTH/1600;
    // Hauteur largeur longeur tableau
    private int h = 8, w = 20;
    // Array de chaque tiles
    private ArrayList<Tiles> arrayTiles = new ArrayList<>();
    // Position joueur
    public int x, y;
    // Position de base sortie
    public Sortie sortie = new Sortie(200,200);
    // Position pieges
    public int xP, yP;
    //Tableau mur
    public List<Mur> listeMur = new ArrayList<>();
    // Tableau de piege
    public List<Piege> listeP = new ArrayList<>();
    // Liste monstre
    public List<Monstre> listeM = new ArrayList<>();
    // Liste Checkpoint
    public List<Flag> listeCheckPoint = new ArrayList<>();
    boolean chowSortie = false;
    boolean isDead = false;
    public boolean isStart = false;
    public String TAG = "GameView";
    int cycle=0;
    boolean flagPiege=true;
    public boolean win = false;
    public int currentLevel = 1;

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
        bmTrap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.impale_0);
        bmTrap1 = Bitmap.createScaledBitmap(bmTrap1, sizeOfMap, sizeOfMap, true);
        bmRedFlag = BitmapFactory.decodeResource(this.getResources(), R.drawable.flag_red);
        bmRedFlag = Bitmap.createScaledBitmap(bmRedFlag, sizeOfMap, sizeOfMap, true);
        bmGreenFlag = BitmapFactory.decodeResource(this.getResources(), R.drawable.flag_green);
        bmGreenFlag = Bitmap.createScaledBitmap(bmGreenFlag, sizeOfMap, sizeOfMap, true);
        bmOpenDoor = BitmapFactory.decodeResource(this.getResources(), R.drawable.open_door);
        bmOpenDoor = Bitmap.createScaledBitmap(bmOpenDoor, sizeOfMap, sizeOfMap, true);
        bmClosedDoor = BitmapFactory.decodeResource(this.getResources(), R.drawable.close_door);
        bmClosedDoor = Bitmap.createScaledBitmap(bmClosedDoor, sizeOfMap, sizeOfMap, true);
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

        if(!win) {
            if(isStart) {
                // Draw caractere
                canvas.drawBitmap(bmCharacter, x * sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap, y * sizeOfMap + 50 * Constants.SCREEN_HEIGHT / 3000, null);
                if(listeM != null){
                    for ( int ctr2=0;ctr2<listeM.size();ctr2++)
                    {
                        // Draw monstre
                        canvas.drawBitmap(bmEye, listeM.get(ctr2).x * sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap, listeM.get(ctr2).y * sizeOfMap + 50 * Constants.SCREEN_HEIGHT / 3000, null);
                    }
                }
            }

            // Draw murs
            if(isStart && win == false) {
                if (listeMur != null)
                    for (Mur m : listeMur){
                        if (!(m.getX() == 0 && m.getY() == 0)) {
                            canvas.drawBitmap(bmMur, m.getX()*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap, m.getY()*sizeOfMap+50*Constants.SCREEN_HEIGHT/3000, null);
                        }
                    }
            }

            // Draw pieges
            if (listeP != null)
                for (int ctr3 = 0; ctr3 < listeP.size(); ctr3++) {
                    if (!(listeP.get(ctr3).getX() == 0 && listeP.get(ctr3).getY() == 0)) {
                        canvas.drawBitmap(bmTrap1, listeP.get(ctr3).getX() * sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap, listeP.get(ctr3).getY() * sizeOfMap + 50 * Constants.SCREEN_HEIGHT / 3000, null);
                    }
                }

            if(listeCheckPoint != null)
                for (int ctr = 0; ctr < listeCheckPoint.size(); ctr++) {
                    if (listeCheckPoint.get(ctr).check)
                        canvas.drawBitmap(bmGreenFlag, listeCheckPoint.get(ctr).x * sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap, listeCheckPoint.get(ctr).y * sizeOfMap + 50 * Constants.SCREEN_HEIGHT / 3000, null);
                    else
                        canvas.drawBitmap(bmRedFlag, listeCheckPoint.get(ctr).x * sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap, listeCheckPoint.get(ctr).y * sizeOfMap + 50 * Constants.SCREEN_HEIGHT / 3000, null);
                }


            // Draw sortie
            if(chowSortie)
                if(!sortie.isCheck())
                    canvas.drawBitmap(bmClosedDoor, sortie.getX()*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap, sortie.getY()*sizeOfMap+50*Constants.SCREEN_HEIGHT/3000, null);
                else{
                    canvas.drawBitmap(bmOpenDoor, sortie.getX()*sizeOfMap + Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap, sortie.getY()*sizeOfMap+50*Constants.SCREEN_HEIGHT/3000, null);
                }
        }
    }

    public void setMur(List<Mur> l) {
        listeMur = l;
        invalidate();
    }

    public void setMonstres(List<Monstre> l) {
        listeM = l;
        invalidate();
    }

    public void setCheckPoint(List<Flag> l) {
        listeCheckPoint = l;
        invalidate();
    }

    public void setSortie(int x, int y) {
        sortie = new Sortie(x,y);
        chowSortie = true;
        invalidate();
    }

    public void setPieges(List<Piege> l) {
        listeP = l;
        invalidate();
    }

    public void setPos(int x, int y) {
        if(!isDead) {
            this.x = x;
            this.y = y;
            //interfaceGameView.sendMessage("prout");
            for (int ctr = 0; ctr < listeM.size(); ctr++) {
                if (x == listeM.get(ctr).getX() && y == listeM.get(ctr).getY()) {
                    isDead=true;
                    //interfaceGameView.sendMessage("playerDead");
                }


            }

            boolean r = true;
            for (int ctr = 0; ctr < listeCheckPoint.size(); ctr++) {
                if (x == listeCheckPoint.get(ctr).x && y == listeCheckPoint.get(ctr).y) {
                    listeCheckPoint.get(ctr).setCheck(true);

                }
                if(!listeCheckPoint.get(ctr).isCheck()){
                    r=false;
                }
            }

            if(r){
                sortie.setCheck(true);
            }





            invalidate();
        }


    }
    public void cyclePiege(){

        switch (cycle){
            case 0:
                bmTrap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.impale_0);
                bmTrap1 = Bitmap.createScaledBitmap(bmTrap1, sizeOfMap, sizeOfMap, true);
                break;
            case 1:
                bmTrap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.impale_1);
                bmTrap1 = Bitmap.createScaledBitmap(bmTrap1, sizeOfMap, sizeOfMap, true);
                break;
            case 2:
                bmTrap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.impale_2);
                bmTrap1 = Bitmap.createScaledBitmap(bmTrap1, sizeOfMap, sizeOfMap, true);
                break;
        }
        if(flagPiege){
            cycle++;
            if(cycle==2){
                flagPiege=false;
            }
        }else{
            cycle--;
            if(cycle==0){
                flagPiege=true;
            }
        }

    }
    public void mouvementMonstre( ){

        if(!isDead) {

            for (int ctrm = 0; ctrm<listeM.size(); ctrm++) {


                switch(listeM.get(ctrm).sens){
                    case 0 :
                        if (listeM.get(ctrm).flag) {

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
                    case 1 :
                        if (listeM.get(ctrm).flag) {

                            listeM.get(ctrm).setDistance(listeM.get(ctrm).getDistance()+1);
                            listeM.get(ctrm).y--;

                            invalidate();
                            if (listeM.get(ctrm).distance== listeM.get(ctrm).distanceMax || listeM.get(ctrm).getY()==0){
                                listeM.get(ctrm).flag=false;
                                listeM.get(ctrm).distance=0;
                            }

                        } else if(!listeM.get(ctrm).flag) {
                            listeM.get(ctrm).y++;
                            listeM.get(ctrm).distance=listeM.get(ctrm).distance+1;
                            invalidate();
                            if (listeM.get(ctrm).distance== listeM.get(ctrm).distanceMax ||listeM.get(ctrm).getY()==7){
                                listeM.get(ctrm).flag=true;
                                listeM.get(ctrm).distance=0;
                            }
                        }
                        break;
                    case 2 :
                        if (listeM.get(ctrm).flag) {

                            listeM.get(ctrm).setDistance(listeM.get(ctrm).getDistance()+1);
                            listeM.get(ctrm).x++;

                            invalidate();
                            if (listeM.get(ctrm).distance== listeM.get(ctrm).distanceMax || listeM.get(ctrm).getX()==19){
                                listeM.get(ctrm).flag=false;
                                listeM.get(ctrm).distance=0;
                            }

                        } else if(!listeM.get(ctrm).flag) {
                            listeM.get(ctrm).x--;
                            listeM.get(ctrm).distance=listeM.get(ctrm).distance+1;
                            invalidate();
                            if (listeM.get(ctrm).distance== listeM.get(ctrm).distanceMax ||listeM.get(ctrm).getX()==0){
                                listeM.get(ctrm).flag=true;
                                listeM.get(ctrm).distance=0;
                            }
                        }
                        break;
                    case 3 :
                        if (listeM.get(ctrm).flag) {

                            listeM.get(ctrm).setDistance(listeM.get(ctrm).getDistance()+1);
                            listeM.get(ctrm).x--;

                            invalidate();
                            if (listeM.get(ctrm).distance== listeM.get(ctrm).distanceMax || listeM.get(ctrm).getX()==0){
                                listeM.get(ctrm).flag=false;
                                listeM.get(ctrm).distance=0;
                            }

                        } else if(!listeM.get(ctrm).flag) {
                            listeM.get(ctrm).x++;
                            listeM.get(ctrm).distance=listeM.get(ctrm).distance+1;
                            invalidate();
                            if (listeM.get(ctrm).distance== listeM.get(ctrm).distanceMax ||listeM.get(ctrm).getX()==19){
                                listeM.get(ctrm).flag=true;
                                listeM.get(ctrm).distance=0;
                            }
                        }
                        break;
                }
            }

        }
    }

    public void reset() {
        x = 0;
        y = 0;
        isStart = false;
        isDead = false;
        win = false;
        listeP = new ArrayList<>();
        listeCheckPoint = new ArrayList<>();
        listeM = new ArrayList<>();
        listeMur = new ArrayList<>();
        sortie = new Sortie(200,200);
        invalidate();
    }
}
