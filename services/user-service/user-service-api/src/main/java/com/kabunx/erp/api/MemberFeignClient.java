package com.kabunx.erp.api;

import com.kabunx.erp.api.fallback.MemberFeignClientFallback;
import com.kabunx.erp.domain.JsonResponse;
import com.kabunx.erp.vo.MemberVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "erp-user-service",
        contextId = "member",
        fallback = MemberFeignClientFallback.class,
        path = "/user"
)
public interface MemberFeignClient {

    @GetMapping("/members/{id}")
    JsonResponse<MemberVO> findByUserId(@PathVariable("id") Long id);
}
