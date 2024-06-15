import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;

public class Obstacle extends Actor {
    public Obstacle() {
        Random random = new Random();
        String imageFile = random.nextBoolean() ? "pedestrian1.png" : "pedestrian2.png";
        GreenfootImage image = new GreenfootImage(imageFile);
        image.scale(image.getWidth() / 2, image.getHeight() / 2); // Scale the image to make it smaller
        setImage(image);
    }
}
