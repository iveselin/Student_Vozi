package hr.ferit.iveselin.studentvozi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class AddRequestActivity extends AppCompatActivity implements View.OnClickListener{
    Button bSubmit;
    DatePicker dpSetDate;
    TimePicker tpSetTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        this.setUI();
        //implementirati provjeru što se treba dodati, ponuda ili potražnja te ovisno o tome ispuniti ekran

    }

    private void setUI() {
        this.bSubmit= (Button) findViewById(R.id.bSubmit);
        this.dpSetDate= (DatePicker) findViewById(R.id.dpDateSelect);

        //treba sa fragmentima, bit će zajebano
    }

    @Override
    public void onClick(View v) {

    }
}
