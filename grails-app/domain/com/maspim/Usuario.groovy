package com.maspim

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class Usuario implements UserDetails {

    String username
    String password
    boolean enabled = true

    static hasMany = [authorities: Role]

    static constraints = {
        username blank: false, unique: true
        password blank: false
    }

    static mapping = {
        password column: '`password`'
    }

    // Implementações necessárias do UserDetails
    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities
    }

    @Override
    boolean isAccountNonExpired() {
        return true // ou lógica para validar se a conta está expirada
    }

    @Override
    boolean isAccountNonLocked() {
        return true // ou lógica para validar se a conta está bloqueada
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true // ou lógica para validar se as credenciais expiraram
    }

    @Override
    boolean isEnabled() {
        return enabled
    }
}