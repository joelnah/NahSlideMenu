package nah.prayer.nahslidemenudrawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private NahSlideMunu navi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navi = new NahSlideMunu(this);
        navi.setLeftBehindContentView(R.layout.layout_menu);
        navi.setOnCloseListner(new NahSlideMunu.OnCloseListner() {
            @Override
            public void onClose() {
            }

            @Override
            public void onView() {
            }
        }) ;

        ((Button) navi.findViewById(R.id.btn0)).setOnClickListener(click);
        ((Button) navi.findViewById(R.id.btn1)).setOnClickListener(click);
        ((Button) navi.findViewById(R.id.btn2)).setOnClickListener(click);

        ((Button) findViewById(R.id.menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navi.toggleLeftDrawer();
            }
        });

        ((RelativeLayout) findViewById(R.id.activity_main)).setOnClickListener(this);
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn0:
                    Toast.makeText(MainActivity.this,"TestBtn0 Click",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn1:
                    Toast.makeText(MainActivity.this,"TestBtn1 Click",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn2:
                    Toast.makeText(MainActivity.this,"TestBtn2 Click",Toast.LENGTH_SHORT).show();
                    break;
            }
            navi.closeLeftSide();
        }
    };

    @Override
    public void onBackPressed() {
        if (navi.isClosed()) {
            finish();
        } else {
            navi.closeLeftSide();
        }
    }


    @Override
    public void onClick(View v) {

    }
}
