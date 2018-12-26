package cn.com.clm.response;

import cn.com.clm.constant.Constant;

/**
 * describe:
 *
 * @author liming.cao
 */
public class CommonReturnData {

    private String status;
    private Object data;

    public static CommonReturnData create(Object result) {
        return CommonReturnData.create(result, Constant.REQUEST_SUCCESS);
    }

    public static CommonReturnData create(Object result, String status) {
        CommonReturnData commonReturnData = new CommonReturnData();
        commonReturnData.setStatus(status);
        commonReturnData.setData(result);
        return commonReturnData;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
