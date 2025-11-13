package edu.epsevg.prop.ac1.cerca.heuristica;

import java.util.List;

import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.model.Posicio;

/** 
 * Distància de Manhattan a la clau més propera 
 * (si queden per recollir) o a la sortida.
 */
public class HeuristicaBasica implements Heuristica {
    @Override
    public int h(Mapa estat) {

        // Si ja hem arribat a la meta, cost 0
        if (estat.esMeta()) return 0;

        List<Posicio> agents = estat.getAgents();
        List<Posicio> claus = estat.getClausPendents();
        Posicio sortida = estat.getSortida();

        // Si hi ha claus pendents: distància fins a la clau més propera (ignorant obstacles)
        if (!claus.isEmpty()) {
            int millor = Integer.MAX_VALUE;
            for (Posicio agent : agents) {
                for (Posicio clau : claus) {
                    int d = manhattan(agent, clau);
                    if (d < millor) millor = d;
                }
            }
            return millor;
        }

        // Si no hi ha claus pendents: distància de Manhattan de l'agent més proper a la sortida
        int millor = Integer.MAX_VALUE;
        for (Posicio agent : agents) {
            int d = manhattan(agent, sortida);
            if (d < millor) millor = d;
        }
        return millor;
    }

    private int manhattan(Posicio a, Posicio b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
}
