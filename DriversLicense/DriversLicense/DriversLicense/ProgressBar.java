import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class ProgressBar extends Actor
{
    private int progress;
    private int total;

    public ProgressBar(int total) {
        this.total = total;
        this.progress = 0;
        updateImage();
    }

    public void addProgress(int value) {
        progress += value;
        if (progress > total) {
            progress = total;
        }
        updateImage();
    }

    private void updateImage() {
        GreenfootImage image = new GreenfootImage(200, 30); // Width and height of the progress bar
        image.setColor(Color.GRAY);
        image.fillRect(0, 0, 200, 30);
        image.setColor(Color.GREEN);
        int width = (int) ((double) progress / total * 200);
        image.fillRect(0, 0, width, 30);
        setImage(image);
    }
}
