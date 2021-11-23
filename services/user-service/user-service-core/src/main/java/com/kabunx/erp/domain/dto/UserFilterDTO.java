package com.kabunx.erp.domain.dto;

import com.kabunx.erp.domain.FilterDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFilterDTO<T> extends FilterDTO<T> {
    private String name;
}