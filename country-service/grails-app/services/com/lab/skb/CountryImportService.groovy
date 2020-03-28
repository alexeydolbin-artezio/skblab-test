package com.lab.skb

import com.lab.skb.exception.BadRequestException
import grails.transaction.Transactional
import org.apache.commons.lang.StringUtils
import org.springframework.web.multipart.commons.CommonsMultipartFile

@Transactional(readOnly = true)
class CountryImportService {

    private static maxFileSize = 1024 * 1024 // 1MB

    private static csvContentType = "text/csv"

    def messageSenderService

    /**
     * Отправляет справочник стран в country-import-service. Предварительно выполняются проверки:
     * - файл не пустой
     * - файл формата csv
     * - содержит как минимум 1 запись, не считая заголовка
     * - объем файла не превышает 1МБ.
     *
     * @param dictionaryCsv - файл со справочником стран.
     */
    void sendCountriesDictionaryData(CommonsMultipartFile dictionaryCsv) {
        if (dictionaryCsv?.empty) {
            throw new IllegalArgumentException("Dictionary CSV file cannot be empty")
        }
        if (csvContentType != dictionaryCsv.contentType) {
            throw new IllegalArgumentException("Dictionary should be csv file")
        }
        if (dictionaryCsv.size > maxFileSize) {
            throw new BadRequestException("Dictionary CSV file exceeds file limit $maxFileSize")
        }
        log.info "Received countries dictionary to update=${dictionaryCsv.name}"
        sendCountriesDictionaryDataInternal(dictionaryCsv)
        log.info("Countries dictionary was successfully sent")
    }

    private void sendCountriesDictionaryDataInternal(CommonsMultipartFile dictionaryCsv) {
        try {
            def countriesData = parseCountriesCsv(dictionaryCsv)
            messageSenderService.sendCountriesDictionary(countriesData)
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to send countries dictionary data", ex)
        }
    }

    private String parseCountriesCsv(CommonsMultipartFile dictionaryCsv) {
        def countryLines = []
        dictionaryCsv.inputStream.eachLine {
            def countryEntryStr = it.trim()
            if (StringUtils.isNotBlank(countryEntryStr)) {
                String[] countryLine = countryEntryStr.split(';')
                if (countryLine.length != 2 || countryLine[0]?.isEmpty() || countryLine[1]?.isEmpty()) {
                    throw new IllegalArgumentException("Country dictionary entry illegal format=$countryLine")
                }
                countryLines += countryLine[0] + ";" + countryLine[1].replaceAll('\"', StringUtils.EMPTY)
            }
        }
        if (countryLines.size() < 2) {
            throw new IllegalArgumentException("Countries dictionary CSV file is empty")
        }
        return countryLines.join("\n")
    }
}
