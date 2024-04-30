package com.hhao.common.extension.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 组合返回结果值
 *
 * @author Wang
 * @since 1.0.0
 */
public class CombinedReturn<R> implements Return {
    private final int SIZE=16;
    private List<R> combinationResult=new ArrayList<>(SIZE);

    protected CombinedReturn(){

    }

    protected CombinedReturn(int size){
        if (size>SIZE) {
            combinationResult = new ArrayList<>(size);
        }
    }

    public List<R> getCombinationResult() {
        return combinationResult;
    }

    public void setCombinationResult(List<R> combinationResult) {
        this.combinationResult = combinationResult;
    }

    public void combineReturnValue(SimpleReturn simpleReturn){
        if (simpleReturn==null){
            return;
        }
        combinationResult.add((R)simpleReturn.getReturnValue());
    }

    public static CombinedReturn build(){
        return new CombinedReturn();
    }

    public static CombinedReturn build(int size){
        return new CombinedReturn(size);
    }
}
