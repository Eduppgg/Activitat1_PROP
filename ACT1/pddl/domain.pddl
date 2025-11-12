(define (domain panys-i-claus-simple)
    (:requirements :strips :typing)

    (:types
        agent casella clau porta - objecte
    )

    (:predicates
        (esta-a ?a - agent ?ca - casella)
        (clau-a ?c - clau ?ca - casella)
        (porta-a ?p - porta ?ca - casella)
        (sortida-a ?ca - casella)

        (casella-buida ?ca - casella)
        (te-clau ?c - clau) 

        (connectades ?ca1 - casella ?ca2 - casella)
        (clau-per-porta ?c - clau ?p - porta)
        
        (es-casella-normal ?ca - casella)
    )

    (:action moure-normal
        :parameters (?a - agent ?origen - casella ?desti - casella)
        :precondition (and
            (esta-a ?a ?origen)
            (connectades ?origen ?desti)
            (casella-buida ?desti)
            (es-casella-normal ?desti)
        )
        :effect (and
            (not (esta-a ?a ?origen))
            (esta-a ?a ?desti)
            (casella-buida ?origen)
            (not (casella-buida ?desti))
        )
    )

    (:action moure-i-agafar-clau
        :parameters (?a - agent ?origen - casella ?desti - casella ?c - clau)
        :precondition (and
            (esta-a ?a ?origen)
            (connectades ?origen ?desti)
            (casella-buida ?desti)
            (clau-a ?c ?desti)
        )
        :effect (and
            (not (esta-a ?a ?origen))
            (esta-a ?a ?desti)
            (casella-buida ?origen)
            (not (casella-buida ?desti))
            (te-clau ?c)
            (not (clau-a ?c ?desti))
            (es-casella-normal ?desti)
        )
    )

    (:action moure-per-porta
        :parameters (?a - agent ?origen - casella ?desti - casella ?p - porta ?c - clau)
        :precondition (and
            (esta-a ?a ?origen)
            (connectades ?origen ?desti)
            (casella-buida ?desti)
            (porta-a ?p ?desti)
            (clau-per-porta ?c ?p)
            (te-clau ?c)
        )
        :effect (and
            (not (esta-a ?a ?origen))
            (esta-a ?a ?desti)
            (casella-buida ?origen)
            (not (casella-buida ?desti))
            (es-casella-normal ?desti)
        )
    )
    
    (:action moure-a-sortida
        :parameters (?a - agent ?origen - casella ?desti - casella)
        :precondition (and
            (esta-a ?a ?origen)
            (connectades ?origen ?desti)
            (casella-buida ?desti)
            (sortida-a ?desti)
        )
        :effect (and
            (not (esta-a ?a ?origen))
            (esta-a ?a ?desti)
            (casella-buida ?origen)
            (not (casella-buida ?desti))
        )
    )
)
