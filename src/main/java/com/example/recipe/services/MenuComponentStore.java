package com.example.recipe.services;

import com.example.recipe.domain.UiModel;
import com.example.recipe.domain.enums.MenuListingType;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MenuComponentStore {
    private static MenuComponentStore instance;
    private final List<UiModel> allMenuComponents;
    private final List<UiModel> myMenuComponents;
    private final List<UiModel> favouriteMenuComponents;

    private MenuComponentStore() {
        allMenuComponents = new ArrayList<>();
        myMenuComponents = new ArrayList<>();
        favouriteMenuComponents = new ArrayList<>();
    }

    public static synchronized MenuComponentStore getInstance() {
        if (instance == null) {
            instance = new MenuComponentStore();
        }
        return instance;
    }

    public void addMenuComponent(UiModel component, MenuListingType menuListingType) {
        if (menuListingType == MenuListingType.ALL_RECIPE) {
            allMenuComponents.add(component);
        } else if (menuListingType == MenuListingType.MY_RECIPE) {
            myMenuComponents.add(component);
        } else {
            favouriteMenuComponents.add(component);
        }
    }

    public void clearMenuComponents(MenuListingType menuListingType) {
        if (menuListingType == MenuListingType.ALL_RECIPE) {
            allMenuComponents.clear();
        } else if (menuListingType == MenuListingType.MY_RECIPE) {
            myMenuComponents.clear();
        } else {
            favouriteMenuComponents.clear();
        }
    }

    public List<VBox> getMenuComponents(MenuListingType menuListingType) {
        if (menuListingType == MenuListingType.ALL_RECIPE) {
            return allMenuComponents.stream().map(UiModel::getvBoxes).toList();
        } else if (menuListingType == MenuListingType.MY_RECIPE) {
            return myMenuComponents.stream().map(UiModel::getvBoxes).toList();
        } else {
            return favouriteMenuComponents.stream().map(UiModel::getvBoxes).toList();
        }
    }

    public List<UiModel> getMenuModel(MenuListingType menuListingType) {
        if (menuListingType == MenuListingType.ALL_RECIPE) {
            return allMenuComponents;
        } else if (menuListingType == MenuListingType.MY_RECIPE) {
            return myMenuComponents;
        } else {
            return favouriteMenuComponents;
        }
    }

    public List<UiModel> getAllMenuModel() {
        return allMenuComponents;
    }
}
