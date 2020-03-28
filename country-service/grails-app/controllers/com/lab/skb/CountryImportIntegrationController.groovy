package com.lab.skb

import grails.rest.RestfulController

class CountryImportIntegrationController extends RestfulController {

    static fileParam = 'dictionary'

    def countryImportService

    /**
     * POST /api/v1/countries/import - Content-Type: multipart/form-data
     * Передача справочника стран (формат csv) в сервис country-import-service. Справочник должен быть передан
     * по ключу 'dictionary' и иметь следующий формат:
     * <pre>
     * <code>code;name
     * AF;"Афганистан"
     * AL;"Албания"
     * AQ;"Антарктида"
     * <...>
     * </code>
     * </pre>
     *
     * @return 200 если сообщение отправлено успешно и 500 в ином случае.
     */
    def index() {
        def dictionaryCsv = request.getFile(fileParam)
        countryImportService.sendCountriesDictionaryData(dictionaryCsv)
        respond status: 200
    }
}
