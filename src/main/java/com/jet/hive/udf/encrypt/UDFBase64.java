package com.jet.hive.udf.encrypt;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.jet.utils.enDeCrypt.Encrypt;

/**
 * @author caihm
 * date: 201907
 */
@Description(name = "base64"
        , value = "_FUNC_(string) - get base64 hash code by given input string."
        , extended = "Example:\n > select _FUNC_(string) from src;")
public class UDFBase64 extends UDF {
    private Text result = new Text();

    public UDFBase64() {
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

        result.set(Encrypt.base64((text.toString())));
        return result;
    }
}
