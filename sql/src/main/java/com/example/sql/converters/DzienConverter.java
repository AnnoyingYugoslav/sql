package com.example.sql.converters;

import com.example.sql.entities.Dzien;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DzienConverter implements AttributeConverter<Dzien, String> {

    @Override
    public String convertToDatabaseColumn(Dzien dzien) {
        if (dzien == null) {
            return null;
        }
        return dzien.toString();
    }

    @Override
    public Dzien convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Dzien.fromString(dbData);
    }
}
