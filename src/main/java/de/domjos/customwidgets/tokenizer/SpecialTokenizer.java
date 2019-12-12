package de.domjos.customwidgets.tokenizer;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView;

public class SpecialTokenizer implements MultiAutoCompleteTextView.Tokenizer {
    @Override
    public int findTokenStart(CharSequence charSequence, int i) {
        while(i > 0) {
            if(charSequence.charAt(i - 1)=='#' || charSequence.charAt(i - 1)=='@') {
                return i-1;
            }
            i--;
        }
        return 0;
    }

    @Override
    public int findTokenEnd(CharSequence charSequence, int i) {
        while(i > 0) {
            if(charSequence.charAt(i - 1)==' ') {
                return i;
            }
            i--;
        }
        return 0;
    }

    @Override
    public CharSequence terminateToken(CharSequence charSequence) {
        int i = charSequence.length();

        while (i > 0 && charSequence.charAt(i - 1) == '#' || charSequence.charAt(i - 1) == '@') {
            i--;
        }

        if (i > 0 && charSequence.charAt(i - 1) == ' ') {
            return charSequence;
        } else {
            if (charSequence instanceof Spanned) {
                SpannableString sp = new SpannableString(charSequence + "\n");
                TextUtils.copySpansFrom((Spanned) charSequence, 0, charSequence.length(), Object.class, sp, 0);
                return sp;
            } else {
                return charSequence + " ";
            }
        }
    }
}
