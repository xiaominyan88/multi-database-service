package data.tree.controller;


import data.tree.annotation.HttpFormatBean;
import data.tree.bean.DeviceData;
import data.tree.bean.TreeNode;
import data.tree.service.tree.AbstractTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tree")
public class TreeController {

    @Qualifier("treeGroupService")
    @Autowired
    AbstractTreeService tgs;

    @Qualifier("treeDevicesService")
    @Autowired
    AbstractTreeService tds;

    @Qualifier("treeDeviceTypesByTags")
    @Autowired
    AbstractTreeService tdtbt;

    @Qualifier("treeDeviceTypesService")
    @Autowired
    AbstractTreeService tdts;

    @Qualifier("treePointService")
    @Autowired
    AbstractTreeService tps;

    @Qualifier("ldLineDataService")
    @Autowired
    AbstractTreeService llds;

    @RequestMapping(value = "/tree_group")
    @ResponseBody
    @HttpFormatBean
    public List<TreeNode> treeGroup(){
        return tgs.treeService();
    }

    @RequestMapping(value = "/tree_devices")
    @ResponseBody
    @HttpFormatBean
    public List<DeviceData> treeDevices(){
        return tds.treeService();
    }

    @RequestMapping(value = "/tree_points")
    @ResponseBody
    @HttpFormatBean
    public Map<String,List<String>> treePoints(){
        return tps.treeService();
    }

    @RequestMapping(value = "/tree_devicetypes")
    @ResponseBody
    @HttpFormatBean
    public List<DeviceData> treeDeviceTypes(){
        return tdts.treeService();
    }

    @RequestMapping(value = "/tree_devicetypes_by_tag")
    @ResponseBody
    @HttpFormatBean
    public List<DeviceData> treeDeviceTypesByTag(){
        return tdtbt.treeService();
    }

    @RequestMapping(value = "/ld_line_data")
    @ResponseBody
    @HttpFormatBean
    public List<String> ldLineData(){
        return llds.treeService();
    }
}
