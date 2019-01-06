import javax.swing.*;
  
public class SensorNetwork
{
    public static void main(String[] args)
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new PointPanel());
        f.setSize(1000,600);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}