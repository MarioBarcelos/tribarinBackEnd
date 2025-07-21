package com.maspim

class Atendimento {

    String nome
    String cpf
    boolean enabled = true

    static constraints = {
        nome blank: false
        cpf blank: false
    }
}
