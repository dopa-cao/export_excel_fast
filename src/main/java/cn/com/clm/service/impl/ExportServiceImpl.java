package cn.com.clm.service.impl;

import cn.com.clm.constant.Constant;
import cn.com.clm.error.ErrorEnum;
import cn.com.clm.exception.CommonException;
import cn.com.clm.param.People;
import cn.com.clm.service.ExportService;
import cn.com.clm.util.DataUtil;
import cn.com.clm.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * describe:
 *
 * @author liming.cao
 */
@Service
public class ExportServiceImpl implements ExportService {

    @Override
    public Workbook getData() throws CommonException {
        List<People> people = DataUtil.buildList();
        InputStream stream = ExportServiceImpl.class.getClassLoader().getResourceAsStream("template/peopleTemplate"
                + Constant.SUFFIX_2007);
        Workbook workbook;
        try {
            workbook = new XSSFWorkbook(stream);
            Sheet sheet = workbook.getSheetAt(0);
            ExcelUtil.parse(sheet, People.class, people, 1);
            return workbook;
        } catch (IOException e) {
            throw new CommonException(ErrorEnum.IMPORT_ERROR);
        }
    }

}
