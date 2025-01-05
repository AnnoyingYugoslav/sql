package com.example.sql.converters;

import com.example.sql.entities.Godzina;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GodzinaConverter implements AttributeConverter<Godzina, String> {

    @Override
    public String convertToDatabaseColumn(Godzina godzina) {
        if (godzina == null) {
            return null;
        }
        return godzina.toString();
    }

    @Override
    public Godzina convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Godzina.fromString(dbData);
    }
}
