package com.jet.hive.udf.decrypt;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.jet.utils.enDeCrypt.DecryptUtils;
import com.jet.utils.enDeCrypt.EncryptUtils;

/**
 * @author caihm
 * date: 201907
 */
@Description(name = "base64"
        , value = "_FUNC_(string) - get base64 decode by given input string."
        , extended = "Example:\n > select _FUNC_(string) from src;")
public class UDFBase64 extends UDF {
    private Text result = new Text();

    public UDFBase64() {
    }

    /**
     * base64 decode
     *
     * @param text 字符串
     * @return base64 decode.
     */
    public Text evaluate(Text text) {
        if (text == null) {
            return null;
        }

        result.set(DecryptUtils.base64((text.toString())));
        return result;
    }
}
