package com.example.ekzhu.habittracker.data;

import android.provider.BaseColumns;

/**
 * Created by ekzhu on 15.07.2017.
 */

public final class NovelContract {

    private NovelContract() {}

    public static final class NovelEntry implements BaseColumns {

        public final static String TABLE_NAME = "novels";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NOVEL_NAME ="name";
        public final static String COLUMN_NOVEL_GENRE = "genre";
        public final static String COLUMN_READING_MODE = "reading_mode";
        public final static String COLUMN_TIME = "duration";

        public static final int MODE_NA = 0;
        public static final int MODE_PHYSICAL = 1;
        public static final int MODE_DIGITAL = 2;



    }
}
