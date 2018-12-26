package cn.com.clm.controller;

import cn.com.clm.constant.Constant;
import cn.com.clm.error.ErrorEnum;
import cn.com.clm.exception.CommonException;
import cn.com.clm.response.CommonReturnData;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * describe:
 *
 * @author liming.cao
 */
public abstract class BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

    /**
     * 将文件数据写入到响应流中
     */
    void writeToResponse(Workbook workbook, HttpServletResponse response, String fileName) throws CommonException {
        OutputStream outputStream = null;

        try {
            this.setResponseHeader(response, fileName);
            outputStream = response.getOutputStream();
            //将excel文档写入到响应流中
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new CommonException(ErrorEnum.IMPORT_ERROR);
        } finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LOG.error("stream close fail:{}",e.getMessage());
                }
            }
            if(workbook != null) {
                try {
                    workbook.close();
                }catch (IOException e) {
                    LOG.error("stream close fail:{}",e.getMessage());
                }
            }
        }
    }

    /**
     * 设置response的头部信息
     *
     * @param response                          response
     * @param fileName                          file name
     * @throws UnsupportedEncodingException     exception
     */
    private void setResponseHeader(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.reset();
        response.setCharacterEncoding(Constant.ENCODING);
        response.setHeader("Content-Disposition","attachment;filename=" +
                URLEncoder.encode(fileName, Constant.ENCODING));
        response.setHeader("filename", URLEncoder.encode(fileName, Constant.ENCODING));
        response.setHeader("Access-Control-Expose-Headers", "filename");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleException(HttpServletRequest request, Exception exception) {
        Map<String, Object> response = new HashMap<>(2);
        if (exception instanceof CommonException) {
            CommonException commonException = (CommonException)exception;
            response.put(Constant.ERROR_CODE, commonException.getErrorCode());
            response.put(Constant.ERROR_MSG, commonException.getErrorMsg());
        } else {
            response.put(Constant.ERROR_CODE, ErrorEnum.UNKNOWN_ERROR.getErrorCode());
            response.put(Constant.ERROR_MSG, ErrorEnum.UNKNOWN_ERROR.getErrorMsg());
        }
        return CommonReturnData.create(response, Constant.REQUEST_FAIL);
    }


}
