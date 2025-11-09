(define (problema mapA)
  (:domain panys-claus)

  (:objects
    a1 a2 - agent
    clauA - clau
    portaA - porta
    c11 c12 c13 c14 c15 c16 c17 c18
    c21 c22 c23 c24 c25 c26 c27 c28
    c31 c32 c33 c34 c35 c36 c37 c38
    c41 c42 c43 c44 c45 c46 c47 c48
    - cel-la
  )

  (:init
    (es-a a2 c12)
    (es-a a1 c44)

    (clau-a clauA c22)

    (porta-a portaA c47)

    (sortida c26)

    (lliure c12)(lliure c13)(lliure c14)(lliure c15)
    (lliure c22)(lliure c23)(lliure c24)(lliure c25)
    (lliure c33)(lliure c34)(lliure c35)(lliure c36)
    (lliure c42)(lliure c43)(lliure c44)(lliure c46)
    (lliure c26)
    
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
    
    (adj c25 c26)(adj c26 c25)
    (adj c46 c47)(adj c47 c46)

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
      (es-a a1 c26)
      (es-a a2 c26)
    )
  )
)
