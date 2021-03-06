package com.kabunx.erp.extension.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kabunx.erp.dto.QueryDTO;
import com.kabunx.erp.util.StringPlusUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class PlusWrapper<T> extends QueryWrapper<T> {

    private static final List<String> ignores = Arrays.asList("page", "pageSize");

    public PlusWrapper() {
    }

    public void autoBuild(QueryDTO queryDTO) {
        Class<?> dtoClass = queryDTO.getClass();
        // 获取当前类定义的属性（不包括父类）
        Field[] fields = dtoClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (ignores.contains(field.getName())) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(queryDTO);
                if (null == value) {
                    continue;
                }
                buildWrapper(field, value);
            } catch (Exception ignored) {
                log.warn("反射异常，无法获取【{}】的值", field.getName());
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
    }

    private void buildWrapper(Field field, Object value) {
        String methodName = "where" + StringPlusUtils.capitalize(field.getName());
        Class<?> wrapperClass = this.getClass();
        try {
            Method whereMethod = wrapperClass.getDeclaredMethod(methodName, field.getType());
            whereMethod.setAccessible(true);
            whereMethod.invoke(this, value);
        } catch (Exception ignored) {
            log.warn("反射异常，没有定义方法【{}】", methodName);
        }
    }
}
