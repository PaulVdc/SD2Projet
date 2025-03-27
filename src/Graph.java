import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

public class Graph {

    private static Map<Integer, Artist> artistsIds;// retient tout les artistes
    private static Map<String, Artist> artists;// retient tout les artistes avec le nom comme clé// retient tout les artistes par leur nom
    private static Map<Integer, Mention> mentions;//TODO à retirer ??
    private static Map<Artist, Set<Artist>> artistsMentionnes;//Liste d'adjacence


    public Graph(String artistsFile, String mentionsFile) {
        this.artists = new HashMap<>();
        this.artistsIds = new HashMap<>();
        this.mentions = new HashMap<>();
        this.artistsMentionnes = new HashMap<>();


        try(BufferedReader br = new BufferedReader(new FileReader(artistsFile))){
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
                    this.artistsMentionnes.put(artiste,new HashSet<Artist>()) ;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        try(BufferedReader br = new BufferedReader(new FileReader(mentionsFile))){
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] elements = ligne.split(","); // Séparer les champs par ","
                if (elements.length == 3) { // Vérification du bon format
                    int id_mentionneur = Integer.parseInt(elements[0].trim()); // Convertir l’ID en int
                    int id_mentionne = Integer.parseInt(elements[1].trim());
                    int nb_mention = Integer.parseInt(elements[2].trim());

                    Artist mentionneur = this.artistsIds.get(id_mentionneur);
                    Artist mentionnee = this.artistsIds.get(id_mentionne);

                    // Création de l'objet Mention
                    Mention mention = new Mention(mentionneur, mentionnee, nb_mention);
                    //System.out.println(mention);
                    this.mentions.put(mentionneur.getId_artist(),mention);
                    this.artistsMentionnes.putIfAbsent(mentionneur, new HashSet<>());
                    this.artistsMentionnes.get(mentionneur).add(mentionnee);
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
        Artist artisteCourant = start;
        System.out.println("start = "+start);
        Queue<Artist> fileSommetsPasEncoreAtteints = new LinkedList<>();
        Set<Artist> visited = new HashSet<>();

        // Créer une 3ème structure pour retenir d'ou les sommets viennent ( Faire une map <Artiste_Mentionné,Mentions> )
        Map<Artist, Artist> predecesseurs = new HashMap<>(); //Artist2 == prédécesseur


        fileSommetsPasEncoreAtteints.add(start);
        visited.add(start);
        predecesseurs.put(start, null); //le premier artiste n'a pas de prédécésseur


        while(!artisteCourant.equals(end)) {
            // je oprend  le premier de la fileet je l'enleve et il devient mon courant
            artisteCourant = fileSommetsPasEncoreAtteints.poll();
            System.out.println("artisteCourant : "+artisteCourant);
            // Créer une 3ème structure pour retenir d'ou les sommets viennent ( Faire une map <Artiste_Mentionné,Mentions> )
            // Mégane : Map<Artist, Artist> predecesseurs

            if (artisteCourant.equals(end)){
                reconstruirChemin(predecesseurs, end);
            }

            for (Artist artist : artistsMentionnes.get(artisteCourant)) {
                if(!visited.contains(artist)){
                    visited.add(artist); //Ajout des artistes mentionnés par le courant dans le Set des visited
                    fileSommetsPasEncoreAtteints.add(artist); //Ajout des artistes mentionnés par le courant dans la file des sommets
                    predecesseurs.put(artist, artisteCourant);
                }
            }
        }
    }

    private void reconstruirChemin(Map<Artist, Artist> predecesseurs, Artist end){
        List<Artist> chemin = new ArrayList<>();
        Artist artisteCourant = end;

        while (artisteCourant != null){
            chemin.add(artisteCourant);
            artisteCourant = predecesseurs.get(artisteCourant); // pour remonter dans les prédécesseurs et faire le chemin à l'envers
        }

        Collections.reverse(chemin); //pour inverser l'ordre du chemin

        int logueurDuChemin = chemin.size()-1;
        System.out.println("Longueur du chemin : " + logueurDuChemin);
        System.out.println("Coût total du chemin : ");
        System.out.println("Chemain :");

        for (Artist artist : chemin) {
            System.out.println(artist.getNom_artist() + " (" + artist.getCategorie() + ")");
        }
    }

    public void trouverCheminMaxMentions(String artiste1, String artiste2){


    }
}
