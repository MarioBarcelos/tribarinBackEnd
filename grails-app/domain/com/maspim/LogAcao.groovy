package com.maspim

class LogAcao {

    String verboUtilizado
    String rotaAcessada
    String request
    String response
    Date dtLog

    static mapping = {
        request type: 'text'
        response type: 'text'
    }

    static constraints = {
        verboUtilizado blank: false, nullable: true, maxSize: 256
        rotaAcessada blank: false, nullable: true, maxSize: 65535
        request blank: false, nullable: true, maxSize: 65535
        response blank: false, nullable: true, maxSize: 65535
        dtLog blank: false, nullable: true
    }
}
