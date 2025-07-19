package vendas

import com.maspim.JWTService
import com.maspim.Usuario
import grails.converters.JSON
import org.springframework.http.HttpStatus

class AuthController {

    JWTService jwtService

    def authLogin = {
        def bodyRequest = request.JSON

        if (!bodyRequest?.username) {
            response.status = HttpStatus.BAD_REQUEST.value()
            render ([sucesso: false, mensagem: 'Username Inválido!'] as JSON)
            return
        }
        if (!bodyRequest?.password) {
            response.status = HttpStatus.BAD_REQUEST.value()
            render ([sucesso: false, mensagem: 'Senha Inválida!'] as JSON)
            return
        }

        Usuario usuario = Usuario.findByUsername("${bodyRequest?.username}")
        if (!usuario || !usuario?.password?.equals("${bodyRequest?.password}")) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            render ([sucesso: false, mensagem: 'Credenciais Inválida!'] as JSON)
            return
        }

        Map payLoadToken = [
                id: usuario?.id,
                username: usuario?.username,
                role: usuario?.role
        ]

        String token = jwtService.generateToken(payLoadToken)

        render ([token: token, expiracao: System.currentTimeMillis() + jwtService.JWT_EXPIRATION] as JSON)
    }
}
