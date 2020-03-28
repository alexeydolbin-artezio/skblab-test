package com.lab.skb

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.services.ServiceUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(ServiceUnitTestMixin)
@TestFor(CountryService)
@Mock(Country)
class CountryServiceSpec extends Specification {

    def setup() {
        new Country(id: 1, code: "DE", name: "Германия").save()
        new Country(id: 2, code: "PA", name: "Панама").save()
        new Country(id: 3, code: "RO", name: "Румыния").save()
        new Country(id: 4, code: "RU", name: "Россия").save()
        Country.findAll() // necessary for requests to work. Transaction issues?
    }

    def cleanup() {
    }

    void "save new countries"() {
        given: "A new list of countries"
        def newCountries = [new Country(code: "TN", name: "Тунис"), new Country(code: "GB", name: "Великобритания")]

        when: "save action for new countries is performed"
        service.bulkUpdate(newCountries)

        then: "all new countries are saved"
        Country.count == 6
        Country.findAll()*.code.containsAll(["DE", "PA", "RO", "RU", "TN", "GB"])
    }

    void "save new countries and update existing ones"() {
        given: "A new list of countries"
        def newCountries = [new Country(code: "TN", name: "Тунис"), new Country(code: "PA", name: "ПанамаПанамаПанама")]

        when: "save action for countries is performed"
        service.bulkUpdate(newCountries)

        then: "all new countries are saved and existing are updated"
        Country.count == 5
        Country.findAll()*.code.containsAll(["DE", "PA", "RO", "RU", "TN"])
        Country.findByCode("PA").name == "ПанамаПанамаПанама"
    }
}
