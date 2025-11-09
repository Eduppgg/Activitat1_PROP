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
        // Creem el node inicial
        NodeAStar nodeInicial = new NodeAStar(mapaInicial, null, null, 0, heuristica.h(mapaInicial));

        // Cua de prioritat per escollir el node amb menor f = g + h
        PriorityQueue<NodeAStar> oberts = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        oberts.add(nodeInicial);

        // Guardem el millor cost trobat per cada estat per evitar reexploracions
        Map<Mapa, Integer> millorCost = new HashMap<>();
        millorCost.put(mapaInicial, nodeInicial.f);

        resultat.updateMemoria(oberts.size());

        // Bucle principal: anem traient nodes de la cua i expandint-los
        while (!oberts.isEmpty()) {
            NodeAStar actual = oberts.poll();
            resultat.incNodesExplorats();

            // Si ja hem arribat a la sortida, reconstruïm el camí i sortim
            if (actual.mapa.esMeta()) {
                resultat.setCami(reconstrueixCami(actual));
                return;
            }

            // Generem tots els moviments possibles des d'aquest estat
            for (Moviment moviment : actual.mapa.getAccionsPossibles()) {
                try {
                    // Apliquem el moviment per obtenir el nou estat
                    Mapa mapaSeguent = actual.mapa.mou(moviment);
                    int g = actual.g + 1; // cost fins ara
                    int h = heuristica.h(mapaSeguent); // estimació del que queda
                    int f = g + h; // funció de prioritat

                    // Si és un nou estat o trobem un camí més barat, el guardem
                    if (!millorCost.containsKey(mapaSeguent) || f < millorCost.get(mapaSeguent)) {
                        millorCost.put(mapaSeguent, f);
                        oberts.add(new NodeAStar(mapaSeguent, actual, moviment, g, h));
                    } else {
                        // Si ja s’havia visitat amb millor cost, el tallem
                        resultat.incNodesTallats();
                    }

                    resultat.updateMemoria(oberts.size());
                } catch (Exception e) {
                    // Moviment invàlid (mur, porta tancada, col·lisió, etc.)
                    resultat.incNodesTallats();
                }
            }
        }

        // Si sortim del bucle sense trobar meta, no hi ha solució
        resultat.setCami(null);
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
