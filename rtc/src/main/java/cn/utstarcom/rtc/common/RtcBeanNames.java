package cn.utstarcom.rtc.common;

public final class RtcBeanNames {

    /**
     * this is tool class, private constructor to prevent create instance.
     */
    private RtcBeanNames() {
        // do nothing.
    }
    
    // cn.utstarcom.rtc.config
    public static final String RTCCONFIGURATION = "rtcConfiguration";

    // cn.utstarcom.rtc.netty.http.server.action
    public static final String DATACOLLECTACTION = "dataCollectAction";
    public static final String DEFAULTACTION = "defaultAction";
    public static final String HELLOACTION = "helloAction";
    public static final String NEHEARTBEATACTION = "neHeartbeatAction";
    public static final String PRINTSWITCHACTION = "printSwitchAction";
    
    // cn.utstarcom.rtc.manage
    public static final String NEHEARTBEATMANAGE= "neHeartbeatManage";
    
    // cn.utstarcom.rtc.service.impl
    public static final String NEHEARTBEATSERVICE= "neHeartbeatService";
}
