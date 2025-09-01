// grails-app/jobs/com/maspim/RefreshTokenCleanupJob.groovy

package com.maspim

import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j

@Slf4j
class RefreshTokenCleanupJob {

    static triggers = {
        cron name: 'refreshTokenCleanupTrigger', cronExpression: "0 0/30 * * * ?"
    }

    def refreshTokenService

    @Transactional
    def execute() {
        def now = new Date()
        def tokensExpirados = RefreshToken.where {
            expiration < now
        }.list()

        if (tokensExpirados) {
            tokensExpirados.each { it.delete(flush: true) }
            log.warn "Limpou ${tokensExpirados.size()} refresh_tokens expirados em ${now.format('HH:mm:ss')}"
        } else {
            log.info "Nenhum refresh_token expirado encontrado em ${now.format('HH:mm:ss')}"
        }
    }
}