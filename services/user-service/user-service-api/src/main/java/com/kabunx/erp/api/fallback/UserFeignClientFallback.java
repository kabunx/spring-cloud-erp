package com.kabunx.erp.api.fallback;

import com.kabunx.erp.api.UserFeignClient;
import com.kabunx.erp.domain.JsonResponse;
import com.kabunx.erp.dto.UserFromDTO;
import com.kabunx.erp.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserFeignClientFallback implements UserFeignClient {
    @Override
    public JsonResponse<UserVO> show(Integer id) {
        return withFallbackError();
    }

    @Override
    public JsonResponse<UserVO> phone(String phone) {
        return withFallbackError();
    }

    @Override
    public JsonResponse<UserVO> create(UserFromDTO userFromDTO) {
        return withFallbackError();
    }

    private JsonResponse<UserVO> withFallbackError() {
        log.error("Openfeign远程调用用户服务（创建用户）异常的降级方法");
        return JsonResponse.withFallbackError();
    }
}
