import greenfoot.*;  

public class ChooseCar extends World
{
    public static String selectedCar;
    private boolean carSelected = false;

    public ChooseCar()
    {    
        super(600, 400, 1);
        prepareScreen();
    }

    private void prepareScreen() {
        GreenfootImage background = new GreenfootImage("choosecarbg.png");
        setBackground(background);

        GreenfootImage car1Image = new GreenfootImage("car1.png");
        GreenfootImage car2Image = new GreenfootImage("car2.png");
        car1Image.scale(car1Image.getWidth() / 5, car1Image.getHeight() / 5); // Μικρότερη εικόνα του αυτοκινήτου
        car2Image.scale(car2Image.getWidth() / 5, car2Image.getHeight() / 5); // Μικρότερη εικόνα του αυτοκινήτου

        getBackground().drawImage(car1Image, getWidth() / 2 - 70 - car1Image.getWidth() / 2, getHeight() / 2 - 20);
        getBackground().drawImage(car2Image, getWidth() / 2 + 70 - car2Image.getWidth() / 2, getHeight() / 2 - 20);

       
    }

    public static void setSelectedCar(String car) {
        selectedCar = car;
    }

    public void act() {
        handleInput();
    }

    private void handleInput() {
        if (Greenfoot.isKeyDown("left")) {
            setSelectedCar("Car1");
            carSelected = true;
            updateSelectionDisplay();
            Greenfoot.delay(10); 
        } else if (Greenfoot.isKeyDown("right")) {
            setSelectedCar("Car2");
            carSelected = true;
            updateSelectionDisplay();
            Greenfoot.delay(10);  
        }
        
        if (carSelected && Greenfoot.isKeyDown("space")) {
              MyWorld myWorld = new MyWorld(selectedCar);
            Greenfoot.setWorld(myWorld); 
        }
    }

    private void updateSelectionDisplay() {
        if (selectedCar.equals("Car1")) {
            showText("Car 1 is selected", getWidth() / 2, getHeight() / 2 - 40);
        } else if (selectedCar.equals("Car2")) {
            showText("Car 2 is selected", getWidth() / 2, getHeight() / 2 - 40);
        }
    }
}
