package hr.ferit.iveselin.studentvozi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
        bAddRide.setOnClickListener(this);
        bFindRide.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bAddRide:
                //temporary for testing map activity and fragment
                Intent sampleIntent=new Intent();
                sampleIntent.setClass(getApplicationContext(),LocationActivity.class);
                this.startActivity(sampleIntent);
                break;
            case R.id.bFindRide:

                break;
        }
    }
}
