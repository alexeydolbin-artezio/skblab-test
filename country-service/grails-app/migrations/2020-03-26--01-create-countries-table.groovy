databaseChangeLog = {

    changeSet(author: "alexey.dolbin", id: "2020-03-26--01-create-countries-table-01") {
        createTable(tableName: "country") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "country_pkey")
            }
            column(name: "code", type: "VARCHAR(2)") {
                constraints(nullable: "false", unique: true, uniqueConstraintName: "uidx_country_code")
            }
            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
        createIndex(tableName: "country", indexName: "idx_county_name") {
            column(name: "name")
        }
    }
}
