/**
 * created on 2018年6月9日 下午3:16:19
 */
package cn.utstarcom.rtc.common;

/**
 * @author UTSC0167
 * @date 2018年6月9日
 *
 */
public class RtcConstants {

    /**
     * this is tool class, private constructor to prevent create instance.
     */
    private RtcConstants() {
        // do nothing.
    }

    // probe message related field and value
    public static final String FIELD_OPT = "opt";
    
    public static final String FIELD_OPT_VALUE_GET = "get";

    public static final String FIELD_USER_ID = "user_id";

    public static final String FIELD_DATA = "data";
    
    public static final String FIELD_NAME = "name";
    
    public static final String FIELD_NAME_VALUE_EPGLOG1 = "epglog1";

    public static final String FIELD_HTTPSQS_TIME = "httpsqs_time";
    
    public static final String PROBE_VERSION_1_RETURN_PUT_OK = "PUT_OK";
    
    // constants string
    public static final String EMPTY_STRING = "";
    
    public static final String COLON = ":";
    
    public static final String ZERO_STRING = "0";
    
    //fixed probe message string
    public static final String FIX_QUESTION_MARK = "?";
    
    public static final String FIX_START_SCRIPT = "<script";
    
    public static final String FIX_END_SCRIPT = "/script>";
    
    public static final String FIX_VERTICAL_NUMBER = "|";
    
    public static final String FIX_LINE_FEED_CRLF = "\r\n";
    
    public static final String FIX_LINE_FEED_LF = "\n";
    
    // fixed probe message field value string
    public static final String FIX_VALUE_COMMA = ",";
    
    public static final String FIX_VALUE_STRING_FUNCTION_OBJECT = "function Object()";
    
    public static final String FIX_VALUE_STRING_NATIVE_CODE = "[native code]";
}
