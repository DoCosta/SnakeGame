package SnakeGame;
import java.awt.*;

import javax.lang.model.type.UnionType;
import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener{
    
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; // size of each square
    static final int GAME_UNITS =  (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 70;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 5;
    int applesEaten;
    int appleX;
    int appleY;
    boolean autocomplete = false;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    /** Constructs a new GamePanel */
    
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.gray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        x[0] = 0 + UNIT_SIZE;
        y[0] = 0 + UNIT_SIZE;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running){
            for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
                g.setColor(Color.darkGray);
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);

            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            for(int i = 0; i<bodyParts; i++){
                if( i==0 ){
                    g.setColor(Color.black);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }else{
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

                Font font = new Font("Arial",Font.BOLD,20);
                g.setColor(Color.red);
                g.setFont(font);
                g.drawString("Apples:" + applesEaten, (SCREEN_WIDTH) - 90 ,  18) ;
            }
        }else{
            GameOver(g);
        }
    }

    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    
    }

    public void move(){
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (direction == 'U') {
            y[0] -= UNIT_SIZE;
        } if (direction == 'L') {
            x[0] -= UNIT_SIZE;  
        } if (direction == 'D') {
            y[0] += UNIT_SIZE;
        }  if (direction == 'R') {
            x[0] += UNIT_SIZE;
        }

    }

    public void checkapple(){
        if((x[0] == appleX) && ( y[0] == appleY)){
            applesEaten++;
            bodyParts++;
            newApple();
        }
    }
    public void autocompletion(){
        int prevX = x[0];
        int prevY = y[0];
        for(int K = 1100; K >= SCREEN_WIDTH*SCREEN_HEIGHT; K--){
             if(x[0] == SCREEN_WIDTH - K && direction != 'D'){
                prevY = y[0];
                direction = 'D';
            }if((y[0] == SCREEN_HEIGHT - K ) || (direction == 'D' && (y[0] != prevY)) && direction != 'L'){
                direction = 'L';    
            }if(x[0] == 0 + K && direction != 'U'){
                direction = 'U';
            }if(y[0] == 0 + K && direction != 'R'){
                direction = 'R';
            } 
            
        } 
            
        
    }

    public void checkCollisions(){
        for(int i = bodyParts; i > 0; i--){
            // If head collides with any part of the body then game over
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // Checks if snake is out of bounds, also game over in this case
        if (x[0] <  0 || y[0] < 0) {
            running = false;
        }else if (x[0] >= SCREEN_WIDTH || y[0] >= SCREEN_HEIGHT ) {
            running = false;
        }
        if(!running){
            timer.stop();
        }        
    }

    public void GameOver(Graphics g){
        Font font = new Font("Arial",Font.BOLD,50);
        g.setColor(Color.red);
        g.setFont(font);
        String message ="Game Over!";
        int boxWidth = (int)(g.getFontMetrics().getStringBounds(message,g).getWidth() + 20);
        int boxHeight= g.getFontMetrics().getHeight()+5 ;
        int boxLocation = (SCREEN_HEIGHT - boxHeight)/2;
        g.drawRect(SCREEN_WIDTH/2-boxWidth/2,boxLocation ,boxWidth ,boxHeight );
        g.drawString(message, (SCREEN_WIDTH - boxWidth)/2 + 10 , (SCREEN_HEIGHT + boxHeight)/2 - 15);

        Font applefont = new Font("Arial",Font.BOLD,28);
        g.setColor(Color.red);
        g.setFont(applefont);
        g.drawString("Apples:" + applesEaten, SCREEN_WIDTH /2 - 50 ,  SCREEN_HEIGHT/2 + 65) ;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkapple();
            if(autocomplete){
                autocompletion();
            }
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    if(direction!='D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                    if(direction!='U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_A:
                    if(direction!='R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_D:
                    if(direction!='L'){
                       direction = 'R';
                    }
                    break;
                case KeyEvent.VK_F:
                    if (autocomplete == false){
                        autocomplete = true;
                    }else{
                        autocomplete = false;
                    }
                    break;
            }
        }
        
    }
    
}
