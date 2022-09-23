package pl.devopsi.hazelcasttutorial.controller;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Paths {

    public static final String USERS ="v1/users";

    public static class Users {

        public static final String GET_SINGLE_USER = "/{pesel}";
        public static final String ADD_SINGLE_USER = "";

        public static final String UPDATE_SINGLE_USER = "";
    }
}
