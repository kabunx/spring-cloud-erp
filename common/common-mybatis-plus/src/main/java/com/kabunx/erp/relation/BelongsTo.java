package com.kabunx.erp.relation;

import com.kabunx.erp.extension.mapper.PlusMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.BiConsumer;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class BelongsTo<TC, TP> extends Relation<TC, TP, BelongsTo<TC, TP>> {
    private final static String name = "belongTo";

    /**
     * 自定义回调，关联数据回填到主属性中
     */
    private BiConsumer<TP, TC> merge;

    public BelongsTo(PlusMapper<TP> parent) {
        super(parent);
    }

    @Override
    public void initRelation(List<TP> records) {
        if (!requiredConditions()) {
            return;
        }
        initRelatedData(records, foreignKey, localKey);
        for (TP record : records) {
            TC relatedValue = getOneRelatedValue(record, foreignKey);
            merge.accept(record, relatedValue);
        }
    }

    private Boolean requiredConditions() {
        return requiredRelatedArgs() && merge != null;
    }
}
