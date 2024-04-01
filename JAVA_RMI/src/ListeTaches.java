import java.util.ArrayList;
import java.util.List;

public interface ListeTaches {
    void ajouterTache(String tache);
    void supprimerTache(String tache);
    List<String> recupererListeTaches();
}

