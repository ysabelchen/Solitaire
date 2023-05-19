import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * The SolitaireDisplay class creates the display of the Solitaire game, including
 * borders, cards, and the green background
 * @author Ysabel Chen
 */
public class SolitaireDisplay extends JComponent implements MouseListener
{
    private static final int CARD_WIDTH = 73;
    private static final int CARD_HEIGHT = 97;
    private static final int SPACING = 5;  //distance between cards
    private static final int FACE_UP_OFFSET = 15;  //distance for cascading face-up cards
    private static final int FACE_DOWN_OFFSET = 5;  //distance for cascading face-down cards

    private JFrame frame;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Solitaire game;

    /**
     * Constructs a SolitaireDisplay object, and creates the frame
     * @param game the Solitaire game
     */
    public SolitaireDisplay(Solitaire game)
    {
        this.game = game;

        frame = new JFrame("Solitaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);

        this.setPreferredSize(new Dimension(CARD_WIDTH * 7 + SPACING * 8,
                                CARD_HEIGHT * 2 + SPACING * 3 + FACE_DOWN_OFFSET * 7 +
                                13 * FACE_UP_OFFSET));
        this.addMouseListener(this);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Creates the card designs and the borders
     * @param g the graphics
     */
    public void paintComponent(Graphics g)
    {
        //background
        g.setColor(new Color(0, 128, 0));
        g.fillRect(0, 0, getWidth(), getHeight());

        //face down
        drawCard(g, game.getStockCard(), SPACING, SPACING);

        //stock
        drawCard(g, game.getWasteCard(), SPACING * 2 + CARD_WIDTH, SPACING);
        if (selectedRow == 0 && selectedCol == 1)
            drawBorder(g, SPACING * 2 + CARD_WIDTH, SPACING);

        //aces
        for (int i = 0; i < 4; i++)
            drawCard(g, game.getFoundationCard(i),
                    SPACING * (4 + i) + CARD_WIDTH * (3 + i), SPACING);

        //foundations
        if (selectedRow == 0)
        {
            if(selectedCol > 2 && selectedCol < 7)
                drawBorder(g,SPACING + (CARD_WIDTH + SPACING) * selectedCol, SPACING);
        }

        //piles
        for (int i = 0; i < 7; i++)
        {
            Stack<Card> pile = game.getPile(i);
            int offset = 0;
            for (int j = 0; j < pile.size(); j++)
            {
                drawCard(g, pile.get(j),
                        SPACING + (CARD_WIDTH + SPACING) * i, CARD_HEIGHT + 2 * SPACING + offset);
                if (selectedRow == 1 && selectedCol == i && j == pile.size() - 1)
                    drawBorder(g, SPACING + (CARD_WIDTH + SPACING) * i, CARD_HEIGHT +
                                2 * SPACING + offset);

                if (pile.get(j).isFaceUp())
                    offset += FACE_UP_OFFSET;
                else
                    offset += FACE_DOWN_OFFSET;
            }
        }
    }

    /**
     * Create the card designs by getting the respective file names
     * @param g the graphics
     * @param card the card
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    private void drawCard(Graphics g, Card card, int x, int y)
    {
        if (card == null)
        {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        }
        else
        {
            String fileName = card.getFileName();
            if (!new File(fileName).exists())
                throw new IllegalArgumentException("bad file name:  " + fileName);
            Image image = new ImageIcon(fileName).getImage();
            g.drawImage(image, x, y, CARD_WIDTH, CARD_HEIGHT, null);
        }
    }

    /**
     * Checks whether the mouse has exited
     * @param e MouseEvent
     */
    public void mouseExited(MouseEvent e)
    {
    }

    /**
     * Checks whether the mouse has enteredted
     * @param e MouseEvent
     */
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * Checks whether the mouse has been released
     * @param e MouseEvent
     */
    public void mouseReleased(MouseEvent e)
    {
    }

    /**
     * Checks whether the mouse has been pressed
     * @param e MouseEvent
     */
    public void mousePressed(MouseEvent e)
    {
    }

    /**
     * Checks what card was clicked
     * @param e the MouseEvent
     */
    public void mouseClicked(MouseEvent e)
    {
        //none selected previously
        int col = e.getX() / (SPACING + CARD_WIDTH);
        int row = e.getY() / (SPACING + CARD_HEIGHT);
        if (row > 1)
            row = 1;
        if (col > 6)
            col = 6;

        if (row == 0 && col == 0)
            game.stockClicked();
        else if (row == 0 && col == 1)
            game.wasteClicked();
        else if (row == 0 && col >= 3)
            game.foundationClicked(col - 3);
        else if (row == 1)
            game.pileClicked(col);
        repaint();
    }

    /**
     * Creates the yellow border around the clicked card
     * @param g the graphics
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    private void drawBorder(Graphics g, int x, int y)
    {
        g.setColor(Color.YELLOW);
        g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        g.drawRect(x + 1, y + 1, CARD_WIDTH - 2, CARD_HEIGHT - 2);
        g.drawRect(x + 2, y + 2, CARD_WIDTH - 4, CARD_HEIGHT - 4);
    }

    /**
     * Deselects what is selected
     */
    public void unselect()
    {
        selectedRow = -1;
        selectedCol = -1;
    }

    /**
     * Checks whether the waste is selected
     * @return true is the waste is selected; otherwise,
     *         false
     */
    public boolean isWasteSelected()
    {
        return selectedRow == 0 && selectedCol == 1;
    }

    /**
     * Changes the selected row and column to the waste
     */
    public void selectWaste()
    {
        selectedRow = 0;
        selectedCol = 1;
    }

    /**
     * Checks whether a pile is selected
     * @return true is a pile is selected; otherwise,
     *         false
     */
    public boolean isPileSelected()
    {
        return selectedRow == 1;
    }

    /**
     * Gets the index of the selected pile
     * @return the index of the selected pile
     */
    public int selectedPile()
    {
        if (selectedRow == 1)
            return selectedCol;
        else
            return -1;
    }

    /**
     * Changes the selected row and column to the pile
     * @param index the index of the pile
     */
    public void selectPile(int index)
    {
        selectedRow = 1;
        selectedCol = index;
    }

    /**
     * Checks whether a pile is selected
     * @return true is a pile is selected; otherwise,
     *         false
     */
    public boolean isFoundationSelected()
    {
        if (selectedRow == 0)
            if (selectedCol > 2 && selectedCol < 7)
                return true;
        return false;
    }

    /**
     * Gets the index of the selected foundation
     * @return the index of the selected foundation
     */
    public int selectedFoundation()
    {
        if (isFoundationSelected())
            return selectedCol - 3;
        else
            return -1;
    }

    /**
     * Changes the selected row and column to the foundation
     * @param index the index of the foundation
     */
    public void selectFoundation(int index)
    {
        selectedRow = 0;
        selectedCol = index + 3;
    }
}
