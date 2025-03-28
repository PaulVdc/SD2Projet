import java.util.Objects;

public class Artist {
    private int id_artist;
    private String nom_artist;
    private String categorie;

    public Artist(int id_artist, String nom_artist, String categorie) {
        this.id_artist = id_artist;
        this.nom_artist = nom_artist;
        this.categorie = categorie;
    }

    public int getId_artist() {
        return id_artist;
    }

    public String getNom_artist() {
        return nom_artist;
    }

    public String getCategorie() {
        return categorie;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Artist artist)) return false;
        return id_artist == artist.id_artist;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id_artist);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id_artist=" + id_artist +
                ", nom_artist='" + nom_artist + '\'' +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
