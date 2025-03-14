import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    private static Map<Integer, Artist> artists;
    private static Map<Integer, List<Artist>> mentions;

    private Map<Artist, Map<Integer, Artist>> artistsMentionnes;//Liste d'adjacence

    public Graph(String artists, String mentions) {
        this.artists = new HashMap<>();
        this.mentions = new HashMap<>();
        this.artistsMentionnes = new HashMap<>();


        try(BufferedReader br = new BufferedReader(new FileReader(artists))){
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] elements = ligne.split(","); // Séparer les champs par ","
                if (elements.length == 3) { // Vérification du bon format
                    int id = Integer.parseInt(elements[0].trim()); // Convertir l’ID en int
                    String nom = elements[1].trim();
                    String categorie = elements[2].trim();

                    // Création de l'objet Artist
                    Artist artiste = new Artist(id, nom, categorie);

                    //System.out.println(artiste); //Pour verifier si l'ajout s'est bien passé
                    this.artists.put(artiste.getId_artist(), artiste);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        try(BufferedReader br = new BufferedReader(new FileReader(mentions))){
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] elements = ligne.split(","); // Séparer les champs par ","
                if (elements.length == 3) { // Vérification du bon format
                    int id = Integer.parseInt(elements[0].trim()); // Convertir l’ID en int
                    String nom = elements[1].trim();
                    String categorie = elements[2].trim();

                    // Création de l'objet Artist
                    Artist artiste = new Artist(id, nom, categorie);

                    //System.out.println(artiste); //Pour verifier si l'ajout s'est bien passé
                    this.artists.put(artiste.getId_artist(), artiste);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }


    }

    //methodes
    public Object trouverCheminLePlusCourt(String artiste1, String artiste2){
        return null;
    }

    public Object trouverCheminMaxMentions(String artiste1, String artiste2){
        return null;
    }
}
