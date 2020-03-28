package com.lab.skb

import com.lab.skb.dto.CountryFilterResult
import com.lab.skb.dto.Filter
import grails.transaction.Transactional
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.beans.factory.annotation.Value
import org.springframework.validation.annotation.Validated

import javax.persistence.EntityNotFoundException
import javax.validation.constraints.NotNull

@Validated
@Transactional(readOnly = true)
class CountryService {

    @Value('${country.service.limit:10}')
    private Integer defaultLimit;

    @Value('${country.service.offset:0}')
    private Integer defaultOffset;

    /**
     * Возвращает фильтрованный список стран. По умолчанию возвращаются первые 10 стран с сортировкой по коду без фильтрации.
     * Если параметр filter является null, то возвращаются все записи справочника стран.
     *
     * @param filter - паджинация и фильтр. Может быть null.
     * @return список стран из справочника с применением фильтра и общее количество подпадающих под фильтр записей.
     */
    CountryFilterResult findAll(Filter filter) {
        log.debug "Received search request with filter=$filter"
        if (!filter) {
            return new CountryFilterResult(result: Country.list(), total: Country.count)
        }

        if (!filter.limit || filter.limit <= 0) {
            filter.limit = defaultLimit
        }
        if (!filter.offset || filter.offset <= 0) {
            filter.offset = defaultOffset
        }

        def criteriaBuilder = Country.createCriteria()
        List<Country> filteredCountries = criteriaBuilder.list(max: filter.limit, offset: filter.offset) {
            if (filter.searchTerm) {
                or {
                    ilike("code", "${filter.searchTerm}%")
                    ilike("name", "${filter.searchTerm}%")
                }
            }
            order("code", "asc")
        }
        log.debug "The following countries were found=$filteredCountries using filter=$filter"
        return new CountryFilterResult(result: filteredCountries, total: filteredCountries.totalCount)
    }

    /**
     * @param id - первичный ключ справочника стран.
     * @return найденную запись справочника стран.
     * @throws EntityNotFoundException если такой записи не существует.
     */
    Country getById(@NotNull(message = "Country ID cannot be null") Long id) {
        log.debug "Country with id=$id was requested"
        def country = Optional.ofNullable(Country.findById(id)).orElseThrow {
            new EntityNotFoundException("Country with id=${id} was not found")
        }
        log.debug "Country=$country was found by id=$id"
        return country
    }

    /**
     * Возвращает страну из справочника по запросу поиска.
     *
     * @param fieldKey - наименование колонки справочника стран в нижнем регистре.
     * @param fieldValue - значение для поиска по колонке fieldKey в учетом регистра.
     * @return найденную запись из справочника стран.
     * @throws EntityNotFoundException если запрашиваемой страны нет в справочнике.
     * @throws IllegalArgumentException если справочник стран не содержит поля fieldKey.
     */
    Country getByParam(@NotEmpty(message = "Country field key cannot be empty") String fieldKey,
                       @NotEmpty(message = "Country field value cannot be empty") String fieldValue) {
        log.debug "Received search count request by key=$fieldKey with value=$fieldValue"
        def countryProperties = Country.metaClass.properties*.name
        if (!countryProperties.contains(fieldKey)) {
            throw new IllegalArgumentException("No property exists in Country with key=${fieldKey}")
        }
        Country country = Country.createCriteria().get {
            eq(fieldKey, castFieldValueParameter(fieldKey, fieldValue))
        }
        if (!country) {
            throw new EntityNotFoundException("Country was not found by field=${fieldKey} with value=${fieldValue}")
        }
        log.debug "Country=$country was found by key=$fieldValue with value=$fieldValue"
        return country
    }

    private Object castFieldValueParameter(String fieldKey, String fieldValue) {
        if ("id".equalsIgnoreCase(fieldKey)) {
            return Long.parseLong(fieldValue)
        }
        return fieldValue
    }
}
