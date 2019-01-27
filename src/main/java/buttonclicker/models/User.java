package buttonclicker.models;


import java.util.concurrent.ThreadLocalRandom;

public class User {
    private final String name;

    public User() {
        name = ThreadLocalRandom.current()
                .ints(10, 'a', 'z')
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String getName() {
        return name;
    }
}
