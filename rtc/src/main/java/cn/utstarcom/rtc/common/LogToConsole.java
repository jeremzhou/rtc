/**
 * created on 2017年7月25日 下午4:33:47
 */
package cn.utstarcom.rtc.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author UTSC0167
 * @date 2017年7月25日
 *
 */
public final class LogToConsole {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss,SSS");

    /**
     * this is tool class, private constructor to prevent create instance.
     */
    private LogToConsole() {
        // do nothing.
    }

    public static final void out(String className, String msg) {
        System.out.println(printLog(className, msg));
    }

    public static final void err(String className, String msg) {
        System.err.println(printLog(className, msg));
    }

    private static final String printLog(String className, String msg) {

        return new StringBuilder(dateFormat.format(new Date())).append(" - [").append(className)
                .append("]: ").append(msg).toString();
    }
}
