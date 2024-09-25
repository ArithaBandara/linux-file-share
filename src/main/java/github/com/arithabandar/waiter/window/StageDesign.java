package github.com.arithabandar.waiter.window;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class StageDesign {
    public Image getIcon() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/icon.jpg")));
    }
    public double[] setStageCenter(){
        Screen screen = Screen.getPrimary();
        // Get the bounds of the primary screen
        Rectangle2D bounds = screen.getVisualBounds();
        // Get width and height of the screen
        double screenWidth = bounds.getWidth();
        double screenHeight = bounds.getHeight();
        writeError(screenWidth,screenHeight);
        double dimension = Math.min(screenWidth, screenHeight);

        double halfWith=screenWidth/2;
        double halfHeight=screenHeight/2;
        double stageSize=screenWidth-screenHeight;

        double[] array = new double[4];
        array[0]=dimension;
        array[1]=halfWith;
        array[2]=halfHeight;
        array[3]=stageSize;
        return array;
    }
    private void writeError(double screenWidth,double screenHeight){
        // Path to the text file
        String filePath = "output.txt";

        // Writing content to the file
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.append("screenWidth: "+screenWidth);
            writer.newLine();
            writer.append("screenHeight: "+screenHeight);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            try {
                writer.append("error log:"+ e.getMessage());
                writer.newLine();
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
