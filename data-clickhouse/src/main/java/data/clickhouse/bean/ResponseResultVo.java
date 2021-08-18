package data.clickhouse.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResultVo {
    private String measureTag;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ResponseDataVo> responseDataVo;

}
