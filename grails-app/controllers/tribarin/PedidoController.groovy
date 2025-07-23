package tribarin

import com.maspim.LogAcaoService
import com.maspim.PedidoService
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.http.HttpStatus


class PedidoController {

    PedidoService pedidoService
    LogAcaoService logAcaoService
    SpringSecurityService springSecurityService

    def novoPedido = {
        Map dadosRequest = request.JSON
        Arrays dadosAusente
        try {
            if (!dadosRequest) {
                response.status = HttpStatus.BAD_REQUEST.value()
                render([codigo: 400, mensagem: 'Não foi possível receber os dados da Requisição.'] as JSON)
                return
            }

            dadosRequest.each {key,value ->
                if (!value) dadosAusente << key.toUpperCase()
                if (key instanceof Arrays && !value) dadosAusente << key.toUpperCase()
            }

            if (dadosAusente.size() > 0) {
                response.status = HttpStatus.BAD_REQUEST.value()
                render([codigo: 400, mensagem: "Informe os seguintes dados e tente novamente. ${dadosAusente}"] as JSON)
                return
            }

            if (dadosAusente?.codBarras && dadosAusente?.codBarras?.size() == 1) {
                def salvarPedido = pedidoService.unicoProdutoEmNovoPedido(dadosRequest)
                if (salvarPedido.codigo == 200) {
                    render ([codigo: 200, mensagem: 'Compra Realizada com Sucesso!'] as JSON)
                    return
                } else {
                    render ([codigo: 400, mensagem: salvarPedido.mensagem] as JSON)
                    return
                }
            }

        } catch (Exception e) {
            String detalheErro = "tipoErro: ${e.class},\nmesage: ${e.message},\ndadosRecebidos: ${dadosRequest}"
            render ([codigo: 500, mensagem: 'Erro ao iniciar novo Pedido', detalhesErro: detalheErro] as JSON)
        } finally {
            logAcaoService.novoLog([
                    verbo: request.method, rota: request.forwardURI,
                    jsonLog: [request: request.JSON ?: request.queryString, response: response]
            ])
        }
    }
}
