package com.maspim

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService

@Transactional
class PedidoService {

    SpringSecurityService springSecurityService

    def numeroPedido() {
        def anoMes = new Date().format('yyyyMM')
        def sufixo = String.format('%09d', new Random().nextInt(10000))
        return "${anoMes}${sufixo}"
    }

    def unicoProdutoEmNovoPedido(Map object) {
        try {
            Usuario usuario = Usuario.findByUsername("${object?.username?.trim()}")
            Produto produto = Produto.findByCodBarras("${object?.codBarras?.trim()}")

            if (produto.valor != object?.valorPedido?.trim()?.toInteger()) {
                return [codigo: 400, mensagem: 'Valor do Pedido diverge do registrado no Sistema.']
            }

            Pedido pedido = new Pedido()
            pedido.id = UUID.randomUUID().toString()
            pedido.numero = numeroPedido()
            pedido.valor = produto.valor
            pedido.dtCompra = new Date()
            pedido.fase = "0"
            pedido.cobrado = 0
            pedido.usuario = usuario
            pedido.multiProdutos = false
            pedido.produto = produto
            pedido.save(flush: true)
            if (pedido.errors.getAllErrors().size() == 0) {
                return [codigo: 200]
            } else {
                return [codigo: 400, mensagem: 'Erro ao salvar sua Compra conosco.']
            }

        } catch (Exception e) {
            log.error("tipoErro: ${e.class},\nmessage: ${e.message},\ndadosRecebidos: ${object}")
            return [codigo: 400, mensagem: 'Erro ao iniciar novo Pedido']
        }
    }

    def multiProdutosEmNovoPeido(Map object) {
        try {

        } catch (Exception e) {

        }
    }
}
