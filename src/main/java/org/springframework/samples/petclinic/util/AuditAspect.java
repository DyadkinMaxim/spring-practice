package org.springframework.samples.petclinic.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
@Component
public class AuditAspect {

    private static final Logger log =
        LoggerFactory.getLogger(AuditAspect.class);

    @Pointcut("@annotation(org.springframework.samples.petclinic.util.Auditable)")
    public void modificationMethods() {}

    @Before("modificationMethods()")
    public void logModification(JoinPoint joinPoint) {
        String methodName =
            joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("[AUDIT] method={} called at timestamp={}",
            methodName,
            LocalDateTime.now()
        );
    }
}
