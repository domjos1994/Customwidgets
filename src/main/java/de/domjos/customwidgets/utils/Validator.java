/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.utils;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.domjos.customwidgets.R;
import de.domjos.customwidgets.model.BaseDescriptionObject;

public class Validator {
    private StringBuilder result;

    private Map<View, String> messages;
    private Map<View, ValidationExecutor> validationExecutors;
    private Context context;
    private final int icon;

    public Validator(Context context, int icon) {
        this.context = context;
        this.icon = icon;

        this.result = new StringBuilder();
        this.messages = new LinkedHashMap<>();
        this.validationExecutors = new LinkedHashMap<>();
    }

    public void addEmptyValidator(final EditText field) {
        if(field.getHint()!=null && !field.getHint().toString().isEmpty()) {
            field.setHint(field.getHint() + " *");
        }

        this.validationExecutors.put(field, () -> {
            if(field.getText() != null) {
                return !field.getText().toString().isEmpty();
            }
            return false;
        });
        this.messages.put(field, String.format(this.context.getString(R.string.message_validation_empty), field.getHint()));
    }

    public void addEmptyValidator(final Spinner field, final String title) {
        this.validationExecutors.put(field, () -> {
            if(field.getSelectedItem() != null) {
                return !field.getSelectedItem().toString().equals("");
            }
            return false;
        });
        this.messages.put(field, String.format(this.context.getString(R.string.message_validation_empty), title));
    }

    public void addLengthValidator(final EditText field, final int minLength, final int maxLength) {
        this.validationExecutors.put(field, () -> {
            if(field.getText()!=null) {
                return field.getText().length() <= maxLength && field.getText().length() >= minLength;
            }
            return false;
        });
        this.messages.put(field, String.format(this.context.getString(R.string.message_validator_length), field.getHint(), maxLength, minLength));
    }

    public void addIntegerValidator(final EditText field) {
        this.validationExecutors.put(field, () -> {
           if(field.getText() != null && !field.getText().toString().isEmpty()) {
               try {
                   Integer.parseInt(field.getText().toString());
                   return true;
               } catch (Exception ignored) {}
           }
           return false;
        });
        this.messages.put(field, String.format(context.getString(R.string.message_validation_integer), field.getHint()));
    }

    public void addDoubleValidator(final EditText field) {
        this.validationExecutors.put(field, () -> {
            if(field.getText() != null && !field.getText().toString().isEmpty()) {
                try {
                    Double.parseDouble(field.getText().toString());
                    return true;
                } catch (Exception ignored) {}
            }
            return false;
        });
        this.messages.put(field, String.format(context.getString(R.string.message_validation_double), field.getHint()));
    }

    public void addDateValidator(final EditText field) {
        this.validationExecutors.put(field, () -> {
            if(field.getText() != null && !field.getText().toString().isEmpty()) {
                try {
                    ConvertHelper.convertStringToDate(field.getText().toString(), this.context);
                    return true;
                } catch (Exception ignored) {}
            }
            return false;
        });
        this.messages.put(field, String.format(context.getString(R.string.message_validation_date), field.getHint()));
    }

    public void addDateValidator(final EditText field, String format) {
        this.validationExecutors.put(field, () -> {
            if(field.getText() != null && !field.getText().toString().isEmpty()) {
                try {
                    ConvertHelper.convertStringToDate(field.getText().toString(), format);
                    return true;
                } catch (Exception ignored) {}
            }
            return false;
        });
        this.messages.put(field, String.format(context.getString(R.string.message_validation_date), field.getHint()));
    }

    public void addDateValidator(final EditText field, final Date minDate, final Date maxDate) {
        this.validationExecutors.put(field, () -> {
            if(field.getText() != null && !field.getText().toString().isEmpty()) {
                try {
                    Date dt = ConvertHelper.convertStringToDate(field.getText().toString(), this.context);
                    if(dt != null) {
                        if(minDate!=null && maxDate!=null) {
                            if(dt.after(minDate) && dt.before(maxDate)) {
                                return true;
                            }
                        } else if(minDate!=null) {
                            if(dt.after(minDate)) {
                                return true;
                            }
                        } else if(maxDate!=null) {
                            if(dt.before(maxDate)) {
                                return true;
                            }
                        } else {
                            return true;
                        }
                    }
                } catch (Exception ignored) {}
            }
            return true;
        });
        this.messages.put(field, String.format(context.getString(R.string.message_validation_date_min_max), field.getHint(), ConvertHelper.convertDateToString(minDate, this.context), ConvertHelper.convertDateToString(maxDate, this.context)));
    }

    public void addDateValidator(final EditText field, final Date minDate, final Date maxDate, String format) {
        this.validationExecutors.put(field, () -> {
            if(field.getText() != null && !field.getText().toString().isEmpty()) {
                try {
                    Date dt = ConvertHelper.convertStringToDate(field.getText().toString(), format);
                    if(dt!=null) {
                        if(minDate!=null && maxDate!=null) {
                            if(dt.after(minDate) && dt.before(maxDate)) {
                                return true;
                            }
                        } else if(minDate!=null) {
                            if(dt.after(minDate)) {
                                return true;
                            }
                        } else if(maxDate!=null) {
                            if(dt.before(maxDate)) {
                                return true;
                            }
                        } else {
                            return true;
                        }
                    }
                } catch (Exception ignored) {}
            }
            return true;
        });
        this.messages.put(field, String.format(context.getString(R.string.message_validation_date_min_max), field.getHint(), ConvertHelper.convertDateToString(minDate, format), ConvertHelper.convertDateToString(maxDate, format)));
    }

    public boolean checkDuplicatedEntry(String value, long id, List<BaseDescriptionObject> items) {
        boolean isOk = true;
        String itemToSave = value.trim().toLowerCase();
        for(BaseDescriptionObject baseDescriptionObject : items) {
            if(id != baseDescriptionObject.getId() || id == 0) {
                String item = baseDescriptionObject.getTitle().trim().toLowerCase();

                if(itemToSave.equals(item)) {
                    isOk = false;
                }
            }
        }

        if(!isOk) {
            MessageHelper.printMessage(String.format(this.context.getString(R.string.message_validator_duplicated), value), this.icon, this.context);
        }
        return isOk;
    }

    public boolean getState() {
        this.result = new StringBuilder();
        boolean state = true;
        for(Map.Entry<View, ValidationExecutor> executorEntry : this.validationExecutors.entrySet()) {
            if(!executorEntry.getValue().validate()) {
                if(executorEntry.getKey() instanceof EditText) {
                    ((EditText) executorEntry.getKey()).setError(this.messages.get(executorEntry.getKey()));
                } else {
                    MessageHelper.printMessage(this.messages.get(executorEntry.getKey()), this.icon, this.context);
                }
                this.result.append(this.messages.get(executorEntry.getKey())).append("\n");
                state = false;
            } else {
                if(executorEntry.getKey() instanceof EditText) {
                    ((EditText) executorEntry.getKey()).setError(null);
                }
            }
        }
        if(state) {
            this.clear();
        }

        return state;
    }

    public String getResult() {
        return this.result.toString();
    }

    public void clear() {
        this.result = new StringBuilder();
        for(View view : this.validationExecutors.keySet()) {
            if(view instanceof EditText) {
                ((EditText) view).setError(null);
            }
        }
    }

    @FunctionalInterface
    public interface ValidationExecutor {
        boolean validate();
    }
}
