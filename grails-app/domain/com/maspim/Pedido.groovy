package com.maspim

class Pedido {
    String id
    String numero
    int valor
    Date dtCompra
    String fase
    int cobrado
    boolean multiProdutos
    String produtos
    Produto produto
    Pagamento pagamento
    Usuario usuario

    static mapping = {id generator: 'assigned'}
    static constraints = {
        id unique: true
        numero blank: false, nullable: true
        valor blank: false, nullable: true, maxSize: 256
        dtCompra blank: false, nullable: true
        fase blank: false, nullable: true
        cobrado blank: false, nullable: true
        produtos blank: false, nullable: true, maxSize: 5000
        multiProdutos blank: false, nullable: true
    }
}
