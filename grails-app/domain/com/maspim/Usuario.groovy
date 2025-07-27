package com.maspim

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class Usuario implements UserDetails {
    String id
    String username
    String password
    String cpf
    boolean enabled

    static constraints = {
        id unique: true
        username blank: false, unique: true
        password blank: false
        cpf blank: false, unique: true
    }

    static mapping = {
        password column: '`password`'
        id generator: 'assigned'
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        UsuarioRole.findAllByUsuario(this).collect { it.role } as Set
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return enabled
    }
}