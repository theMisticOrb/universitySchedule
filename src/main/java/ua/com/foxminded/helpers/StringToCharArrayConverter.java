package ua.com.foxminded.helpers;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCharArrayConverter implements Converter<String, char[]>{

    @Override
    public char[] convert(String source) {
        return source == null ? null : source.toCharArray();
    }

}
