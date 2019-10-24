package github.haozi.web;

import github.haozi.DocInfo;
import github.haozi.TransferDoc;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wanghao
 * @Description
 * @date 2019-10-24 9:16
 */
@RestController
@RequestMapping("solr")
@Slf4j
public class SolrController {
    public static final String CORE = "new_core";

    public static final String DOC_PATH = "E:\\wanghao\\search-lab\\res\\不动产登记相关法律法规";

    @Autowired
    private SolrClient solrClient;

    @GetMapping("testDoc")
    public String testDoc() {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("fileName_s", "testFile");
        document.addField("filePath_s", "a/b/c");
        document.addField("fileText_txt_cn", "测试一下中文的全文搜索行不行，solr的官方文档真香");

        try {
            solrClient.add(CORE, document);
            solrClient.commit(CORE);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping("uploadDoc")
    public String uploadDoc() {
        TransferDoc transferDoc = new TransferDoc(DOC_PATH);
        List<DocInfo> docInfos = transferDoc.tansfer();
        for (DocInfo docInfo : docInfos) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("fileName_s", docInfo.getFileName());
            document.addField("filePath_s", docInfo.getFilePath());
            document.addField("fileText_txt_cn", docInfo.getFileText());
            try {
                UpdateResponse response = solrClient.add(CORE, document);
                log.info("add doc response: " + response.jsonStr());
                response = solrClient.commit(CORE);
                log.info("commit doc response: " + response.jsonStr());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "success";
    }

    @GetMapping("clearDoc")
    public String clearDoc() {
        try {
            UpdateResponse response = solrClient.deleteByQuery(CORE, "*:*");
            log.info("add doc response: " + response.jsonStr());
            response = solrClient.commit(CORE);
            log.info("commit doc response: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping("searchDoc")
    public Object searchDoc(String keyword) {
        SolrQuery query = new SolrQuery();
        query.setQuery("fileText_txt_cn:" + keyword);
        query.setRows(20);
        query.setHighlight(true);
        query.addHighlightField("fileText_txt_cn");
        log.info("query String:" + query.toQueryString());
        QueryResponse response = null;
        try {
            response = solrClient.query(CORE, query);
            log.info("query response:" + response.jsonStr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response == null) {
            return "not found";
        }

        List<DocInfo> result = new ArrayList<>();
        for (SolrDocument solrDocument : response.getResults()) {
            String id = (String) solrDocument.get("id");
            if (CollectionUtils.isEmpty(response.getHighlighting().get(id))) {
                continue;
            }
            if (CollectionUtils.isEmpty(response.getHighlighting().get(id).get("fileText_txt_cn"))) {
                continue;
            }

            DocInfo docInfo = DocInfo.builder().id(id)
                    .fileName((String) solrDocument.get("fileName_s"))
                    .filePath((String) solrDocument.get("filePath_s"))
                    .fileText(response.getHighlighting().get(id).get("fileText_txt_cn").toString()).build();
            result.add(docInfo);
        }

        return result;
    }

}
