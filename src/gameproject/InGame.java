package gameproject;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class InGame {

    Image playerImage = new Image("image/2-Recovered.png");
    Image wallImage = new Image("image/wall.png");
    Image monsterImage = new Image("image/spider_monster.png");
    Image stairImage = new Image("image/stair.png");

    BlackjackController gameFight = new BlackjackController();

    private Text info = new Text();
    public Entity player;
    private Entity stair;
    public List<Entity> enemy = new ArrayList<Entity>();
    private List<Entity> wall = new ArrayList<Entity>();
    private char move;
    private int temp;
    private String map = "************   *     **** * ****** * *     ** * ***** ** *   *   ** *** * ****     *   ** ******* **         ************";
    private boolean isFight = false;
    private boolean whileCheck = false;
     
    public Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(830, 550);
        Rectangle InGameBG = new Rectangle(830, 550);
        InGameBG.setFill(Color.BLACK);
        Font font = new Font("font/font1.TTF",20);
        info.setFont(font);
        info.setTranslateX(620);
        info.setTranslateY(100);
        info.setFill(Color.WHITE);
        

        mapGen mapgen = new mapGen();

        map = mapgen.getGrid();
        System.out.println(map);
        player = new Entity(50, 50, 50, 50, playerImage);

        int tempx = 0;
        int tempy = 0;
        int temp = 0;

        for (int j = 0; j <= 10; j++) {
            for (int i = 0; i <= 10; i++) {
                if (map.charAt(temp) == '*') {
                    wall.add(new Entity(tempx, tempy, 50, 50, wallImage));
                }
                temp = temp + 1;
                tempx = tempx + 50;
            }
            tempx = 0;
            tempy = tempy + 50;
        }

        int check = 0;
        int xPositionE = 0, yPositionE = 0;
        int xPositionS, yPositionS;

        while (true) {
            int randEnemyX = (int) (3 + Math.random() * 4);
            int randEnemyY = (int) (3 + Math.random() * 4);
            if (check == 0) {
                if (map.charAt(randEnemyX + (randEnemyY * 11)) == ' ') {
                    xPositionE = randEnemyX;
                    yPositionE = randEnemyY;
                    check = 1;
                }
            }
            if (check == 1) {
                randEnemyX = (int) (7 + Math.random() * 4);
                randEnemyY = (int) (7 + Math.random() * 4);
                if (map.charAt(randEnemyX + (randEnemyY * 11)) == ' ') {
                    if (randEnemyX != xPositionE || randEnemyY != yPositionE) {
                        xPositionS = randEnemyX;
                        yPositionS = randEnemyY;
                        check = 0;
                        break;
                    }
                }
            }
        }

        enemy.add(new Entity(xPositionE * 50, yPositionE * 50, 50, 50, monsterImage));
        stair = new Entity(xPositionS * 50, yPositionS * 50, 50, 50, stairImage);

        root.getChildren().addAll(InGameBG, stair, player, info);

        for (Entity objecT : wall) {
            root.getChildren().addAll(objecT);
        }
        for (Entity objecT : enemy) {
            root.getChildren().addAll(objecT);
        }

        return root;
    }

    public boolean checkCollision(Entity a, Entity b) {

        if (a.getBoundsInParent().intersects(b.getBoundsInParent()) && a.getTranslateX() == b.getTranslateX() && a.getTranslateY() == b.getTranslateY()) {
            return true;
        } else {
            return false;
        }
    }

    private static class Entity extends Parent {

        double width;
        double height;

        public Entity(double x, double y, double w, double h, Image image) {
            setTranslateX(x);
            setTranslateY(y);
            width = w;
            height = h;
            Rectangle shape = new Rectangle(w, h);
            shape.setFill(new ImagePattern(image));
            getChildren().add(shape);
        }
    }

    public void upDate(Scene scene) {
        move = '0';
        scene.setOnKeyPressed(event -> {
            if (gameFight.isCheckWindow() == false) {
                if (event.getCode() == KeyCode.W) {
                    player.setTranslateY(player.getTranslateY() - 50);
                    move = 'w';
                }
                if (event.getCode() == KeyCode.S) {
                    player.setTranslateY(player.getTranslateY() + 50);
                    move = 's';
                }
                if (event.getCode() == KeyCode.A) {
                    player.setTranslateX(player.getTranslateX() - 50);
                    move = 'a';
                }
                if (event.getCode() == KeyCode.D) {
                    player.setTranslateX(player.getTranslateX() + 50);
                    move = 'd';
                }
            }
            
            for (int i = 0; i < enemy.size(); i++) {
                if (checkCollision(player, enemy.get(i))) {
                    try {
                        gameFight.showWindow();
                    } catch (IOException ex) {
                        Logger.getLogger(InGame.class.getName()).log(Level.SEVERE, null, ex);
                    }  
                
                }
                if(gameFight.isCheckWindow()) info.setText("PLEASE ENTER");
                else if(gameFight.isCheckWindow() == false) info.setText(" ");
                if (event.getCode() == KeyCode.ENTER) 
                {
                    gameFight.setCheckWindow(false);
                }
                /*if (event.getCode() == KeyCode.F) {
                    info.setText("");
                    enemy.get(i).setVisible(false);
                    enemy.remove(i);
                    gameFight.setCheckWindow(false);
                    
                }*/
                if(gameFight.isPlayerWin() == true && event.getCode() == KeyCode.ENTER) 
                {
                    gameFight.setPlayerWin(false);
                    enemy.get(i).setVisible(false);
                    enemy.remove(i);
                    
                }
                
                //System.out.println("gameFight : " + gameFight.isCheckWindow());
            }
            if (checkCollision(player, stair)) {
            }

            for (Entity objecT : wall) {
                if (checkCollision(player, objecT) == true) {
                    if (move == 'w') {
                        player.setTranslateY(player.getTranslateY() + 50);
                    } else if (move == 's') {
                        player.setTranslateY(player.getTranslateY() - 50);
                    } else if (move == 'a') {
                        player.setTranslateX(player.getTranslateX() + 50);
                    } else if (move == 'd') {
                        player.setTranslateX(player.getTranslateX() - 50);
                    }
                }
            }
            for (int i = 0; i < enemy.size(); i++) {
                if (checkCollision(player, enemy.get(i)) == true) {
                    if (move == 'w') {
                        player.setTranslateY(player.getTranslateY() + 50);
                    } else if (move == 's') {
                        player.setTranslateY(player.getTranslateY() - 50);
                    } else if (move == 'a') {
                        player.setTranslateX(player.getTranslateX() + 50);
                    } else if (move == 'd') {
                        player.setTranslateX(player.getTranslateX() - 50);
                    }
                }
            }
        });
    }
}
