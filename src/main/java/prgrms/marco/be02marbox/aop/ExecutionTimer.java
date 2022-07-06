package prgrms.marco.be02marbox.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class ExecutionTimer {

	private static final Logger log = LoggerFactory.getLogger(ExecutionTimer.class);

	@Pointcut("execution(* prgrms.marco.be02marbox.domain..service.*Service.*(..))")
	private void service() {
	}

	@Around("service()")
	public Object assumeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		Object result = joinPoint.proceed();
		stopWatch.stop();

		long totalTimeMillis = stopWatch.getTotalTimeMillis();

		MethodSignature signature = (MethodSignature)joinPoint.getSignature();

		String methodName = signature.getMethod().getName();

		String[] methodParameters = signature.getParameterNames();

		log.info("실행 메서드: {}, 인자: {}, 실행시간 = {}ms", methodName, methodParameters, totalTimeMillis);

		return result;
	}

}
