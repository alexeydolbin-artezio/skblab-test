package com.lab.skb

import com.lab.skb.dto.Filter
import grails.converters.JSON
import grails.rest.RestfulController

class CountryController extends RestfulController {

    static responseFormats = ['json']

    static offsetQueryParam = 'offset'
    static limitQueryParam = 'limit'
    static searchTermQueryParam = 'searchTerm'

    static fieldKeyQueryParam = 'fieldKey'
    static fieldValueQueryParam = 'fieldValue'

    def countryService

    private static getApiVersion(String version) {
        if ("v1" == version || "v2" == version) {
            return version
        }
        return "v1"
    }

    /**
     * GET /api/v$/countries?offset=0&limit=10&searchTerm=ru
     * Получение справочника стран. Паджнация и фильтрация доступны через параметры запроса:
     * - offset - отступ от первой записи в списке
     * - limit - максимальное число возвращаемых записей
     * - searchTerm - поиск стран по коду и наименованию без учета регистра.
     * Версия API применяемая по умолчанию - v1.
     *
     * @param version - версия API. Доступные значения: [v1, v2].
     * @return справочник стран с примененным фильтром.
     */
    def index(String version) {
        def filter = new Filter(
                offset: params.getInt(offsetQueryParam),
                limit: params.getInt(limitQueryParam),
                searchTerm: params.get(searchTermQueryParam)
        )
        JSON.use(getApiVersion(version)) {
            respond countryService.findAll(filter)
        }
    }

    /**
     * GET /api/v$/countries/{id}* Получить страну из справочника по id.
     *
     * @param version - версия API. Доступные значения: [v1, v2].
     * @param id - первичный ключ страны.
     * @return запись из справочника стран или 404.
     */
    def show(String version, Long id) {
        JSON.use(getApiVersion(version)) {
            respond countryService.getById(id)
        }
    }

    /**
     * GET /api/v$/countries/find?fieldKey=code&fieldValue=RU.
     * Поиск страны из справочника по уникальному идентификатору, коду или наименованию.
     * Доступные значения для параметра запроса fieldKey: [id, code, name].
     *
     * @param version - версия API. Доступные значения: [v1, v2].
     * @return запись из справочника стран или 404.
     */
    def find(String version) {
        JSON.use(getApiVersion(version)) {
            respond countryService.getByParam(
                    (String) params.get(fieldKeyQueryParam),
                    (String) params.get(fieldValueQueryParam))
        }
    }
}
