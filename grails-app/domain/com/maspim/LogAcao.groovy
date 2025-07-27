package com.maspim

class LogAcao {
    String id

    String verboUtilizado
    String rotaAcessada
    String request
    String response
    Date dtLog

    static mapping = {
        id generator: 'assigned'
        request type: 'text'
        response type: 'text'
    }

    static constraints = {
        id unique: true
        verboUtilizado nullable: true, maxSize: 256
        rotaAcessada nullable: true, maxSize: 15535
        request nullable: true, maxSize: 15535
        response nullable: true, maxSize: 15535
        dtLog nullable: true
    }
}
