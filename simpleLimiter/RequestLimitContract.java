package com.sqhtech.fusion.limit;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sqhtech.fusion.annotation.RequestLimit;
import com.sqhtech.fusion.exception.GlobalException;
import com.sqhtech.fusion.util.IpUtil;

@Aspect
@Component
public class RequestLimitContract {
	private Logger logger = LoggerFactory.getLogger(RequestLimitContract.class);
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
    
	@Before("execution(public * com.sqhtech.fusion.controller.*.*(..)) && @annotation(limit)")
	public void requestLimit(final JoinPoint joinPoint, RequestLimit limit) {

		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof HttpServletRequest) {
				request = (HttpServletRequest) args[i];
				break;
			}
		}
		if (request == null) {
			throw new GlobalException(500, "方法中缺失HttpServletRequest参数");
		}
		String ip = IpUtil.getIpAddr(request);
		String url = request.getRequestURL().toString();
		String key = "req_limit_".concat(url).concat(ip);
		long count = redisTemplate.opsForValue().increment(key, 1);
		if (count == 1) {
			redisTemplate.expire(key, limit.time(), TimeUnit.MILLISECONDS);
		}
		if (count > limit.count()) {
			logger.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
			throw new GlobalException(500, "访问频率过快！");
		}
	}
}