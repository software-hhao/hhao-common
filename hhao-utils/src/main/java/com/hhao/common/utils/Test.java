package com.hhao.common.utils;

import com.hhao.common.utils.random.RandomUtils;

/**
 * @author Wang
 * @since 1.0.0
 */
public class Test {
    public static void main(String [] args){
        for(int i=0;i<10;i++){
            System.out.println(RandomUtils.randomString(10));
        }


    }
}
