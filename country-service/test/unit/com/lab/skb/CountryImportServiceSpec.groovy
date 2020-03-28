package com.lab.skb

import com.lab.skb.exception.BadRequestException
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.services.ServiceUnitTestMixin
import org.apache.commons.io.IOUtils
import org.springframework.web.multipart.commons.CommonsMultipartFile
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(ServiceUnitTestMixin)
@TestFor(CountryImportService)
class CountryImportServiceSpec extends Specification {

    static validDictionaryContent = "code;name\n" +
            "NA;\"Намибия\"\n" +
            "NR;\"Науру\"\n" +
            "NP;\"Непал\"\n" +
            "\n" +
            ""

    static malformedDictionaryContent = "code;name\n" +
            "NA;\"\"\n" +
            ";\"Науру\"\n" +
            "NP;\"Непал\"\n"

    static emptyDictionaryContent = "code;name\n"

    def messageSenderService

    CommonsMultipartFile dictionaryFile

    def setup() {
        messageSenderService = Mock(MessageSenderService)
        service.messageSenderService = messageSenderService

        dictionaryFile = Mock(CommonsMultipartFile)
    }

    def cleanup() {
    }

    void "verify send update country dictionary file is not csv"() {
        given: "Non-empty dictionary file with wrong content type"
        dictionaryFile.isEmpty() >> false
        dictionaryFile.getContentType() >> "application/json"

        when: "send dictionary invoked"
        service.sendCountriesDictionaryData(dictionaryFile)

        then: "illegal content type is thrown"
        thrown IllegalArgumentException

        and: "No message was send to country-import-service"
        0 * messageSenderService.sendCountriesDictionary(_)
    }

    void "verify country dictionary file exceed 1 MB exception"() {
        given: "Non-empty dictionary file with correct content type but exceeding size"
        dictionaryFile.isEmpty() >> false
        dictionaryFile.getContentType() >> "text/csv"
        dictionaryFile.getSize() >> 1024 * 1024 * 2

        when: "send dictionary invoked"
        service.sendCountriesDictionaryData(dictionaryFile)

        then: "exception is thrown due to exceeding maximum file size"
        thrown BadRequestException

        and: "No message was send to country-import-service"
        0 * messageSenderService.sendCountriesDictionary(_)
    }

    void "verify country dictionary file has no entries"() {
        given: "Non-empty dictionary file with correct content type and correct size but with empty content"
        mockDictionaryFileWithContent(emptyDictionaryContent)

        when: "send dictionary invoked"
        service.sendCountriesDictionaryData(dictionaryFile)

        then: "exception is thrown due to no country entries are present in file"
        thrown IllegalStateException

        and: "No message was send to country-import-service"
        0 * messageSenderService.sendCountriesDictionary(_)
    }

    void "verify country dictionary file malformed"() {
        given: "Non-empty dictionary file with correct content type and correct size but with malformed content"
        mockDictionaryFileWithContent(malformedDictionaryContent)

        when: "send dictionary invoked"
        service.sendCountriesDictionaryData(dictionaryFile)

        then: "exception is thrown due to malformed country entries"
        thrown IllegalStateException

        and: "No message was send to country-import-service"
        0 * messageSenderService.sendCountriesDictionary(_)
    }

    void "verify country dictionary file parsed and send to message queue"() {
        given: "Non-empty dictionary file with correct content type and correct size but with malformed content"
        mockDictionaryFileWithContent(validDictionaryContent)

        when: "send dictionary invoked"
        service.sendCountriesDictionaryData(dictionaryFile)

        then: "Message was send to country-import-service successfully"
        1 * messageSenderService.sendCountriesDictionary({ it == "code;name\nNA;Намибия\nNR;Науру\nNP;Непал" })
    }

    private def mockDictionaryFileWithContent(String content) {
        dictionaryFile.isEmpty() >> false
        dictionaryFile.getContentType() >> "text/csv"
        dictionaryFile.getSize() >> 1024
        dictionaryFile.getInputStream() >> IOUtils.toInputStream(content)
    }
}
