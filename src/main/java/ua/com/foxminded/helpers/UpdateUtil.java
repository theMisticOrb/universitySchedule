package ua.com.foxminded.helpers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class UpdateUtil {

    private UpdateUtil() {

    }

    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public static <D, E> List<E> updateListIfNotNull(Collection<? extends D> dtoList,
            Function<? super D, ? extends E> mapper) {
        if (dtoList == null) {
            return Collections.emptyList();
        }

        return dtoList.stream().map(mapper).collect(Collectors.toList());
    }
}
