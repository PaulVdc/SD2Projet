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

                    //System.out.println(artiste); //Pour verifier si l'ajout s'est bien passé
                    this.artistsIds.put(artiste.getId_artist(), artiste);
                    this.artists.put(artiste.getNom_artist(), artiste);
                    //this.artistsMentionnes.put(artiste,new HashSet<Artist>()) ;
                    this.mentionsDunArtiste.put(artiste, new HashSet<Mention>());
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
                    //System.out.println(mention);
                    //this.artistsMentionnes.putIfAbsent(mentionneur, new HashSet<Artist>());
                    //this.artistsMentionnes.get(mentionneur).add(mentionnee);
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

        // Créer une 3ème structure pour retenir d'ou les sommets viennent ( Faire une map <Artiste_Mentionné,Mentions> )
        Map<Artist, Mention> provenence = new HashMap<>(); //artiste mentionné, mention

        fileSommetsPasEncoreAtteints.add(start);
        visited.add(start);
        provenence.put(start, null);

        while(!artisteCourant.equals(end)) {
            // je prend  le premier de la file et je l'enleve et il devient mon courant
            artisteCourant = fileSommetsPasEncoreAtteints.poll();

            if (provenence.size()<2){
                throw new RuntimeException("Aucun chemin entre " + start + " et " + end);
            }
            if (artisteCourant.equals(end)){
                reconstruirChemin(provenence, end);
            }

            for (Mention m : mentionsDunArtiste.get(artisteCourant)) {
                if(!visited.contains(m.getArtiste_mentionne())){
                    visited.add(m.getArtiste_mentionne()); //Ajout des artistes mentionnés par le courant dans le Set des visited
                    fileSommetsPasEncoreAtteints.add(m.getArtiste_mentionne()); //Ajout des artistes mentionnés par le courant dans la file des sommets
                    provenence.put(m.getArtiste_mentionne(), m);
                }
            }
        }
    }//End of trouverCheminLePlusCourt*/

    private void reconstruirChemin(Map<Artist, Mention> provenence, Artist end){
        List<Artist> chemin = new ArrayList<>();
        Artist artisteCourant = end;
        double coutTotal = 0;

        while (artisteCourant != null){
            chemin.add(artisteCourant);

            if (provenence.get(artisteCourant)!=null){
                //debug System.out.println("provenence.get(artisteCourant).getNb_mentions() : " + provenence.get(artisteCourant).getNb_mentions());
                coutTotal += provenence.get(artisteCourant).getNb_mentions();
            }

            Mention m = provenence.get(artisteCourant) ;
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

    public void trouverCheminMaxMentions(String artiste1, String artiste2){
        Artist start = this.artists.get(artiste1);
        Artist end = this.artists.get(artiste2);

        //retient le cout minimal pour atteindre chaque artiste
        Map<Artist, Double> etiquetteProvisoire = new HashMap<>();
        Map<Artist, Double> etiquetteDefinitive = new HashMap<>();

        //retient d'ou viennent chaque artiste
        Map<Artist, Mention> provenence = new HashMap<>(); //artiste mentionné, mention

        etiquetteProvisoire.put(start, 0.0);

        Artist artisteCourrant=null;

        //algo de Dijkstra
        while (!etiquetteProvisoire.isEmpty()){


            Double coutMin = Double.MAX_VALUE;
            for (Artist a : etiquetteProvisoire.keySet()) {
                if (etiquetteProvisoire.get(a)<coutMin){
                    coutMin = etiquetteProvisoire.get(a);
                    artisteCourrant = a;
                }
            }

            //ajout de artiste courrant dans etiquette prov avec cout
            etiquetteDefinitive.put(artisteCourrant, etiquetteProvisoire.get(artisteCourrant));
            etiquetteProvisoire.remove(artisteCourrant);

            if (provenence.size()<2){
                throw new RuntimeException("Aucun chemin entre " + start + " et " + end);
            }
            if (artisteCourrant.equals(end)){
                reconstruirChemin(provenence, end);
            }

            for (Mention m : this.mentionsDunArtiste.get(artisteCourrant)) {
               Artist artisteMentionne = m.getArtiste_mentionne();
                Double nouveauCout = etiquetteDefinitive.get(artisteCourrant) + m.getNb_mentions();
                if (!etiquetteDefinitive.keySet().contains(artisteMentionne)){
                    if (!etiquetteProvisoire.keySet().contains(artisteMentionne)){
                        etiquetteProvisoire.put(artisteMentionne, nouveauCout);
                        provenence.put(artisteMentionne, m);
                    }

                    if (etiquetteProvisoire.keySet().contains(artisteMentionne)){

                        if (etiquetteProvisoire.get(artisteMentionne)>nouveauCout){
                            etiquetteProvisoire.put(artisteMentionne, nouveauCout);
                            provenence.put(artisteMentionne, m);
                        }
                    }
                }
            }
        }

    }//End of trouverCheminMaxMentions
}
