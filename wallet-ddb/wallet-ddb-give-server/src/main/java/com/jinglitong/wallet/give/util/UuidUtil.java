/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.jinglitong.wallet.give.util;

import java.util.UUID;

public class UuidUtil {
    public UuidUtil(){

    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }
}
