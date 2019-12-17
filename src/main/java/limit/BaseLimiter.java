package limit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import limit.base.LimiterKV;
import limit.util.LimiterReturnUtils;

public abstract class BaseLimiter {

    protected static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 限流适配器需要实现此接口
     * @param packageName 包名
     * @param methodName 方法名
     * @param returnValue 当拒绝是返回的信息
     * @param returnType 方法返回的类型
     * @param limiterKVs 配置信息
     * @param args 方法参数
     * @return
     */
    public abstract LimiterResult isPass(String packageName, String methodName, String returnValue, Class<?> returnType, LimiterKV[] limiterKVs, Object[] args);

    protected static Object returnRejectObject(String rejectReturnVal, Class<?> returnType) throws Exception {
        return LimiterReturnUtils.returnRejectObject(rejectReturnVal, returnType);
    }

}
