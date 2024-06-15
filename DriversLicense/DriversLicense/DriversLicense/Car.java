import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Car extends Actor
{
    private GreenfootImage[] images;
    private int imageIndex;
    private int animationCycle;
    private static String selectedCar;
    private boolean moving = false;

    public Car(String theCar) {
        
        selectedCar = theCar;

        images = new GreenfootImage[4];  // Assuming you have 4 images
        for (int i = 0; i < images.length; i++) {
            images[i] = new GreenfootImage(theCar + (i + 1) + ".png");
            int newWidth = images[i].getWidth() / 4;
            int newHeight = images[i].getHeight() / 4;
            images[i].scale(newWidth, newHeight);
        }
        setImage(images[0]);  // Set the initial image
        imageIndex = 0;
        animationCycle = 0;
    }

    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (!world.isQuizActive()) {
            checkKeys();
            checkSignal();
            wrapAround();
        }
    }

     public boolean isMoving() {
        return moving;
    }
    
    private void animateMovement() {
        animationCycle++;
        if (animationCycle % 5 == 0) {
            imageIndex = (imageIndex + 1) % images.length;
            setImage(images[imageIndex]);
        }
    }

    private void checkKeys() {
        if (Greenfoot.isKeyDown("left")) {
            animateMovement();
            moving = true;
            setLocation(getX() - 5, getY());
        }
        if (Greenfoot.isKeyDown("right")) {
            animateMovement();
            moving = true;
            setLocation(getX() + 5, getY());
        }else{
            moving = false;
        }
    }

    private void checkSignal() {
        if (isTouching(Signal.class)) {
            Signal signal = (Signal) getOneIntersectingObject(Signal.class);
            MyWorld world = (MyWorld) getWorld();

            if ("stop".equals(signal.getType()) && !signal.isQuizAnswered()) {
                world.showQuiz(world.getCurrentQuizID());
                signal.setQuizAnswered(true); // Mark the quiz as answered for this signal
                // Do not remove the stop signal after showing the quiz
            } else if ("go".equals(signal.getType()) && Greenfoot.isKeyDown("g")) {
                world.increaseCorrectAnswers();
                world.removeObject(signal); // Remove the go signal after scoring
            } else if ("yield".equals(signal.getType()) && Greenfoot.isKeyDown("y")) {
                world.increaseCorrectAnswers();
                world.removeObject(signal); // Remove the yield signal after scoring
            } else if (Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("g") || Greenfoot.isKeyDown("y")) {
                world.increaseIncorrectAnswers(); // Penalize for wrong key press
            }
        }
    }

    private void wrapAround() {
        if (getX() >= getWorld().getWidth() - 1) {
            MyWorld world = (MyWorld) getWorld();
            setLocation(0, getY());
            world.nextPage(); // Move to the next page and place a new stop sign
        }
    }
}