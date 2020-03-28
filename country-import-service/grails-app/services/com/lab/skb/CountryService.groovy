package com.lab.skb

import grails.transaction.Transactional
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.validation.annotation.Validated

@Validated
@Transactional
class CountryService {

    /**
     * Обновляет список стран в таблице country. Если была передана запись с уже существующим кодом,
     * то новая не создается, а происходит ее обновление. Существующие записи, которые не были переданы в качестве
     * аргумента, не удаляются. Страны в которых отсутствует либо код, либо наименования игнорируются.
     *
     * @param countriesDictionary - список стран.
     */
    void bulkUpdate(@NotEmpty(message = "Countries dictionary cannot be empty") List<Country> countriesDictionary) {
        log.info "Received ${countriesDictionary.size()} country dictionary entries to update"
        def validCountryEntries = getValidCountryEntries(countriesDictionary)
        def countryCodes = validCountryEntries.collect { country -> country.code }
        Map<String, Country> existingCountriesMap = Country.findAllByCodeInList(countryCodes).collectEntries { [it.code, it] }

        def countriesToSave = validCountryEntries.collect { country ->
            if (existingCountriesMap.containsKey(country.code)) {
                def existingCountry = existingCountriesMap.get(country.code)
                if (existingCountry.name != country.name) {
                    existingCountry.name = country.name
                    return existingCountry
                }
            } else {
                return country
            }
        }.findAll { Objects.nonNull(it) }
        if (!countriesToSave.isEmpty()) {
            Country.saveAll(countriesToSave)
            log.info "Countries dictionary was updated successfully"
        }
    }

    private List<Country> getValidCountryEntries(List<Country> countriesDictionary) {
        return countriesDictionary.findAll { country ->
            boolean entryValid = !country.code?.isEmpty() && !country.name?.isEmpty()
            if (!entryValid) {
                log.warn "Invalid country record is ignored=$country"
            }
            return entryValid
        }
    }
}
