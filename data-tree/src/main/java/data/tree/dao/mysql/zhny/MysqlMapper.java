package data.tree.dao.mysql.zhny;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MysqlMapper {

    int checkPoint(@Param("measurePointIds") List<String> pointIds);

    int checkPointTags(@Param("measurePointIds") List<String> pointIds, @Param("tags") String tags);

}
