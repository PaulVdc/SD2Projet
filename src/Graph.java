import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Graph {

    private static Map<Integer, Artist> artistsIds;// retient tout les artistes
    private static Map<String, Artist> artists;// retient tout les artistes avec le nom comme clé// retient tout les artistes par leur nom
    private static Map<Integer, Mention> mentions;//TODO à retirer ??

    private Map<Artist, Set<Artist>> artistsMentionnes;//Liste d'adjacence

    public Graph(String artists, String mentions) {
        this.artists = new HashMap<>();
        this.artistsIds = new HashMap<>();
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
                    this.artistsIds.put(artiste.getId_artist(), artiste);
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
                    Mention mention = new Mention(this.artistsIds.get(id_mentionneur), this.artistsIds.get(id_mentionne), nb_mention);
                    System.out.println(mention);
                    this.mentions.put(mention.getArtiste_mentionne().getId_artist(),mention);

                   /* System.out.println(mention);*/ //Pour verifier si l'ajout s'est bien passé
                    //TODO ajouter la mention à this.mentions

                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }


    }

    //methodes
    public void trouverCheminLePlusCourt(String artiste1, String artiste2) {
        Artist start = this.artists.get(artiste1);
        Artist end = this.artists.get(artiste2);
        Queue<Artist> fileSommets = new LinkedList<>();
        Set<Artist> visited = new HashSet<>();
        Artist artisteCourant = start;


        fileSommets.add(start);
        visited.add(start);
        /*if(mentions.containsKey(start.getId_artist())){
            fileSommets.add(mentions.get(start.getId_artist()).getArtiste_mentionne());
            System.out.println(fileSommets.toString());

        }*/
        while(!artisteCourant.equals(end)) {
            // je oprend  le premier de la fileet je l'enleve et il devient mon courant

            // Créer une 3ème structure pour retenir d'ou les sommets viennent ( Faire une map <Artiste_Mentionné,Mentions> )

            for (Artist artist : artistsMentionnes.get(artisteCourant)) {
                if(!visited.contains(artisteCourant)){
                    visited.add(artist);
                    fileSommets.add(artist);
                }
            }
            artisteCourant = fileSommets.remove();
        }

    }

    public void trouverCheminMaxMentions(String artiste1, String artiste2){


    }
}
