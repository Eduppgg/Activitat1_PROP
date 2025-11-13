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
     * Guarda l'estat actual del mapa, el camí recorregut fins a ell
     * i una referència al node pare (necessari per al control de cicles en branca).
     */
    private static class Node {
        Mapa mapa;
        List<Moviment> cami;
        Node pare; // Per al control de cicles en branca (usarLNT=false)

        Node(Mapa mapa, List<Moviment> cami, Node pare) {
            this.mapa = mapa;
            this.cami = cami;
            this.pare = pare;
        }
    }

    @Override
    public void ferCerca(Mapa mapaInicial, ResultatCerca resultat) {
        resultat.startTime(); // inici del cronòmetre

        Queue<Node> cua = new ArrayDeque<>();
        
        // Map<Mapa, Profunditat>
        Map<Mapa, Integer> visitatsLNT = new HashMap<>();

        // Afegim el node inicial
        Node nodeInicial = new Node(mapaInicial, new ArrayList<>(), null);
        cua.add(nodeInicial);
        
        if (usarLNT) {
            visitatsLNT.put(mapaInicial, 0);
            resultat.updateMemoria(visitatsLNT.size() + cua.size());
        } else {
            resultat.updateMemoria(cua.size());
        }


        // Bucle principal: extreure i expandir nodes fins trobar la meta
        while (!cua.isEmpty()) {
            Node actual = cua.poll();
            Mapa mapa = actual.mapa;
            int profunditatActual = actual.cami.size();
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
                    int novaProfunditat = profunditatActual + 1;

                    boolean explorar = false;

                    if (usarLNT) {
                        // Comprovem si ja s'ha visitat a menor profunditat
                        if (!visitatsLNT.containsKey(mapaSeguent) || novaProfunditat < visitatsLNT.get(mapaSeguent)) {
                            visitatsLNT.put(mapaSeguent, novaProfunditat);
                            explorar = true;
                        }
                    } else {
                        // Comprovem si és al camí (branca) actual
                        if (!estaEnCami(actual, mapaSeguent)) {
                            explorar = true;
                        }
                    }

                    if (explorar) {
                        // Copiem el camí i hi afegim el nou moviment
                        List<Moviment> nouCami = new ArrayList<>(actual.cami);
                        nouCami.add(moviment);
                        // Afegim el nou estat a la cua
                        cua.add(new Node(mapaSeguent, nouCami, actual));
                    } else {
                        // Estat ja visitat (a menor profunditat) o cicle en branca
                        resultat.incNodesTallats();
                    }

                } catch (IllegalArgumentException e) {
                    // Moviment no vàlid (mur, porta tancada, etc.)
                    resultat.incNodesTallats();
                }
            }

            // Actualitzem la memòria màxima utilitzada
            if (usarLNT) {
                resultat.updateMemoria(visitatsLNT.size() + cua.size());
            } else {
                resultat.updateMemoria(cua.size()); 
            }
        }

        // Si hem acabat i no hem trobat sortida
        resultat.stopTime();
        resultat.setCami(null);
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