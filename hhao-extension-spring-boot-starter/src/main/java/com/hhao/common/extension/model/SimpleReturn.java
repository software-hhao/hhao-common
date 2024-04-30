package com.hhao.common.extension.model;

/**
 * 单值返回
 * 组合式扩展点采用@ExtensionPointAutowired方式导入时，方法的返回值必须是SimpleReturn及其子类
 *
 * @author Wang
 * @since 1.0.0
 */
public class SimpleReturn<R> implements Return<R> {
    private R returnValue;

    protected SimpleReturn(){

    }

    protected SimpleReturn(R returnValue){
        this.returnValue=returnValue;
    }

    public R getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(R returnValue) {
        this.returnValue = returnValue;
    }

    public static <R> SimpleReturn of(R returnValue){
        return new SimpleReturn(returnValue);
    }
}
