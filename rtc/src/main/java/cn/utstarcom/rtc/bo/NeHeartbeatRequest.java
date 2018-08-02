/**
 * created on 2017年7月25日 下午12:07:23
 */
package cn.utstarcom.rtc.bo;

/**
 * @author UTSC0167
 * @date 2017年7月25日
 *
 */
public class NeHeartbeatRequest {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NeHeartbeatRequest [message=" + message + "]";
    }
}
