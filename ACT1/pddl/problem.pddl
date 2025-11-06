(define (problem mapA)
  (:domain locks-keys)

  (:objects
    a1 a2 - agent
    keyA - key
    doorA - door
    c11 c12 c13 c14 c15 c16 c17 c18
    c21 c22 c23 c24 c25 c26 c27 c28
    c31 c32 c33 c34 c35 c36 c37 c38
    c41 c42 c43 c44 c45 c46 c47 c48
    - cell
  )

  (:init
    ;; Agentes
    (at a2 c12)
    (at a1 c44)

    ;; Llave
    (key-at keyA c22)

    ;; Puerta
    (door-at doorA c47)

    ;; Salida
    (exit c26)

    ;; Celdas libres (NO paredes)
    (free c12)(free c13)(free c14)(free c15)
    (free c22)(free c23)(free c24)(free c25)
    (free c33)(free c34)(free c35)(free c36)
    (free c42)(free c43)(free c44)(free c46)

    ;; Adyacencias (solo libres)
    ;; fila 2
    (adj c12 c13)(adj c13 c12)
    (adj c13 c14)(adj c14 c13)
    (adj c14 c15)(adj c15 c14)
    (adj c22 c23)(adj c23 c22)
    (adj c23 c24)(adj c24 c23)
    (adj c24 c25)(adj c25 c24)
    (adj c33 c34)(adj c34 c33)
    (adj c34 c35)(adj c35 c34)
    (adj c35 c36)(adj c36 c35)
    (adj c42 c43)(adj c43 c42)
    (adj c43 c44)(adj c44 c43)
    (adj c44 c45)(adj c45 c44)
    (adj c45 c46)(adj c46 c45)

    ;; verticales
    (adj c12 c22)(adj c22 c12)
    (adj c13 c23)(adj c23 c13)
    (adj c14 c24)(adj c24 c14)
    (adj c15 c25)(adj c25 c15)
    (adj c33 c43)(adj c43 c33)
    (adj c34 c44)(adj c44 c34)
    (adj c35 c45)(adj c45 c35)
    (adj c36 c46)(adj c46 c36)
  )

  (:goal
    (or
      (at a1 c26)
      (at a2 c26)
    )
  )
)
