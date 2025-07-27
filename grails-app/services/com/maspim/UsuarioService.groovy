// grails-app/services/com/maspim/UsuarioService.groovy
package com.maspim

import grails.gorm.transactions.Transactional
import org.springframework.security.crypto.password.PasswordEncoder

import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Transactional
class UsuarioService {

    PasswordEncoder passwordEncoder
    LogAcaoService logAcaoService

    Usuario novoUsuario(String username, String password, String cpf, HttpRequest request) {
        if (Usuario.findByUsername(username)) {
            throw new IllegalArgumentException("Nome de usuário já existe: ${username}")
        }

        def usuario = new Usuario(username: username, password: passwordEncoder.encode(password), cpf: cpf, enabled: true)
        if (!usuario.save(flush: true)) {
            usuario.errors.allErrors.each {
                println it
            }
            throw new RuntimeException("Erro ao salvar usuário: ${usuario.errors}")
        }

        logAcaoService.novoLog([verbo:request.method,rota:request.forwardURI,request:request.JSON,response:usuario])
        return usuario
    }
}