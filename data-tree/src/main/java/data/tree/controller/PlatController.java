package data.tree.controller;


import data.tree.annotation.HttpFormatBean;
import data.tree.bean.DevAndMeasurePoint;
import data.tree.service.plat.AbstractPlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/plat")
public class PlatController {

    @Qualifier("inverterPointsService")
    @Autowired
    AbstractPlatService ips;

    @Qualifier("measureIdAndNameService")
    @Autowired
    AbstractPlatService mns;

    @Qualifier("meterAndPointService")
    @Autowired
    AbstractPlatService mps;

    @Qualifier("siteInfoService")
    @Autowired
    AbstractPlatService sis;

    @Qualifier("stationInfoService")
    @Autowired
    AbstractPlatService stis;

    @RequestMapping(value = "/siteInformation")
    @ResponseBody
    @HttpFormatBean
    public Map<String,String> getSiteInfomation(){
        return sis.platService();
    }

    @RequestMapping(value = "/name_point")
    @ResponseBody
    @HttpFormatBean
    public Map<String,String> getNamePoint(){
        return mns.platService();
    }

    @RequestMapping(value = "/meter_info")
    @ResponseBody
    @HttpFormatBean
    public List<DevAndMeasurePoint> getMeterInfo(){
        return mps.platService();
    }

    @RequestMapping(value = "/inverter_pointids")
    @ResponseBody
    @HttpFormatBean
    public Map<String,List<String>> getInverterPointIds(){
        return ips.platService();
    }

    @RequestMapping(value = "/station_info")
    @ResponseBody
    @HttpFormatBean
    public Map<String,String> getStationInfo(){
        return stis.platService();
    }

}
