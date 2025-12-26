package ua.com.foxminded.helpers;

import java.util.function.Consumer;

public final class UpdateUtils {

    private UpdateUtils() {
        
    }
    
    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
