package com.maspim

import org.springframework.security.core.GrantedAuthority

class Role implements GrantedAuthority {
    String authority

    static constraints = {
        authority blank: false, unique: true
    }

    @Override
    String getAuthority() {
        return authority
    }
}
