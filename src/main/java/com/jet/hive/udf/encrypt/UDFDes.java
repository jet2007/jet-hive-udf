package com.jet.hive.udf.encrypt;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.jet.utils.enDeCrypt.Constants;
import com.jet.utils.enDeCrypt.Encrypt;

/**
 * @author ruifeng.shan
 * date: 2016-07-25
 * time: 14:29
 */
@Description(name = "des"
        , value = "_FUNC_(string[,string]) - get des hash code by given input string."
        , extended = "Example:\n > select _FUNC_(string[,string]) from src;")
public class UDFDes extends UDF {
    private Text result = new Text();

    public UDFDes() {
    }

    /**
     * des 加密.
     *
     * @param text 字符串（密钥使用默认值）
     * @return des 加密.
     */
    public Text evaluate(Text text) {
        return evaluate(text,new Text(Constants.DES3_KEY));
    }
    
    /**
     * des3 加密
     * @param text 加密前的新内容
     * @param key 密钥
     * @return 加密后的内容
     */
    public Text evaluate(Text text,Text key) {
        if (text == null || key ==null) {
            return null;
        }
        if (key.toString().length() != 24){
        	return null;
        }

        result.set(Encrypt.des3( key.toString() , text.toString() ) );
        return result;
    }
    
}
