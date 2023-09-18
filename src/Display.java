import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

class DrawPanel extends JPanel {
    Ball[] Balls;
    int SIZE_UNIT;
    public DrawPanel (int SIZE_UNIT) {
        super();
        this.SIZE_UNIT = SIZE_UNIT;
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    void setBalls(Ball[] Balls){
        this.Balls = Balls;
    }
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        for (int i = 0; i < Game.HOLES.length; i++){
            g2.setColor(Color.DARK_GRAY);
            Ellipse2D.Double shape = new Ellipse2D.Double((Game.HOLES[i][0] - Constant.HOLE_SIZE) * SIZE_UNIT ,
                    (Game.HOLES[i][1] - Constant.HOLE_SIZE) * SIZE_UNIT,
                    Constant.HOLE_SIZE * SIZE_UNIT * 2,
                    Constant.HOLE_SIZE * SIZE_UNIT * 2);
            g2.fill(shape);
        }
        for (int i = 0; i < Balls.length; i++){
            if (Balls[i].isValid == false) continue;
            g2.setColor(Balls[i].color);
            Ellipse2D.Double shape = new Ellipse2D.Double((Balls[i].x - Ball.DIAMETER/2) * SIZE_UNIT ,
                    (Balls[i].y - Ball.DIAMETER/2) * SIZE_UNIT,
                    Ball.DIAMETER * SIZE_UNIT,
                    Ball.DIAMETER * SIZE_UNIT);
            g2.fill(shape);
        }
    }
}
public class Display {
    private JFrame frame;

    private DrawPanel panel;
    private JPanel tableFrame;
    private String title;
    private int width, height, SIZE_UNIT;

    public Display(String title, int width, int height, int SIZE_UNIT) {
        this.title = title;
        this.width = width * SIZE_UNIT;;
        this.height = height * SIZE_UNIT;
        this.SIZE_UNIT = SIZE_UNIT;
        createDisplay();
    }

    public void setBalls(Ball[] Balls){
        panel.setBalls(Balls);
    }
    private void createDisplay(){
        frame = new JFrame(title);
        frame.setSize(width + 100, height + 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        panel = new DrawPanel(SIZE_UNIT);
        //panel.setSize(new Dimension(width, height));
        panel.setBackground(new Color(0x1D6132));
        panel.setBounds(50,50, width, height);
        tableFrame = new JPanel();
        tableFrame.setBackground(new Color(0x654321));

        frame.add(panel);
        frame.add(tableFrame);
        frame.setVisible(true);
    }

    public void draw(){
        panel.repaint();
    }
}

