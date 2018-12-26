package cn.com.clm.controller;

import cn.com.clm.constant.Constant;
import cn.com.clm.error.ErrorEnum;
import cn.com.clm.exception.CommonException;
import cn.com.clm.response.CommonReturnData;
import cn.com.clm.service.ExportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * describe:
 *
 * @author liming.cao
 */

@Api("export - controller")
@RestController
@RequestMapping(value = "/v1")
public class ExportController extends BaseController{

    @Autowired
    private ExportService exportService;

    @ApiOperation(value = "导出测试接口")
    @PostMapping(value = "/exportTest")
    public ResponseEntity<CommonReturnData> exportTest(HttpServletResponse response) throws CommonException {
        Workbook workbook = exportService.getData();
        if (null == workbook) {
            throw new CommonException(ErrorEnum.IMPORT_ERROR);
        }
        this.writeToResponse(workbook, response, "人员列表" + Constant.SUFFIX_2007);
        return ResponseEntity.ok(CommonReturnData.create(Constant.REQUEST_SUCCESS));
    }


}
