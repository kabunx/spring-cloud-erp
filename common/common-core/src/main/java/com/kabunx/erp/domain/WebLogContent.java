package com.kabunx.erp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 统一的日志结构体
 */
@Data
@EqualsAndHashCode
public class WebLogContent {
    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作用户
     */
    private String username;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 请求类型
     */
    private String method;

    /**
     * URL
     */
    private String url;

    /**
     * 根路径
     */
    private String basePath;

    /**
     * URI
     */
    private String uri;

    /**
     * 请求参数
     */
    private Object parameter;

    /**
     * 返回结果
     */
    private Object result;

    /**
     * 操作时间
     */
    private Long startTime;

    /**
     * 消耗时间
     */
    private Integer spendTime;
}
