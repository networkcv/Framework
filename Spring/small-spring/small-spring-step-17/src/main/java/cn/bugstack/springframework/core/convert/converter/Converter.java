package cn.bugstack.springframework.core.convert.converter;

/**
 * A converter converts a source object of type {@code S} to a target of type {@code T}.
 *
 * 类型转换处理接口
 *

 */
public interface Converter<S, T>  {

    /** Convert the source object of type {@code S} to target type {@code T}. */
    T convert(S source);

}
