package maspim

import grails.converters.JSON

class RestExceptionHandlerController {
    static responseFormats = ['json']

    def error() {
        def status = response.status ?: 500
        def message = request?.exception?.message ?: 'Erro desconhecido'
        render([error: message, status: status] as JSON)
    }
}