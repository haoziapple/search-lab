package github.haozi;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author wanghao
 * @Description
 * @date 2019-10-23 9:17
 */
@Data
@ToString
@Builder
public class DocInfo {
    private String fileName;

    private String filePath;

    private String fileText;
}
