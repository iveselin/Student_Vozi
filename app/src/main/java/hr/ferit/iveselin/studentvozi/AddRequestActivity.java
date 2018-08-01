package hr.ferit.iveselin.studentvozi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import hr.ferit.iveselin.studentvozi.base.BaseActivity;

public class AddRequestActivity extends BaseActivity {

    public static Intent getLaunchIntent(Context fromContext){
        return new Intent(fromContext, AddRequestActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

    }


}
