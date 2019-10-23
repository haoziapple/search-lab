package github.haozi;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.poi.word.Word07Writer;
import jodd.io.findfile.FindFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class SearchLabApplication {

    public static void main(String[] args) throws Exception {
        FindFile ff = new FindFile()
                .recursive(true)
                .includeDirs(true)
                .searchPath("E:\\wanghao\\search-lab\\res\\不动产登记相关法律法规");

        ff.forEach(file -> {
            // process file
            System.out.println(file.getAbsolutePath());
        });

        FileWriter writer = new FileWriter("E:\\wanghao\\search-lab\\res\\test.properties");
        writer.write("test");

        //默认UTF-8编码，可以在构造中传入第二个参数做为编码
        FileReader fileReader = new FileReader("E:\\wanghao\\search-lab\\res\\test.properties");
        String result = fileReader.readString();

        System.out.println(FileUtil.file("E:\\wanghao\\search-lab\\res\\test.properties").getAbsolutePath());

        System.out.println(result);

        SpringApplication.run(SearchLabApplication.class, args);
    }

}
