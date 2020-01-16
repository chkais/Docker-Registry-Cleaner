package de.christopherkaiser.dockerRegistryCleaner.api.factory;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import de.christopherkaiser.dockerRegistryCleaner.api.V1Compatibility;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.V1CompatibilityDTO;
import org.apache.commons.io.input.CharSequenceReader;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Singleton
public class V1CompatibilityFactory {
    static JsonFactory JSON_FACTORY = new JacksonFactory();
    static DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .appendLiteral('Z')
            .toFormatter();

    V1Compatibility createFromJson(String json) throws IOException {
        V1CompatibilityDTO v1CompatibilityDTO = getDTOFromJson(json);
        LocalDateTime created = LocalDateTime.parse(v1CompatibilityDTO.getCreated(), formatter);
        return V1Compatibility.builder().created(created).id(v1CompatibilityDTO.getId()).build();

    }

    private V1CompatibilityDTO getDTOFromJson(String json) throws IOException {
        JsonObjectParser jsonObjectParser = new JsonObjectParser(JSON_FACTORY);
        Reader targetReader = new CharSequenceReader(json);
        return jsonObjectParser.parseAndClose(targetReader, V1CompatibilityDTO.class);
    }
}
