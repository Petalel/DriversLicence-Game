import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.FontMetrics;

public class Quiz extends Actor
{
    private String question;
    private String[] answers;
    private int correctAnswer;
    private int incorrectAnswer;
    private String theory;
    private boolean answered = false;
    private boolean showingTheory = false;
    private String quizImage;

    public Quiz(String question, String[] answers, int correctAnswer, int incorrectAnswer, String theory, String quizImage) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswer = incorrectAnswer;
        this.theory = theory;
        this.quizImage = quizImage;
    }

    protected void addedToWorld(World world) {
        displayQuiz();
    }

    private void displayQuiz() {
        GreenfootImage quizImageCanvas = new GreenfootImage(600, 400);
        quizImageCanvas.setColor(Color.WHITE);
        quizImageCanvas.fillRect(50, 50, 500, 300);
        quizImageCanvas.setColor(Color.BLACK);
        quizImageCanvas.drawRect(50, 50, 500, 300);

        // Draw the question
        drawWrappedString(quizImageCanvas, question, 70, 80, 460);

        for (int i = 0; i < answers.length; i++) {
            drawWrappedString(quizImageCanvas, (char)('A' + i) + ": " + answers[i], 70, 140 + i * 40, 460);
        }

        // Load and draw the quiz image
        if (quizImage != null && !quizImage.isEmpty()) {
            GreenfootImage quizImageLoaded = new GreenfootImage(quizImage);
            quizImageLoaded.scale(100, 100); 
            quizImageCanvas.drawImage(quizImageLoaded, 400, 100); 
        }

        setImage(quizImageCanvas);
    }

    public void act() {
        if (!answered) {
            checkAnswer();
        } else if (showingTheory) {
            if (Greenfoot.mouseClicked(this) || Greenfoot.isKeyDown("space")) {
                ((MyWorld)getWorld()).resumeGame();
                getWorld().removeObject(this);
            }
        }
    }

    private void checkAnswer() {
        for (int i = 0; i < answers.length; i++) {
            if (Greenfoot.isKeyDown("" + (char)('A' + i))) {
                answered = true;
                boolean isCorrect = (i == correctAnswer);
                provideFeedback(isCorrect);
                break;
            }
        }
    }

    private void provideFeedback(boolean isCorrect) {
        MyWorld world = (MyWorld)getWorld();
        String feedback = isCorrect ? "Correct!!" : "Inorrect!";
        Color color = isCorrect ? Color.GREEN : Color.RED;

        GreenfootImage feedbackImage = new GreenfootImage(600, 400);
        feedbackImage.setColor(Color.WHITE);
        feedbackImage.fillRect(50, 50, 500, 300);
        feedbackImage.setColor(Color.BLACK);
        feedbackImage.drawRect(50, 50, 500, 300);
        feedbackImage.setColor(color);
        feedbackImage.drawString(feedback, 250, 200);

        setImage(feedbackImage);
        Greenfoot.delay(50); 

        if (!isCorrect) {
            // Update the incorrect answers count and handle quiz logic
            world.quizCompleted(false, world.getCurrentQuizID());
            showTheory();
        } else {
            // Call quizCompleted to update the scores and handle the quiz logic
            world.quizCompleted(isCorrect, world.getCurrentQuizID());
            getWorld().removeObject(this);
        }
    }

    private void showTheory() {
        showingTheory = true;

        GreenfootImage theoryImage = new GreenfootImage(600, 400);
        theoryImage.setColor(Color.WHITE);
        theoryImage.fillRect(50, 50, 500, 300);
        theoryImage.setColor(Color.BLACK);
        theoryImage.drawRect(50, 50, 500, 300);

        // Draw the theory text
        drawWrappedString(theoryImage, "Incorrect!", 70, 80, 460);
        drawWrappedString(theoryImage, theory, 70, 120, 460);

        // Draw the OK button
        theoryImage.drawString("OK (press space)", 70, 320);

        setImage(theoryImage);
    }

    private void drawWrappedString(GreenfootImage image, String text, int x, int y, int width) {
        FontMetrics fm = image.getAwtImage().getGraphics().getFontMetrics();
        int lineHeight = fm.getHeight();
        
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y;

        for (String word : words) {
            if (fm.stringWidth(line + word) < width) {
                line.append(word).append(" ");
            } else {
                image.drawString(line.toString(), x, lineY);
                line = new StringBuilder(word + " ");
                lineY += lineHeight;
            }
        }
        image.drawString(line.toString(), x, lineY);
    }
}
