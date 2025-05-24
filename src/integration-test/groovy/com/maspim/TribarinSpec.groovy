package com.maspim

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import geb.spock.*

/**
 * See https://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class TribarinSpec extends GebSpec {

    // Verifica se o ambiente não é 'production'
    void "test something"() {
        if (grails.env != 'production') {
            when: "The home page is visited"
            go '/'

            then: "The title is correct"
            title == "Welcome to Grails"
        } else {
            println "Test skipped in production environment"
        }
    }
}
