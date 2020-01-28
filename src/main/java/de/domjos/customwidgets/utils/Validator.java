/*
 * Copyright (C) 2017-2019  Dominic Joas
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import de.domjos.customwidgets.R;

public class Validator {
    private Map<Integer, Boolean> states;
    private Context context;
    private final int icon;

    public Validator(Context context, int icon) {
        this.context = context;
        this.states = new LinkedHashMap<>();
        this.icon = icon;
    }

    public void addEmptyValidator(final EditText txt) {
        states.put(txt.getId(), false);
        txt.setError(String.format(this.context.getString(R.string.message_validation_empty), txt.getHint()));
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                states.put(txt.getId(), !editable.toString().equals(""));
                if(editable.toString().equals("")) {
                    txt.setError(String.format(context.getString(R.string.message_validation_empty), txt.getHint()));
                } else {
                    txt.setError(null);
                }
            }
        });
    }

    public void addEmptyValidator(final Spinner sp, final String title) {
        if(sp.getSelectedItem()!=null) {
            if(sp.getSelectedItem().toString().equals("")) {
                states.put(sp.getId(), false);
                MessageHelper.printMessage(String.format(this.context.getString(R.string.message_validation_empty), title), this.icon, this.context);
            } else {
                states.put(sp.getId(), true);
            }
        }

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(((String)sp.getAdapter().getItem(position)).isEmpty()) {
                    MessageHelper.printMessage(String.format(context.getString(R.string.message_validation_empty), title), icon, context);
                    states.put(sp.getId(), false);
                } else {
                    states.put(sp.getId(), true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                MessageHelper.printMessage(String.format(context.getString(R.string.message_validation_empty), title), icon, context);
                states.put(sp.getId(), false);
            }
        });
    }

    public void addLengthValidator(final EditText txt, final int minLength, final int maxLength) {
        states.put(txt.getId(), txt.getText().length()<=maxLength && txt.getText().length()>=minLength);
        if(txt.getText().length()<=maxLength && txt.getText().length()>=minLength) {
            txt.setError(null);
        } else {
            txt.setError(String.format(this.context.getString(R.string.message_validator_length), txt.getHint(), maxLength, minLength));
        }

        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                states.put(txt.getId(), txt.getText().length()<=maxLength && txt.getText().length()>=minLength);
                if(txt.getText().length()<=maxLength && txt.getText().length()>=minLength) {
                    txt.setError(null);
                } else {
                    txt.setError(String.format(context.getString(R.string.message_validator_length), txt.getHint(), maxLength, minLength));
                }
            }
        });
    }

    public void addIntegerValidator(final EditText txt) {
        if(txt.getText().toString().isEmpty()) {
            states.put(txt.getId(), false);
            txt.setError(String.format(this.context.getString(R.string.message_validation_integer), txt.getHint()));
        } else {
            states.put(txt.getId(), true);
            txt.setError(null);
        }
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    states.put(txt.getId(), true);
                    txt.setError(null);
                } catch (Exception ex) {
                    states.put(txt.getId(), false);
                    txt.setError(String.format(context.getString(R.string.message_validation_integer), txt.getHint()));
                }
            }
        });
    }

    public void addDoubleValidator(final EditText txt) {
        if(!txt.getText().toString().equals("")) {
            states.put(txt.getId(), false);
            txt.setError(String.format(this.context.getString(R.string.message_validation_double), txt.getHint()));
        }
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    try {
                        states.put(txt.getId(), true);
                        txt.setError(null);
                    } catch (Exception ex) {
                        states.put(txt.getId(), false);
                        txt.setError(String.format(context.getString(R.string.message_validation_double), txt.getHint()));
                    }
                } else {
                    states.put(txt.getId(), true);
                    txt.setError(null);
                }
            }
        });
    }

    public void addDateValidator(final EditText txt) {
        this.validate(txt, R.string.message_validation_date, txt.getText().toString().equals(""));

        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateDate(txt, null, null, context.getString(R.string.date_format));
            }
        });
    }

    public void addDateValidator(final EditText txt, String format) {
        this.validate(txt, R.string.message_validation_date, txt.getText().toString().equals(""));

        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateDate(txt, null, null, format);
            }
        });
    }

    public void addDateValidator(final EditText txt, final Date minDate, final Date maxDate) {
        this.validate(txt, R.string.message_validation_date, txt.getText().toString().equals(""));

        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateDate(txt, minDate, maxDate, context.getString(R.string.date_format));
            }
        });
    }

    public void addDateValidator(final EditText txt, final Date minDate, final Date maxDate, String format) {
        this.validate(txt, R.string.message_validation_date, txt.getText().toString().equals(""));

        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateDate(txt, minDate, maxDate, format);
            }
        });
    }

    private void validateDate(EditText txt, Date minDate, Date maxDate, String format) {
        try {
            if(!txt.getText().toString().equals("")) {
                Date dt = Converter.convertStringToDate(txt.getText().toString(), format);
                if(dt==null) {
                    validate(txt, R.string.message_validation_date, false);
                } else {
                    validate(txt, 0, true);
                    if(minDate!=null) {
                        if(dt.before(minDate)) {
                            validate(txt, String.format(context.getString(R.string.message_validation_date_min), txt.getHint(), Converter.convertDateToString(minDate, format)), false);
                        } else {
                            validate(txt, "", true);
                        }
                    }
                    if(maxDate!=null) {
                        if(dt.after(maxDate)) {
                            validate(txt, String.format(context.getString(R.string.message_validation_date_max), txt.getHint(), Converter.convertDateToString(maxDate, format)), false);
                        } else {
                            validate(txt, "", true);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            validate(txt, R.string.message_validation_date, false);
        }
    }

    public boolean getState() {
        for(boolean state : states.values()) {
            if(!state) {
                return false;
            }
        }
        return true;
    }

    private void validate(EditText txt, int resID, boolean state) {
        states.put(txt.getId(), state);
        if(state) {
            txt.setError(null);
        } else {
            txt.setError(String.format(this.context.getString(resID), txt.getHint()));
        }
    }

    private void validate(EditText txt, String message, boolean state) {
        states.put(txt.getId(), state);
        if(state) {
            txt.setError(null);
        } else {
            txt.setError(message);
        }
    }
}
