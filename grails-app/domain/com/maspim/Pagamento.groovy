package com.maspim

class Pagamento {

    String nossoNumero
    String cpfPagador
    String nomePagador
    Date dtPagamento
    boolean enabled = true

    static constraints = {
        nossoNumero blank: false,nullable: true
        cpfPagador blank: false, nullable: true
        nomePagador blank: false, nullable: true
        dtPagamento blank: false
    }
}
