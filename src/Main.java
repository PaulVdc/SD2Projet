public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph("artists.txt", "mentions.txt");
        graph.trouverCheminLePlusCourt("Juliette Armanet", "The Beatles");
		System.out.println("--------------------------");

        graph.trouverCheminMaxMentions("Juliette Armanet", "The Beatles");
    }
}
