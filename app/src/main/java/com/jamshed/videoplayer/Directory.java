package com.jamshed.videoplayer;

import java.io.File;

public class Directory {
    private int count;
    private File path;

    public Directory(File file, int i) {
        this.path = file;
        this.count = i;
    }

    public File getPath() {
        return this.path;
    }

    public void setPath(File file) {
        this.path = file;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }
}
