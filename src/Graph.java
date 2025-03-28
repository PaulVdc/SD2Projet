import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    private  Map<Integer, Artist> artistsIds;// retient tout les artistes avec leur id comme clé
    private  Map<String, Artist> artists;// retient tout les artistes avec le nom comme clé
    private  Map<Artist, Set<Mention>> mentionsDunArtiste;//Liste d'adjacence


    public Graph(String artistsFile, String mentionsFile) {
        this.artists = new HashMap<>();
        this.artistsIds = new HashMap<>();
        this.mentionsDunArtiste = new HashMap<>();

        //Lecture des artistes dans artistsFile
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

                    this.artistsIds.put(artiste.getId_artist(), artiste);
                    this.artists.put(artiste.getNom_artist(), artiste);
                    this.mentionsDunArtiste.put(artiste, new HashSet<>());
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        //Lecture des mentions dans mentionsFile
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

                    //ajout de la mention au Set de mentions de l'artiste mentionneur dans mentionsDunArtiste
                    this.mentionsDunArtiste.get(mentionneur).add(mention);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }//End of constructor

    public void trouverCheminLePlusCourt(String artiste1, String artiste2) {
        Artist start = this.artists.get(artiste1);
        Artist end = this.artists.get(artiste2);

        Artist artisteCourant = start;

        Queue<Artist> fileSommetsPasEncoreAtteints = new LinkedList<>();
        Set<Artist> visited = new HashSet<>();

        //retient d'où viennet les sommets
        Map<Artist, Mention> provenences = new HashMap<>(); //artiste mentionné, mention

        //ajout de start dans les 3 SD
        fileSommetsPasEncoreAtteints.add(start);
        visited.add(start);
        provenences.put(start, null);

        //algo BFS
        while(!artisteCourant.equals(end)) {

            if (fileSommetsPasEncoreAtteints.isEmpty()){
                throw new RuntimeException("Aucun chemin entre " + start.getNom_artist() + " et " + end.getNom_artist());
            }

            // j'enleve le premier de la file et il devient le courant
            artisteCourant = fileSommetsPasEncoreAtteints.poll();

            //fin du BFS quand le courant == end
            if (artisteCourant.equals(end)){
                reconstruirChemin(provenences, end);
            }

            //parcourir le set des mentions de l'artiste courant
            for (Mention m : mentionsDunArtiste.get(artisteCourant)) {
                if(!visited.contains(m.getArtiste_mentionne())){
                    visited.add(m.getArtiste_mentionne()); //Ajout des artistes mentionnés par le courant dans le Set des visited
                    fileSommetsPasEncoreAtteints.add(m.getArtiste_mentionne()); //Ajout des artistes mentionnés par le courant dans la file des sommets
                    provenences.put(m.getArtiste_mentionne(), m);
                }
            }
        }
    }//End of trouverCheminLePlusCourt*/

    public void trouverCheminMaxMentions(String artiste1, String artiste2){
        Artist start = this.artists.get(artiste1);
        Artist end = this.artists.get(artiste2);

        //retient le cout minimal pour atteindre chaque artiste
        Map<Artist, Double> etiquetteProvisoire = new HashMap<>();
        Map<Artist, Double> etiquetteDefinitive = new HashMap<>();

        //retient d'ou viennent chaque artiste
        Map<Artist, Mention> provenences = new HashMap<>(); //artiste mentionné, mention

        boolean cheminTrouve = false;//util pour gérer les cas où aucun chemin n'est trouvé

        etiquetteProvisoire.put(start, 0.0);

        Artist artisteCourrant = null;

        //algo de Dijkstra
        while (!etiquetteProvisoire.isEmpty()){

            //cherche l'artiste avec le cout minimal
            Double coutMin = Double.MAX_VALUE;
            for (Artist a : etiquetteProvisoire.keySet()) {
                if (etiquetteProvisoire.get(a)<coutMin){
                    coutMin = etiquetteProvisoire.get(a);
                    artisteCourrant = a;
                }
            }

            //ajoute artiste courrant dans etiquette definitive avec cout ( cout : etiquetteProvisoire.get(artisteCourrant) )
            etiquetteDefinitive.put(artisteCourrant, etiquetteProvisoire.get(artisteCourrant));
            etiquetteProvisoire.remove(artisteCourrant);

            if (artisteCourrant.equals(end)){
                reconstruirChemin(provenences, end);
                cheminTrouve = true;
                break;
            }

            //parcourir le set des mentions de l'artiste courant
            for (Mention m : this.mentionsDunArtiste.get(artisteCourrant)) {
                Artist artisteMentionne = m.getArtiste_mentionne();
                Double nouveauCout = etiquetteDefinitive.get(artisteCourrant) + m.getNb_mentions();

                if (!etiquetteDefinitive.keySet().contains(artisteMentionne)){
                    if (!etiquetteProvisoire.keySet().contains(artisteMentionne)){
                        //ajout de l'artiste mentionné dans l'étiquette prov s'il n'est ni dans la prov, ni dans la definitive
                        etiquetteProvisoire.put(artisteMentionne, nouveauCout);
                        provenences.put(artisteMentionne, m);
                    }

                    if (etiquetteProvisoire.keySet().contains(artisteMentionne)){
                        if (etiquetteProvisoire.get(artisteMentionne)>nouveauCout){
                            //modif du cout de l'artisteMentionne dans etiquetteProvisoire si nouveauCout inférieur à l'ancien
                            etiquetteProvisoire.put(artisteMentionne, nouveauCout);
                            provenences.put(artisteMentionne, m);
                        }
                    }
                }
            }
        }//End of while

        if (!cheminTrouve){
            throw new RuntimeException("Aucun chemin entre " + start.getNom_artist() + " et " + end.getNom_artist());
        }

    }//End of trouverCheminMaxMentions

    private void reconstruirChemin(Map<Artist, Mention> provenences, Artist end){
        List<Artist> chemin = new ArrayList<>();
        Artist artisteCourant = end;
        double coutTotal = 0;

        while (artisteCourant != null){
            chemin.add(artisteCourant);

            if (provenences.get(artisteCourant)!=null){
                //debug System.out.println("provenence.get(artisteCourant).getNb_mentions() : " + provenence.get(artisteCourant).getNb_mentions());
                coutTotal += provenences.get(artisteCourant).getNb_mentions();//recup le cout dans la mention
            }

            Mention m = provenences.get(artisteCourant) ;
            if (m!=null){
                artisteCourant = m.getArtiste_mentionneur();
            } else {
                artisteCourant = null ;
            }
        }

        Collections.reverse(chemin); //pour inverser l'ordre du chemin

        int logueurDuChemin = chemin.size()-1;//-1 car le premier n'est pas compté

        System.out.println("Longueur du chemin : " + logueurDuChemin);
        System.out.println("Coût total du chemin : " + coutTotal);
        System.out.println("Chemain :");

        for (Artist artist : chemin) {
            System.out.println(artist.getNom_artist() + " (" + artist.getCategorie() + ")");
        }
    }//End of reconstruirChemin

}//End of Class Graph
