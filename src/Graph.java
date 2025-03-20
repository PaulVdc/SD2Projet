import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    /*private static Map<Integer, Artist> artists;*/// retient tout les artistes
    private static Map<String, Artist> artists;// retient tout les artistes avec le nom comme clé// retient tout les artistes par leur nom
    private static Map<Integer, Mention> mentions;//TODO à retirer ??

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
                    /*this.artists.put(artiste.getId_artist(), artiste);*/
                    this.artists.put(artiste.getNom_artist(), artiste);
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
                    int id_mentionneur = Integer.parseInt(elements[0].trim()); // Convertir l’ID en int
                    int id_mentionne = Integer.parseInt(elements[1].trim());
                    int nb_mention = Integer.parseInt(elements[2].trim());


                    // Création de l'objet Mention
                    Mention mention = new Mention(this.artists.get(id_mentionneur), this.artists.get(id_mentionne), nb_mention);

                   /* System.out.println(mention);*/ //Pour verifier si l'ajout s'est bien passé
                    //TODO ajouter la mention à this.mentions

                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }


    }

    //methodes
    public Object trouverCheminLePlusCourt(String artiste1, String artiste2){
        Artist start = this.artists.get(artiste1);
        Artist end = this.artists.get(artiste2);
        System.out.println(start.toString() + end.toString());
        return null;
    }

    public Object trouverCheminMaxMentions(String artiste1, String artiste2){
        return null;
    }
}
