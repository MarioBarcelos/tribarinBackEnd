import grails.plugin.springsecurity.SecurityFilterPosition
import grails.plugin.springsecurity.SpringSecurityUtils

// Executado na inicialização
SpringSecurityUtils.clientRegisterFilter('restAuthenticationFilter', SecurityFilterPosition.FORM_LOGIN_FILTER.order - 1)

grails.plugin.springsecurity.filterChain.chainMap = [
        [pattern: '/api/login/**', filters: 'JOINED_FILTERS,-csrfFilter'],
        [pattern: '/api/**',       filters: 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-csrfFilter'],
        [pattern: '/**',           filters: 'JOINED_FILTERS']
]
