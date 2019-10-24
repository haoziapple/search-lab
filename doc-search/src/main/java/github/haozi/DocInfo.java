package github.haozi;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.io.Serializable;

/**
 * @author wanghao
 * @Description
 * @date 2019-10-23 9:17
 */
@Data
@ToString
@Builder
@SolrDocument(solrCoreName = "new_core")
public class DocInfo implements Serializable {
    @Id
    @Field
    private String id;

    @Field("fileName_s")
    private String fileName;
    @Field("filePath_s")
    private String filePath;
    @Field("fileText_txt_cn")
    private String fileText;
}
