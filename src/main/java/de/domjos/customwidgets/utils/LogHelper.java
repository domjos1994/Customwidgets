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

package de.domjos.customwidgets.utils;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class LogHelper {
    private File logFile;


    public LogHelper(Activity activity) {
        try {
            this.logFile = new File(activity.getFilesDir().getAbsolutePath() + File.separatorChar + "error.log");
            if (!this.logFile.exists()) {
                if (this.logFile.createNewFile()) {
                    Log.v("Log-File", "NewFile");
                }
            }
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage(), ex);
        }
    }

    void logError(Exception ex) {
        try {
            StringBuilder builder = new StringBuilder(Objects.requireNonNull(ex.getMessage()));
            builder.append("\n");
            builder.append(ex.toString());
            for (StackTraceElement element : ex.getStackTrace()) {
                builder.append(String.format("%s:%s#%s(%s)%n", element.getFileName(), element.getClassName(), element.getMethodName(), element.getLineNumber()));
            }

            BufferedWriter buf = new BufferedWriter(new FileWriter(this.logFile, true));
            buf.append(builder.toString());
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            Log.e("Error", e.getMessage(), e);
        }
    }

    void logMessage(String message) {
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(this.logFile, true));
            buf.append(message);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            Log.e("Error", e.getMessage(), e);
        }
    }

    public void clearFile() {
        try {
            if (this.logFile.delete()) {
                if (this.logFile.createNewFile()) {
                    this.logMessage("Successfully create a new Log-File!");
                }
            }
        } catch (IOException e) {
            Log.e("Error", e.getMessage(), e);
        }
    }

    public void export(String folder) throws Exception {
        File exportFile = new File(folder + File.separatorChar + this.logFile.getName());
        this.copy(this.logFile, exportFile);
    }

    private void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }
}
