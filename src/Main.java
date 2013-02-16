import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import view.Canvas;


/**
 * Creates window that can be moved, resized, and closed by the user.
 *
 * @author Robert C. Duvall
 */
public class Main
{
    // constants
    public static final Dimension SIZE = new Dimension(1000, 768);
    public static final String TITLE = "Space Invaders!";


    /**
     * main --- where the program starts
     * @param args
     */
    public static void main (String args[])
    {
        // view of user's content
        Canvas display = new Canvas(SIZE);
        // container that will work with user's OS
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // add our user interface components
        frame.getContentPane().add(display, BorderLayout.CENTER);
        // display them
        frame.pack();
        frame.setVisible(true);
        // start animation
        display.start();
        // TODO: handle iconify (reason for start/stop)
        // TODO: full screen?
    }
}
