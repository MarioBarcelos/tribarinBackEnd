package com.maspim

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService

import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Transactional
class LogAcaoService {

    def springSecurityService = new SpringSecurityService()

    def novoLog(Map object) {
        try {
            LogAcao logAcao = new LogAcao()
            logAcao.id = UUID.randomUUID().toString()
            logAcao.dtLog = new Date()
            logAcao.verboUtilizado = object?.verbo ?: 'LOG_VERBO_NAO_RECEBIDO'
            logAcao.rotaAcessada = object?.rota ?: 'LOG_ROTA_NAO_RECEBIDA'
            logAcao.request = object?.jsonLog?.request ?: 'LOG_REQUEST_NAO_RECEBIDO'
            logAcao.response = object?.jsonLog?.response ?: 'LOG_RESPONSE_NAO_RECEBIDO'
            logAcao.save(flush: true)
            if (!logAcao.save(flush: true)) {
                log.error("[novoLog] - Erro ao Salvar!\n${logAcao.errors.allErrors}")
            }
        } catch (Exception e) {
            log.error("[novoLog] - Erro ao Salvar!\nTipo Erro: ${e.class}\nMesage: ${e.message}")
        } finally {
            log.info("[novoLog]")
        }
    }
}
