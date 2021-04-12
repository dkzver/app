package com.wearetogether.v2.app.encryption;

public abstract class EncryptionAlgorithm {

    public static final String STRING_KEY = "Hello";
    protected String source;

    public EncryptionAlgorithm(String source) {

        this.source = source;
    }

    public abstract String encode(String... params);

    public abstract String decode(String... params);
}

