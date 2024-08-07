package com.example.recipe.services;

import com.example.recipe.domain.enums.MenuListingType;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MenuComponentStore {
    private static MenuComponentStore instance;
    private final List<VBox> allMenuComponents;
    private final List<VBox> myMenuComponents;

    private MenuComponentStore() {
        allMenuComponents = new ArrayList<>();
        myMenuComponents = new ArrayList<>();
    }

    public static synchronized MenuComponentStore getInstance() {
        if (instance == null) {
            instance = new MenuComponentStore();
        }
        return instance;
    }

    public void addMenuComponent(VBox component, MenuListingType menuListingType) {
        if (menuListingType == MenuListingType.ALL_RECIPE) {
            allMenuComponents.add(component);
        } else if (menuListingType == MenuListingType.MY_RECIPE) {
            myMenuComponents.add(component);
        }
    }

    public List<VBox> getMenuComponents(MenuListingType menuListingType) {
        if (menuListingType == MenuListingType.ALL_RECIPE) {
            return allMenuComponents;
        }
        return myMenuComponents;
    }
}
