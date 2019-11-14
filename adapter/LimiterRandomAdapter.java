package com.songshushan.fusion.limiter.adapter;


import com.songshushan.fusion.limiter.BaseLimiter;
import com.songshushan.fusion.limiter.LimiterResult;
import com.songshushan.fusion.limiter.base.LimiterKV;
import com.songshushan.fusion.limiter.util.LimiterKvUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class LimiterRandomAdapter extends BaseLimiter {

    private static Logger logger = Logger.getLogger(LimiterRandomAdapter.class);

    @Override
    public LimiterResult isPass(String packageName, String method, String returnValue, Class<?> returnType, LimiterKV[] limiterKVs, Object[] args) {
        try {
            // 提取配置中的"rate"
            double rate = LimiterKvUtils.getValDouble(limiterKVs, "rate", 1.0);
            double random = Math.random();
            if (rate > random) {
                return new LimiterResult(true, null);
            } else {
                return new LimiterResult(false, returnRejectObject(returnValue, returnType));
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            // 异常放行不影响业务流程
            return new LimiterResult(true, null);
        }
    }
}
