package at.dangl.mailclient.util;

public class Const {

    public static final String DISPLAY_NEW_LINE = "\n";
    public static final String DISPLAY_SUMMARY_WITH_EMPTY_ROW = "Summary: "+DISPLAY_NEW_LINE+DISPLAY_NEW_LINE;
    public static final String DISPLAY_FROM = "FROM: ";
    public static final String DISPLAY_SUBJECT = "SUBJECT: ";
    public static final String DISPLAY_TEXT = "TEXT: ";
    public static final String DISPLAY_ATTACHMENTS = "ATTACHMENTS: ";
    public static final String DISPLAY_FOLDER = "Folder: ";

    public static final String DISPLAY_EXCEL_INDEX_NAME = "INDEX_NAME: ";
    public static final String DISPLAY_EXCEL_INDEX_EMAIL = "INDEX_EMAIL: ";
    public static final String DISPLAY_SENT_COUNT = "Sent email count: ";
    public static final String DISPLAY_DURATION_SECONDS = "Duration in seconds around: ";

    public static final int ASYNC_CORE_POOLSIZE = 20;
    public static final int ASYNC_MAX_POOLSIZE = 50;
    public static final int ASYNC_QUEUE_CAPACITY = 100;
    public static long ASYNC_SLEEP_INTERVAL_START = 500;
    public static long ASYNC_SLEEP_INTERVAL_END = 1750;

    private Const(){

    }
}
