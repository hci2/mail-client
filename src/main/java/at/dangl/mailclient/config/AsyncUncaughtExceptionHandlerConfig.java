package at.dangl.mailclient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncUncaughtExceptionHandlerConfig implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... parameters) {
        log.error("Exception occurred during async execution of method: {} with parameter {}, exception: {}", method.getName(), parameters ,throwable.getMessage());
    }

}
