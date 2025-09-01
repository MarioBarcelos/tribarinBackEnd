package tribarin.security

import com.maspim.JWTService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService

    JwtAuthenticationFilter(JWTService jwtService) {
        this.jwtService = jwtService
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String authHeader = request.getHeader("Authorization")
        if (authHeader?.startsWith("Bearer ")) {
            String token = authHeader.substring(7)
            def claims = jwtService.decodeToken(token)
            if (claims) {
                def authorities = [new SimpleGrantedAuthority("ROLE_" + (claims.role ?: "USER"))]
                def authentication = new UsernamePasswordAuthenticationToken(claims.username, null, authorities)
                SecurityContextHolder.context.authentication = authentication
            }
        }
        filterChain.doFilter(request, response)
    }
}