package example.controller;

import limit.base.LimiterQuickIp;
import limit.base.LimiterQuickTotal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;

@RestController
@RequestMapping("/user")
public class TestController {




    @LimiterQuickIp(tps = 5 , rejectReturnVal = "{'info':\"暂无数据\"}")
    @PostMapping("/getUser")
    public String getUserName(String userId){
        return "userName"+userId;
    }


    @LimiterQuickTotal(tps = 5,rejectReturnVal = "过载")
    @PostMapping("/getUserRid")
    public String getUserNameAndRId(String userId){
        SecureRandom random = new SecureRandom();

        return "userName:"+random.nextInt()+":"+userId;
    }


}
