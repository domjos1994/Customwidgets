package de.domjos.customwidgets.sample.activities;

import android.content.Intent;
import android.widget.Button;

import de.domjos.customwidgets.model.AbstractActivity;
import de.domjos.customwidgets.sample.R;

public class MainActivity extends AbstractActivity {
    private Button cmdMainList, cmdMainCalendar;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void initActions() {
        this.cmdMainList.setOnClickListener(event -> {
            Intent intent = new Intent(this.getApplicationContext(), SwipeRefreshDeleteListActivity.class);
            this.startActivity(intent);
        });
        this.cmdMainCalendar.setOnClickListener(event -> {
            Intent intent = new Intent(this.getApplicationContext(), CalendarActivity.class);
            this.startActivity(intent);
        });
    }

    @Override
    protected void initControls() {
        this.cmdMainList = this.findViewById(R.id.cmdMainList);
        this.cmdMainCalendar = this.findViewById(R.id.cmdMainCalendar);
    }
}
