(define (problem mapa-a-simple)
    (:domain panys-i-claus-simple)

    (:objects
        agent1 agent2 - agent
        clau-a - clau
        porta-A - porta
        
        c-1-1 c-1-2 c-1-3 c-1-4 c-1-5 c-1-6 - casella
        c-2-1 c-2-2 c-2-3 c-2-4 c-2-6 - casella
        c-3-1 c-3-2 c-3-3 c-3-4 c-3-5 c-3-6 - casella
    )

    (:init
        (clau-per-porta clau-a porta-A)

        (esta-a agent1 c-3-4)
        (esta-a agent2 c-1-2)

        (clau-a clau-a c-2-1)
        (porta-a porta-A c-3-6)
        (sortida-a c-2-6)

        (es-casella-normal c-1-1) (es-casella-normal c-1-3) (es-casella-normal c-1-4) (es-casella-normal c-1-5) (es-casella-normal c-1-6)
        (es-casella-normal c-2-2) (es-casella-normal c-2-3) (es-casella-normal c-2-4)
        (es-casella-normal c-3-1) (es-casella-normal c-3-2) (es-casella-normal c-3-3) (es-casella-normal c-3-5)

        (casella-buida c-1-1) (casella-buida c-1-3) (casella-buida c-1-4) (casella-buida c-1-5) (casella-buida c-1-6)
        (casella-buida c-2-1) (casella-buida c-2-2) (casella-buida c-2-3) (casella-buida c-2-4) (casella-buida c-2-6)
        (casella-buida c-3-1) (casella-buida c-3-2) (casella-buida c-3-3) (casella-buida c-3-5) (casella-buida c-3-6)

        (connectades c-1-1 c-1-2) (connectades c-1-2 c-1-1)
        (connectades c-1-2 c-1-3) (connectades c-1-3 c-1-2)
        (connectades c-1-3 c-1-4) (connectades c-1-4 c-1-3)
        (connectades c-1-4 c-1-5) (connectades c-1-5 c-1-4)
        (connectades c-1-5 c-1-6) (connectades c-1-6 c-1-5)

        (connectades c-2-1 c-2-2) (connectades c-2-2 c-2-1)
        (connectades c-2-2 c-2-3) (connectades c-2-3 c-2-2)
        (connectades c-2-3 c-2-4) (connectades c-2-4 c-2-3)

        (connectades c-3-1 c-3-2) (connectades c-3-2 c-3-1)
        (connectades c-3-2 c-3-3) (connectades c-3-3 c-3-2)
        (connectades c-3-3 c-3-4) (connectades c-3-4 c-3-3)
        (connectades c-3-4 c-3-5) (connectades c-3-5 c-3-4)
        (connectades c-3-5 c-3-6) (connectades c-3-6 c-3-5)

        (connectades c-1-1 c-2-1) (connectades c-2-1 c-1-1)
        (connectades c-2-1 c-3-1) (connectades c-3-1 c-2-1)

        (connectades c-1-2 c-2-2) (connectades c-2-2 c-1-2)
        (connectades c-2-2 c-3-2) (connectades c-3-2 c-2-2)

        (connectades c-1-3 c-2-3) (connectades c-2-3 c-1-3)
        (connectades c-2-3 c-3-3) (connectades c-3-3 c-2-3)

        (connectades c-1-4 c-2-4) (connectades c-2-4 c-1-4)
        (connectades c-2-4 c-3-4) (connectades c-3-4 c-2-4)

        (connectades c-1-6 c-2-6) (connectades c-2-6 c-1-6)
        (connectades c-2-6 c-3-6) (connectades c-3-6 c-2-6)
    )

    (:goal
        (or
            (esta-a agent1 c-2-6)
            (esta-a agent2 c-2-6)
        )
    )
)
