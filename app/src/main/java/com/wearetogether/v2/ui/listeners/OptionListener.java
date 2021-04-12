package com.wearetogether.v2.ui.listeners;

import android.os.Bundle;
import com.wearetogether.v2.app.model.Option;

import java.util.List;

public interface OptionListener {
    void clickToOption(int key, Bundle bundle, Class<?> cls);
    List<Option> getOptions(Bundle bundle, Class<?> cls);
}
