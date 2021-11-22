package com.kabunx.erp.controller;

import com.kabunx.erp.api.MemberFeignClient;
import com.kabunx.erp.domain.JsonResponse;
import com.kabunx.erp.vo.MemberVo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController implements MemberFeignClient {

    @Override
    public JsonResponse<MemberVo> findOneById(Integer id) {
        return null;
    }
}
