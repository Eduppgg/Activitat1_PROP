package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;
import java.util.*;


public class CercaIDS extends Cerca {

    public CercaIDS(boolean usarLNT) {
        super(usarLNT);
    }

    /** Classe auxiliar per representar un node de la cerca. */
    private static class Node {
        Mapa mapa;
        List<Moviment> cami;
        int profunditat;

        Node(Mapa mapa, List<Moviment> cami, int profunditat) {
            this.mapa = mapa;
            this.cami = cami;
            this.profunditat = profunditat;
        }
    }

    @Override
    public void ferCerca(Mapa mapaInicial, ResultatCerca resultat) {
        resultat.startTime();

        int limit = 0;          // profunditat inicial
        int limitMax = 1000;    // límit màxim de seguretat
        boolean trobat = false;

        // Anem augmentant el límit de profunditat progressivament
        while (!trobat && limit <= limitMax) {
            trobat = cercaLimitada(mapaInicial, resultat, limit);
            limit++;
        }

        resultat.stopTime();
    }

    /**
     * Realitza una cerca en profunditat limitada.
     * Retorna true si troba la meta dins del límit.
     */
    private boolean cercaLimitada(Mapa mapaInicial, ResultatCerca resultat, int limit) {
        Deque<Node> pila = new ArrayDeque<>();
        Set<Mapa> visitats = new HashSet<>();

        // Node inicial
        pila.push(new Node(mapaInicial, new ArrayList<>(), 0));
        visitats.add(mapaInicial);
        resultat.updateMemoria(pila.size() + visitats.size());

        while (!pila.isEmpty()) {
            Node actual = pila.pop();
            Mapa mapa = actual.mapa;
            int profunditat = actual.profunditat;
            resultat.incNodesExplorats();

            // Si hem arribat a la meta, retornem el camí
            if (mapa.esMeta()) {
                resultat.setCami(actual.cami);
                return true;
            }

            // Si superem el límit, tallem aquesta branca
            if (profunditat >= limit) {
                resultat.incNodesTallats();
                continue;
            }

            // Generem successors
            for (Moviment mov : mapa.getAccionsPossibles()) {
                try {
                    Mapa seguent = mapa.mou(mov);

                    if (!visitats.contains(seguent)) {
                        visitats.add(seguent);

                        List<Moviment> nouCami = new ArrayList<>(actual.cami);
                        nouCami.add(mov);

                        pila.push(new Node(seguent, nouCami, profunditat + 1));
                    } else {
                        resultat.incNodesTallats();
                    }

                } catch (IllegalArgumentException e) {
                    // Moviment invàlid (mur, porta, col·lisió, etc.)
                    resultat.incNodesTallats();
                }
            }

            // Actualitzem el pic de memòria
            resultat.updateMemoria(pila.size() + visitats.size());
        }

        return false; // No s'ha trobat solució en aquest límit
    }
}
