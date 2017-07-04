import com.jy.common.utils.base.Const;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

/**
 * Created by spring on 2017/2/20.
 */
public class Test {

    public static void main(String[] a){
        try {
            java.io.File from = new java.io.File("E:\\myprojects\\codes\\cms_file\\src\\main\\webapp\\upload");
            java.io.File to = new java.io.File("e:/nas/20170220");
            try {
                FileUtils.copyDirectory(from, to);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
    }
}
