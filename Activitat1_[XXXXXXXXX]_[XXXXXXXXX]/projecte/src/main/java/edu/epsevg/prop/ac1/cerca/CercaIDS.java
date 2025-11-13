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
        Node pare; 

        Node(Mapa mapa, List<Moviment> cami, int profunditat, Node pare) {
            this.mapa = mapa;
            this.cami = cami;
            this.profunditat = profunditat;
            this.pare = pare;
        }
    }

    @Override
    public void ferCerca(Mapa mapaInicial, ResultatCerca resultat) {
        resultat.startTime();

        int limit = 0;          
        int limitMax = 1000;    
        boolean trobat = false;

        // NOTA: La LNT no es crea aquí fora.

        while (!trobat && limit <= limitMax) {
            
            if (usarLNT) {
                // **LA CORRECCIÓ ÉS AQUÍ:**
                // Creem una NOVA LNT per a CADA iteració (cada DLS)
                Map<Mapa, Integer> visitatsLNT = new HashMap<>();
                trobat = cercaLimitadaLNT(mapaInicial, resultat, limit, visitatsLNT);
            } else {
                trobat = cercaLimitadaPath(mapaInicial, resultat, limit);
            }
            limit++;
        }

        resultat.stopTime();
    }

    /**
     * Realitza una cerca en profunditat limitada (DLS)
     * USANT LNT (Llista de Nodes Tancats).
     */
    private boolean cercaLimitadaLNT(Mapa mapaInicial, ResultatCerca resultat, int limit, Map<Mapa, Integer> visitatsLNT) {
        Deque<Node> pila = new ArrayDeque<>();
        
        pila.push(new Node(mapaInicial, new ArrayList<>(), 0, null));
        visitatsLNT.put(mapaInicial, 0); // Afegim l'arrel a la LNT d'aquesta iteració
        
        resultat.updateMemoria(pila.size() + visitatsLNT.size());

        while (!pila.isEmpty()) {
            Node actual = pila.pop();
            Mapa mapa = actual.mapa;
            int profunditat = actual.profunditat;
            resultat.incNodesExplorats();

            // 1. COMPROVAR META
            if (mapa.esMeta()) {
                resultat.setCami(actual.cami);
                return true;
            }

            // 2. COMPROVAR LÍMIT
            if (profunditat >= limit) {
                resultat.incNodesTallats();
                continue;
            }
            
            // 3. EXPANDIR (Només si profunditat < limit)
            for (Moviment mov : mapa.getAccionsPossibles()) {
                try {
                    Mapa seguent = mapa.mou(mov);
                    int novaProfunditat = profunditat + 1;

                    // Comprovació LNT (dins d'aquesta iteració)
                    if (!visitatsLNT.containsKey(seguent) || novaProfunditat < visitatsLNT.get(seguent)) {
                        visitatsLNT.put(seguent, novaProfunditat);

                        List<Moviment> nouCami = new ArrayList<>(actual.cami);
                        nouCami.add(mov);

                        pila.push(new Node(seguent, nouCami, novaProfunditat, actual));
                    } else {
                        resultat.incNodesTallats(); 
                    }

                } catch (IllegalArgumentException e) {
                    resultat.incNodesTallats();
                }
            }
            
            resultat.updateMemoria(pila.size() + visitatsLNT.size());
        }

        return false; 
    }
    
    
    /**
     * Realitza una cerca en profunditat limitada (DLS)
     * USANT CONTROL DE CICLES EN BRANCA (Path checking).
     */
    private boolean cercaLimitadaPath(Mapa mapaInicial, ResultatCerca resultat, int limit) {
        Deque<Node> pila = new ArrayDeque<>();
        
        pila.push(new Node(mapaInicial, new ArrayList<>(), 0, null));
        resultat.updateMemoria(pila.size());

        while (!pila.isEmpty()) {
            Node actual = pila.pop();
            Mapa mapa = actual.mapa;
            int profunditat = actual.profunditat;
            resultat.incNodesExplorats();

            // 1. COMPROVAR META
            if (mapa.esMeta()) {
                resultat.setCami(actual.cami);
                return true;
            }

            // 2. COMPROVAR LÍMIT
            if (profunditat >= limit) {
                 resultat.incNodesTallats();
                continue;
            }

            // 3. EXPANDIR
            for (Moviment mov : mapa.getAccionsPossibles()) {
                try {
                    Mapa seguent = mapa.mou(mov);

                    if (!estaEnCami(actual, seguent)) {
                        List<Moviment> nouCami = new ArrayList<>(actual.cami);
                        nouCami.add(mov);
                        pila.push(new Node(seguent, nouCami, profunditat + 1, actual));
                    } else {
                        resultat.incNodesTallats(); 
                    }

                } catch (IllegalArgumentException e) {
                    resultat.incNodesTallats();
                }
            }

            resultat.updateMemoria(pila.size());
        }

        return false; 
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