/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.tokenizer;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView;

/**
 * Tokenizer-Class for MultiAutoCompleteTextView
 * Get Elements separated by comma
 * @see android.widget.MultiAutoCompleteTextView.Tokenizer
 * @see android.widget.MultiAutoCompleteTextView
 * @author Dominic Joas
 */
public class CommaTokenizer implements MultiAutoCompleteTextView.Tokenizer {

    /**
     * Class which finds the Start of the Token
     * @param text the Text
     * @param cursor the Cursor-Position
     * @return The Token-Start
     */
    @Override
    public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;

        while (i > 0 && text.charAt(i - 1) != ' ') {
            i--;
        }
        while (i < cursor && text.charAt(i) == '\n') {
            i++;
        }

        return i;
    }

    /**
     * Class which finds the End of the Token
     * @param text the Text
     * @param cursor the Cursor-Position
     * @return The Token-End
     */
    @Override
    public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();

        while (i < len) {
            if (text.charAt(i) == '\n') {
                return i;
            } else {
                i++;
            }
        }

        return len;
    }

    /**
     * Class to Terminate the Token
     * @param text The Text
     * @return The Text
     */
    @Override
    public CharSequence terminateToken(CharSequence text) {
        int i = text.length();

        while (i > 0 && text.charAt(i - 1) == ' ') {
            i--;
        }

        if (i > 0 && text.charAt(i - 1) == ' ') {
            return text;
        } else {
            if (text instanceof Spanned) {
                SpannableString sp = new SpannableString(text + "\n");
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                return sp;
            } else {
                return text + " ";
            }
        }
    }
}
