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
 * Get Elements separated by @ or #
 * @see android.widget.MultiAutoCompleteTextView.Tokenizer
 * @see android.widget.MultiAutoCompleteTextView
 * @author Dominic Joas
 */
public class SpecialTokenizer implements MultiAutoCompleteTextView.Tokenizer {

    /**
     * Class which finds the Start of the Token
     * @param text the Text
     * @param cursor the Cursor-Position
     * @return The Token-Start
     */
    @Override
    public int findTokenStart(CharSequence text, int cursor) {
        while(cursor > 0) {
            if(text.charAt(cursor - 1)=='#' || text.charAt(cursor - 1)=='@') {
                return cursor-1;
            }
            cursor--;
        }
        return 0;
    }

    /**
     * Class which finds the End of the Token
     * @param text the Text
     * @param cursor the Cursor-Position
     * @return The Token-End
     */
    @Override
    public int findTokenEnd(CharSequence text, int cursor) {
        while(cursor > 0) {
            if(text.charAt(cursor - 1)==' ') {
                return cursor;
            }
            cursor--;
        }
        return 0;
    }

    /**
     * Class to Terminate the Token
     * @param text The Text
     * @return The Text
     */
    @Override
    public CharSequence terminateToken(CharSequence text) {
        int i = text.length();

        while (i > 0 && text.charAt(i - 1) == '#' || text.charAt(i - 1) == '@') {
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
