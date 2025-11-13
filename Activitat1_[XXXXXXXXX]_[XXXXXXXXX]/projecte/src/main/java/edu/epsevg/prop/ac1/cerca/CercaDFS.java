package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;
import java.util.*;

public class CercaDFS extends Cerca {

    private static final int LIMIT_PROFUNDITAT_DFS = 50;

    public CercaDFS(boolean usarLNT) {
        super(usarLNT);
    }

    /**
     * Classe auxiliar per representar un node dins de la cerca.
     * Conté l’estat actual, el camí fet i el node pare.
     */
    private static class Node {
        Mapa mapa;
        List<Moviment> cami;
        Node pare;

        Node(Mapa mapa, List<Moviment> cami, Node pare) {
            this.mapa = mapa;
            this.cami = cami;
            this.pare = pare;
        }
    }

    @Override
    public void ferCerca(Mapa mapaInicial, ResultatCerca resultat) {
        resultat.startTime(); // Iniciem el cronòmetre

        Deque<Node> pila = new ArrayDeque<>();
        
        // Map<Mapa, Profunditat>
        Map<Mapa, Integer> visitatsLNT = new HashMap<>();

        // Afegim el node inicial
        Node nodeInicial = new Node(mapaInicial, new ArrayList<>(), null);
        pila.push(nodeInicial);
        
        if (usarLNT) {
            visitatsLNT.put(mapaInicial, 0);
            resultat.updateMemoria(visitatsLNT.size() + pila.size());
        } else {
             resultat.updateMemoria(pila.size());
        }

        // Bucle principal: traiem nodes de la pila fins trobar sortida o buidar-la
        while (!pila.isEmpty()) {
            Node actual = pila.pop();
            Mapa mapa = actual.mapa;
            int profunditatActual = actual.cami.size();
            resultat.incNodesExplorats();

            // Si ja som a la sortida
            if (mapa.esMeta()) {
                resultat.setCami(actual.cami);
                resultat.stopTime();
                return;
            }

            if (profunditatActual >= LIMIT_PROFUNDITAT_DFS) {
                resultat.incNodesTallats();
                continue; // Tallem aquesta branca
            }

            // Generem tots els moviments possibles
            List<Moviment> moviments = mapa.getAccionsPossibles();

            // Opcionalment, podem invertir-los per variar l'ordre de recorregut
            // Collections.reverse(moviments);

            for (Moviment mov : moviments) {
                try {
                    Mapa seguent = mapa.mou(mov);
                    int novaProfunditat = profunditatActual + 1;
                    
                    boolean explorar = false;
                    
                    if (usarLNT) {
                        if (!visitatsLNT.containsKey(seguent) || novaProfunditat < visitatsLNT.get(seguent)) {
                            visitatsLNT.put(seguent, novaProfunditat);
                            explorar = true;
                        }
                    } else {
                        if (!estaEnCami(actual, seguent)) {
                            explorar = true;
                        }
                    }

                    if (explorar) {
                        List<Moviment> nouCami = new ArrayList<>(actual.cami);
                        nouCami.add(mov);
                        pila.push(new Node(seguent, nouCami, actual));
                    } else {
                        resultat.incNodesTallats();
                    }

                } catch (IllegalArgumentException e) {
                    // Moviment no vàlid (mur, porta tancada, col·lisió...)
                    resultat.incNodesTallats();
                }
            }

            // Actualitzem el pic de memòria usat
            if (usarLNT) {
                 resultat.updateMemoria(visitatsLNT.size() + pila.size());
            } else {
                 resultat.updateMemoria(pila.size());
            }
        }

        // Si hem acabat i no hem trobat cap camí
        resultat.setCami(null);
        resultat.stopTime();
    }
    
    /**
     * Comprova si un estat (mapaBuscat) ja existeix a la branca actual,
     * recorrent els pares des del node 'node' cap amunt.
     */
    private boolean estaEnCami(Node node, Mapa mapaBuscat) {
        Node current = node;
        while (current != null) {
            if (current.mapa.equals(mapaBuscat)) {
                return true;
            }
            current = current.pare;
        }
        return false;
    }
}