import java.util.Objects;

public class Mention {
    private Artist artiste_mentionneur;
    private Artist artiste_mentionne;
    private double nb_mentions;

    public Mention(Artist artiste_mentionneur, Artist artiste_mentionne, Integer nb_mentions) {
        this.artiste_mentionneur = artiste_mentionneur;
        this.artiste_mentionne = artiste_mentionne;
        this.nb_mentions = 1.0 /nb_mentions;
    }

    public Artist getArtiste_mentionneur() {
        return artiste_mentionneur;
    }

    public Artist getArtiste_mentionne() {
        return artiste_mentionne;
    }

    public double getNb_mentions() {
        return nb_mentions;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Mention mention)) return false;
        return Double.compare(nb_mentions, mention.nb_mentions) == 0 && Objects.equals(artiste_mentionneur, mention.artiste_mentionneur) && Objects.equals(artiste_mentionne, mention.artiste_mentionne);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artiste_mentionneur, artiste_mentionne, nb_mentions);
    }

    @Override
    public String toString() {
        return "Mention{" +
                "artiste_mentionneur=" + artiste_mentionneur +
                ", artiste_mentionne=" + artiste_mentionne +
                ", nb_mentions=" + nb_mentions +
                '}';
    }
}
