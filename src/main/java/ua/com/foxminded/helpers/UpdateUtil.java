package ua.com.foxminded.helpers;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public final class UpdateUtil {

    private UpdateUtil() {

    }

    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public static <D, E> void updateListIfNotNull(Collection<D> dtoList, Collection<E> entityList,
            Function<D, E> mapper) {
        if (dtoList != null) {
            entityList.clear();
            dtoList.stream().map(mapper).forEach(entityList::add);
        }
    }
}
