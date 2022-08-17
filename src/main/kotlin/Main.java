import java.time.Instant;
import java.util.ArrayList;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        String title = "Interesting science and psychology articles to read - part 2";
        long epochSecond = Instant.now().getEpochSecond();
        ArrayList<Object> list = new ArrayList<>();

        joinToString(
                new ArrayList<String>(),
                new StringBuilder(),
                ", ",
                "",
                "",
                -1,
                "...",
                null
        );

    }

    public static <T> StringBuilder joinToString(
            Iterable<T> iterable,
            StringBuilder buffer,
            CharSequence separator,
            CharSequence prefix,
            CharSequence postfix,
            int limit,
            CharSequence truncated,
            Function<T, CharSequence> transform
    ) {
        buffer.append(prefix);
        var count = 0;
        for (T element : iterable) {
            if (++count > 1) buffer.append(separator);
            if (limit < 0 || count <= limit) {
                if (transform != null) buffer.append(transform.apply(element));
                else buffer.append(element);
            } else break;
        }
        if (limit >= 0 && count > limit) buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
    }
}
