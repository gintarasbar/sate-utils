package edu.tfai.sate2.utils;

import java.util.concurrent.Callable;


public class LambdaHelpers {
    public static <T> T uncheckCall(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            return sneakyThrow(e);
        }
    }

    public static void uncheckRun(RunnableExc r) {
        try {
            r.run();
        } catch (Exception e) {
            sneakyThrow(e);
        }
    }

    public static <T> T sneakyThrow(Throwable e) {
        return LambdaHelpers.<RuntimeException, T>sneakyThrow0(e);
    }

    private static <E extends Throwable, T> T sneakyThrow0(Throwable t) throws E {
        throw (E) t;
    }

    public interface RunnableExc {
        void run() throws Exception;
    }
}
