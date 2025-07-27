package com.maspim

class Produto {
    String id
    String nome
    String codBarras
    int valor
    boolean enabled = true

    static mapping = {id generator: 'assigned'}
    static constraints = {
        id unique: true
        nome blank: false, nullable: true
        codBarras blank: false, nullable: true
        valor blank: false, nullable: true
    }
}
