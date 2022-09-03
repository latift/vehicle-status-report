package com.example.vehiclestatus.logging;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.util.*;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

/**
 * Logs all the request, response and exception from Vehicle Status Report API
 */
@Aspect
@Component
public class HandlerLogger {
    private static final Logger logger = LoggerFactory.getLogger(HandlerLogger.class);

    final private ObjectMapper mapper;

    public HandlerLogger(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Pointcut("within(com.example.vehiclestatus.VehicleStatusReportController)")
    public void postMappingpointcut() {
    }

    @Before("postMappingpointcut()")
    public void logMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Map<String, Object> parameters = getParameters(joinPoint);

        try {
            logger.info("Before",signature.getMethod().toString(), mapper.writeValueAsString(parameters));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }

    @AfterReturning(pointcut = "postMappingpointcut()", returning = "entity")
    public void logMethodAfter(JoinPoint joinPoint, ResponseEntity<?> entity) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        try {
            logger.info("After",signature.getMethod().toString(), mapper.writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }

    @AfterThrowing(pointcut = "postMappingpointcut()", throwing = "exception")
    public void logMethodExceptionThrow(JoinPoint joinPoint, Throwable exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        PostMapping mapping = signature.getMethod().getAnnotation(PostMapping.class);
        //loggerService.log("Exception", signature.getMethod().toString(), exception.getMessage());
        logger.info("<== path(s): {}, method(s): {}, retuning: {}", mapping.path(), signature.getMethod(),exception);
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        HashMap<String, Object> map = new HashMap<>();
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }
        return map;
    }

}