/**
 * created on 2018年6月9日 上午10:41:42
 */
package cn.utstarcom.rtc.util;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.utstarcom.rtc.common.RtcConstants;

/**
 * @author UTSC0167
 * @date 2018年6月9日
 *
 */
public class RtcUtilTest {

    private static final Logger log = LoggerFactory.getLogger(RtcUtilTest.class);

    private static final String okContent = "{\"action_type\":\"tv_playing\",\"user_id\":\"0760251999910000549529\",\"user_group_id\":\"888\",\"epg_group_id\":\"147\",\"epg_url\":\"http://10.36.61.2/portal.php\",\"terminal_type\":\"linux_STB\",\"stb_ip\":\"128.12.391.144\",\"page_title\":\"\",\"mediacode\":\"00000001000000050000000000000260\",\"channel_num\":\"92384\",\"physical_addr\":\"channel9\",\"start_playing_time\":\"1474880656\",\"stb_type\":\"HG600\",\"playing_duration\":\"0\"}";

    @Test
    public void testReplace() {

        String testContent = "{\"action_type\":\"tv_playing\",\"user_?id\":\"0760251999910000549529\",\"user_group_id\":\"888\",\"epg_group_id\":\"147\",\"epg_url\":\"http://10.36.61.2/portal.php\",\"terminal_type\":\"linux_STB\",\"stb_ip\":\"128.12.391.144\",\"page_title\":\"\",\"mediacode\":\"00000001000000050000000000000260\",\"channel_num\":\"92384\",\"physical_addr\":\"channel9\",\"start_playing_time\":\"1474880656?\",\"stb_type\":\"HG600\",\"playing_duration\":\"0\"}?";

        StringBuilder sBuilder = new StringBuilder(testContent);
        assertThat(RtcUtil.replace(sBuilder, "?", RtcConstants.EMPTY_STRING).toString(),
                equalTo(okContent));

        testContent = "{\"action_type\"|\"tv_playing\",\"user_id\":\"0760251999910000549529\",\"user_group_id\"|\"888\",\"epg_group_id\":\"147\",\"epg_url\":\"http://10.36.61.2/portal.php\",\"terminal_type\":\"linux_STB\",\"stb_ip\":\"128.12.391.144\",\"page_title\":\"\",\"mediacode\":\"00000001000000050000000000000260\",\"channel_num\":\"92384\",\"physical_addr\":\"channel9\",\"start_playing_time\":\"1474880656\",\"stb_type\":\"HG600\",\"playing_duration\":\"0\"}";
        sBuilder = new StringBuilder(testContent);
        assertThat(RtcUtil.replace(sBuilder, RtcConstants.FIX_VERTICAL_NUMBER, RtcConstants.COLON)
                .toString(), equalTo(okContent));

        testContent = "{\"action_type\":\"tv_playing\",\"user_id\":\"0760251999910000549529<script>hsdfhsfsdfsd</script>\",\"user_group_id\":\"888\",\"epg_group_id\":\"147\",\"epg_url\":\"http://10.36.61.2/portal.php\",\"terminal_type\":\"linux_STB\",\"stb_ip\":\"128.12.391.144\",\"page_title\":\"\",\"mediacode\":\"00000001000000050000000000000260\",\"channel_num\":\"92384\",\"physical_addr\":\"channel9\",\"start_playing_time\":\"1474880656\",\"stb_type\":\"HG600\",\"playing_duration\":\"0\"}";
        sBuilder = new StringBuilder(testContent);
        RtcUtil.replace(sBuilder, "<", RtcConstants.EMPTY_STRING);
        RtcUtil.replace(sBuilder, ">", RtcConstants.EMPTY_STRING);
        assertThat(RtcUtil.replace(sBuilder, "script", "/script", RtcConstants.EMPTY_STRING)
                .toString(), equalTo(okContent));
        sBuilder = new StringBuilder(testContent);
        assertThat(
                RtcUtil.replace(sBuilder, RtcConstants.FIX_START_SCRIPT,
                        RtcConstants.FIX_END_SCRIPT, RtcConstants.EMPTY_STRING).toString(),
                equalTo(okContent));

        testContent = "{\"action_type\":\"tv_playing\",\"user_id\":\"0760251999910000549529 <script>hsdfhsfsdfsd</script> \",\"user_group_id\":\"888\",\"epg_group_id\":\"147\",\"epg_url\":\"http://10.36.61.2/portal.php\",\"terminal_type\":\"linux_STB\",\"stb_ip\":\"128.12.391.144\",\"page_title\":\"\",\"mediacode\":\"00000001000000050000000000000260\",\"channel_num\":\"92384\",\"physical_addr\":\"channel9\",\"start_playing_time\":\"1474880656\",\"stb_type\":\"HG600\",\"playing_duration\":\"0\"}";
        sBuilder = new StringBuilder(testContent);
        assertThat(RtcUtil.replace(sBuilder, " <script", "/script> ", RtcConstants.EMPTY_STRING)
                .toString(), equalTo(okContent));

        testContent = "{\"action_type\":\"tv_playing\",\"user_id\":\"0760251999910000549529 <script>{\"action_type\":\"tv_playing\",\"user_id\":\"0760251999910000549529\",\"user_group_id\":\"888\",\"epg_group_id\":\"147\",\"epg_url\":\"http://10.36.61.2/portal.php\",\"terminal_type\":\"linux_STB\",\"stb_ip\":\"128.12.391.144\",\"page_title\":\"\",\"mediacode\":\"00000001000000050000000000000260\",\"channel_num\":\"92384\",\"physical_addr\":\"channel9\",\"start_playing_time\":\"1474880656\",\"stb_type\":\"HG600\",\"playing_duration\":\"0\"}</script> \",\"user_group_id\":\"888\",\"epg_group_id\":\"147\",\"epg_url\":\"http://10.36.61.2/portal.php\",\"terminal_type\":\"linux_STB\",\"stb_ip\":\"128.12.391.144\",\"page_title\":\"\",\"mediacode\":\"00000001000000050000000000000260\",\"channel_num\":\"92384\",\"physical_addr\":\"channel9\",\"start_playing_time\":\"1474880656\",\"stb_type\":\"HG600\",\"playing_duration\":\"0\"}";
        sBuilder = new StringBuilder(testContent);
        assertThat(RtcUtil.replace(sBuilder, " <script", "/script> ", RtcConstants.EMPTY_STRING)
                .toString(), equalTo(okContent));

        testContent = "{\"action_type\":\"tv_playing\",\"user_id\":\"0760251999910000549529 <script>hsdfhsfsdfsd</script> \",\"user_group_id\":\"888\",\"epg_group_id\":\"147\",\"epg_url\":\"http://10.36.61.2/portal.php\", <script>hsdfhsfsdfsd</script> \"terminal_type\":\"linux_STB\", <script>hsdfhsfsdfsd</script> \"stb_ip\":\"128.12.391.144\",\"page_title\":\"\",\"mediacode\":\"00000001000000050000000000000260\",\"channel_num\":\"92384\",\"physical_addr\":\"channel9\",\"start_playing_time\":\"1474880656\",\"stb_type\":\"HG600\",\"playing_duration\":\"0\"}";
        sBuilder = new StringBuilder(testContent);
        assertThat(RtcUtil.replace(sBuilder, " <script", "/script> ", RtcConstants.EMPTY_STRING)
                .toString(), equalTo(okContent));

        testContent = "{\"action_type\":\"tv_playing\",\"user_?id\":\"0760251999910000549529\",\"user_group_id\":\"888\",\"epg_group_id\":\"147\",\"epg_url\":\"http://10.36.61.2/portal.php\",\"terminal_type\":\"linux_STB\",\"stb_ip\":\"128.12.391.144\",\"page_title\":\"\",\"mediacode\":\"00000001000000050000000000000260\",\"channel_num\":\"92384\",\"physical_addr\":\"channel9\",\"start_playing_time\":\"1474880656?\",\"httpsqs_time\":\"1474880656\",\"stb_type\":\"HG600\",\"playing_duration\":\"0\"}?";
        sBuilder = new StringBuilder(testContent);
        assertThat(RtcUtil.replace(sBuilder, "action", "action", RtcConstants.EMPTY_STRING),
                nullValue());
        assertThat(RtcUtil.replace(sBuilder, "action", "ct", RtcConstants.EMPTY_STRING),
                nullValue());

        final String okData2 = "{\"action_type\":\"vod_playing\",\"sys_id\":\"< nativecode > isd fsdfsdfsdfsd\",\"user_id\":\"i520601748@itv\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"stb_ip\":\"172.34.28.137\",\"stb_id\":\"F010049900703720000004957304F286\",\"stb_type\":\"B860AV1.1-T2\",\"stb_mac\":\"04:95:73:04:F2:86\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1529462637\",\"mediacode\":\"30000001000000010000000013896438\",\"seriescode\":\"30000001000000010000000013896438\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"start_time\":\"1529406453\",\"currentplaytime\":\"4300\",\"refer_type\":\"3\",\"refer_page_id\":\"P006T02A000\",\"refer_page_name\":\"精选漫影-详情页\",\"refer_pos_id\":\"Anav0Pnav0_0\",\"refer_pos_name\":\"c286::p30000001000000010000000013896438\",\"refer_mediacode\":\"\",\"refer_parent_id\":\"286\",\"httpsqs_time\":\"1530156238\"}";
        String data = "{\"action_type\":\"vod_playing\",\"sy\ns_id\":\"< nativecode > isd\n fsdfsdfsdfsd\",\"user_id\":\"i520601748@itv\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"stb_ip\":\"172.34.28.137\",\"stb_id\":\"F010049900703720000004957304F286\",\"stb_type\":\"B860AV1.1-T2\",\"stb_mac\":\"04:95:73:04:F2:86\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1529462637\",\"mediacode\":\"30000001000000010000000013896438\",\"seriescode\":\"30000001000000010000000013896438\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"start_time\":\"1529406453\",\"currentplaytime\":\"4300\",\"refer_type\":\"3\",\"refer_page_id\":\"P006T02A000\",\"refer_page_name\":\"精选漫影-详情页\",\"refer_pos_id\":\"Anav0Pnav0_0\",\"refer_pos_name\":\"c286::p30000001000000010000000013896438\",\"refer_mediacode\":\"\",\"refer_parent_id\":\"286\",\"httpsqs_time\":\"1530156238\"}";
        assertThat(data.contains(RtcConstants.FIX_LINE_FEED_LF), equalTo(true));
        sBuilder = new StringBuilder(data);
        RtcUtil.replace(sBuilder, RtcConstants.FIX_LINE_FEED_LF, RtcConstants.EMPTY_STRING);
        assertThat(sBuilder.toString(), equalTo(okData2));

        data = "{\"action_type\":\"vod_playing\",\"sy\r\ns_id\":\"< nativecode > isd\r\n fsdfsdfsdfsd\",\"user_id\":\"i520601748@itv\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"stb_ip\":\"172.34.28.137\",\"stb_id\":\"F010049900703720000004957304F286\",\"stb_type\":\"B860AV1.1-T2\",\"stb_mac\":\"04:95:73:04:F2:86\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1529462637\",\"mediacode\":\"30000001000000010000000013896438\",\"seriescode\":\"30000001000000010000000013896438\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"start_time\":\"1529406453\",\"currentplaytime\":\"4300\",\"refer_type\":\"3\",\"refer_page_id\":\"P006T02A000\",\"refer_page_name\":\"精选漫影-详情页\",\"refer_pos_id\":\"Anav0Pnav0_0\",\"refer_pos_name\":\"c286::p30000001000000010000000013896438\",\"refer_mediacode\":\"\",\"refer_parent_id\":\"286\",\"httpsqs_time\":\"1530156238\"}";
        assertThat(data.contains(RtcConstants.FIX_LINE_FEED_CRLF), equalTo(true));
        sBuilder = new StringBuilder(data);
        RtcUtil.replace(sBuilder, RtcConstants.FIX_LINE_FEED_CRLF, RtcConstants.EMPTY_STRING);
        assertThat(sBuilder.toString(), equalTo(okData2));

        final String okData3 = "{\"action_type\":\"browsing\",\"sys_id\":\"t\",\"user_id\":\"057642557211\",\"user_group_id\":\"-1\",\"epg_group_id\":\"defaultsmchd\",\"stb_ip\":\"10.130.105.163\",\"stb_id\":\"2210029900701150000030F31DBD297C\",\"stb_type\":\"B700V2A\",\"stb_mac\":\"28:6E:D4:50:75:88\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1493023076\",\"mediacode\":\"02000000000000050000000000000669\",\"seriescode\":\"\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"currentplaytime\":\"360\",\"refer_type\":\"3\",\"page_id\":\"123\",\"refer_page_id\":\"12\",\"refer_page_name\":\"电影列表页\",\"refer_pos_id\":\"1\",\"schedule_code\":\"123\",\"refer_pos_name\":\"\",\"refer_parent_id\":\"02000000000000050000000000000265\",\"mediaduration\":\"7870\"}";
        data = "{\"action_type\":\"browsing\",\"sys_id\":\"t\",\"user_id\":\"057642557211\",\"user_group_id\":\"-1\",\"epg_group_id\":\"defaultsmchd\",\"stb_ip\":\"10.130.105.163\",\"stb_id\":\"2210\n0299007011500\n00030F31DBD297C\",\"stb_type\":\"B700V2A\",\"stb_mac\":\"28:6E:D4:50:75:88\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1493023076\",\"mediacode\":\"02000000000000050000000000000669\",\"seriescode\":\"\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"currentplaytime\":\"360\",\"refer_type\":\"3\",\"page_id\":\"123\",\"refer_page_id\":\"12\",\"refer_page_name\":\"电影列表页\",\"refer_pos_id\":\"1\",\"schedule_code\":\"123\",\"refer_pos_name\":\"\",\"refer_parent_id\":\"02000000000000050000000000000265\",\"mediaduration\":\"7870\"}";
        log.info("testReplace data: {} contains RtcConstants.FIX_LINE_FEED_LF: {}", data,
                data.contains(RtcConstants.FIX_LINE_FEED_LF));
        assertThat(data.contains(RtcConstants.FIX_LINE_FEED_LF), equalTo(true));
        sBuilder = new StringBuilder(data);
        RtcUtil.replace(sBuilder, RtcConstants.FIX_LINE_FEED_LF, RtcConstants.EMPTY_STRING);
        assertThat(sBuilder.toString(), equalTo(okData3));
    }

    @Test
    public void testJson2LinkedHashMap() {

        LinkedHashMap<String, String> jsonMap = RtcUtil.json2LinkedHashMap(okContent);
        String json = RtcUtil.linkedHashMap2Json(jsonMap);
        log.info("testJson2LinkedHashMap: {}", json);
        assertThat(json, equalTo(okContent));

        long curUnixTime = RtcUtil.getCurrentUnixTime();
        String httpsqs_time = "httpsqs_time";
        String addContent = okContent.substring(0, okContent.length() - 1) + ",\"httpsqs_time\":\""
                + curUnixTime + "\"}";
        jsonMap.put(httpsqs_time, curUnixTime + RtcConstants.EMPTY_STRING);
        json = RtcUtil.linkedHashMap2Json(jsonMap);
        log.info("testJson2LinkedHashMap: {}", json);
        assertThat(json, equalTo(addContent));

        String testContent = "{\"action_type\":\"tv_playing\",\"user_id\":\"    \"}";
        jsonMap = RtcUtil.json2LinkedHashMap(testContent);
        assertThat(jsonMap.get("user_id").trim(), equalTo(RtcConstants.EMPTY_STRING));
        
        String addContent2 = okContent.substring(0, okContent.length() - 1) + ",\"httpsqs_time\":"
                + curUnixTime + "}";
        jsonMap = RtcUtil.json2LinkedHashMap(addContent2);
        json = RtcUtil.linkedHashMap2Json(jsonMap);
        log.info("addContent2 testJson2LinkedHashMap: {}", json);
        assertThat(json, equalTo(addContent));
    }

    @Test
    public void testStringReplace() {

        final String okData = "{\"action_type\":\"vod_playing\",\"sys_id\":\"t\",\"user_id\":\"i5201139516@itv\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"stb_ip\":\"173.65.8.54\",\"stb_id\":\"0010039900E0680005165CE3B6CE91C0\",\"stb_type\":\"HG680-JDF9H-52\",\"stb_mac\":\"5C:E3:B6:CE:91:C0\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1528788361\",\"mediacode\":\"30000001000000010000000014155237\",\"seriescode\":\"30000001000000010000000014155237\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"start_time\":\"1528786921\",\"currentplaytime\":\"1532\",\"refer_type\":\"3\",\"refer_page_id\":\"P010T02A000\",\"refer_page_name\":\"时光剧场-电影详情页\",\"refer_pos_id\":\"A0P0\",\"refer_pos_name\":\"c4966::p30000001000000010000000014155237\",\"refer_mediacode\":\"\",\"refer_parent_id\":\"4966\"}";

        String data = "{\"action_type\":\"vod_playing\",\"sys_id\":\"t\",\"user_id\":\"i5201139516@itv\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"stb_ip\":\"173.65.8.54\",\"stb_id\":\"0010039900E0680005165CE3B6CE91C0\",\"stb_type\":\"HG680-JDF9H-52\",\"stb_mac\":\"5C:E3:B6:CE:91:C0\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1528788361\",\"mediacode\":\"30000001000000010000000014155237\",\"seriescode\":\"30000001000000010000000014155237\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"start_time\":\"1528786921\",\"currentplaytime\":\"1532\",\"refer_type\":\"3\",\"refer_page_id\":\"P010T02A000\",\"refer_page_name\":\"时光剧场-电影详情页\",\"refer_pos_id\":\"A0P0\",\"refer_pos_name\":\"c4966||p30000001000000010000000014155237\",\"refer_mediacode\":\"\",\"refer_parent_id\":\"4966\"}";

        assertThat(data.replaceAll("\\" + RtcConstants.FIX_VERTICAL_NUMBER, RtcConstants.COLON),
                equalTo(okData));
        assertThat(data.contains(RtcConstants.FIX_VERTICAL_NUMBER), equalTo(true));
        StringBuilder sBuilder = new StringBuilder(data);
        assertThat(RtcUtil.replace(sBuilder, RtcConstants.FIX_VERTICAL_NUMBER, RtcConstants.COLON)
                .toString(), equalTo(okData));

        final String okData1 = "{\"action_type\":\"vod_playing\",\"sys_id\":\"t\",\"user_id\":\"i520601748@itv\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"stb_ip\":\"172.34.28.137\",\"stb_id\":\"F010049900703720000004957304F286\",\"stb_type\":\"B860AV1.1-T2\",\"stb_mac\":\"04:95:73:04:F2:86\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1529462637\",\"mediacode\":\"30000001000000010000000013896438\",\"seriescode\":\"30000001000000010000000013896438\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"start_time\":\"1529406453\",\"currentplaytime\":\"4300\",\"refer_type\":\"3\",\"refer_page_id\":\"P006T02A000\",\"refer_page_name\":\"精选漫影-详情页\",\"refer_pos_id\":\"Anav0Pnav0_0\",\"refer_pos_name\":\"c286::p30000001000000010000000013896438\",\"refer_mediacode\":\"\",\"refer_parent_id\":\"286\",\"httpsqs_time\":\"1530156238\"}";
        data = "{\"action_type\":\"vod_playing\",\"sys_id\":\"t\",\"user_id\":\"i520601748@itv\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"stb_ip\":\"172.34.28.137\",\"stb_id\":\"F010049900703720000004957304F286\",\"stb_type\":\"B860AV1.1-T2\",\"stb_mac\":\"04:95:73:04:F2:86\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1529462637\",\"mediacode\":\"30000001000000010000000013896438\",\"seriescode\":\"30000001000000010000000013896438\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"start_time\":\"1529406453\",\"currentplaytime\":\"4300\",\"refer_type\":\"3\",\"refer_page_id\":\"P006T02A000\",\"refer_page_name\":\"精选漫影-详情页\",\"refer_pos_id\":\"Anav0Pnav0_0\",\"refer_pos_name\":\"c286||p30000001000000010000000013896438\",\"refer_mediacode\":\"\",\"refer_parent_id\":\"286\",\"httpsqs_time\":\"1530156238\"}";
        assertThat(data.contains(RtcConstants.FIX_VERTICAL_NUMBER), equalTo(true));
        assertThat(data.replaceAll("\\" + RtcConstants.FIX_VERTICAL_NUMBER, RtcConstants.COLON),
                equalTo(okData1));

        sBuilder = new StringBuilder(data);
        RtcUtil.replace(sBuilder, RtcConstants.FIX_VERTICAL_NUMBER, RtcConstants.COLON);
        assertThat(sBuilder.toString(), equalTo(okData1));
    }

    @Test
    public void testJsonMapValueReplace() {

        final String okData = "{\"action_type\":\"vod_playing\",\"sys_id\":\"t\",\"user_id\":\"i5201139516@itv\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"stb_ip\":\"173.65.8.54\",\"stb_id\":\"0010039900E0680005165CE3B6CE91C0\",\"stb_type\":\"HG680-JDF9H-52\",\"stb_mac\":\"5C:E3:B6:CE:91:C0\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1528788361\",\"mediacode\":\"30000001000000010000000014155237\",\"seriescode\":\"30000001000000010000000014155237\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"start_time\":\"1528786921\",\"currentplaytime\":\"1532\",\"refer_type\":\"3\",\"refer_page_id\":\"P010T02A000\",\"refer_page_name\":\"时光剧场-电影详情页\",\"refer_pos_id\":\"A0P0\",\"refer_pos_name\":\"c4966::p30000001000000010000000014155237\",\"refer_mediacode\":\"\",\"refer_parent_id\":\"4966\"}";
        String data = "{\"action_type\":\"vod,_playing\",\"sys_id\":\"t\",\"user_id\":\"i520,,,113,,,9516@itv\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"stb_ip\":\"173.65.8.54\",\"stb_id\":\"0010039900E0680005165CE3B6CE91C0\",\"stb_type\":\"HG680-JDF9H-52\",\"stb_mac\":\"5C:E3:B6:CE:91:C0\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1528788361\",\"mediacode\":\"30000001000000010000000014155237\",\"seriescode\":\"30000001000000010000000014155237\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"start_time\":\"1528786921\",\"currentplaytime\":\"1532\",\"refer_type\":\"3\",\"refer_page_id\":\"P010T02A000\",\"refer_page_name\":\"时光剧场,,,-电影详情页,,,\",\"refer_pos_id\":\"A0P0\",\"refer_pos_name\":\"c4966::p30000001000000010000000014155237\",\"refer_mediacode\":\"\",\"refer_parent_id\":\"4966\"}";
        log.info("testJsonMapValueReplace test data2: {}", data);

        final LinkedHashMap<String, String> jsonMap = RtcUtil.json2LinkedHashMap(data);
        jsonMap.forEach((key, value) -> {

            if (!RtcConstants.EMPTY_STRING.equals(value)) {
                String okValue = value;
                if (okValue.contains(RtcConstants.FIX_VALUE_COMMA)) {
                    okValue = okValue.replaceAll(RtcConstants.FIX_VALUE_COMMA,
                            RtcConstants.EMPTY_STRING);
                }
                if (!value.equals(okValue))
                    jsonMap.put(key, okValue);
            }
        });
        String content = RtcUtil.linkedHashMap2Json(jsonMap);
        log.info("testJsonMapValueReplace jsonMap to content: {}", content);
        assertThat(content.equals(okData), equalTo(true));

        final String okData2 = "{\"action_type\":\"vod_playing\",\"sys_id\":\"t\",\"user_id\":\"i5201139516@itv\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"stb_ip\":\"\",\"stb_id\":\"\",\"stb_type\":\"HG680-JDF9H-52\",\"stb_mac\":\"5C:E3:B6:CE:91:C0\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1528788361\",\"mediacode\":\"30000001000000010000000014155237\",\"seriescode\":\"30000001000000010000000014155237\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"start_time\":\"1528786921\",\"currentplaytime\":\"1532\",\"refer_type\":\"3\",\"refer_page_id\":\"P010T02A000\",\"refer_page_name\":\"时光剧场-电影详情页\",\"refer_pos_id\":\"A0P0\",\"refer_pos_name\":\"c4966::p30000001000000010000000014155237\",\"refer_mediacode\":\"\",\"refer_parent_id\":\"4966\"}";
        data = "{\"action_type\":\"vod,_playing\",\"sys_id\":\"t\",\"user_id\":\"i520,,,113,,,9516@itv\",\"user_group_id\":\"1\",\"epg_group_id\":\"2\",\"stb_ip\":\"173.65 function Object() { navtive code.8.54\",\"stb_id\":\"0010039900E sdfsdf [native code] 0680005165CE3B6CE91C0\",\"stb_type\":\"HG680-JDF9H-52\",\"stb_mac\":\"5C:E3:B6:CE:91:C0\",\"terminal_type\":\"linux_STB\",\"log_time\":\"1528788361\",\"mediacode\":\"30000001000000010000000014155237\",\"seriescode\":\"30000001000000010000000014155237\",\"seriesflag\":\"0\",\"definition\":\"2\",\"bitrate\":\"\",\"start_time\":\"1528786921\",\"currentplaytime\":\"1532\",\"refer_type\":\"3\",\"refer_page_id\":\"P010T02A000\",\"refer_page_name\":\"时光剧场,,,-电影详情页,,,\",\"refer_pos_id\":\"A0P0\",\"refer_pos_name\":\"c4966::p30000001000000010000000014155237\",\"refer_mediacode\":\"\",\"refer_parent_id\":\"4966\"}";

        log.info("testJsonMapValueReplace test data2: {}", data);
        final LinkedHashMap<String, String> jsonMap2 = RtcUtil.json2LinkedHashMap(data);
        jsonMap2.forEach((key, value) -> {

            if (!RtcConstants.EMPTY_STRING.equals(value)) {

                String okValue = value;
                if (okValue.contains(RtcConstants.FIX_VALUE_COMMA)) {
                    okValue = okValue.replaceAll(RtcConstants.FIX_VALUE_COMMA,
                            RtcConstants.EMPTY_STRING);
                }

                if (okValue.contains(RtcConstants.FIX_VALUE_STRING_FUNCTION_OBJECT)) {
                    okValue = RtcConstants.EMPTY_STRING;
                }

                if (okValue.contains(RtcConstants.FIX_VALUE_STRING_NATIVE_CODE)) {
                    okValue = RtcConstants.EMPTY_STRING;
                }

                if (!value.equals(okValue))
                    jsonMap2.put(key, okValue);
            }
        });
        content = RtcUtil.linkedHashMap2Json(jsonMap2);
        log.info("testJsonMapValueReplace jsonMap2 to content2: {}", content);
        assertThat(content.equals(okData2), equalTo(true));
    }

}
