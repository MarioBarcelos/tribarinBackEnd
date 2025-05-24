package vendas

import grails.converters.JSON

class AtendimentoController {

    def utilidadesDoLar = {
        render(view: "utilidadesDoLar", model: [nome: "Tribarin"], layout: "main")
    }

    def validarNovoUsuario = {
        def retorno = [:]
        def params = request.JSON
        log.info("Iniciando validação de novo Usuário")
        def logFim = { "Concluindo validação, resultado: ${retorno.mensagem}" }

        if (!params.username || !params.senha) {
            retorno.codigo = 400
            retorno.mensagem = "Por favor, verifique suas informações de Acesso e tente novamente"
            render retorno as JSON
            log.error(logFim())
            return
        }
    }
}
