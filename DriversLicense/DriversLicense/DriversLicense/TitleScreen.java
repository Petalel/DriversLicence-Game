import greenfoot.*;  

public class TitleScreen extends World
{
    public TitleScreen()
    {    
        super(600, 400, 1);
        
        // Load and set the background image
        GreenfootImage background = new GreenfootImage("startgamebg.png");
        setBackground(background);

        
    }

    public void act() {
        if (Greenfoot.isKeyDown("space")) {
            Greenfoot.setWorld(new ChooseCar());
        }
    }
}
