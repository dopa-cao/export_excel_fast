package cn.com.clm.service.impl;

import cn.com.clm.exception.CommonException;
import cn.com.clm.param.People;
import cn.com.clm.service.ImportService;
import cn.com.clm.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * describe: import service impl
 *
 * @author liming.cao@hand-china.com
 */

@Service
public class ImportServiceImpl implements ImportService {

    @Override
    public List<People> importExcel(MultipartFile file) throws CommonException {
        Workbook workbook = ExcelUtil.getWorkbookByFile(People.class, file);
        Sheet sheet = workbook.getSheetAt(0);
        List<People> peoples = (List<People>) ExcelUtil.parse(sheet, People.class, 1);
        return peoples.stream().filter(people -> people.getSex() == 1).collect(Collectors.toList());
    }

}
