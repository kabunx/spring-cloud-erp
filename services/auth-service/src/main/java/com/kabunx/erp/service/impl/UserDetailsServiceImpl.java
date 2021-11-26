package com.kabunx.erp.service.impl;

import com.kabunx.erp.api.UserFeignClient;
import com.kabunx.erp.domain.JsonResponse;
import com.kabunx.erp.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDetailsServiceImpl {

    @Resource
    UserFeignClient userFeignClient;

    public UserVO loadUserById(Integer id) {
        JsonResponse<UserVO> response = userFeignClient.show(id);
        if (response.unavailable()) {
            return null;
        }
        return response.getData();
    }
}
