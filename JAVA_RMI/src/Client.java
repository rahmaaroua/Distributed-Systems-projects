import java.rmi.Naming;
import java.util.List;

public class Client {

    public static void main(String[] args) throws Exception {
        ListeTaches listeTaches = (ListeTaches) Naming.lookup("ListeTaches");

        // Ajout d'une tâche
        listeTaches.ajouterTache("Acheter du pain");

        // Suppression d'une tâche
        listeTaches.supprimerTache("Faire la lessive");

        // Récupération de la liste des tâches
        List<String> taches = listeTaches.recupererListeTaches();
        for (String tache : taches) {
            System.out.println(tache);
        }
    }

}