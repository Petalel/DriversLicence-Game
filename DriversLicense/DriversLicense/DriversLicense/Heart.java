import greenfoot.*;

public class Heart extends Actor {

    public Heart() {
        GreenfootImage heartImage = new GreenfootImage("heart.png");
                heartImage.scale(heartImage.getWidth() / 20, heartImage.getHeight() / 20); // Scale the image to half its original size
// Assuming "heart.png" is the image file for the heart icon
        setImage(heartImage);
    }
}