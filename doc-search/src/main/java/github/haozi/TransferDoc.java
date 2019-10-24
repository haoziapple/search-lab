package github.haozi;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import jodd.io.findfile.FindFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wanghao
 * @Description
 * @date 2019-10-23 9:14
 */
@Slf4j
public class TransferDoc {
    /**
     * 原始文件的根目录
     */
    private String rawDocPath;

    public TransferDoc(String rawDocPath) {
        this.rawDocPath = rawDocPath;
    }

    public void clearTxtDoc() {
        // 使用jodd的FindFile遍历文件
        FindFile ff = new FindFile()
                .recursive(true)
                .includeDirs(true)
                .searchPath(rawDocPath);
        ff.forEach(file -> {
            if (file.isDirectory()) {
                return;
            }
            if(file.getName().endsWith(".txt")) {
                boolean deleted = FileUtil.del(file);
                log.info("delete " + (deleted ? "success: " : "fail: ") + file.getAbsolutePath());
            }
        });
    }


    public List<DocInfo> tansfer() {
        List<DocInfo> result = new ArrayList<>();

        // 使用jodd的FindFile遍历文件
        FindFile ff = new FindFile()
                .recursive(true)
                .includeDirs(true)
                .searchPath(rawDocPath);

        ff.forEach(file -> {
            // process file
            if (file.isDirectory()) {
                return;
            }

            if (file.isHidden()) {
                log.warn("The file is hidden: " + file.getAbsolutePath());
                return;
            }


            if (!file.canRead()) {
                log.warn("Cannot read file: " + file.getAbsolutePath());
                return;
            }


            System.out.println(file.getAbsolutePath());
            if (file.getName().endsWith(".pdf")) {
                try {
                    PDDocument doc = PDDocument.load(file);
                    PDFTextStripper pdfStripper = new PDFTextStripper();
                    String text = pdfStripper.getText(doc);
                    doc.close();

                    DocInfo docInfo = new DocInfo.DocInfoBuilder()
                            .fileName(file.getName())
                            .filePath(file.getAbsolutePath().substring(rawDocPath.length()))
                            .fileText(text).build();
                    System.out.println(docInfo);
                    result.add(docInfo);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (file.getName().endsWith(".doc") || file.getName().endsWith(".docx")) {
                InputStream is = null;
                try {
                    is = new FileInputStream(file.getAbsolutePath());
                    String text = null;
                    if (file.getName().endsWith(".doc")) {
                        WordExtractor extractor = new WordExtractor(is);
                        text = extractor.getText();
                    }

                    if (file.getName().endsWith(".docx")) {
                        XWPFWordExtractor docx = new XWPFWordExtractor(new XWPFDocument(is));
                        text = docx.getText();
                    }

                    DocInfo docInfo = new DocInfo.DocInfoBuilder()
                            .fileName(file.getName())
                            .filePath(file.getAbsolutePath().substring(rawDocPath.length()))
                            .fileText(text).build();
                    System.out.println(docInfo);
                    result.add(docInfo);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }
        });

        return result;
    }

    public void writeToTxt(List<DocInfo> docInfos) {
        for (DocInfo docInfo : docInfos) {
            FileWriter writer = new FileWriter(rawDocPath + docInfo.getFilePath() + ".txt");
            writer.write(docInfo.getFileText());
        }
    }

    public static void main(String[] args) {
        TransferDoc transferDoc = new TransferDoc("E:\\wanghao\\search-lab\\res\\不动产登记相关法律法规");

//        List<DocInfo> docInfos = transferDoc.tansfer();
//        transferDoc.writeToTxt(docInfos);
        transferDoc.clearTxtDoc();
    }
}
