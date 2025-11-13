package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.cerca.heuristica.Heuristica;
import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

import java.util.*;


public class CercaAStar extends Cerca {

    private final Heuristica heuristica;

    public CercaAStar(boolean usarLNT, Heuristica heuristica) { 
        super(usarLNT); 
        this.heuristica = heuristica; 
    }

    @Override
    public void ferCerca(Mapa mapaInicial, ResultatCerca resultat) {
        resultat.startTime();

        if (usarLNT) {
            ferCercaLNT(mapaInicial, resultat);
        } else {
            ferCercaPath(mapaInicial, resultat);
        }
        
        resultat.stopTime();
    }

    /**
     * Versió de A* amb Llista de Nodes Tancats (LNT).
     */
    private void ferCercaLNT(Mapa mapaInicial, ResultatCerca resultat) {
        NodeAStar nodeInicial = new NodeAStar(mapaInicial, null, null, 0, heuristica.h(mapaInicial));

        PriorityQueue<NodeAStar> oberts = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        oberts.add(nodeInicial);

        Map<Mapa, Integer> millorG = new HashMap<>();
        millorG.put(mapaInicial, 0);

        resultat.updateMemoria(oberts.size() + millorG.size());

        while (!oberts.isEmpty()) {
            NodeAStar actual = oberts.poll();
            resultat.incNodesExplorats();

            if (actual.mapa.esMeta()) {
                resultat.setCami(reconstrueixCami(actual));
                return;
            }
            
            if (actual.g > millorG.getOrDefault(actual.mapa, Integer.MAX_VALUE)) {
                 resultat.incNodesTallats();
                 continue;
            }


            for (Moviment moviment : actual.mapa.getAccionsPossibles()) {
                try {
                    Mapa mapaSeguent = actual.mapa.mou(moviment);
                    int g = actual.g + 1; 
                    int h = heuristica.h(mapaSeguent); 
                    int f = g + h; 

                    if (g < millorG.getOrDefault(mapaSeguent, Integer.MAX_VALUE)) {
                        millorG.put(mapaSeguent, g);
                        oberts.add(new NodeAStar(mapaSeguent, actual, moviment, g, h));
                    } else {
                        resultat.incNodesTallats();
                    }
                    
                } catch (Exception e) {
                    resultat.incNodesTallats();
                }
            }
            resultat.updateMemoria(oberts.size() + millorG.size());
        }

        resultat.setCami(null);
    }
    
    /**
     * Versió de A* amb control de cicles en branca (Path Checking).
     */
    private void ferCercaPath(Mapa mapaInicial, ResultatCerca resultat) {
        NodeAStar nodeInicial = new NodeAStar(mapaInicial, null, null, 0, heuristica.h(mapaInicial));

        PriorityQueue<NodeAStar> oberts = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        oberts.add(nodeInicial);
        
        resultat.updateMemoria(oberts.size());

        while (!oberts.isEmpty()) {
            NodeAStar actual = oberts.poll();
            resultat.incNodesExplorats();

            if (actual.mapa.esMeta()) {
                resultat.setCami(reconstrueixCami(actual));
                return;
            }

            for (Moviment moviment : actual.mapa.getAccionsPossibles()) {
                try {
                    Mapa mapaSeguent = actual.mapa.mou(moviment);
                    
                    if (!estaEnCami(actual, mapaSeguent)) {
                        int g = actual.g + 1; 
                        int h = heuristica.h(mapaSeguent); 
                        oberts.add(new NodeAStar(mapaSeguent, actual, moviment, g, h));
                    } else {
                        resultat.incNodesTallats();
                    }

                } catch (Exception e) {
                    resultat.incNodesTallats();
                }
            }
             resultat.updateMemoria(oberts.size());
        }
        
        resultat.setCami(null);
    }
    
    
    /**
     * Comprova si un estat (mapaBuscat) ja existeix a la branca actual,
     * recorrent els pares des del node 'node' cap amunt.
     */
    private boolean estaEnCami(NodeAStar node, Mapa mapaBuscat) {
        NodeAStar current = node;
        while (current != null) {
            if (current.mapa.equals(mapaBuscat)) {
                return true;
            }
            current = current.pare;
        }
        return false;
    }


    /**
     * Classe interna que representa un node dins la cerca A*.
     * Guarda l'estat del mapa, el pare, el moviment aplicat i els costos.
     */
    private static class NodeAStar {
        Mapa mapa;
        NodeAStar pare;
        Moviment moviment;
        int g; // cost acumulat
        int h; // cost estimat restant
        int f; // total f = g + h

        NodeAStar(Mapa mapa, NodeAStar pare, Moviment moviment, int g, int h) {
            this.mapa = mapa;
            this.pare = pare;
            this.moviment = moviment;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
    }

    /**
     * Reconstrueix el camí complet des de la meta fins a l'inici
     * recorrent els nodes pare.
     */
    private List<Moviment> reconstrueixCami(NodeAStar node) {
        LinkedList<Moviment> cami = new LinkedList<>();
        while (node != null && node.moviment != null) {
            cami.addFirst(node.moviment);
            node = node.pare;
        }
        return cami;
    }
}
