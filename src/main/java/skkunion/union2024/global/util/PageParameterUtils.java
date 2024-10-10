package skkunion.union2024.global.util;

import java.util.List;
import java.util.function.Function;

import static java.lang.Math.max;

public class PageParameterUtils<T, R> {

    public static Integer PAGE_SIZE = 15;

    public static <T> boolean hasNext(List<T> pageObject) {
        return pageObject.size() > PAGE_SIZE;
    }

    public static <T> int getSize(List<T> pageObject) {
        return max(pageObject.size() - 1, 0);
    }

    public static <T, R> R getLongCursor(List<T> pageObject, Function<T, R> getCursorFunction) {
        T targetObject = pageObject.get(PAGE_SIZE - 1);
        return getCursorFunction.apply(targetObject);
    }
}
