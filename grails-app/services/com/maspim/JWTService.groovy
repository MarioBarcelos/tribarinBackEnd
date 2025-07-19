package com.maspim

import grails.gorm.transactions.Transactional
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import grails.util.Holders
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Transactional
class JWTService {

    private Algorithm ALGORITHM
    private long JWT_EXPIRATION
    private String _claimKey
    private String _currentToken
    private HttpServletRequest _request
    private HttpServletResponse _response

    JWTService(HttpServlet request, HttpServlet response) {
        _request = request
        _response = response
        _currentToken = ''
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

        _currentToken = token
        return  _currentToken
    }

    String getToken () {
        String token = _request.getHeader('Authorization')?.replace('Bearer','')?.trim() ?: ''

        _currentToken = token
        return _currentToken
    }

    def decodeToken() {
        JsonSlurper json = new JsonSlurper()
        String token = getToken()

        if (!token) return [sucesso: false, token: token]

        try {
            DecodedJWT jwt = JWT.require(ALGORITHM)
                    .build()
                    .verify(token)

            Claim payload = jwt.getClaim(_claimKey)
            return json.parseText(payload.asString())
        } catch (Exception e) {
            log.error("Tipo de Erro: ${e.class}, Mesage: ${e.message}")
        }

        return [sucesso: true, token: token]
    }
}
