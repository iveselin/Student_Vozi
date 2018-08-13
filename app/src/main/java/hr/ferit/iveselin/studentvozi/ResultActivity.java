package hr.ferit.iveselin.studentvozi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hr.ferit.iveselin.studentvozi.base.BaseActivity;

public class ResultActivity extends BaseActivity {

    private static final String TAG = "ResultActivity";

    public static Intent getLaunchIntent(Context fromContext) {
        return new Intent(fromContext, ResultActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }
}
