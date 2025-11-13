package edu.epsevg.prop.ac1.cerca;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.epsevg.prop.ac1.cerca.heuristica.Heuristica;
import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.model.Moviment;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

public class CercaAStar extends Cerca {

    private final Heuristica heuristica;

    public CercaAStar(boolean usarLNT, Heuristica heuristica) { 
        super(usarLNT); 
        this.heuristica = heuristica; 
    }

    @Override
    public void ferCerca(Mapa mapaInicial, ResultatCerca resultat) {
        // Node inicial
        NodeAStar nodeInicial = new NodeAStar(
            mapaInicial, null, null,
            0,
            heuristica.h(mapaInicial)
        );

        // Cua de prioritat per f = g + h
        PriorityQueue<NodeAStar> oberts =
            new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        oberts.add(nodeInicial);

        // Millor cost conegut per a cada estat
        Map<Mapa, Integer> millorCost = new HashMap<>();
        millorCost.put(mapaInicial, nodeInicial.f);

        resultat.updateMemoria(oberts.size());

        // Bucle principal A*
        while (!oberts.isEmpty()) {
            NodeAStar actual = oberts.poll();
            resultat.incNodesExplorats();

            // Si ja hem arribat a la meta -> reconstruïm camí
            if (actual.mapa.esMeta()) {
                resultat.setCami(reconstrueixCami(actual));
                return;
            }

            // Expansió de successors
            for (Moviment moviment : actual.mapa.getAccionsPossibles()) {
                try {
                    Mapa mapaSeguent = actual.mapa.mou(moviment);
                    int g = actual.g + 1;
                    int h = heuristica.h(mapaSeguent);
                    int f = g + h;

                    if (!millorCost.containsKey(mapaSeguent) || f < millorCost.get(mapaSeguent)) {
                        millorCost.put(mapaSeguent, f);
                        oberts.add(new NodeAStar(mapaSeguent, actual, moviment, g, h));
                    } else {
                        resultat.incNodesTallats();
                    }

                    resultat.updateMemoria(oberts.size());
                } catch (Exception e) {
                    // moviment invàlid
                    resultat.incNodesTallats();
                }
            }
        }

        // No hi ha solució
        resultat.setCami(null);
    }

    // ---------- Node intern A* ----------

    private static class NodeAStar {
        Mapa mapa;
        NodeAStar pare;
        Moviment moviment;
        int g; // cost acumulat
        int h; // cost heurístic
        int f; // g + h

        NodeAStar(Mapa mapa, NodeAStar pare, Moviment moviment, int g, int h) {
            this.mapa = mapa;
            this.pare = pare;
            this.moviment = moviment;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
    }

    // ---------- Reconstrucció del camí ----------

    private List<Moviment> reconstrueixCami(NodeAStar node) {
        LinkedList<Moviment> cami = new LinkedList<>();
        while (node != null && node.moviment != null) {
            cami.addFirst(node.moviment);
            node = node.pare;
        }
        return cami;
    }
}
