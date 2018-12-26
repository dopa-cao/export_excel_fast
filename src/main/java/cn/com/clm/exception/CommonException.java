package cn.com.clm.exception;

import cn.com.clm.error.CommonError;

/**
 * describe: 包装器业务异常类
 *
 * @author liming.cao
 */
public class CommonException extends Exception implements CommonError {

    private CommonError commonError;

    public CommonException(CommonError commonError) {
        this.commonError = commonError;
    }

    public CommonException(CommonError commonError, String errorMsg) {
        this.commonError = commonError;
        this.commonError.setErrorMsg(errorMsg);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.commonError.getErrorMsg();
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.commonError.setErrorMsg(errorMsg);
        return this;
    }
}
