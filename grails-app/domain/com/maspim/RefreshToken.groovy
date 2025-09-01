package com.maspim

import grails.gorm.annotation.Entity

@Entity
class RefreshToken {
    String token
    String username
    Date expiration

    static constraints = {
        token (unique: true, maxSize: 2048, type: 'text')
        username nullable: false
        expiration nullable: false
    }
}