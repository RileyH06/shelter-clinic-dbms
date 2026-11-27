package com.shelterclinic;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;

public class AppState {

    private static final BooleanProperty loggedIn = new SimpleBooleanProperty(false);
    private static final StringProperty shelterName = new SimpleStringProperty("");
    private static final BooleanProperty isShelter = new SimpleBooleanProperty(false);

    public static BooleanProperty loggedInProperty(){
        return loggedIn;
    }

    public static boolean isLoggedIn() {
        return loggedIn.get();
    }

    public static void login(){
        loggedIn.set(true);
    }

    public static void logout(){
        loggedIn.set(false);
        isShelter.set(false);
    }

    public static BooleanProperty isShelterProperty() {
        return isShelter;
    }

    public static boolean isShelter() {
        return isShelter.get();
    }

    public static void setShelter(boolean value){
        isShelter.set(value);
    }

    public static StringProperty shelterNameProperty() {
        return shelterName;
    }

    public static void setShelterName(String name){
        shelterName.set(name);
    }

}
