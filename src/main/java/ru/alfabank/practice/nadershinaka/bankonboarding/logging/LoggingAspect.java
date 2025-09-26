package ru.alfabank.practice.nadershinaka.bankonboarding.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("@annotation(ru.alfabank.practice.nadershinaka.bankonboarding.logging.Log)")
    public Object logMethod(ProceedingJoinPoint pjp) throws Throwable {
        //Получаем полное имя метода
        String methodFull = pjp.getSignature().toShortString();

        // Логируем входящие аргументы
        String args = serialize(pjp.getArgs());
        log.info("Вызов {} с аргументами: {}", methodFull, args);
        try {
            // Выполняем оригинальный метод
            Object result = pjp.proceed();

            // Логируем результат
            String resultStr = serialize(result);
            log.info("Метод {} завершён, результат: {}", methodFull, resultStr);

            return result;
        } catch (Throwable ex) {
            // Логируем ошибку, если метод упал
            log.error("❌ Ошибка в {}: {}", methodFull, ex.getMessage());
            throw ex;
        }
    }

    // Пробуем превратить объект в JSON, если не получилось — используем toString()
    private String serialize(Object obj) throws JsonProcessingException {
        if (obj == null) return "null";
        else {
            return objectMapper.writeValueAsString(obj);
        }
    }
}

