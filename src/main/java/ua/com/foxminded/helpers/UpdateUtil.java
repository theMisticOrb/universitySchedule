package ua.com.foxminded.helpers;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;

public final class UpdateUtil {

    private UpdateUtil() {

    }

    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public static <D, E, I> void replaceList(List<E> currentList, Collection<? extends D> updatedDtoList,
            Function<? super D, ? extends E> mapper, JpaRepository<E, I> repository) {
        if (updatedDtoList == null) {
            return;
        }

        List<E> newEntities = updatedDtoList.stream().map(mapper).collect(Collectors.toList());

        currentList.clear();
        currentList.addAll(newEntities);

    }

    public static <D, E, I> void replaceSet(Set<E> currentSet, Collection<? extends D> updatedDtoList,
            Function<? super D, ? extends E> mapper, JpaRepository<E, I> repository) {

        if (updatedDtoList == null) {
            return;
        }

        List<E> newEntities = updatedDtoList.stream().map(mapper).collect(Collectors.toList());

        repository.deleteAll(currentSet);

        List<E> saved = repository.saveAll(newEntities);

        currentSet.clear();
        currentSet.addAll(saved);
    }
}
