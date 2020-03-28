class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/api/$version/countries"(controller: "country")
        "/api/$version/countries/find"(controller: "country", action: "find")
        "/api/$version/countries/$id"(controller: "country", action: "show")

        "/api/v1/countries/import"(controller: "countryImportIntegration")

        "500"(controller: "error", action: "exceptionHandling")
        "404"(controller: "error", action: "exceptionHandling")
	}
}
