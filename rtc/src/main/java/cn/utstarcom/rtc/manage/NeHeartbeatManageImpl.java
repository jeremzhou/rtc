/**
 * created on 2017年9月6日 上午9:48:31
 */
package cn.utstarcom.rtc.manage;

import org.springframework.stereotype.Repository;

import cn.utstarcom.rtc.common.RtcBeanNames;

/**
 * @author UTSC0167
 * @date 2017年9月6日
 *
 */
@Repository(RtcBeanNames.NEHEARTBEATMANAGE)
public class NeHeartbeatManageImpl implements INeHeartbeatManage {

    @Override
    public int getNeStatus() {

        return 0;
    }

}
