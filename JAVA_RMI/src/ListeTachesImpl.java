import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;

public class ListeTachesImpl implements ListeTaches, Remote {

    private List<String> taches = new ArrayList<>();

    @Override
    public void ajouterTache(String tache) {
        taches.add(tache);
    }

    @Override
    public void supprimerTache(String tache) {
        taches.remove(tache);
    }

    @Override
    public List<String> recupererListeTaches() {
        return taches;
    }

}
