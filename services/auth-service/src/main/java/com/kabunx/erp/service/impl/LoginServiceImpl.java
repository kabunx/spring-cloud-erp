package com.kabunx.erp.service.impl;

import com.kabunx.erp.cache.CacheType;
import com.kabunx.erp.constant.AuthConstant;
import com.kabunx.erp.constant.SecurityConstant;
import com.kabunx.erp.converter.Hydrate;
import com.kabunx.erp.domain.dto.LoginAccountDTO;
import com.kabunx.erp.domain.dto.LoginCaptchaDTO;
import com.kabunx.erp.domain.dto.LoginMiniAppDTO;
import com.kabunx.erp.domain.dto.LoginSmsCodeDTO;
import com.kabunx.erp.domain.vo.AuthTokenVO;
import com.kabunx.erp.entity.UserEntity;
import com.kabunx.erp.exception.AuthException;
import com.kabunx.erp.service.CaptchaService;
import com.kabunx.erp.service.LoginService;
import com.kabunx.erp.service.SmsCodeService;
import com.kabunx.erp.service.UserService;
import com.kabunx.erp.util.JwtUtils;
import com.kabunx.erp.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    CaptchaService captchaService;

    @Resource
    SmsCodeService smsCodeService;

    @Resource
    JwtUtils jwtUtils;

    @Resource
    UserService userService;

    @Override
    public AuthTokenVO loginByAccountWithoutCaptcha(LoginAccountDTO loginAccountDTO) {
        // 查找用户
        UserVO user = userService.loadByAccount(loginAccountDTO.getAccount());
        if (user == null) {
            throw new AuthException(AuthConstant.USERNAME_PASSWORD_ERROR);
        }
        // 验证密码
        // 生成token
        return generateAuthToken(user);
    }

    @Override
    public AuthTokenVO loginByCaptcha(LoginCaptchaDTO loginCaptchaDTO) {
        // 1、图形验证码是否正确
        boolean isRight = captchaService.validate(
                loginCaptchaDTO.getCaptchaKey(),
                loginCaptchaDTO.getCaptchaCode(),
                CacheType.CAPTCHA
        );
        if (!isRight) {
            throw new AuthException(AuthConstant.CAPTCHA_ERROR);
        }
        // 2、服务调用，查找账号
        UserVO user = userService.loadByAccount(loginCaptchaDTO.getAccount());
        // 3、密码校验
        // 4、生成token
        return generateAuthToken(user);
    }

    @Override
    public AuthTokenVO loginBySmsCode(LoginSmsCodeDTO loginSmsCodeDTO) {
        // 1、短信验证码是否正确
        boolean isRight = smsCodeService.validate(
                loginSmsCodeDTO.getSmsKey(),
                loginSmsCodeDTO.getSmsCode(),
                CacheType.SMS_CODE
        );
        if (!isRight) {
            throw new AuthException(AuthConstant.SMS_CODE_ERROR);
        }
        // 2、服务调用，通过手机号查找用户
        UserVO user = userService.loadByPhone(loginSmsCodeDTO.getPhone());
        // 3、生成token
        return generateAuthToken(user);
    }

    @Override
    public AuthTokenVO loginByMiniApp(LoginMiniAppDTO loginMiniAppDTO) {
        return null;
    }


    private AuthTokenVO generateAuthToken(UserVO userVO) {
        UserEntity user = Hydrate.map2Target(userVO, UserEntity.class);
        if (user == null) {
            return null;
        }
        return AuthTokenVO.builder()
                .accessToken(jwtUtils.generate(user, SecurityConstant.AUTHORIZATION_ACCESS_TYPE))
                .refreshToken(jwtUtils.generate(user, SecurityConstant.AUTHORIZATION_REFRESH_TYPE))
                .build();
    }
}
