package Utils;

public class TimeManager {
    public static String getTimesTamp()
    {
        return new java.text.SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(new java.util.Date());
    }
    public static String getSimpleTimesTamp()
    {
        return Long.toString(System.currentTimeMillis()).substring(Long.toString(System.currentTimeMillis()).length()-4);
    }
}
