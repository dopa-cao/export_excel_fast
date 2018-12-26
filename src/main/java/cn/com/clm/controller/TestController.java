package cn.com.clm.controller;

import cn.com.clm.exception.CommonException;
import cn.com.clm.response.CommonReturnData;
import cn.com.clm.util.DataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * describe:
 *
 * @author liming.cao
 */

@Api("test - controller")
@RestController
@RequestMapping(value = "/v1")
public class TestController extends BaseController{

    @ApiOperation(value = "测试", notes = "测试的接口")
    @GetMapping(value = "/test")
    public ResponseEntity<CommonReturnData> test() throws CommonException {
        return ResponseEntity.ok(CommonReturnData.create(DataUtil.buildList()));
    }

}
