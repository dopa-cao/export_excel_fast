package cn.com.clm.service;

import cn.com.clm.exception.CommonException;
import cn.com.clm.param.People;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * describe: import service
 *
 * @author liming.cao@hand-china.com
 */
public interface ImportService {

    /**
     * import excel
     *
     * @param file      excel file
     * @return          result
     */
    List<People> importExcel(MultipartFile file) throws CommonException;

}
