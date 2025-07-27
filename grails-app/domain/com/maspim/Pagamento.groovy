package com.maspim

class Pagamento {
    String id
    String nossoNumero
    String cpfPagador
    String nomePagador
    Date dtPagamento
    boolean enabled = true

    static mapping = {id generator: 'assigned'}
    static constraints = {
        id unique: true
        nossoNumero blank: false,nullable: true
        cpfPagador blank: false, nullable: true
        nomePagador blank: false, nullable: true
        dtPagamento blank: false
    }
}
