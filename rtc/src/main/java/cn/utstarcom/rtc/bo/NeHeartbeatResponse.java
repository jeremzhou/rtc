/**
 * created on 2017年7月25日 下午12:07:35
 */
package cn.utstarcom.rtc.bo;

/**
 * @author UTSC0167
 * @date 2017年7月25日
 *
 */
public class NeHeartbeatResponse {

    private int appType;
    private String appVersion;
    private int serviceStatus;

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    @Override
    public String toString() {
        return "NeHeartbeatResponse [appType=" + appType + ", appVersion=" + appVersion
                + ", serviceStatus=" + serviceStatus + "]";
    }
}
