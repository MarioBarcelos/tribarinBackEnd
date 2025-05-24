package vendas

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class AtendimentoControllerSpec extends Specification implements ControllerUnitTest<AtendimentoController> {

    def setup() {
    }

    def cleanup() {
    }

    void "test validarNovoUsuario sem parâmetros retorna erro 400"() {
        when: "A requisição é feita sem parâmetros"
        request.method = 'POST'
        controller.validarNovoUsuario()

        then: "O código de resposta deve ser 400"
        response.json.codigo == 400
        response.json.mensagem == "Por favor, verifique suas informações de Acesso e tente novamente"
    }
}
