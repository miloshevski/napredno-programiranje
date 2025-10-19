package cards;

public class MultipleDeck {
    private Deck[] decks;

    public MultipleDeck(int numberOfDecks){
        this.decks = new Deck[numberOfDecks];
        for(int i = 0;i < numberOfDecks; i++){
            decks[i] = new Deck();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Deck deck : decks){
            stringBuilder.append(deck.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    static void main() {
        MultipleDeck multipleDecks = new MultipleDeck(3);
        System.out.println(multipleDecks);
    }
}
