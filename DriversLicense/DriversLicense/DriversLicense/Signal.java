import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Signal extends Actor
{
    private String type;
    private boolean quizAnswered;

    public Signal(String type) {
        this.type = type;
        this.quizAnswered = false;
        GreenfootImage image = new GreenfootImage(type + ".png"); // Load the signal image
        image.scale(image.getWidth() / 4, image.getHeight() / 4); // Resize the image to a quarter of its original size
        setImage(image);
    }

    public String getType() {
        return type;
    }

    public boolean isQuizAnswered() {
        return quizAnswered;
    }

    public void setQuizAnswered(boolean quizAnswered) {
        this.quizAnswered = quizAnswered;
    }
}



