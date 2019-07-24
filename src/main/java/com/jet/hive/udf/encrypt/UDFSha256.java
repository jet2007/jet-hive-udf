package com.jet.hive.udf.encrypt;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.jet.utils.enDeCrypt.Encrypt;

/**
 * @author ruifeng.shan
 * date: 2016-07-25
 * time: 14:29
 */
@Description(name = "sha256"
        , value = "_FUNC_(string) - get sha256 hash code by given input string."
        , extended = "Example:\n > select _FUNC_(string) from src;")
public class UDFSha256 extends UDF {
    private Text result = new Text();

    public UDFSha256() {
    }

    /**
     * sha256 hash.
     *
     * @param text 字符串
     * @return sha256 hash.
     */
    public Text evaluate(Text text) {
        if (text == null) {
            return null;
        }

        result.set(Encrypt.sha256((text.toString())));
        return result;
    }
}
