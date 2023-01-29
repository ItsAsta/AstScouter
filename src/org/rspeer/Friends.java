package org.rspeer;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.EnterInput;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Tab;
import org.rspeer.runetek.api.component.tab.Tabs;
import org.rspeer.ui.Log;

public class Friends {

    private static boolean openAdd() {
        InterfaceComponent addFriendComp = Interfaces.getComponent(429, 14);

        return addFriendComp != null && addFriendComp.click();
    }

    private static boolean openTab() {
        return !Tabs.isOpen(Tab.FRIENDS_LIST) && Tabs.open(Tab.FRIENDS_LIST);
    }

    private static boolean isFriendAdded(String name) {
        InterfaceComponent friendList = Interfaces.getComponent(429, 11);

        if (friendList == null) {
            return false;
        }

        for (InterfaceComponent comp : friendList.getComponents()) {
            if (comp.getText().replace('\u00A0', ' ').equalsIgnoreCase(name)) {
                Log.fine("Friend already added");
                return true;
            }
        }
        return false;
    }

    private static boolean isOnline(String name) {
        InterfaceComponent status = Interfaces.getComponent(429, 11);

        if (status == null) {
            return false;
        }

        for (InterfaceComponent comp : status.getComponents()) {
            InterfaceComponent statusChild = Interfaces.getComponent(429, comp.getIndex() + 2);
            if (statusChild == null) {
                return false;
            }

            if (comp.getText().replace('\u00A0', ' ').equalsIgnoreCase(name) &&
                    !statusChild.getText().equalsIgnoreCase("Offline")) {
                return true;
            }
        }
        return false;
    }

    public static boolean addFriend(String name) {

        if (openTab()) {
            return isFriendAdded(name);
        }

        if (!Dialog.isOpen()) {
            return openAdd();
        }

        return EnterInput.initiate(name);
    }

    public static boolean isFriendOnline(String name) {
        return isOnline(name);
    }
}
