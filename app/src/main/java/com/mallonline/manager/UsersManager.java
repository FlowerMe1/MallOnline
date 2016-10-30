package com.mallonline.manager;

import com.activeandroid.query.Select;
import com.mallonline.models.User;

/**
 * Created by Dina on 10/09/2016.
 */

public class UsersManager {

    public final static String USER_ID = "id";
    public final static String ISLOGIN = "isLogin";

    private static UsersManager _instance;

    public static UsersManager getInstance() {
        if (_instance == null)
            _instance = new UsersManager();
        return _instance;
    }

    public User getCurrentUser(){
        return new Select().from(User.class).where(ISLOGIN + " = ? ", true).executeSingle();
    }

    public void logout(){
        User loginUser = new Select().from(User.class).where(ISLOGIN + " = ? ", true).executeSingle();
        loginUser.setLogin(false);
        loginUser.save();
    }



}
