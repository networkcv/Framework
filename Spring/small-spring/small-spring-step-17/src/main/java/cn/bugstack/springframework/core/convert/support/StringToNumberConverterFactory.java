package cn.bugstack.springframework.core.convert.support;

import cn.bugstack.springframework.core.convert.converter.Converter;
import cn.bugstack.springframework.core.convert.converter.ConverterFactory;
import cn.bugstack.springframework.util.NumberUtils;

/**
 * Converts from a String any JDK-standard Number implementation.
 * <p>

 */
public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {

    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumber<>(targetType);
    }

    private static final class StringToNumber<T extends Number> implements Converter<String, T> {

        private final Class<T> targetType;

        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if (source.isEmpty()) {
                return null;
            }
            return NumberUtils.parseNumber(source, this.targetType);
        }
    }

}
