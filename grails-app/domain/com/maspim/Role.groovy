package com.maspim

import org.springframework.security.core.GrantedAuthority

class Role implements GrantedAuthority {
    String id
    String authority
    String nome

    static mapping = {id generator: 'assigned'}
    static constraints = {
        id unique: true
        authority blank: false, unique: true
        nome blank: false, nullable: true
    }

    @Override
    String getAuthority() {
        return authority
    }
}
