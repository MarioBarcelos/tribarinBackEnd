package com.maspim

class Produto {

    String nome
    String codBarras
    boolean enabled = true

    static constraints = {
        nome blank: false
        codBarras blank: false
    }
}
