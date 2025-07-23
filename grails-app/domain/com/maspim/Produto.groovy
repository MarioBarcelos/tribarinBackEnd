package com.maspim

class Produto {

    String nome
    String codBarras
    int valor
    boolean enabled = true

    static constraints = {
        nome blank: false, nullable: true
        codBarras blank: false, nullable: true
        valor blank: false, nullable: true
    }
}
