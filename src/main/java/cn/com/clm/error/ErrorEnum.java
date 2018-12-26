package cn.com.clm.error;

/**
 * describe:
 *
 * @author liming.cao
 */
public enum ErrorEnum implements CommonError {
    UNKNOWN_ERROR(20001, "未知错误"),
    CALCULATE_ERROR(30001, "计算时出现错误"),
    IMPORT_ERROR(50001, "导出时出现错误");

    private Integer errorCode;
    private String errorMsg;

    ErrorEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
