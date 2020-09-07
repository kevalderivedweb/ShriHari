package com.example.shreehari.UserSession;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kshitij on 12/18/17.
 */

public class UserSession {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "UserSessionPref";

    // First time logic Check
    public static final String FIRST_TIME = "firsttime";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_REGNO = "KEY_REGNO";
    public static final String KEY_REGDATE = "KEY_REGDATE";
    public static final String KEY_PARENT_NAME = "name";
    public static final String KEY_MNAME = "KEY_MNAME";
    public static final String KEY_LNAME = "KEY_LNAME";
    public static final String KEY_USERTYPE = "KEY_USERTYPE";
    public static final String KEY_LANGUAGE = "KEY_LANGUAGE";
    public static final String KEY_CURRENCY = "KEY_CURRENCY";
    public static final String KEY_APITOKEN = "KEY_APITOKEN";
    public static final String KEY_PARENT_LNAME = "KEY_LNAME";
    public static final String KEY_isActive = "KEY_isActive";
    public static final String KEY_isEnablePushNotification = "KEY_isEnablePushNotification";
    public static final String KEY_USER_ID = "KEY_USER_ID";
    public static final String FIREBASE_TOKEN = "FIREBASE_TOKEN";
    public static final String KEY_PROFILE = "KEY_PROFILE";
    public static final String KEY_MOBILE = "KEY_MOBILE";
    public static final String KEY_COACH_STUDENT = "KEY_COACH_STUDENT";
    public static final String KEY_BRANCH = "KEY_BRANCH";
    public static final String KEY_PARENT = "KEY_PARENT";
    public static final String KEY_PARENT_USER_ID = "KEY_PARENT_USER_ID";
    public static final String KEY_SELECTED_USER_ID = "KEY_SELECTED_USER_ID";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PARENT_EMAIL = "PARENT_email";

    // Mobile number (make variable public to access from outside)
    public static final String KEY_MOBiLE = "mobile";
    public static final String KEY_PARENT_MOBiLE = "PARENT_mobile";

    // user avatar (make variable public to access from outside)
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_PARENT_PHOTO = "PARENT_photo";

    // number of items in our cart
    public static final String KEY_CART = "cartvalue";
    public static final String KEY_PINCODE = "KEY_PINCODE";
    public static final String KEY_ADDRESS = "KEY_ADDRESS";

    // number of items in our wishlist
    public static final String KEY_WISHLIST = "wishlistvalue";

    // check first time app launch
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // Constructor
    public UserSession(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void FirebaseToken(String token){
        editor.putString(FIREBASE_TOKEN, token);
        editor.commit();
    }

    public String getFirebaseToken() {
        return pref.getString(FIREBASE_TOKEN, "");
    }


    public void createLoginSession(String mobile_user_master_id
            , String first_name
            , String last_name
            , String profile_pic
            , String mobile_no
            , String email
            , String user_type
            , String coaching_student_id
            , String branch_id
            , String parent_id
            , String APIToken,String coaching_reg_no,String registered_date) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER_ID, mobile_user_master_id);
        editor.putString(KEY_PROFILE, profile_pic);
        editor.putString(KEY_MOBILE, mobile_no);
        editor.putString(KEY_COACH_STUDENT, coaching_student_id);
        editor.putString(KEY_BRANCH, branch_id);
        editor.putString(KEY_PARENT, parent_id);
        editor.putString(KEY_REGDATE, registered_date);
        editor.putString(KEY_REGNO, coaching_reg_no);

        editor.putString(KEY_NAME, first_name);
        editor.putString(KEY_LNAME, last_name);
        editor.putString(KEY_USERTYPE, user_type);
        editor.putString(KEY_APITOKEN, APIToken);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing phone number in pref
     //   editor.putString(KEY_MOBiLE, mobile);

        // Storing image url in pref
     //   editor.putString(KEY_PHOTO, photo);
    //    editor.putString(KEY_PARENT_PHOTO, photo);
      //  editor.putString(KEY_PARENT_MOBiLE, mobile);
        editor.putString(KEY_PARENT_EMAIL, email);
      //  editor.putString(KEY_PARENT_USER_ID, user_id);
     //   editor.putString(KEY_SELECTED_USER_ID, user_id);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_MNAME, pref.getString(KEY_MNAME, null));
        user.put(KEY_LNAME, pref.getString(KEY_LNAME, null));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user phone number
        user.put(KEY_MOBiLE, pref.getString(KEY_MOBiLE, null));

        // user avatar
        user.put(KEY_PHOTO, pref.getString(KEY_PHOTO, null));

        // return user
        return user;
    }

    /**
     * Get stored session data
     */
    public UserAccount getParentUserDetails() {
        // user name


        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(pref.getString(KEY_PARENT_NAME, null));
        userAccount.setLastname(pref.getString(KEY_PARENT_LNAME, null));
        userAccount.setImg(pref.getString(KEY_PARENT_PHOTO, null));
        userAccount.setEmail(pref.getString(KEY_PARENT_EMAIL, null));
        userAccount.setUserId(pref.getString(KEY_PARENT_USER_ID, null));
        return userAccount;
    }


    /**
     * Clear session details
     */
    public String getAddress() {
        return pref.getString(KEY_ADDRESS, "");
    }
    public String getUserType() {
        return pref.getString(KEY_USERTYPE, "");
    }
    public String getName() {
        return pref.getString(KEY_NAME, "");
    }

    public String getRegistrationNO() {
        return pref.getString(KEY_REGNO, "");
    }
    public String getRegDate() {
        return pref.getString(KEY_REGDATE, "");
    }
    public String getLastName() {
        return pref.getString(KEY_LNAME, "");
    }

    public String getAPIToken() {
        return pref.getString(KEY_APITOKEN, "");
    }

    public String getPicode() {
        return pref.getString(KEY_PINCODE, "");
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }


    public String getBranchId() {
        return pref.getString(KEY_BRANCH, "");
    }
    public String getProfile() {
        return pref.getString(KEY_PROFILE, "");
    }


    public String getUserImagd() {
        return pref.getString(KEY_PHOTO, "");
    }


    public void setFirstTime() {
        pref.edit().putBoolean("FIRST_TIME", true).commit();
    }


    public boolean getIsFirstTime() {
        return pref.getBoolean("FIRST_TIME", true);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean logout() {
        return pref.edit().clear().commit();
    }

    public int getCartValue() {
        return pref.getInt(KEY_CART, 0);
    }

    public int getWishlistValue() {
        return pref.getInt(KEY_WISHLIST, 0);
    }

    public Boolean getFirstTime() {
        return pref.getBoolean(FIRST_TIME, true);
    }

    public void setFirstTime(Boolean n) {
        editor.putBoolean(FIRST_TIME, n);
        editor.commit();
    }

    public void setSelctedUser(String user_id) {
        editor.putString(KEY_USER_ID, user_id);
        editor.commit();
    }


    public void increaseCartValue() {
        int val = getCartValue() + 1;
        editor.putInt(KEY_CART, val);
        editor.commit();
        Log.e("Cart Value PE", "Var value : " + val + "Cart Value :" + getCartValue() + " ");
    }

    public void increaseWishlistValue() {
        int val = getWishlistValue() + 1;
        editor.putInt(KEY_WISHLIST, val);
        editor.commit();
        Log.e("Cart Value PE", "Var value : " + val + "Cart Value :" + getCartValue() + " ");
    }

    public void decreaseCartValue() {
        int val = getCartValue() - 1;
        editor.putInt(KEY_CART, val);
        editor.commit();
        Log.e("Cart Value PE", "Var value : " + val + "Cart Value :" + getCartValue() + " ");
    }

    public void decreaseWishlistValue() {
        int val = getWishlistValue() - 1;
        editor.putInt(KEY_WISHLIST, val);
        editor.commit();
        Log.e("Cart Value PE", "Var value : " + val + "Cart Value :" + getCartValue() + " ");
    }

    public void setCartValue(int count) {
        editor.putInt(KEY_CART, count);
        editor.commit();
    }


    public void setAddress(String address) {
        editor.putString(KEY_ADDRESS, address);
        editor.commit();
    }

    public void setPincode(String pincode) {
        editor.putString(KEY_PINCODE, pincode);
        editor.commit();
    }


    public void setWishlistValue(int count) {
        editor.putInt(KEY_WISHLIST, count);
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}