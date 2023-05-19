import java.util.*;

/**
 * The Solitaire class mimics the classic Klondike Solitaire card game
 * @author Ysabel Chen
 */
public class Solitaire
{
    /**
     * Creates a new game of Solitaire
     * @param args the main entry point
     */
    public static void main(String[] args)
    {
        new Solitaire();
    }

    private Stack<Card> stock;
    private Stack<Card> waste;
    private Stack<Card>[] foundations;
    private Stack<Card>[] piles;
    private SolitaireDisplay display;

    /**
     * Constructs a Solitaire object, and initializes the stock, waste, foundations,
     * and piles to be empty Stacks of Cards
     */
    public Solitaire()
    {
        foundations = new Stack[4];
        for (int i = 0; i < 4; i++)
            foundations[i] = new Stack<Card>();
        piles = new Stack[7];
        for (int i = 0; i < 7; i++)
            piles[i] = new Stack<Card>();
        stock = new Stack<Card>();
        waste = new Stack<Card>();
        display = new SolitaireDisplay(this);
        this.createStock();
        this.deal();
    }

    /**
     * Returns the card on top of the stock or null if the stock is empty
     * @return the top Card or null
     */
    public Card getStockCard()
    {
        if (!stock.isEmpty())
            return stock.peek();
        else
            return null;
    }

    /**
     * Returns the card on top of the waste or null if the waste is empty
     * @return the top Card or null
     */
    public Card getWasteCard()
    {
        if (!waste.isEmpty())
            return waste.peek();
        else
            return null;
    }

    /**
     * Gets the top card of the given foundation
     * @precondition 0 <= index < 4
     * @param index the index of the foundation
     * @return the card on top of the given foundation, or null if the foundation is empty
     */
    public Card getFoundationCard(int index)
    {
        Stack<Card> f = foundations[index];
        if (!f.isEmpty())
            return f.peek();
        else
            return null;
    }

    /**
     * @precondition  0 <= index < 7
     * @param index the index of the pile
     * @return the reference to the pile
     */
    public Stack<Card> getPile(int index)
    {
        return piles[index];
    }

    /**
     * Creates an ArrayList with each card in a deck, and then randomly adds them to the stock
     * @postcondition the stock is a stack of shuffled cards
     */
    public void createStock()
    {
        List<Card> deck = new ArrayList<Card>();
        for (int rank = 1; rank < 14; rank++)
        {
            deck.add(new Card(rank,"c"));
            deck.add(new Card(rank,"d"));
            deck.add(new Card(rank,"h"));
            deck.add(new Card(rank,"s"));
        }
        while(deck.size() != 0)
        {
            int r = (int)(Math.random() * deck.size());
            stock.push(deck.remove(r));
        }
    }

    /**
     * Deals cards from the stock to the 7 piles. Turns the top card of each pile face up.
     * @postcondition populates the 7 piles
     */
    public void deal()
    {
        for(int i = 0; i < 7; i++)
        {
            int num = 0;
            while (num <= i)
            {
                piles[i].push(stock.pop());
                num++;
            }
        }
        for(int p = 0; p <= 6; p++)
        {
            piles[p].peek().turnUp();
        }
    }

    /**
     * Moves the top three cards from the stock onto the waste and turn them up. If there
     * are fewer than three cards left, move whatever is left
     * @postcondition 3 cards are moved to the waste stack and turned up
     */
    private void dealThreeCards()
    {
        int i = 0;
        while(i < 3)
        {
            if (!stock.isEmpty())
            {
                Card added = stock.pop();
                waste.push(added);
                added.turnUp();
            }
            i++;
        }
    }

    /**
     * Repeatedly moves the top card from the waste to the stock and turns them down
     */
    private void resetStock()
    {
        while(!waste.isEmpty())
        {
            Card added = waste.pop();
            stock.push(added);
            added.turnDown();
        }
    }

    /**
     * Called when the stock is clicked
     */
    public void stockClicked()
    {
        if(!display.isWasteSelected() && !display.isPileSelected() &&
        !display.isFoundationSelected())
        {
            if(!stock.isEmpty())
                this.dealThreeCards();
            else
                this.resetStock();
        }
    }

    /**
     * Called when the waste is clicked
     */
    public void wasteClicked()
    {
        if(!waste.isEmpty() && !display.isWasteSelected() &&
        !display.isPileSelected() && !display.isFoundationSelected())
            display.selectWaste();
        else if (display.isWasteSelected())
            display.unselect();
    }

    /**
     * Called when given pile is clicked
     * @precondition 0 <= index < 7
     * @param index the index of the pile
     */
    public void pileClicked(int index)
    {
        if(!display.isWasteSelected() && !display.isPileSelected() &&
        !display.isFoundationSelected() && !piles[index].isEmpty())
        {
            if (piles[index].peek().isFaceUp())
                display.selectPile(index);
            else if (!piles[index].peek().isFaceUp())
                piles[index].peek().turnUp();
        }
        else if(display.isPileSelected() && display.selectedPile() == index)
            display.unselect();
        else if(display.isWasteSelected() && canAddToPile(waste.peek(), index))
        {
            piles[index].push(waste.pop());
            display.unselect();
        }
        else if(display.isPileSelected() && display.selectedPile() != index)
        {
            Stack<Card> removed = removeFaceUpCards(display.selectedPile());
            if (canAddToPile(removed.peek(), index))
            {
                addToPile(removed, index);
                display.unselect();
            }
            else
                addToPile(removed, display.selectedPile());
        }
        else if(display.selectedFoundation() >= 0 &&
        canAddToPile(foundations[display.selectedFoundation()].peek(), index))
        {
            piles[index].push(foundations[display.selectedFoundation()].pop());
            display.unselect();
        }
    }

    /**
     * Checks a card's color and rank to see whether it can be added to the pile
     * @precondition 0 <= index < 7
     * @return true if the given card can be legally moved to the top of the pile
     */
    private boolean canAddToPile(Card card, int index)
    {
        if(piles[index].isEmpty() && card.getRank() == 13)
            return true;
        else if(!piles[index].isEmpty() && piles[index].peek().isFaceUp() &&
        card.isRed() != piles[index].peek().isRed() &&
        card.getRank() == piles[index].peek().getRank() - 1)
            return true;
        return false;
    }

    /**
     * Removes and returns the face-up cards on the top of the pile
     * @precondition 0 <= index < 7
     * @return a stack containing these cards
     */
    private Stack<Card> removeFaceUpCards(int index)
    {
        Stack<Card> faceUp = new Stack<Card>();
        Stack<Card> pile = piles[index];
        while(!pile.isEmpty() && pile.peek().isFaceUp())
        {
            faceUp.push(pile.pop());
        }
        return faceUp;
    }

    /**
     * Adds cards from a stack to a pile
     * @precondition 0 <= index < 7
     * @postcondition removes elements from cards, and adds them to the given pile
     */
    private void addToPile(Stack<Card> cards, int index)
    {
        Stack<Card> pile = piles[index];
        while(!cards.isEmpty())
            pile.push(cards.pop());
    }

    /**
     * Called when given foundation is clicked
     * @precondition 0 <= index < 4
     * @param index the index of the foundation
     */
    public void foundationClicked(int index)
    {
        if(display.isFoundationSelected() && display.selectedFoundation() == index)
            display.unselect();
        else if(display.isWasteSelected() && !waste.isEmpty() &&
        canAddToFoundation(waste.peek(), index))
        {
            foundations[index].push(waste.pop());
            display.unselect();
            if (checkForWin())
                System.out.println("Congratulations! You win!");
        }
        else if(display.isPileSelected() && !piles[display.selectedPile()].isEmpty() &&
        canAddToFoundation(piles[display.selectedPile()].peek(), index))
        {
            foundations[index].push(piles[display.selectedPile()].pop());
            display.unselect();
            if (checkForWin())
                System.out.println("Congratulations! You win!");
        }
        else if(!foundations[index].isEmpty() &&
        !display.isWasteSelected() && !display.isPileSelected())
        {
            display.selectFoundation(index);
        }
    }

    /**
     * Checks to see whether the card can be legally moved to the top of the given foundation
     * @precondition 0 <= index < 4
     * @return true if the given card can be legally moved to the top of the
     * given foundation
     */
    private boolean canAddToFoundation(Card card, int index)
    {
        Stack<Card> foundation = foundations[index];
        if(foundation.isEmpty() && card.getRank() == 1)
            return true;
        else if (!foundation.isEmpty() && foundation.peek().isRed() == card.isRed() &&
        card.getRank() == foundation.peek().getRank() + 1)
            return true;
        return false;
    }

    /**
     * Checks whether the top card of all foundations is a king
     * @return true if all top cards are kings; otherwise,
     *         false
     */
    private boolean checkForWin()
    {
        for (int i = 0; i < 4; i++)
        {
            if (foundations[i].size() == 0)
                return false;
            else if (foundations[i].peek().getRank() != 13)
                return false;
        }
        return true;
    }
}
