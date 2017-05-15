package hr.ferit.iveselin.studentvozi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button bAddRide, bFindRide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setUI();
        //check if loged in, if not redirect to loginActivity
    }

    private void setUI() {
        this.bAddRide= (Button) findViewById(R.id.bAddRide);
        this.bFindRide= (Button) findViewById(R.id.bFindRide);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bAddRide:

                break;
            case R.id.bFindRide:

                break;
        }
    }
}
