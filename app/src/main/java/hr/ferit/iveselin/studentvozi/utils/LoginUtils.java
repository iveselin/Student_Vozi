package hr.ferit.iveselin.studentvozi.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginUtils {

    public static boolean checkLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }
}
