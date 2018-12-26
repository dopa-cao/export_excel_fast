package cn.com.clm.service;

import cn.com.clm.exception.CommonException;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * describe:
 *
 * @author liming.cao
 */
public interface ExportService {

    /**
     * 获取数据
     *
     * @return  workbook
     */
    Workbook getData() throws CommonException;

}
