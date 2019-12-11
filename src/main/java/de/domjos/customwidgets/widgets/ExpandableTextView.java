/*
 * MamaPlanner
 * Copyright (C) 2019 Domjos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.domjos.customwidgets.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import de.domjos.customwidgets.R;
import de.domjos.customwidgets.utils.WidgetUtils;

import static android.view.Gravity.CENTER;

public class ExpandableTextView extends LinearLayout {
    private Context context;
    private AttributeSet attributeSet;
    private ImageView imageView;
    private TextView content, header;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.attributeSet = attributeSet;

        this.setOrientation(LinearLayout.VERTICAL);
        this.setWeightSum(10f);

        this.addControls();

        this.setOnClickListener(v -> {
            if (this.content.getVisibility() == GONE) {
                this.content.setVisibility(VISIBLE);
                this.imageView.setImageDrawable(WidgetUtils.getDrawable(this.context, R.drawable.ic_expand_more_black_24dp));
            } else {
                this.content.setVisibility(GONE);
                this.imageView.setImageDrawable(WidgetUtils.getDrawable(this.context, R.drawable.ic_expand_more_black_24dp));
            }
        });
    }

    public String getTitle() {
        return this.header.getText().toString();
    }

    public void setTitle(String title) {
        this.header.setText(title);
    }

    public String getContent() {
        return this.content.getText().toString();
    }

    public void setContent(CharSequence content) {
        this.content.setText(content);
    }

    public TextView getContextTextView() {
        return this.content;
    }

    private void addControls() {
        LinearLayout linearContent = new LinearLayout(this.context);
        linearContent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        linearContent.setOrientation(HORIZONTAL);
        linearContent.setWeightSum(10f);

        this.imageView = new ImageView(this.context);
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2);
        layoutParams.gravity = CENTER;
        this.imageView.setLayoutParams(layoutParams);
        this.imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
        linearContent.addView(this.imageView);

        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 8));
        linearLayout.setOrientation(VERTICAL);

        this.header = new TextView(this.context);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(3, 3, 3, 3);
        this.header.setLayoutParams(layoutParams);
        this.header.setTextSize(16);
        this.header.setTypeface(null, Typeface.BOLD);
        this.header.setPadding(3, 3, 3, 3);
        this.header.setText(this.getContentFromAttr(R.styleable.ExpandableTextView_title));
        linearLayout.addView(this.header);


        this.content = new TextView(this.context);
        this.content.setLayoutParams(layoutParams);
        this.content.setTextSize(14);
        this.content.setTypeface(null, Typeface.NORMAL);
        this.content.setPadding(3, 3, 3, 3);
        this.content.setVisibility(GONE);
        this.content.setText(this.getContentFromAttr(R.styleable.ExpandableTextView_text));
        linearLayout.addView(this.content);

        linearContent.addView(linearLayout);
        this.addView(linearContent);

        LinearLayout splitter = new LinearLayout(this.context);
        splitter.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1, 10f));
        splitter.setBackgroundColor(WidgetUtils.getColor(this.context, android.R.color.darker_gray));
        this.addView(splitter);
    }


    private String getContentFromAttr(int styleable) {
        if (this.attributeSet != null) {
            TypedArray array = this.context.obtainStyledAttributes(this.attributeSet, R.styleable.ExpandableTextView);
            for (int i = 0; i <= array.getIndexCount() - 1; i++) {
                int attribute = array.getIndex(i);
                if (attribute == styleable) {
                    return array.getString(attribute);
                }
            }
            array.recycle();
        }
        return "";
    }
}
