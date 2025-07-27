package com.maspim

class Atendimento {
    String id
    String nome
    String cpf
    boolean enabled = true

    static mapping = { id generator: 'assigned' }
    static constraints = {
        id unique: true
        nome blank: false
        cpf blank: false
    }
}
