package edu.epsevg.prop.ac1.cerca.heuristica;

import java.util.List;

import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.model.Posicio;

public class HeuristicaAvancada implements Heuristica {

    @Override
    public int h(Mapa estat) {
        if (estat.esMeta()) return 0;

        List<Posicio> agents = estat.getAgents();
        Posicio sortida = estat.getSortida();

        int millor = Integer.MAX_VALUE;

        for (Posicio ag : agents) {
            int d = Math.abs(ag.x - sortida.x) + Math.abs(ag.y - sortida.y);
            if (d < millor) millor = d;
        }

        return millor;
    }
}
