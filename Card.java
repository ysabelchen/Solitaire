/**
 * The Card class creates Card objects that each represent a single playing
 * card with three atributes: rank, suit, isFaceUp
 * @author Ysabel Chen
 */
public class Card
{
    private int rank;
    private String suit;
    private boolean isFaceUp;

    /**
     * Creates a Card object with a specific rank and a suit
     * @param r the rank
     * @param s the suit
     */
    public Card(int r, String s)
    {
        rank = r;
        suit = s;
        isFaceUp = false;
    }

    /**
     * Gets the rank of the card
     * @return the rank of the card
     */
    public int getRank()
    {
        return rank;
    }

    /**
     * Gets the suit of the card
     * @return the suit of the card
     */
    public String getSuit()
    {
        return suit;
    }

    /**
     * Checks whether the card is a red card (a heart or diamond)
     * @return true if the card is red; otherwise,
     *         false
     */
    public boolean isRed()
    {
        if (suit.equals("d") || suit.equals("h"))
            return true;
        return false;
    }

    /**
     * Checks whether the card is face up
     * @return true if the card is face up; otherwise,
     *         false
     */
    public boolean isFaceUp()
    {
        return isFaceUp;
    }

    /**
     * Turn the card face up
     */
    public void turnUp()
    {
        isFaceUp = true;
    }

    /**
     * Turn the card face down
     */
    public void turnDown()
    {
        isFaceUp = false;
    }

    /**
     * Gets the corresponding file name for the card
     * @return the file name of the card
     */
    public String getFileName()
    {
        if (isFaceUp)
        {
            if (rank > 1 && rank <= 9)
                return "/Users/22ysabelc/Downloads/Solitaire/cards/" +
                rank + suit + ".gif";
            else if (rank == 1)
                return "/Users/22ysabelc/Downloads/Solitaire/cards/a" +
                suit + ".gif";
            else if (rank == 10)
                return "/Users/22ysabelc/Downloads/Solitaire/cards/t" +
                suit + ".gif";
            else if (rank == 11)
                return "/Users/22ysabelc/Downloads/Solitaire/cards/j" +
                suit + ".gif";
            else if (rank == 12)
                return "/Users/22ysabelc/Downloads/Solitaire/cards/  " +
                suit + ".gif";
            else
                return "/Users/22ysabelc/Downloads/Solitaire/cards/k" +
                suit + ".gif";
        }
        else
            return "/Users/22ysabelc/Downloads/Solitaire/cards/backhds.gif";
    }
}
