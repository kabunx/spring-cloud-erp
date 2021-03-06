package com.kabunx.erp.domain.dto;

import com.kabunx.erp.constraints.HashSecret;
import com.kabunx.erp.pojo.BaseDTO;
import com.kabunx.erp.validator.ValidatorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmsCodeDTO extends BaseDTO {
    @NotNull
    @NotEmpty
    String type;

    @NotNull
    @NotEmpty
    String phone;

    /**
     * 前后端统一加密
     */
    @HashSecret(type = ValidatorEnum.SMS_CODE_SECRET)
    String secret;
}
