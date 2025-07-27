package com.maspim

import com.auth0.jwt.exceptions.JWTVerificationException
import grails.gorm.transactions.Transactional
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import grails.plugin.springsecurity.SpringSecurityService
import grails.util.Holders
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder

@Transactional
@Service
class JWTService {

    SpringSecurityService springSecurityService

    private Algorithm ALGORITHM
    long JWT_EXPIRATION
    private String _claimKey
    JWTService() {
        _claimKey = 'principal'
        JWT_EXPIRATION = Holders.config.getProperty('jwt.expiration', Long, 1800000L)
        ALGORITHM = Algorithm.HMAC256("${Holders.config.getProperty('jwt.secret')}")
    }

    String gerarToken(Map payLoad) {
        Date now = new Date()
        Date expirationDate = new Date(now.getTime() + JWT_EXPIRATION)
        String jsonString = new JsonBuilder(payLoad).toString()
        String token = JWT.create()
                .withSubject(payLoad.username as String ?: '')
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .withClaim(_claimKey, jsonString)
                .sign(ALGORITHM)
        return token
    }

    String getToken() {
        GrailsWebRequest webRequest = RequestContextHolder.currentRequestAttributes()
        def request = webRequest.request as HttpServletRequest
        String authHeader = request.getHeader('Authorization')
        return authHeader?.replace('Bearer', '')?.trim() ?: ''
    }

    def decodeToken(String token) {
        if (!token) return null
        try {
            DecodedJWT jwt = JWT.require(ALGORITHM).build().verify(token)
            Claim payload = jwt.getClaim(_claimKey)
            return new JsonSlurper().parseText(payload.asString())
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            log.warn("Token expirado: ${e.message}")
            return null
        } catch (com.auth0.jwt.exceptions.JWTVerificationException e) {
            log.warn("Erro de verificação JWT: ${e.message}")
            return null
        } catch (Exception e) {
            log.error("Erro inesperado ao decodificar token: ${e.message}", e)
            return null
        }
    }
}