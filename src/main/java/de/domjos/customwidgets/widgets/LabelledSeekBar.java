/*
 * Copyright (C) 2017-2019  Dominic Joas
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import de.domjos.customwidgets.R;
import de.domjos.customwidgets.utils.Helper;

/**
 * @author Dominic Joas
 */

public class LabelledSeekBar extends LinearLayout {
    private EditText txtLabel;
    private SeekBar sbBar;
    private CheckBox chkEnableLabel;
    private int mMax;
    private float mCurrent, realCurrent;
    private Context context;
    private Runnable runnable;

    public LabelledSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        this.initChildren();
        this.initProperties(attrs);
        this.initCheckBoxEvent();
        this.initEvents();
    }

    public boolean isEditableLabel() {
        return this.txtLabel.isEnabled();
    }

    public void setEditableLabel(boolean editableLabel) {
        this.txtLabel.setEnabled(editableLabel);
        invalidate();
        requestLayout();
    }

    public int getMax() {
        return this.mMax;
    }

    public void setMax(float max) {
        this.setMax((int) max);
    }

    public void setMax(int max) {
        this.mMax = max;
        this.sbBar.setMax(this.mMax * 100);
        invalidate();
        requestLayout();
    }

    public float getCurrent() {
        return this.mCurrent;
    }

    public void setCurrent(double current) {
        this.setCurrent((float) current, true);
    }

    public void setCurrent(float current) {
        this.setCurrent(current, true);
    }

    private void setCurrent(float current, boolean withControls) {
        this.mCurrent = current;
        this.realCurrent = this.mCurrent*100.0f;

        if(withControls) {
            this.sbBar.setProgress((int) this.realCurrent);
            this.txtLabel.setText(String.valueOf(this.mCurrent));
        }

        invalidate();
        requestLayout();
    }

    public void setOnChangeListener(Runnable runnable) {
        this.runnable = runnable;
    }

    private void initProperties(AttributeSet attrs) {
        TypedArray properties = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LabelledSeekBar, 0, 0);

        try {
            this.setEditableLabel(properties.getBoolean(R.styleable.LabelledSeekBar_editableLabel, false));
            this.setMax(properties.getInt(R.styleable.LabelledSeekBar_max, 0));
            this.setCurrent(properties.getFloat(R.styleable.LabelledSeekBar_current, 0.0f));

            this.txtLabel.setText(String.valueOf(mCurrent));
        } finally {
            properties.recycle();
        }
    }

    private void initChildren() {
        this.setWeightSum(20.0f);
        this.setOrientation(HORIZONTAL);

        this.sbBar = new SeekBar(this.context);
        this.sbBar.setLayoutParams(this.returnLayout(14.0f));
        this.addView(this.sbBar);

        this.txtLabel = new EditText(this.context);
        this.txtLabel.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        this.txtLabel.setTextSize(12.0f);
        this.txtLabel.setLayoutParams(this.returnLayout(3.0f));
        this.addView(this.txtLabel);

        this.chkEnableLabel = new CheckBox(context);
        this.chkEnableLabel.setChecked(!this.isEditableLabel());
        this.chkEnableLabel.setLayoutParams(this.returnLayout(3.0f));
        this.chkEnableLabel.setChecked(true);
        this.addView(this.chkEnableLabel);
    }

    private void initCheckBoxEvent() {
        this.chkEnableLabel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setEditableLabel(!b);
            }
        });
    }

    private void initEvents() {
        this.sbBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(chkEnableLabel.isChecked()) {
                    try {
                        float realValue = 0.0f;
                        if(sbBar.getProgress()!=0) {
                            realValue = sbBar.getProgress() / 100.0f;
                        }
                        setCurrent(realValue, false);
                        txtLabel.setText(String.valueOf(realValue));
                        if(runnable!=null) {
                            runnable.run();
                        }
                    } catch (Exception ex) {
                        Helper.printException(context, ex);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(!chkEnableLabel.isChecked()) {
                    try {
                        float realValue = 0.0f;
                        if(sbBar.getProgress()!=0) {
                            realValue = sbBar.getProgress() / 100.0f;
                        }
                        setCurrent(realValue, false);
                        txtLabel.setText(String.valueOf(realValue));
                        if(runnable!=null) {
                            runnable.run();
                        }
                    } catch (Exception ex) {
                        Helper.printException(context, ex);
                    }
                }
            }
        });

        this.txtLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    float current = Float.parseFloat(editable.toString());
                    setCurrent(current, false);
                    sbBar.setProgress((int) realCurrent);
                    if(runnable!=null) {
                        runnable.run();
                    }
                } catch (Exception ex) {
                    Helper.printException(context, ex);
                }
            }
        });
    }

    private LayoutParams returnLayout(float weight) {
        return new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight);
    }
}
