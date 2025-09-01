package tribarin

import com.maspim.*
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import grails.util.Holders as HD
import utils.exception.TribarinException

class LoginController {
    LogAcaoService logAcaoService
    PasswordEncoder passwordEncoder
    JWTService jwtService = HD.grailsApplication.mainContext.getBean(JWTService)

    static namespace = "api"
    static responseFormats = ['json', 'html']

    def autenticar() {
        Map bodyRequest = request.JSON ?: params
        try {
            if (!bodyRequest?.username || !bodyRequest?.password) {
                response.status = HttpStatus.BAD_REQUEST.value()
                throw new TribarinException(!bodyRequest?.username ? 'Username Inválido!' : 'Senha Inválida!',HttpStatus.BAD_REQUEST.value())
            }

            Usuario usuario = Usuario.findByUsername("${bodyRequest?.username}")
            if (!usuario || !passwordEncoder.matches(bodyRequest?.password as String, usuario?.password)) {
                response.status = HttpStatus.UNAUTHORIZED.value()
                throw new TribarinException('Credencial Inválida!',HttpStatus.UNAUTHORIZED.value())
            }

            Map payLoadToken = [id: usuario?.id, username: usuario?.username, role: usuario?.authorities?.nome]
            String accessToken = jwtService.gerarToken(payLoadToken)
            String refreshToken = jwtService.gerarRefreshToken(payLoadToken)
            new RefreshToken(
                    token: refreshToken,
                    username: usuario.username,
                    expiration: new Date(System.currentTimeMillis() + 86400000)
            ).save(flush: true)

            def cookie = new javax.servlet.http.Cookie("refresh_token", refreshToken)
            cookie.setHttpOnly(true)
            cookie.setSecure(true)
            cookie.setPath("/")
            cookie.setMaxAge(60 * 60 * 24)
            response.addCookie(cookie)

            render([access_token: accessToken, expiracao: System.currentTimeMillis() + jwtService.JWT_EXPIRATION] as JSON)

        } catch (TribarinException te) {
            render([codigo: te.getCodigo(), mensagem: te.getMensagem()] as JSON)

        } catch (Exception e) {
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            render([codigo: HttpStatus.INTERNAL_SERVER_ERROR.value(), mensagem: 'Erro ao analisar Usuário'] as JSON)
            e.printStackTrace()
        } finally {
            logAcaoService.novoLog([verbo: request.method, rota: request.forwardURI,
                jsonLog: [request: request.JSON ?: request.queryString, response: response]
            ])
        }
    }

    def refresh() {
        try {
            def cookies = request.getCookies()
            String refreshToken = cookies?.find { it.name == 'refresh_token' }?.value

            def tokenRecord = RefreshToken.findByToken(refreshToken)
            if (!tokenRecord) throw new TribarinException('Refresh token inválido ou expirado', HttpStatus.UNAUTHORIZED.value())

            def claims = jwtService.decodeToken(refreshToken)
            if (!claims) throw new TribarinException('Refresh token inválido ou expirado', HttpStatus.UNAUTHORIZED.value())

            Map payLoad = [id: claims.id, username: claims.username, role: claims.role]
            def novoAccessToken = jwtService.gerarToken(payLoad)

            render([access_token: novoAccessToken] as JSON)

        } catch (TribarinException te) {
            render([codigo: te.getCodigo(), mensagem: te.getMensagem()] as JSON)
        } catch (Exception e) {
            render([codigo: HttpStatus.INTERNAL_SERVER_ERROR.value(), mensagem: 'Erro Interno no Servidor'] as JSON)
        }
    }

    def logout() {
        try {
            def cookies = request.getCookies()
            String refreshToken = cookies?.find { it.name == 'refresh_token' }?.value

            if (refreshToken) RefreshToken.findByToken(refreshToken)?.delete(flush: true)

            def expiredCookie = new javax.servlet.http.Cookie("refresh_token", null)
            expiredCookie.setHttpOnly(true)
            expiredCookie.setSecure(true)
            expiredCookie.setPath("/")
            expiredCookie.setMaxAge(0)
            response.addCookie(expiredCookie)

            render([mensagem: "Logout realizado com sucesso"] as JSON)

        } catch (Exception e) {
            render([codigo: HttpStatus.INTERNAL_SERVER_ERROR.value(), mensagem: 'Erro ao realizar logout'] as JSON)
        }
    }
}
