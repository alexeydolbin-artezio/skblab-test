package com.lab.skb

import com.lab.skb.dto.Filter
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.services.ServiceUnitTestMixin
import spock.lang.Specification

import javax.persistence.EntityNotFoundException

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

    void "find all countries without filter and pagination"() {
        given: "An empty filter object"
        def filter = new Filter()

        when: "find all entries is invoked"
        def result = service.findAll(filter)

        then: "all countries were returned"
        result.total == 4
        result.result.size() == 4
        result.result*.code.containsAll("RU", "RO", "DE", "PA")
    }

    void "find all countries with pagination"() {
        given: "The filter object with pagination"
        def filter = new Filter(offset: 1, limit: 2)

        when: "find all entries is invoked"
        def result = service.findAll(filter)

        then: "only PA and RU are returned but all countries are total"
        result.total == 4
        result.result.size() == 2
        result.result[0].code == "PA"
        result.result[1].code == "RO"
    }

    void "no countries found because pagination offset is exceeding"() {
        given: "The filter object offset value greater then number of countries"
        def filter = new Filter(offset: 100, limit: 10)

        when: "find all entries is invoked"
        def result = service.findAll(filter)

        then: "no countries in result"
        result.total == 4
        result.result.size() == 0
    }

    void "find countries with filter"() {
        given: "The filter object with search term"
        def filter = new Filter(searchTerm: "r")

        when: "find all entries is invoked"
        def result = service.findAll(filter)

        then: "there are only countries that start with letter 'r'"
        result.total == 2
        result.result.size() == 2
        result.result[0].name == "Румыния"
        result.result[1].name == "Россия"
    }

    void "find country by primary key"() {
        given: "Existing country primary key"
        def id = 4

        when: "find country by id is invoked"
        def result = service.getById(id)

        then: "country is returned"
        result.code == "RU"
        result.name == "Россия"
    }

    void "request country by id that doesn't exist result in exception"() {
        given: "Non-existing country primary key"
        def id = 9999

        when: "find country by id is invoked"
        service.getById(id)

        then: "exception is thrown"
        thrown EntityNotFoundException
    }

    void "find country by code"() {
        given: "Existing country code"
        def code = "RU"

        when: "find country by code param is invoked"
        def country = service.getByParam("code", code)

        then: "country was found"
        country.code == code
        country.name == "Россия"
    }

    void "find country by name"() {
        given: "Existing country name"
        def name = "Россия"

        when: "find country by name param is invoked"
        def country = service.getByParam("name", name)

        then: "country was found"
        country.code == "RU"
        country.name == name
    }

    void "no country found result in exception"() {
        given: "Non-existing country code"
        def code = "ZZ"

        when: "find country by code is invoked"
        service.getByParam("code", code)

        then: "exception is thrown"
        thrown EntityNotFoundException
    }
}
