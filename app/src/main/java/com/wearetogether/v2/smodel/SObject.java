package com.wearetogether.v2.smodel;

import java.io.Serializable;

public class SObject implements Serializable {

    protected boolean nullValue(String value) {
        if (value == null) return true;
        if (value.equals("")) return true;
        return false;
    }
}
