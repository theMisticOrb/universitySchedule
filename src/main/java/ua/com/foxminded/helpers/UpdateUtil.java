package ua.com.foxminded.helpers;

import java.util.function.Consumer;

public final class UpdateUtil {

    private UpdateUtil() {
        
    }
    
    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
