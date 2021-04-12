package com.wearetogether.v2.ui.listeners;

public interface OptionPlaceListener {
    void clickToViewPlace(Long place_unic, int position);

    void clickToEditPlace(Long place_unic, int position);

    void clickToRemovePlace(Long place_unic, int position);
}
