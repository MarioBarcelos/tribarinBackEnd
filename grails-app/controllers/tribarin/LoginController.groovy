package tribarin

import com.maspim.JWTService
import com.maspim.Usuario
import grails.converters.JSON
import org.springframework.http.HttpStatus

class LoginController {
    static namespace = "api"
    static responseFormats = ['json', 'html']
    JWTService jwtService

    def autenticar() {
        try {
            def bodyRequest = request.JSON ?: params
            log.info("bodyRequest: ${bodyRequest}")

            if (!bodyRequest?.username) {
                log.info("Username inválido")
                response.status = HttpStatus.BAD_REQUEST.value()
                render([sucesso: false, mensagem: 'Username Inválido!'] as JSON)
                return
            }
            if (!bodyRequest?.password) {
                log.info("Senha inválida")
                response.status = HttpStatus.BAD_REQUEST.value()
                render([sucesso: false, mensagem: 'Senha Inválida!'] as JSON)
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
            log.info("Token gerado com sucesso")

            render([token: token, expiracao: System.currentTimeMillis() + jwtService.JWT_EXPIRATION] as JSON)
        } catch (Exception e) {
            log.error("Erro no auth: ${e.message}", e)
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            render([sucesso: false, mensagem: 'Erro interno', erro: e.message] as JSON)
        }
    }
}
