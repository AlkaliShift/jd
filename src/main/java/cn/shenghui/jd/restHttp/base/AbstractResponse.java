package cn.shenghui.jd.restHttp.base;

import lombok.Data;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 10:23
 */
@Data
public class AbstractResponse {

    /**
     * 状态码
     */
    protected int statusCode;

    /**
     * 消息
     */
    protected String msg;

    public void setStatusInfo(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
