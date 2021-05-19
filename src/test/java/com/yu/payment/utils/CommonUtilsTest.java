package com.yu.payment.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommonUtilsTest {

    @Test
    public void getUUID() {
        String uuid = CommonUtils.generateUUID();
        System.out.println(uuid);
    }
}