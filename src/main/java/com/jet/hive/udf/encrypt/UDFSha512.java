package com.jet.hive.udf.encrypt;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.jet.utils.enDeCrypt.EncryptUtils;

/**
 * @author ruifeng.shan
 * date: 2016-07-25
 * time: 14:29
 */
@Description(name = "sha512"
        , value = "_FUNC_(string) - get sha512 hash code by given input string."
        , extended = "Example:\n > select _FUNC_(string) from src;")
public class UDFSha512 extends UDF {
    private Text result = new Text();

    public UDFSha512() {
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

        result.set(EncryptUtils.sha512((text.toString())));
        return result;
    }
}
