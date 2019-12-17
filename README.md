# limiter

一个多场景的限流器，包括接口总请求限流、ip限流。也可以自定义限流规则。


## 单独使用Guava
### 


### 限流方式-----采用guava

* 第一步pom
```
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>18.0</version>
</dependency>

```
第二步引入限流器
```java
@Service
public class AccessLimitService {

    //每秒只发出5个令牌
    RateLimiter rateLimiter = RateLimiter.create(5.0);
    /**
     * 尝试获取令牌
     * @return
     */
    public boolean tryAcquire(){
        return rateLimiter.tryAcquire();
    }
}
```
第三步使用限流器
```java
    @GetMapping("/access")
    public String access(){
        //尝试获取令牌
        if(accessLimitService.tryAcquire()){
            //模拟业务执行500毫秒
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return "aceess success [" + sdf.format(new Date()) + "]";
        }else{
            return "aceess limit [" + sdf.format(new Date()) + "]";
        }
    }
```


## SpringAop的方式使用
基本思路 方法作为Key RateLimiter作为值



### Ip限流的方法
map.put（methodKey,Map<String,Counter>） 
缓存拿到根据方法拿到 方法的所有的Key-value （Ip -> 计数器 ），然后根据IP 获取到Counter , counter++
