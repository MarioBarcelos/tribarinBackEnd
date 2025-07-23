package com.maspim

import grails.gorm.transactions.Transactional
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import grails.util.Holders
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.stereotype.Service

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder

@Transactional
@Service
class JWTService {

    def springSecurityService = new SpringSecurityService()

    private Algorithm ALGORITHM
    long JWT_EXPIRATION
    private String _claimKey
    JWTService() {
        _claimKey = 'principal'
        JWT_EXPIRATION = Holders.config.getProperty('jwt.expiration', Long, 1800000L)
        ALGORITHM = Algorithm.HMAC256("${Holders.config.getProperty('jwt.secret')}")
    }

    String generateToken(Map sender) {
        Date now = new Date()
        Date expirationDate = new Date(now.getTime() + JWT_EXPIRATION)

        String jsonString = new JsonBuilder(sender).toString()
        String token = JWT.create()
                .withSubject('')
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
        } catch (Exception e) {
            log.error("Erro ao decodificar token: ${e.message}", e)
            return null
        }
    }
}