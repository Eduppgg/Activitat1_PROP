(define (domain panys-claus)
  (:requirements :typing :strips)
  (:types agent clau porta cel-la)

  (:predicates
    (es-a ?a - agent ?c - cel-la)
    (adj ?c1 - cel-la ?c2 - cel-la)
    (lliure ?c - cel-la)
    (sortida ?c - cel-la)
    (porta-a ?d - porta ?c - cel-la)
    (clau-a ?k - clau ?c - cel-la)
    (te ?a - agent ?k - clau)
    (porta-oberta ?d - porta)
  )

  (:action moure
    :parameters (?a - agent ?des-de - cel-la ?cap-a - cel-la)
    :precondition (and
        (es-a ?a ?des-de)
        (adj ?des-de ?cap-a)
        (lliure ?cap-a))
    :effect (and
        (not (es-a ?a ?des-de))
        (es-a ?a ?cap-a))
  )

  (:action agafar-clau
    :parameters (?a - agent ?k - clau ?c - cel-la)
    :precondition (and
        (es-a ?a ?c)
        (clau-a ?k ?c))
    :effect (and
        (te ?a ?k)
        (not (clau-a ?k ?c)))
  )

  (:action obrir-porta
    :parameters (?a - agent ?d - porta ?k - clau ?c - cel-la)
    :precondition (and
        (es-a ?a ?c)
        (porta-a ?d ?c)
        (te ?a ?k))
    :effect (and
        (porta-oberta ?d)
        (lliure ?c))
  )
)
