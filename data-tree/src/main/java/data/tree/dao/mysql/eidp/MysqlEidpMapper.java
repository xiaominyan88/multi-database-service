package data.tree.dao.mysql.eidp;


import data.tree.annotation.MapF2F;
import data.tree.bean.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface MysqlEidpMapper {
    /**
     * select node_id  from cispplatform.md_treeview_node where class_id  IN   (select class_id from cispplatform.md_table where table_name = 'DEV_PWStationPSR')  AND TREEVIEW_ID = '5900'
     * @param parameter
     * @return
     */
    List<TreeResultBean> findNodeId(TreeParameterBean parameter);

    /**
     * "select PARENT_NODE_ID,NODE_ID,ORDER_NO from md_treeview_node_belong where PARENT_NODE_ID = '"
     * 				+ node.getNodeId() + "'"
     * @param parameter
     * @return
     */
    List<TreeResultBean> findBelongNode(TreeParameterBean parameter);

    /**
     * "select NODE_ID,CLASS_ID,NODE_NAME,NODE_TYPE from md_treeview_node where node_id ='"
     * 						+ nodeId + "'"
     * @param parameter
     * @return
     */
    List<TreeResultBean> findChildNodeInformation(TreeParameterBean parameter);

    /**
     * select class_id,table_name from md_table where class_id = " + " '" + classId
     * @param parameter
     * @return
     */
    List<TreeResultBean> findChildNodeTable(TreeParameterBean parameter);

    /**
     * select 1 flag from information_schema.columns where table_name = '"
     * 										+ childNode.getTableName() + "' and column_name = 'RUN_STATUS' limit 1
     * @param parameter
     * @return
     */
    List<TreeResultBean> findFlagStatus(TreeParameterBean parameter);

    /**
     * status have or not have
     *"select id, class_name,parent_class_name,parent_id,name,RUN_STATUS from "
     * 											+ childNode.getTableName() + " where parent_id in (" + ids + ")"
     * @param parameter
     * @return
     */
    List<TreeResultBean> findDeviceData(TreeParameterBean parameter);

    /**
     * className = MeterPSR
     * "select id from cispplatform.me_conf_measurepoint where end_dev_id = 'devId'"
     * className != MeterPSR
     * "select id from cispplatform.me_conf_measurepoint where dev_id = 'devId'"
     * @param className
     * @return
     */
    List<String> getPoint(@Param("className") String className, @Param("devId") String devId);

    /**
     * "select id from cispplatform.me_conf_measurepoint where end_dev_id in
     * (select associatedmeter from cisp_dev.dev_pwtransformerpsr where id in('" + devId +"'))"
     * @param devId
     * @return
     */
    List<String> getTransformerPoint(@Param("devId") String devId);

    /**
     * if dd.getClassName() == 'DYBayPSR'
     * "select is_circuit from dev_" + dd.getClassName() + " where id = '" + dd.getId() + "'"
     * if dd.getClassName() == 'PWBayPSR'
     * "select type,is_circuit from dev_" + dd.getClassName() + " where id = '" + dd.getId() + "'"
     * @param deviceData
     * @return
     */
    CircuitBean isCircuit(DeviceData deviceData);

    /**
     * "select value,name from bp_code where code_type ='DZ_JGLX'"
     * @return
     */
    @MapF2F
    Map<String,String> findCodeMap(@Param("codeType") String value);

    /**
     * if "PWBayPSR".equals(className)
     * "select concat(class_name,'-',type) key,type from cisp_dev.PWBayPSR where id = 'devId'"
     * else
     * "select class_name key,class_desc from cispplatform.md_class where id = 'devId'"
     * @param devId
     * @param className
     * @return
     */
    DeviceData findDeviceTypeAndClassName(@Param("devId") String devId, @Param("className") String className);

    /**
     * if "LAMPSTATION".equals(className)
     * "select ID from me_conf_measurepoint where DEV_ID in (select id from dev_dylinepsr where 1=1 and ROOT_ID = 'devId')"
     * if "SUBADMINISTRAINTIONREGION".equals(className)
     * "select ID from me_conf_measurepoint where DEV_ID in (select id from dev_dylinepsr where 1=1 and parent_id = 'devId')"
     * if "ADMINISTRATIONREGION".equals(className)
     * "select ID from me_conf_measurepoint where DEV_ID in (select id from dev_dylinepsr where 1=1 and parent_id in (select id from dev_SubAdministrationRegion where parent_id ='devId'))"
     * if "DYLINEPSR".equals(className)
     * "select ID from me_conf_measurepoint where DEV_ID = 'devId'"
     * @param devId
     * @param className
     * @return
     */
    List<String> findMeasureIdFromDyLine(@Param("devId") String devId, @Param("className") String className);

    /**
     * 这两条语句最有效的方法是启用存储过程函数，但是由于华云能源分公司与华云创维之间的一些问题，
     * 只能采用两条语句去完成此功能，望后续有人继续优化
     * "select root,concat('dev_',root_type) tab from cispplatform.me_conf_measurepoint where dev_id = 'id'"
     * "select root_id sitId,root_class_name sitType,name siteName,supervisor supervisor from cisp_dev." + tab + " where root_id = '" + root + "'"
     * @param id
     * @return
     */
    Map<String,String> findSiteInfo(@Param("id") String id);
    Map<String,String> findSiteAllInfomation(Map<String, String> map);

    /**
     * "select id,name from cispplatform.me_conf_measurepoint where id in (" + pointIds + ")"
     * @param pointIds
     * @return
     */
    @MapF2F
    Map<String,String> findMeasureIdAndNameMap(@Param("measurePointIds") List<String> pointIds);

    /**
     * select b.id as id,b.name as devName,b.dev_id as dev_id,a.id as meterId,a.name as name from cisp_dev.dev_meterpsr a
     * inner join
     * cispplatform.me_conf_measurepoint b
     * on a.id = b.end_dev_id and a.POSITION = '10' and a.ROOT_ID = 'id'
     * union all
     * select c.id as id,c.name as devName,c.dev_id as dev_id,'' as meterId,'' as name from cispplatform.me_conf_measurepoint c where not EXISTS (select b.id id,b.name devName,b.dev_id dev_id,a.id meterId,a.name name,a.root_id rootId from cisp_dev.dev_meterpsr a
     * inner join
     * cispplatform.me_conf_measurepoint b
     * on a.id = b.end_dev_id and a.POSITION = '10' and a.ROOT_ID = 'id' ) and c.ISCALCULATE = 1 and c.root = 'id';
     * @param id
     * @return
     */
    List<DevAndMeasurePoint> getMeterAndPoint(@Param("stationId") String id);

    /**
     * select id,root from cispplatform.me_conf_measurepoint where dev_id in(
     * select id from cisp_dev.dev_inverterpsr where root_id = 'stationId')
     * @param id
     * @return
     */
    List<String> getInverterPoints(@Param("stationId") String id);

    /**
     * select ENERGY_TYPE from dev_" + type + " where id ='" + stationId + "'
     * @param stationId
     * @param type
     * @return
     */
    String findEnergyType(@Param("stationId") String stationId, @Param("type") String type);

}
