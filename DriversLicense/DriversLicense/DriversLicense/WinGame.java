import greenfoot.*; 


public class WinGame extends World
{
 
    private GreenfootSound winSound;

    public WinGame()
    {    
        super(600, 400, 1);
         
        winSound = new GreenfootSound("win.wav");
        winSound.play();
        
        GreenfootImage background = new GreenfootImage("congratsbg.png");
        setBackground(background);

        showText(" " + MyWorld.correctAnswers ,getWidth() / 2 + 100, getHeight() / 2 - 8);  // Assuming score is kept in Spaceship class
        showText(" " + MyWorld.incorrectAnswers ,getWidth() / 2 + 110, getHeight() / 2 + 13);  // Assuming score is kept in Spaceship class
        showText(" " + MyWorld.timer + " s" ,getWidth() / 2 + 80, getHeight() / 2 + 35);  // Assuming score is kept in Spaceship class

        Greenfoot.start();
    }
    public void act()
    {
        if (Greenfoot.isKeyDown("space")) Greenfoot.setWorld(new TitleScreen());
    }
}
