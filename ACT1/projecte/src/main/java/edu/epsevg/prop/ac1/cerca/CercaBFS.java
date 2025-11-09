package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;
import java.util.*;


public class CercaBFS extends Cerca {

    public CercaBFS(boolean usarLNT) {
        super(usarLNT);
    }

    /**
     * Classe auxiliar que representa un node dins de la cerca BFS.
     * Guarda l'estat actual del mapa i el camí recorregut fins a ell.
     */
    private static class Node {
        Mapa mapa;
        List<Moviment> cami;

        Node(Mapa mapa, List<Moviment> cami) {
            this.mapa = mapa;
            this.cami = cami;
        }
    }

    @Override
    public void ferCerca(Mapa mapaInicial, ResultatCerca resultat) {
        resultat.startTime(); // inici del cronòmetre

        // Inicialitzem les estructures
        Queue<Node> cua = new ArrayDeque<>();
        Set<Mapa> visitats = new HashSet<>();

        // Afegim el node inicial
        cua.add(new Node(mapaInicial, new ArrayList<>()));
        visitats.add(mapaInicial);
        resultat.updateMemoria(1);

        // Bucle principal: extreure i expandir nodes fins trobar la meta
        while (!cua.isEmpty()) {
            Node actual = cua.poll();
            Mapa mapa = actual.mapa;
            resultat.incNodesExplorats();

            // Si hem arribat a la sortida
            if (mapa.esMeta()) {
                resultat.setCami(actual.cami);
                resultat.stopTime();
                return;
            }

            // Generem tots els moviments possibles
            for (Moviment moviment : mapa.getAccionsPossibles()) {
                try {
                    // Apliquem el moviment
                    Mapa mapaSeguent = mapa.mou(moviment);

                    // Si no s’ha visitat, el guardem per explorar
                    if (!visitats.contains(mapaSeguent)) {
                        visitats.add(mapaSeguent);

                        // Copiem el camí i hi afegim el nou moviment
                        List<Moviment> nouCami = new ArrayList<>(actual.cami);
                        nouCami.add(moviment);

                        // Afegim el nou estat a la cua
                        cua.add(new Node(mapaSeguent, nouCami));
                    } else {
                        // Estat ja visitat → no cal reexplorar
                        resultat.incNodesTallats();
                    }

                } catch (IllegalArgumentException e) {
                    // Moviment no vàlid (mur, porta tancada, etc.)
                    resultat.incNodesTallats();
                }
            }

            // Actualitzem la memòria màxima utilitzada
            resultat.updateMemoria(visitats.size() + cua.size());
        }

        // Si hem acabat i no hem trobat sortida
        resultat.stopTime();
        resultat.setCami(null);
    }
}
