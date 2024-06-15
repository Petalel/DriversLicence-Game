import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

public class MyWorld extends World {
    public static int correctAnswers = 0;
    public static int incorrectAnswers = 0;
    private Car car;
    private Signal startSignal;
    private Signal stopSign;
    private boolean isFirstPage = true;
    private boolean quizActive = false;
    private int QuizID = 1;
    private ArrayList<Integer> wrongAnswers = new ArrayList<>();
    private static String theCar;
    public static int timer; 
    private int timerCount;
    private GreenfootSound correctSound;
    private GreenfootSound wrongSound;   
    private ProgressBar progressBar;
    private int numberOfQuizes = 40;
    private Obstacle obstacle; // Add an obstacle
    private boolean obstacleActive = false;
    private int obstacleTimer = 0; // Timer for obstacle
    private int carResponseTimer = 0;
    private boolean lifeLostForCurrentObstacle = false; // Track if life is lost for current obstacle
    private int lives = 3; // Add lives count
   
    
     public MyWorld(String selectedCar) {    
        super(600, 400, 1);
        theCar = selectedCar;
        prepare();
        checkEndofGame();
        showTime();
        timer = 0;
        timerCount = 0;
        
        correctSound = new GreenfootSound("correct.wav");
        wrongSound = new GreenfootSound("wrong.wav");
        
        progressBar = new ProgressBar(numberOfQuizes);
        addObject(progressBar, getWidth() / 2, 30);
    }

    public void act() {
        checkEndofGame();
        showTime();
        checkObstacle();
        
        if (Greenfoot.getRandomNumber(1000) < 3 && !obstacleActive && !quizActive) { 
            showObstacle();
        } else if (obstacleActive && obstacleTimer >= 300) { 
            removeObstacle();
        }
    }

    public void updateStatsDisplay() {
        showText("Correct: " + correctAnswers, 50, 20);
        showText("Incorrect: " + incorrectAnswers, 58, 45);
        removeObjects(getObjects(Heart.class));

        for (int i = 0; i < lives; i++) {
            addObject(new Heart(), 13 + i * 22, 72);
        }
    }

    public void showTime(){
        timerCount++;
        if (timerCount % 60 == 0) { 
            timer++;
            showText("Time: ", 530, 20);
            showText(timer + " s", 580, 21);
        } else {           
            showText("Time: ", 530, 20);
            showText(timer + " s", 580, 21);
        }
    }

    private void prepare() {
        Road road = new Road();
        addObject(road, getWidth() / 2, getHeight() - road.getImage().getHeight() / 2);

        car = new Car(theCar);
        addObject(car, 50, 250); 

        showStartSignal();
        updateStatsDisplay();
    }

    private void restartGame() {
        correctAnswers = 0;
        incorrectAnswers = 0;
        isFirstPage = true;
        quizActive = false;
        QuizID = 1;
        wrongAnswers = new ArrayList<>();
        progressBar = new ProgressBar(numberOfQuizes);
        lives = 3;
    }

    public void checkEndofGame() {
        if (incorrectAnswers == 6 || lives == 0) { 
            GameOver gameOver = new GameOver();
            Greenfoot.setWorld(gameOver);
            restartGame();
        }
        
        if (correctAnswers == (numberOfQuizes - 6)) {
            WinGame winGame = new WinGame();
            Greenfoot.setWorld(winGame);
            restartGame();
        }
    }
    
    public void increaseCorrectAnswers() {
        correctAnswers++;
        correctSound.play();
        progressBar.addProgress(1);
        updateStatsDisplay();
    }

    public void increaseIncorrectAnswers() {
        wrongSound.play();
        incorrectAnswers++;
        updateStatsDisplay();
    }


    public void quizCompleted(boolean correct, int quizID) {
        quizActive = false;

        if (correct) {
            increaseCorrectAnswers();
            if (wrongAnswers.contains(quizID)) {
                wrongAnswers.remove(Integer.valueOf(quizID));
            }
        } else {
            increaseIncorrectAnswers();
            if (!wrongAnswers.contains(quizID)) {
                wrongAnswers.add(quizID);
            }
        }
        QuizID++;
    }

    public int getCurrentQuizID() {
        return QuizID;
    }   

    public void showStartSignal() {
        if (startSignal != null) {
            removeObject(startSignal);
        }
        startSignal = new Signal("go");
        addObject(startSignal, car.getX() + 50, car.getY());
        isFirstPage = true;
    }

    public void placeStopSign() {
        if (stopSign != null) {
            removeObject(stopSign);
        }

        stopSign = new Signal("stop");
        int xPosition = Greenfoot.getRandomNumber(getWidth() - 200) + 100;
        addObject(stopSign, xPosition, 250);
    }

    public void nextPage() {
        car.setLocation(50, 250);
        placeStopSign();
        if (isFirstPage) {
            removeObject(startSignal);
            isFirstPage = false;
        }
    }

    public boolean isQuizActive() {
        return quizActive;
    }

    public void resumeGame() {
        quizActive = false;
    }

    public void showObstacle() {
        if (obstacle != null) {
            removeObject(obstacle);
        }
        obstacle = new Obstacle();
        addObject(obstacle, 450, car.getY());
        obstacleActive = true;
        obstacleTimer = 0; 
        lifeLostForCurrentObstacle = false; 
    }

    public void removeObstacle() {
        if (obstacle != null) {
            removeObject(obstacle);
        }
        obstacleActive = false;
        obstacleTimer = 0; 
    }
    
      public void checkObstacle() {
        carResponseTimer++;
        obstacleTimer++;
        if (carResponseTimer > 300 && obstacleActive && car.isMoving()) { 
            if(!lifeLostForCurrentObstacle){
                  loseLife();
                  lifeLostForCurrentObstacle = true;
            }
           
        }
    }
    
    public void loseLife() {
        lives--;
        updateStatsDisplay();
    }

        
    public void showQuiz(int QuizID) {
        quizActive = true;
        
        String question = "What does the 'Yield' sign mean?";
        String[] answers = { "Yield", "Go", "Stop", "Turn Left" };
        int correctAnswer = 0; // index of the correct answer
        String theory = "όταν ο χρήστης βλέπει το μήνυμα stop...";
        String quizImage = "";
    
        if(QuizID == 1) {
            question = "What should you do if your windscreen is shattered by a stone from a passing vehicle?";
            answers = new String[] { "Ease off the accelerator, wind down the side window to look out of, brake lightly and stop off the road."
                , "Brake firmly to bring your vehicle to a stop as quickly as you can.",
                "Immediately punch a hole in the windscreen with your fist so that you can see if it is safe ahead." }; // 3 answers
            correctAnswer = 0;
            theory = "The correct answer is 'Ease off the accelerator, wind down the side window to look out of, brake lightly and stop off the road.' This approach ensures that you maintain control of your vehicle while gradually coming to a stop in a safe location. Abrupt braking can cause accidents, and punching a hole in the windscreen is unsafe and impractical.";
            quizImage = "";
        }
        if(QuizID == 2) {
            question = "The adult passenger in the rear is breaking the law because she is-";
            answers = new String[] { "Sitting in the back seat.", "Not wearing the available seat belt.", "Distracting the driver." };
            correctAnswer = 1;
            theory = "The correct answer is 'Not wearing the available seat belt.' In many places, the law requires all passengers in a vehicle, including those in the rear seats, to wear seat belts. This is to ensure their safety in case of an accident.";
            quizImage = "familyinsidecar.png";
        }
        if(QuizID == 3) {
            question = "At a children's crossing you must-";
            answers = new String[] { "Slow down or stop to avoid hitting the person.", "Stop and give way to any pedestrian or bicycle rider entering the crossing.", "Stop and give way to any child on the crossing." };
            correctAnswer = 1;
            theory = "The correct answer is 'Stop and give way to any pedestrian or bicycle rider entering the crossing.' At a children's crossing, it is essential to stop and allow all pedestrians and bicycle riders to cross safely, not just children. This ensures the safety of all individuals using the crossing.";
        }
        if(QuizID == 4) {
            question = "What does this sign mean?";
            answers = new String[] { "No turns permitted.", "Double lane bridge ahead.", "Traffic travels in each direction." };
            correctAnswer = 2;
            theory = "The correct answer is 'Traffic travels in each direction.' This sign indicates that the road ahead has two-way traffic, meaning vehicles will be traveling in both directions. It is important to stay on the correct side of the road and be aware of oncoming traffic.";
            quizImage = "twowaysign.png";
        }
        if(QuizID == 5) {
            question = "When driving near children playing or walking near the edge of the road, you should-";
            answers = new String[] { "Sound your horn to warn them of your presence.", "Slow down and be ready to make a safe stop.", "Continue at the same speed and drive around them." };
            correctAnswer = 1;
            theory = "The correct answer is 'Slow down and be ready to make a safe stop.' Children can be unpredictable, and it is essential to reduce your speed and be prepared to stop quickly to avoid any potential accidents. Sounding your horn can startle them, and continuing at the same speed is unsafe.";
        }
        if(QuizID == 6) {
            question = "Which one of the following conditions is a driver allowed to have an arm extended from a moving vehicle?";
            answers = new String[] { "Only when adjusting your outside mirrors.", "When holding on to an overhanging load, if safe.", "Only while giving a hand signal." };
            correctAnswer = 2;
            theory = "The correct answer is 'Only while giving a hand signal.' Extending your arm from a moving vehicle is legally permissible only when giving a hand signal to indicate a turn or lane change. It ensures other drivers can see your intentions clearly. Extending your arm for other reasons is generally unsafe and often illegal.";
        }
        if(QuizID == 7) {
            question = "Double unbroken dividing lines are marked on a roadway. You may-";
            answers = new String[] { "Cross them to avoid an obstruction, if it is safe to do so.", "Cross them to make a U-turn.", "Cross them to overtake a car ahead if it is safe to do so." };
            correctAnswer = 0;
            theory = "The correct answer is 'Cross them to avoid an obstruction, if it is safe to do so.' Double unbroken dividing lines indicate that you should not cross them under normal driving conditions. However, you may cross these lines to avoid an obstruction on the road, provided it is safe to do so. Making a U-turn or overtaking a car by crossing double unbroken lines is illegal and unsafe.";
        }
        if(QuizID == 8) {
            question = "If there are no lanes marked on the road, you should drive-";
            answers = new String[] { "Anywhere on your side of the road.", "As near as practical to the left hand side of the road.", "Along the middle of the road." };
            correctAnswer = 1;
            theory = "The correct answer is 'As near as practical to the left hand side of the road.' When there are no lanes marked on the road, you should keep as far to the left as possible. This helps ensure safety by reducing the risk of head-on collisions with oncoming traffic and allows space for other vehicles to pass if necessary.";
        }
        if(QuizID == 9) {
            question = "If you double your speed and you crash into a large stationary object such as a 'stobie' pole, what happens to your risk of being injured or killed?";
            answers = new String[] { "Could increase by 4 times or more.", "Doubles.", "Increases slightly." };
            correctAnswer = 0;
            theory = "The correct answer is 'Could increase by 4 times or more.' When you double your speed, the kinetic energy of your vehicle quadruples. This significant increase in energy means that the force of impact during a crash is much greater, substantially raising the risk of severe injury or death.";
        }
        if(QuizID == 10) {
            question = "Which ONE of the following statements is TRUE?";
            answers = new String[] { "The speed limit in School Zones only applies when children in the School Zone are dressed in school uniform.", "You are allowed to travel faster than the School Zone speed limit if there is no child in the School Zone.", "The speed limit in School Zones applies on school days only." };
            correctAnswer = 2;
            theory = "The correct answer is 'The speed limit in School Zones applies on school days only.' School zone speed limits are in effect during specific hours on school days to protect children who are arriving at or leaving school. This limit applies regardless of whether children are visible or in school uniforms.";
        }
        if(QuizID == 11) {
            question = "What should you do on approaching a railway level crossing displaying a STOP sign?";
            answers = new String[] { "Slow down to 10 km/h, then proceed through the crossing.", "Stop, only if a train is at the crossing.", "Stop at all times and proceed when safe to do so." };
            correctAnswer = 2;
            theory = "The correct answer is 'Stop at all times and proceed when safe to do so.' A STOP sign at a railway level crossing means that you must come to a complete stop regardless of whether you see a train. Only proceed after stopping and ensuring that it is safe to cross.";
            quizImage = "railwaycrossing.png";
        }
        if(QuizID == 12) {
            question = "You give a friend and her 12 year old son a lift. Your friend sits in the front and her son gets in the back. The boy does not put his seat belt on. By law, who has to make sure the child wears his seat belt?";
            answers = new String[] { "You do, because you are the driver.", "The child does because it is always the passenger's responsibility.", "Your friend does because she is the parent." };
            correctAnswer = 0;
            theory = "The correct answer is 'You do, because you are the driver.' By law, the driver is responsible for ensuring that all passengers under the age of 16 are wearing their seat belts. It is important to ensure that everyone in the vehicle is properly restrained to ensure their safety.";
        }
        if(QuizID == 13) {
            question = "What is this driver doing that is negligent and illegal?";
            answers = new String[] { "Crossing an unbroken line at a curve.", "Overtaking on the kerb side.", "Not signalling to change lanes." };
            correctAnswer = 0;
            theory = "The correct answer is 'Crossing an unbroken line at a curve.' It is both negligent and illegal to cross an unbroken line, especially at a curve where visibility is limited. This maneuver increases the risk of head-on collisions and other accidents.";
            quizImage = "yellowcar.png";
        }
        if(QuizID == 14) {
            question = "What does this sign mean?";
            answers = new String[] { "Steep down grade in the road ahead, slow down.", "Road under repair, slow down.", "Road ahead slippery when wet, drive carefully." };
            correctAnswer = 0;
            theory = "The correct answer is 'Steep down grade in the road ahead, slow down.' This sign warns drivers that the road ahead has a steep downhill section, and it is important to reduce speed to maintain control of the vehicle.";
            quizImage = "yellowSign.png";
        }
        if(QuizID == 15) {
            question = "Turn signals are";
            answers = new String[] { "Always required for any turn.", "Not required when turning at traffic lights with a green arrow.", "Not required when turning at intersections." };
            correctAnswer = 0;
            theory = "The correct answer is 'Always required for any turn.' Turn signals must be used to indicate any turn or lane change to alert other drivers and pedestrians of your intentions. This ensures safety by providing clear communication on the road.";
        }
        if(QuizID == 16) {
            question = "Are you permitted to park in the direction of the arrow?";
            answers = new String[] { "Yes, if you are carrying two or more passengers.", "No, unless the vehicle you are driving is a taxi.", "Yes, provided no taxis are using the area." };
            correctAnswer = 1;
            theory = "The correct answer is 'No, unless the vehicle you are driving is a taxi.' This parking area is designated for taxis only, and other vehicles are not permitted to park there regardless of the number of passengers or whether any taxis are currently using the area.";
            quizImage = "taxizone.png";
        }
        if(QuizID == 17) {
            question = "At which ONE of the following places are U-turns permitted?";
            answers = new String[] { "At intersections that have traffic lights showing a green right turn arrow and when it is safe.", "On a freeway.", "In an intersection where a 'Stop' or 'Give Way' sign is displayed." };
            correctAnswer = 2;
            theory = "The correct answer is 'In an intersection where a 'Stop' or 'Give Way' sign is displayed.' You are permitted to make U-turns at intersections with 'Stop' or 'Give Way' signs, provided there is no 'No U-Turn' sign present. It is important to ensure the turn can be made safely and without disrupting traffic.";
        }
        if(QuizID == 18) {
            question = "When you see this sign you should-";
            answers = new String[] { "Drive with caution.", "Not pass another vehicle.", "Not drive beyond the sign." };
            correctAnswer = 2;
            theory = "The correct answer is 'Not drive beyond the sign.' The 'No Entry' sign indicates that vehicles are prohibited from entering the area beyond the sign. It is essential to obey this sign to avoid entering a restricted or potentially dangerous area.";
            quizImage = "noentry.png";
        }
        if(QuizID == 19) {
            question = "When you apply the brakes, it takes at least 20 metres to bring your car to a stop on a road from 50 km/h. If you double your speed to 100 km/h, what minimum distance will it take to stop your car when you apply the brakes on the same road surface?";
            answers = new String[] { "40 metres.", "30 metres.", "80 metres." };
            correctAnswer = 2;
            theory = "The correct answer is '80 metres.' When you double your speed, the braking distance increases by a factor of four, not two, because the stopping distance is proportional to the square of the speed. Therefore, if it takes 20 metres to stop at 50 km/h, it will take at least 80 metres to stop at 100 km/h.";
        }
        if(QuizID == 20) {
            question = "Where there is parallel kerbside parking, are you allowed to double park alongside a parked vehicle?";
            answers = new String[] { "No, not at any time.", "Yes, if delivering goods.", "Yes, if not obstructing traffic." };
            correctAnswer = 0;
            theory = "The correct answer is 'No, not at any time.' Double parking is illegal and unsafe because it obstructs the flow of traffic, creates a hazard for other drivers, and can lead to accidents. It is important to always park legally and considerately.";
        }
        if(QuizID == 21) {
            question = "You are sitting in a seat that has a seat belt fitted to it. When must you wear it?";
            answers = new String[] { "Only when driving long distances.", "Any time the vehicle is travelling forwards.", "Only if you are travelling greater than 30 km/h." };
            correctAnswer = 1;
            theory = "The correct answer is 'Any time the vehicle is travelling forwards.' Seat belts must be worn at all times when the vehicle is in motion. This is a legal requirement and a crucial safety measure to protect passengers in case of sudden stops or collisions.";
        }
        if(QuizID == 22) {
            question = "When can a private car travel in a lane marked by this sign?";
            answers = new String[] { "When carrying at least two passengers.", "Only to overtake another vehicle.", "Only within 100 metres of making a turn." };
            correctAnswer = 2;
            theory = "The correct answer is 'Only within 100 metres of making a turn.' A bus lane is designated primarily for buses, but private cars are allowed to use it only when they need to turn within a short distance (typically 100 metres) before making the turn. This helps maintain the flow of traffic while allowing necessary maneuvers.";
            quizImage = "lane.png";
        }
        if(QuizID == 23) {
            question = "Which one of the following is most likely to cause you to lose control of your vehicle on a bend?";
            answers = new String[] { "Entering the bend too fast.", "A wet road.", "A gravel road surface." };
            correctAnswer = 0;
            theory = "The correct answer is 'Entering the bend too fast.' While wet or gravel road surfaces can contribute to loss of control, entering a bend too fast significantly increases the risk as it reduces the vehicle's stability and traction, making it difficult to navigate the curve safely.";
        }
        if(QuizID == 24) {
            question = "What should you do if a rear tyre on your vehicle should deflate rapidly while travelling along the road?";
            answers = new String[] { "Accelerate lightly to hold the vehicle on track.", "Ease off the accelerator, hold the steering wheel firmly and very lightly brake to a stop.", "Brake firmly to slow the vehicle quickly." };
            correctAnswer = 1;
            theory = "The correct answer is 'Ease off the accelerator, hold the steering wheel firmly and very lightly brake to a stop.' This method helps maintain control of the vehicle, preventing sudden swerves or loss of control. Braking too hard can cause the vehicle to skid, especially with a deflated tyre.";
        }
        if(QuizID == 25) {
            question = "You are approaching a green light in vehicle A. An ambulance sounding its siren is approaching the same intersection and has a red light. You should-";
            answers = new String[] { "Slow down and stop if necessary to prevent getting in its way.", "Keep driving because you have the green light.", "Pull over to the left before you reach the intersection." };
            correctAnswer = 0;
            theory = "The correct answer is 'Slow down and stop if necessary to prevent getting in its way.' Emergency vehicles such as ambulances have the right of way when their sirens and lights are on, even if they have a red light. It is essential to slow down, stop if necessary, and allow them to pass safely.";
            quizImage = "roadforquiz.png";
        }
        if(QuizID == 26) {
            question = "You are in car marked A. You wish to overtake car marked B. You should-";
            answers = new String[] { "Cross the unbroken single line and overtake.", "Cross the line and overtake only if it is safe.", "Wait behind car B until it moves into the left lane." };
            correctAnswer = 2;
            theory = "The correct answer is 'Wait behind car B until it moves into the left lane.' You cannot cross an unbroken single line to overtake a vehicle. It is illegal and unsafe. You must wait until the vehicle ahead moves into a different lane or until it is safe and legal to overtake.";
            quizImage = "roadforquiz2.png";
        }
        if(QuizID == 27) {
            question = "This sign means you should-";
            answers = new String[] { "Drive carefully, roundabout ahead.", "Turn left at next street.", "Pass to the left of the sign." };
            correctAnswer = 2;
            theory = "The correct answer is 'Pass to the left of the sign.' This sign indicates that vehicles must pass to the left of the obstruction or median. It ensures orderly traffic flow and safety at the designated point on the road.";
            quizImage = "keepleft.png";
        }
        if(QuizID == 28) {
            question = "You must give way to pedestrians-";
            answers = new String[] { "Only on marked foot crossings and traffic light pedestrian crossings.", "Only on marked foot crossings.", "At all times, even if there is no marked crossing." };
            correctAnswer = 2;
            theory = "The correct answer is 'At all times, even if there is no marked crossing.' Pedestrians have the right of way in many situations, and drivers must always be prepared to yield to them to ensure their safety, regardless of whether a marked crossing is present.";
        }
        if(QuizID == 29) {
            question = "When turning right from a two way unlaned road to another two way unlaned road, you must approach the intersection from-";
            answers = new String[] { "Any position on the left side of the road.", "A position parallel to and just left of the centre of the road.", "The middle of your side of the road." };
            correctAnswer = 1;
            theory = "The correct answer is 'A position parallel to and just left of the centre of the road.' When turning right from a two-way unlaned road, you should position your vehicle as close to the centre of the road as possible, without crossing it. This positioning helps you make the turn safely and allows other vehicles to pass on your left if necessary.";
        }
        if(QuizID == 30) {
            question = "You should expect this sign-";
            answers = new String[] { "If a one way street is ahead.", "When a divided road ends and two way traffic is ahead.", "If a one lane bridge is ahead." };
            correctAnswer = 1;
            theory = "The correct answer is 'When a divided road ends and two way traffic is ahead.' This sign indicates that the divided road is ending, and you will be entering an area where traffic moves in both directions. It is important to be prepared for the change in traffic flow and adjust your driving accordingly.";
            quizImage = "yellowSign2.png";
        }
        if(QuizID == 31) {
            question = "If you get sleepy while driving, it is best to-";
            answers = new String[] { "Stop, rest, and change drivers if possible.", "Turn on the radio very loud.", "Turn on the air conditioning or open the windows." };
            correctAnswer = 0;
            theory = "The correct answer is 'Stop, rest, and change drivers if possible.' When you feel sleepy while driving, the safest option is to stop and rest or change drivers. Turning on the radio or air conditioning might provide temporary alertness, but they do not address the root issue of fatigue and can be dangerous.";
        }
        if(QuizID == 32) {
            question = "What is the most common cause for young provisional drivers being involved in crashes with cross traffic at intersections?";
            answers = new String[] { "Inexperience at selecting safe gaps in moving traffic.", "Inattention.", "Alcohol consumption." };
            correctAnswer = 0;
            theory = "The correct answer is 'Inexperience at selecting safe gaps in moving traffic.' Young provisional drivers often lack the experience to accurately judge the speed and distance of oncoming vehicles, leading to poor decisions and increased risk of collisions at intersections.";
        }
        if(QuizID == 33) {
            question = "What does this sign mean?";
            answers = new String[] { "One way traffic ahead do not overtake any other vehicles.", "Narrow bridge ahead, room for only two vehicles, slow down and drive carefully.", "Two lane traffic ahead." };
            correctAnswer = 1;
            theory = "The correct answer is 'Narrow bridge ahead, room for only two vehicles, slow down and drive carefully.' This sign indicates that there is a narrow bridge ahead and that drivers should slow down and proceed with caution as there is only enough room for two vehicles to pass.";
            quizImage = "yellowSign3.png";
        }
        if(QuizID == 34) {
            question = "You are driving a motor vehicle, which is a utility (ute). Where are passengers allowed to be carried?";
            answers = new String[] { "In either the cabin or on the back of the utility, so long as it is safe and the passengers stay sitting down within the sides of the vehicle.", "In either the cabin or back of the utility and can be sitting down or standing.", "Only in the cabin of the utility." };
            correctAnswer = 2;
            theory = "The correct answer is 'Only in the cabin of the utility.' Passengers are only allowed to be carried in the cabin of a utility vehicle. Carrying passengers in the back of the utility is unsafe and typically illegal as it exposes them to significant risk of injury in case of an accident.";
        }
        if(QuizID == 35) {
            question = "When driving past parked vehicles, which of the following is it most important to do-";
            answers = new String[] { "Stay as close as possible to the vehicle in front of you.", "Drive in the right hand lane, if there is one.", "Watch for pedestrians, animals and car doors opening." };
            correctAnswer = 2;
            theory = "The correct answer is 'Watch for pedestrians, animals and car doors opening.' When driving past parked vehicles, it's crucial to be vigilant for potential hazards such as pedestrians stepping out between cars, animals running into the street, and car doors opening unexpectedly. This helps ensure the safety of everyone on the road.";
        }
        if(QuizID == 36) {
            question = "When is a young driver most at risk of having a serious crash?";
            answers = new String[] { "When driving to and from work in peak hour.", "When driving at night and on weekends.", "When driving on holidays." };
            correctAnswer = 1;
            theory = "The correct answer is 'When driving at night and on weekends.' Young drivers are most at risk during these times due to factors such as reduced visibility, increased fatigue, and the higher likelihood of encountering other drivers who may be impaired or driving recklessly. Additionally, night driving and weekend activities often involve social events where alcohol consumption may be a factor.";
        }
        if(QuizID == 37) {
            question = "What does this sign mean?";
            answers = new String[] { "All right lane traffic must turn right at the next intersection.", "Right lane traffic may turn right or go straight ahead at the next intersection.", "One way traffic ahead." };
            correctAnswer = 0;
            theory = "The correct answer is 'All right lane traffic must turn right at the next intersection.' This sign indicates that vehicles in the right lane are required to turn right at the upcoming intersection. It is important to follow this directive to maintain proper traffic flow and avoid confusion or accidents.";
            quizImage = "rightlane.png";
        }
        if(QuizID == 38) {
            question = "If an overtaking vehicle signals that it must move in, in front of you, you should-";
            answers = new String[] { "Flash your lights at the overtaking vehicle.", "Speed up and not let the vehicle back in.", "Prepare to slow down to allow room." };
            correctAnswer = 2;
            theory = "The correct answer is 'Prepare to slow down to allow room.' When an overtaking vehicle signals its intention to move in front of you, it is important to be courteous and safe by preparing to slow down and allow space for the vehicle to merge back into the lane. This helps prevent accidents and promotes smooth traffic flow.";
            quizImage = "roadforquiz3.png";
        }
        if(QuizID == 39) {
            question = "When is a driver allowed to go over the speed limit?";
            answers = new String[] { "At anytime so long as the driver does not do more than 10 km/h over the speed limit.", "At anytime so long as the driver does not do more than 5 km/h over the speed limit.", "Not at anytime." };
            correctAnswer = 2;
            theory = "The correct answer is 'Not at anytime.' Speed limits are set for the safety of all road users and must be adhered to at all times. Exceeding the speed limit, even by a small margin, is illegal and increases the risk of accidents and severe consequences.";
        }
        if(QuizID == 40) {
            question = "At a pedestrian crossing you must-";
            answers = new String[] { "Slow down and sound your horn to hurry up the person.", "Maintain your speed and swerve around the person.", "Give way to any pedestrian or bicycle rider on the crossing." };
            correctAnswer = 2;
            theory = "The correct answer is 'Give way to any pedestrian or bicycle rider on the crossing.' When approaching a pedestrian crossing, you must stop and give way to any pedestrian or cyclist who is on or entering the crossing. This is essential for their safety and is required by law.";
            quizImage = "roadforquiz4.png";
        }
    
        addObject(new Quiz(question, answers, correctAnswer, incorrectAnswers, theory, quizImage), getWidth() / 2, getHeight() / 2);
    }

}
