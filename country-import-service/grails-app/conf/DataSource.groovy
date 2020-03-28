dataSource {
    pooled = true
    jmxExport = false
    driverClassName = "org.postgresql.Driver"
    username = "postgres"
    password = "postgres"
    logSql = true
}
hibernate {
    cache.use_second_level_cache = false
    cache.use_query_cache = false
//    cache.region.factory_class = 'org.hibernate.cache.SingletonEhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'never' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {
    test {
        dataSource {
            dbCreate = ""
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1"
            username = "sa"
            password = ""
            pooled = false
            driverClassName = "org.h2.Driver"
        }
    }
    development {
        dataSource {
            dbCreate = ""
            url = "jdbc:postgresql://localhost:5432/business_data_db"
            properties {
                // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
                jmxEnabled = true
                initialSize = 5
                maxActive = 10
                minIdle = 5
                maxIdle = 10
                maxWait = 10000
                maxAge = 10 * 60000
                timeBetweenEvictionRunsMillis = 5000
                minEvictableIdleTimeMillis = 60000
                validationQuery = "SELECT 1"
                validationQueryTimeout = 3
                validationInterval = 15000
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = false
                jdbcInterceptors = "ConnectionState"
                defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
            }
        }
    }
}
