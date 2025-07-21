package com.maspim

class Pedido {

    String numero
    String valor
    boolean enabled = true

    static constraints = {
        numero blank: false
        valor blank: false
    }
}
