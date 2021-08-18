package data.clickhouse.controller;


import data.clickhouse.bean.ParameterBeanVo;
import data.clickhouse.service.mysql.zhny.ZhNyMysqlService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/mysqlData")
public class MysqlController {

    @Autowired
    ZhNyMysqlService zhNyMysqlService;

    @RequestMapping(value = "/dwOffLine", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="用电指数",httpMethod = "POST")
    public Map<String,String> findDwOffLineMethod(@RequestBody ParameterBeanVo parameterBean){
        parameterBean.setFilterStation(true);
        Map<String, String> resultMap = zhNyMysqlService.selectDwOffLine(parameterBean);
        return resultMap;
    }

}
