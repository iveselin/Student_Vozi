package hr.ferit.iveselin.studentvozi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.ferit.iveselin.studentvozi.base.BaseActivity;
import hr.ferit.iveselin.studentvozi.results.ResultActivity;
import hr.ferit.iveselin.studentvozi.utils.LoginUtils;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    public static Intent getLaunchIntent(Context fromContext) {
        return new Intent(fromContext, MainActivity.class);
    }

    @BindView(R.id.sign_in_out)
    Button signInOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUi();
    }

    private void setUi() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (LoginUtils.checkLogin()) {
            signInOut.setText(R.string.sign_out_text);
        } else {
            signInOut.setText(R.string.sign_in_text);
        }
    }

    @OnClick(R.id.sign_in_out)
    void onLoginLogoutClick() {
        if (LoginUtils.checkLogin()) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            signInOut.setText(R.string.sign_in_text);

        } else {
            startActivity(LoginActivity.getLaunchIntent(this));
        }
    }

    @OnClick(R.id.add_request)
    void onLocateClick() {
        startActivity(AddRequestActivity.getLaunchIntent(this));
    }

    @OnClick(R.id.show_rides)
    void onShowRidesCLick() {
        startActivity(ResultActivity.getLaunchIntent(this));
    }
}
