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
                .searchPath("/some/path");

        ff.forEach(file -> {
            // process file
        });

        FileWriter writer = new FileWriter("test.properties");
        writer.write("test");

        //默认UTF-8编码，可以在构造中传入第二个参数做为编码
        FileReader fileReader = new FileReader("test.properties");
        String result = fileReader.readString();

        System.out.println(FileUtil.file("test.properties").getAbsolutePath());

        System.out.println(result);

        /** 读取pdf */
        //创建PdfDocument实例
        File file = new File("test.pdf");

        //加载PDF文件
        PDDocument doc= PDDocument.load(file);

        StringBuilder sb= new StringBuilder();

        PDFTextStripper pdfStripper = new PDFTextStripper();

        String text = pdfStripper.getText(doc);
        doc.close();
        System.out.println(text);

        /** 读取word */
        SpringApplication.run(SearchLabApplication.class, args);
    }

}
