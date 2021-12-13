package com.kabunx.erp.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kabunx.erp.exception.DBException;
import com.kabunx.erp.exception.DBExceptionEnum;
import com.kabunx.erp.extension.mapper.PlusMapper;
import com.kabunx.erp.relation.HasMany;
import com.kabunx.erp.relation.HasOne;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Builder<T> {

    private Integer offsetNum;

    private Integer limitNum;

    private final PlusMapper<T> plusMapper;

    private final QueryWrapper<T> queryWrapper;

    // 关系
    private final ArrayList<HasOne<?, T>> oneRelations = new ArrayList<>();

    private final ArrayList<HasMany<?, T>> manyRelations = new ArrayList<>();

    public Builder(PlusMapper<T> plusMapper) {
        this.plusMapper = plusMapper;
        this.queryWrapper = new QueryWrapper<>();
    }

    public Builder<T> where(String column, Object value) {
        return where(column, "eq", value);
    }

    public Builder<T> where(String column, String operator, Object value) {
        switch (operator) {
            case "eq":
                queryWrapper.eq(column, value);
                break;
            case "like":
                queryWrapper.like(column, value);
                break;
        }
        return this;
    }

    public Builder<T> offset(int value) {
        offsetNum = value;
        return this;
    }

    public Builder<T> limit(int value) {
        limitNum = value;
        return this;
    }

    public List<T> get() {
        String lastLimit = "";
        if (limitNum != null) {
            if (offsetNum != null) {
                lastLimit = "LIMIT " + offsetNum + "," + limitNum;
            } else {
                lastLimit = "LIMIT " + limitNum;
            }
        }
        if (!lastLimit.isEmpty()) {
            queryWrapper.last(lastLimit);
        }
        List<T> records = plusMapper.selectList(queryWrapper);
        eagerLoadRelationsData(records);
        return records;
    }

    public List<T> get(String... columns) {
        queryWrapper.select(columns);
        return get();
    }

    public T find(Serializable id) {
        return plusMapper.selectById(id);
    }

    public T findOrFail(Serializable id) {
        T result = find(id);
        if (result == null) {
            throw new DBException(DBExceptionEnum.NOT_FOUND);
        }
        return result;
    }

    public T first() {
        List<T> records = limit(1).get();
        if (records.isEmpty()) {
            return null;
        }
        return records.get(0);
    }

    public T firstOrFail() {
        T result = first();
        if (result == null) {
            throw new DBException(DBExceptionEnum.NOT_FOUND);
        }
        return result;
    }

    public T latest(String $column) {
        queryWrapper.orderByDesc($column);
        return first();
    }

    public T sole() {
        List<T> records = limit(2).get();
        if (records.isEmpty()) {
            throw new DBException(DBExceptionEnum.NOT_FOUND);
        }
        if (records.size() > 1) {
            throw new DBException(DBExceptionEnum.MULTIPLE_RECORDS);
        }
        return records.get(0);
    }

    public void paginate() {
    }

    public Builder<T> withOne(PlusMapper<?> mapper, String foreignKey, String localKey) {
        Class<?> mapperClass = mapper.getClass();
        oneRelations.add(
                new HasOne(mapper, plusMapper, foreignKey, localKey)
        );
        return this;
    }

    public Builder<T> withMany(BaseMapper<?> mapper, String foreignKey, String localKey) {
        manyRelations.add(
                new HasMany(mapper, plusMapper, foreignKey, localKey)
        );
        return this;
    }

    private void eagerLoadRelationsData(List<T> records) {
        if (records.size() > 0) {
            for (HasOne relation : oneRelations) {
                relation.initRelation(records);
            }
            for (HasMany relation : manyRelations) {
                relation.initRelation(records);
            }
        }
    }
}