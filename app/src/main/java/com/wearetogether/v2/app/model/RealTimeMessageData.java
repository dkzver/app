package com.wearetogether.v2.app.model;

import com.wearetogether.v2.Consts;
import com.wearetogether.v2.app.encryption.EncryptionAlgorithm;
import com.wearetogether.v2.app.encryption.EncryptionAlgorithmAes;
import com.wearetogether.v2.database.model.Message;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RealTimeMessageData {
    public String content;
    public String type;
    public Long user_unic;
    public String is_read;

    public RealTimeMessageData() {
    }

    public RealTimeMessageData(Message m, boolean isDecode) {
        if(m.type == Consts.MESSAGE_TYPE_TEXT && isDecode) {
            EncryptionAlgorithm encryptionAlgorithm = new EncryptionAlgorithmAes(m.content);
            this.content = encryptionAlgorithm.decode(Consts.ENCRYPTION_STRING_KEY);
        } else {
            this.content = m.content;
        }
        this.type = String.valueOf(m.type);
        this.user_unic = m.user_unic;
        this.is_read = m.is_read;
    }

    public Message getMessage(String key, boolean isEncode) {
        return getMessage(Long.parseLong(key), isEncode);
    }

    public Message getMessage(Long unic, boolean isEncode) {
        Message message = new Message();
        message.unic = unic;
        message.type = Integer.parseInt(type);
        if(message.type == Consts.MESSAGE_TYPE_TEXT && isEncode) {
            EncryptionAlgorithm encryptionAlgorithm = new EncryptionAlgorithmAes(content);
            message.content = encryptionAlgorithm.encode(Consts.ENCRYPTION_STRING_KEY);
        } else {
            message.content = content;
        }
        message.user_unic = user_unic;
        message.is_read = is_read;
        return message;
    }
}
