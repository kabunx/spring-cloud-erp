package com.kabunx.erp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kabunx.erp.pojo.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "role", autoResultMap = true)
public class RoleDO extends BaseDO {
    @TableId(type = IdType.AUTO)
    Long id;

    String name;

    /**
     * 当需要使用n-n关系的是否，必须定义，用于聚合数据
     */
    @TableField(exist = false)
    Long pivotForeignId;
}
