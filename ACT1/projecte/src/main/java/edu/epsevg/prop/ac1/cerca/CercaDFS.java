package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;
import java.util.*;

public class CercaDFS extends Cerca {

    public CercaDFS(boolean usarLNT) {
        super(usarLNT);
    }

    /**
     * Classe auxiliar per representar un node dins de la cerca.
     * Conté l’estat actual i el camí fet fins arribar-hi.
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
        resultat.startTime(); // Iniciem el cronòmetre

        // Estructures bàsiques
        Deque<Node> pila = new ArrayDeque<>();
        Set<Mapa> visitats = new HashSet<>();

        // Afegim el node inicial
        pila.push(new Node(mapaInicial, new ArrayList<>()));
        visitats.add(mapaInicial);
        resultat.updateMemoria(pila.size() + visitats.size());

        // Bucle principal: traiem nodes de la pila fins trobar sortida o buidar-la
        while (!pila.isEmpty()) {
            Node actual = pila.pop();
            Mapa mapa = actual.mapa;
            resultat.incNodesExplorats();

            // Si ja som a la sortida
            if (mapa.esMeta()) {
                resultat.setCami(actual.cami);
                resultat.stopTime();
                return;
            }

            // Generem tots els moviments possibles
            List<Moviment> moviments = mapa.getAccionsPossibles();

            // Opcionalment, podem invertir-los per variar l'ordre de recorregut
            // Collections.reverse(moviments);

            for (Moviment mov : moviments) {
                try {
                    Mapa seguent = mapa.mou(mov);

                    // Si no hem visitat aquest estat
                    if (!visitats.contains(seguent)) {
                        visitats.add(seguent);
                        List<Moviment> nouCami = new ArrayList<>(actual.cami);
                        nouCami.add(mov);
                        pila.push(new Node(seguent, nouCami));
                    } else {
                        // Si ja estava visitat, no cal tornar-hi
                        resultat.incNodesTallats();
                    }

                } catch (IllegalArgumentException e) {
                    // Moviment no vàlid (mur, porta tancada, col·lisió...)
                    resultat.incNodesTallats();
                }
            }

            // Actualitzem el pic de memòria usat
            resultat.updateMemoria(visitats.size() + pila.size());
        }

        // Si hem acabat i no hem trobat cap camí
        resultat.setCami(null);
        resultat.stopTime();
    }
}
