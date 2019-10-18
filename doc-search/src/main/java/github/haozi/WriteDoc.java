package github.haozi;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2019/10/18.
 */
public class WriteDoc {
    public void testWrite() throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("2018-08-08");
        list.add("2017-07-07");

        String templatePath = "D:\\template.doc";
        InputStream is = new FileInputStream(templatePath);
        OutputStream os = null;
        HWPFDocument doc = new HWPFDocument(is);
        Range range = doc.getRange();
        for (int i = 0; i < list.size(); i++) {
            String date = list.get(i);
            //把range范围内的${reportDate}替换为当前的日期
            range.replaceText("${date}", date);
            os = new FileOutputStream(new File("D:\\test.doc"));
            //把doc输出到输出流中
            doc.write(os);
        }
        os.close();
        is.close();
    }
}
