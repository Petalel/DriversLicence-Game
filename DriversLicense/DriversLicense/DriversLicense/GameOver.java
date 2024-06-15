import greenfoot.*; 


public class GameOver extends World
{
 
    private GreenfootSound wrongSound;

    public GameOver()
    {    
        super(600, 400, 1);
        
        wrongSound = new GreenfootSound("gameover.wav");
        wrongSound.play();
        
        GreenfootImage background = new GreenfootImage("gameoverbg.png");
        setBackground(background);

        showText(" " + MyWorld.incorrectAnswers ,getWidth() / 2 + 70, getHeight() / 2 - 33);  // Assuming score is kept in Spaceship class
        showText(" " + MyWorld.timer + " s",getWidth() / 2 + 50, getHeight() / 2 + 2);  // Assuming score is kept in Spaceship class
        //showText(" " + MyWorld.correctAnswers ,getWidth() / 2 + 30, getHeight() / 2 + 35);  // Assuming score is kept in Spaceship class

        Greenfoot.start();
    }
    public void act()
    {
        if (Greenfoot.isKeyDown("space")) Greenfoot.setWorld(new TitleScreen());
    }
}


