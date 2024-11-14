package ie.setu.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule


//More info: https://www.baeldung.com/jackson-object-mapper-tutorial
//           https://www.baeldung.com/jackson-serialize-dates
//           https://www.baeldung.com/kotlin/reified-functions


fun jsonObjectMapper(): ObjectMapper
        = ObjectMapper()
    .registerModule(JavaTimeModule())
    .registerModule(JodaModule())
    .registerModule(KotlinModule.Builder().build())
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)


