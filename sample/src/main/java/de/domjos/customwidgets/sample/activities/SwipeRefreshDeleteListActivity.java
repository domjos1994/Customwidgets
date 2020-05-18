package de.domjos.customwidgets.sample.activities;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.domjos.customwidgets.model.AbstractActivity;
import de.domjos.customwidgets.model.BaseDescriptionObject;
import de.domjos.customwidgets.sample.R;
import de.domjos.customwidgets.widgets.swiperefreshdeletelist.SwipeRefreshDeleteList;

public class SwipeRefreshDeleteListActivity extends AbstractActivity {
    private SwipeRefreshDeleteList lvList;
    private EditText txtTitle, txtSubTitle;
    private Button cmdAdd;

    public SwipeRefreshDeleteListActivity() {
        super(R.layout.activity_swipe_refresh_delete_list);
    }

    @Override
    protected void initActions() {
        this.cmdAdd.setOnClickListener(event -> {
            BaseDescriptionObject baseDescriptionObject = new BaseDescriptionObject();
            baseDescriptionObject.setTitle(this.txtTitle.getText().toString());
            baseDescriptionObject.setDescription(this.txtSubTitle.getText().toString());
            this.lvList.getAdapter().add(baseDescriptionObject);

            this.txtTitle.setText("");
            this.txtSubTitle.setText("");
        });

        this.lvList.setOnDeleteListener(listObject -> Toast.makeText(this.getApplicationContext(), listObject.getTitle() + " deleted!", Toast.LENGTH_LONG).show());
    }

    @Override
    protected void initControls() {
        this.lvList = this.findViewById(R.id.lvList);

        this.txtTitle = this.findViewById(R.id.txtTitle);
        this.txtSubTitle = this.findViewById(R.id.txtSubTitle);
        this.cmdAdd = this.findViewById(R.id.cmdAdd);
    }
}
