package com.akat.filmreel.util;

import java.util.concurrent.Executor;

public class SingleExecutors extends AppExecutors {
    private static Executor instant = command -> command.run();

    public SingleExecutors() {
        super(instant, instant, instant);
    }
}
