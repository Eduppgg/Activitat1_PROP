(define (domain locks-keys)
  (:requirements :typing :strips)
  (:types agent key door cell)

  (:predicates
    (at ?a - agent ?c - cell)
    (adj ?c1 - cell ?c2 - cell)
    (free ?c - cell)
    (exit ?c - cell)
    (door-at ?d - door ?c - cell)
    (key-at ?k - key ?c - cell)
    (has ?a - agent ?k - key)
    (door-open ?d - door)
  )

  ;; Moverse a una celda libre
  (:action move
    :parameters (?a - agent ?from - cell ?to - cell)
    :precondition (and
        (at ?a ?from)
        (adj ?from ?to)
        (free ?to))
    :effect (and
        (not (at ?a ?from))
        (at ?a ?to))
  )

  ;; Recoger una llave
  (:action pick-key
    :parameters (?a - agent ?k - key ?c - cell)
    :precondition (and
        (at ?a ?c)
        (key-at ?k ?c))
    :effect (and
        (has ?a ?k)
        (not (key-at ?k ?c)))
  )

  ;; Abrir puerta
  (:action open-door
    :parameters (?a - agent ?d - door ?k - key ?c - cell)
    :precondition (and
        (at ?a ?c)
        (door-at ?d ?c)
        (has ?a ?k))
    :effect (and
        (door-open ?d)
        (free ?c)) ;; La puerta ahora permite pasar
  )
)
