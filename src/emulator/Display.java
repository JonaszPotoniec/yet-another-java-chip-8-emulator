package emulator;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Display {
    final private Pane gamePane;
    final private boolean[][] displayArray;
    final private int upScaler;
    final private Color backgroundColor;
    final private Color foregroundColor;

    Display(Pane gamePane){
        this.gamePane = gamePane;
        displayArray = new boolean[64][32];
        upScaler = 8;
        foregroundColor = Color.web("08181F");
        backgroundColor = Color.web("7DB7A3");
        autoScale(gamePane);
        clearScreen();
    }

    public void clearScreen(){
        for(int x = 0; x < 64; x++)
            for(int y = 0; y < 32; y++)
                displayArray[x][y] = false;

        Rectangle rectangle = new Rectangle(upScaler*64,upScaler*32, backgroundColor);
        rectangle.relocate(0,0);
        gamePane.getChildren().add(rectangle);
    }

    public boolean drawSprite(byte []tab, int x,  int y){
        boolean erased = false;

        for(int i = 0; i < tab.length; i++)
            for(byte bit = 0; bit < 8; bit++)
                if((tab[i] >> bit & 0b00000001) != 0) {
                    int draw_x = ((x+7-bit) % 64);
                    int draw_y = ((y+i) % 32);
                if(displayArray[draw_x][draw_y]){
                    erased = true;
                    displayArray[draw_x][draw_y] = false;

                    Rectangle rectangle = new Rectangle(upScaler,upScaler, backgroundColor);
                    rectangle.relocate(draw_x*upScaler,draw_y*upScaler);
                    gamePane.getChildren().add(rectangle);
                } else {
                    displayArray[draw_x][draw_y] = true;

                    Rectangle rectangle = new Rectangle(upScaler,upScaler, foregroundColor);
                    rectangle.relocate(draw_x*upScaler,draw_y*upScaler);
                    gamePane.getChildren().add(rectangle);
                }
            }

        return erased;
    }

    private void autoScale(Pane gamePane){
        gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
            gamePane.setTranslateX(0);
            gamePane.setScaleX((Double)newVal / 512);
        });

        gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {
            gamePane.setTranslateY(0);
            gamePane.setScaleY((Double)newVal / 256);
        });
    }
}
