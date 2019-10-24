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
    /**
     * 也可以使用post-tool上传pdf、word文档，但是不知道如何进行全文搜索(TODO)
     * 官方文档：http://lucene.apache.org/solr/guide/8_1/post-tool.html
     */


    /**
     * 使用solr自带中文分词，
     * 1. managed-schema添加：
     * <dynamicField name="*_txt_cn" type="text_cn" indexed="true" stored="true"/>
     *
     * <fieldType name="text_cn" class="solr.TextField" positionIncrementGap="100">
     *     <analyzer>
     *       <tokenizer class="org.apache.lucene.analysis.cn.smart.HMMChineseTokenizerFactory"/>
     *       <filter class="solr.CJKWidthFilterFactory"/>
     *       <filter class="solr.StopFilterFactory" words="org/apache/lucene/analysis/cn/smart/stopwords.txt"/>
     *       <filter class="solr.PorterStemFilterFactory"/>
     *       <filter class="solr.LowerCaseFilterFactory"/>
     *     </analyzer>
     *   </fieldType>
     *
     *   2. solrconfig.xml
     *   <lib dir="${solr.install.dir:../../../..}/contrib/analysis-extras/lucene-libs" regex="lucene-analyzers-smartcn-\d.*\.jar" />
     *
     *   3. 官方文档：http://lucene.apache.org/solr/guide/8_1/language-analysis.html
     */
}
