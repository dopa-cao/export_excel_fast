package cn.com.clm.controller;

import cn.com.clm.error.ErrorEnum;
import cn.com.clm.exception.CommonException;
import cn.com.clm.response.CommonReturnData;
import cn.com.clm.service.ImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * describe: import controller
 *
 * @author liming.cao@hand-china.com
 */
@Api(value = "import - controller")
@RestController
@RequestMapping(value = "/v1")
public class ImportController extends BaseController {

    @Autowired
    private ImportService importService;

    @ApiOperation("导入测试")
    @PostMapping("/importTest")
    public ResponseEntity<CommonReturnData> importTest(@RequestParam("file") MultipartFile file) throws CommonException {
        if (null == file) {
            throw new CommonException(ErrorEnum.UNKNOWN_ERROR, "导入文件不可为空");
        }
        return ResponseEntity.ok(CommonReturnData.create(importService.importExcel(file)));
    }

}
