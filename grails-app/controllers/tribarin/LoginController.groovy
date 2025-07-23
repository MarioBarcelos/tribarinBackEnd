package tribarin

import com.maspim.JWTService
import com.maspim.LogAcaoService
import com.maspim.Usuario
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.http.HttpStatus

class LoginController {
    static namespace = "api"
    static responseFormats = ['json', 'html']
    JWTService jwtService
    LogAcaoService logAcaoService
    SpringSecurityService springSecurityService

    def autenticar() {
        Map bodyRequest = request.JSON ?: params
        try {
            if (!bodyRequest?.username || !bodyRequest?.password) {
                String mensagem = !bodyRequest?.username ? 'Username Inválido!' : 'Senha Inválida!'
                response.status = HttpStatus.BAD_REQUEST.value()
                render([codigo: 400, mensagem: mensagem] as JSON)
                return
            }

            Usuario usuario = Usuario.findByUsername("${bodyRequest?.username}")
            if (!usuario || !usuario?.password?.equals("${bodyRequest?.password}")) {
                response.status = HttpStatus.UNAUTHORIZED.value()
                render ([codigo: 400, mensagem: 'Credenciais Inválida!'] as JSON)
                return
            }

            Map payLoadToken = [id: usuario?.id, username: usuario?.username, role: usuario?.role]
            String token = jwtService.generateToken(payLoadToken)

            render([token: token, expiracao: System.currentTimeMillis() + jwtService.JWT_EXPIRATION] as JSON)
        } catch (Exception e) {
            log.warn("tipoErro: ${e.class},\nmesage: ${e.message},\ndadosRecebidos: ${bodyRequest}")
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            render([codigo: 400, mensagem: 'Erro ao analisar Usuário'] as JSON)
        } finally {
            logAcaoService.novoLog([
                verbo: request.method, rota: request.forwardURI,
                jsonLog: [request: request.JSON ?: request.queryString, response: response]
            ])
        }
    }
}
