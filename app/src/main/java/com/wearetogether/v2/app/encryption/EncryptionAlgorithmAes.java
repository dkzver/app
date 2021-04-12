package com.wearetogether.v2.app.encryption;

public class EncryptionAlgorithmAes extends EncryptionAlgorithm {

    private static String initVector = "RandomInitVector";

    public EncryptionAlgorithmAes(String source) {
        super(source);
    }

    @Override
    public String encode(String... params) {
        try {
            return encode(params[0], source);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    public String decode(String... params) {//раскодировать
        try {
            return decode(params[0], source);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String decode(String key, String value) {
        String result = "";
        int keylen = key.length();
        int msglen = value.length();
        int j;
        j = 0;
        for(int i = 0; i < msglen; i++) {
            result = result + (char) (value.charAt(i) ^ key.charAt(j));
            j++;
            if(j==keylen) {
                j=0;
            }
        }
        return result;
    }

    public static String encode(String key, String value) {
        String result = "";
        int keylen = key.length();
        int msglen = value.length();
        int j;

        j = 0;
        for(int i = 0; i < msglen; i++) {
            result = result + (char) (value.charAt(i) ^ key.charAt(j));
            j++;
            if(j==keylen) {
                j=0;
            }
        }
        return result;
    }
}
