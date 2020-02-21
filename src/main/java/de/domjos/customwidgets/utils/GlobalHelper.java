/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.utils;

import java.util.Locale;

public class GlobalHelper {

    public static Locale getLocale() {
        Locale locale = Locale.getDefault();
        if(locale.getLanguage().equals(Locale.GERMAN.getLanguage())) {
            return Locale.GERMAN;
        } else {
            return Locale.ENGLISH;
        }
    }
}
