package com.jet.hive.udf.encrypt;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.jet.utils.enDeCrypt.EncryptUtils;

/**
 * Created by ruifengshan on 16/3/18.
 */
@Description(name = "md5"
        , value = "_FUNC_(string) - get md5 hash code by given input string."
        , extended = "Example:\n > select _FUNC_(string) from src;")
public class UDFMd5 extends UDF {
    private Text result = new Text();

    public UDFMd5() {
    }

    /**
     * md5 hash.
     *
     * @param text 字符串
     * @return md5 hash.
     */
    public Text evaluate(Text text) {
        if (text == null) {
            return null;
        }
        result.set(EncryptUtils.md5((text.toString())));
        return result;
    }
}
