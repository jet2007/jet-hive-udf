[TOC]

## UDF



## mvn打包

```
cd ${project_home}
mvn clean package
```

- 如果你想跳过单元测试，可以这样运行：

```
cd ${project_home}
mvn clean package -DskipTests
```

命令执行完成后, 将会在target目录下生成



## 函数列表

### 函数分类

| 分类     | 分类          | 描述                                                         | 示例                                                 |
| -------- | ------------- | ------------------------------------------------------------ | ---------------------------------------------------- |
| 数据脱敏 | 固定化        | 将数据置为常量，一般用于处理不需要的敏感字段。               | 500 ->0，false->true                                 |
| 数据脱敏 | 截断          | 截断字符                                                     | 13811001111截断为138，舍弃必要信息来保证数据的模糊性 |
| 数据脱敏 | 变换          | 将数据置换成新值，保持数据大小顺序不变                       | 数值的线性变换-12.68->-12 12580->12000               |
| 数据脱敏 | 混淆          | 保持一部分内容不变，混淆另一部分；混淆：置换成随机字符如字母a-zA-Z | 18965432100->18985214789(混淆后4位)                  |
| 数据脱敏 | 掩码          | 保持一部分内容不变，掩码另一部分；掩码：置换成指定特殊字符如星号 | 18965432100->189****2100                             |
| 解析函数 | IP地址解析    | 解析IP地址，返回国家、地区等信息                             | 根据解析规则返回国家、地区等信息                     |
| 解析函数 | URL解析       | 解析URL,返回域名、参数等                                     | 根据解析规则返回域名、参数等信息                     |
| 解析函数 | UserAgent解析 | 解析UserAgent，返回浏览器信息、设备信息等                    | 根据解析规则返回浏览器、设备等信息                   |
| 日期函数 | 日期增减      | 输入日期增减天数                                             | 2019-08-20 ->2019-08-23                              |
| 日期函数 | 日期格式转换  | 转换日期格式                                                 | 2019-01-03 ->2019/01/03                              |



### 函数列表



#### 函数列表-数据脱敏

| 分类          | 函数                                                        | 说明                                                         |
| :------------ | ----------------------------------------------------------- | ------------------------------------------------------------ |
| 固定化-通用型 | desenHiding(col [,default_val])                             | 脱敏成一个固定值                                             |
| 截断-通用型   | desenFloorNumberCutRight(col, right_shift_size)             | 整数值的截断,从右起截断后n位且置为0                          |
| 截断-通用型   | desenFloorNumberReserveLeft                                 | 整数值的截断,从左起保留n位，其余置为0                        |
| 截断-通用型   | desenFloorDateCut(date_or_datetime ,cuts)                   | 日期时间值的截断,指定位置的值置'零'（置'零':从年到秒,被置零依次为'1970,1,1,0,0,0'） |
| 变换-通用型   | desenLinearShift(col \[,k\]\[,b\])                          | 进行线性变化，保持大小顺序不变；实现方式y=kx+b               |
| 混淆-通用型   | desenConfuseLeft(str,len [,randomType] )                    | 混淆左侧len长度的字符，其余保持不变                          |
| 混淆-通用型   | desenConfuseRight(str ,len [,randomType])                   | 混淆右侧len长度的字符，其余保持不变                          |
| 混淆-通用型   | desenConfuseSubstr(str ,begin,end [,randomType])            | 混淆从begin到end位置的字符，其余字符保持不变                 |
| 混淆-通用型   | desenConfusePositions(str ,postitions [,type])              | 指定位置混淆，其余部分保持不变(混淆：置换成随机字符如字母a-zA-Z等) |
| 混淆-场景型   | desenConfuseMobile(mobile)                                  | 手机号码混淆(后4位)                                          |
| 掩码-场景型   | desenMaskIDCard(IDCard [,mask])                             | 身份证号码保留前3位和后4位，其他脱敏成掩码                   |
| 掩码-场景型   | desenMaskMobilePhone(mobile [,mask])                        | 手机号码保留前3位和后4位，其他脱敏成掩码                     |
| 掩码-场景型   | desenMaskFixedPhone(fixedPhone[,mask])                      | 固话号码保留后4位，其他脱敏成掩码                            |
| 掩码-场景型   | desenMaskBankCard(bankCard[,mask])                          | 银行卡号保留前6位和后4位，其他脱敏成掩码                     |
| 掩码-场景型   | desenMaskPassword(password [,mask])                         | 密码全部脱敏成掩码                                           |
| 掩码-场景型   | desenMaskChineseName(chineseName [,mask])                   | 中文名字只保留第一个汉字，其他脱敏成掩码                     |
| 掩码-场景型   | desenMaskChineseName(chineseName [,mask])                   | 电子邮箱前缀保留前两位,其他脱敏成掩码，@和后缀保持不变       |
| 掩码-通用型   | desenMaskLeft(str,leftLen \[,mask\]\[,fixLen\])             | 字符串左侧前leftLen个字符脱敏成长度为fixLen(默认为leftLen)的掩码，其他保持不变 |
| 掩码-通用型   | desenMaskRight(str,leftLen \[,mask\]\[,fixLen\])            | 字符串左侧前rightLen个字符脱敏成长度为fixLen(默认为rightLen)的掩码，其他保持不变 |
| 掩码-通用型   | desenMaskMiddle(str,leftLen,rightLen, \[,mask\]\[,fixLen\]) | 字符串左侧和左右分别保留leftLen和rightLen个字符，中间部分脱敏成长度为fixLen(默认跟脱敏前字符长度一致)的掩码 |
| 掩码-通用型   | desenMaskPositionInclude(str,numsStr [,mask])               | 指定位置掩码，numsStr中包含指定的掩码字符位置                |
| 掩码-通用型   | desenMaskPositionExclude(str,numsStr [,mask])               | 指定位置以外的字符掩码                                       |

#### 函数列表-解析函数

| 分类           | 函数                       | 说明                                                         |
| -------------- | -------------------------- | ------------------------------------------------------------ |
| IP地址解析(一) | ip2geo(ip [,fileName])     | 解析IP地址，返回MAP类型，示例：{"cityName":"上海市","ispName":"联通","regionName":null,"continentName":"中国","provinceName":"上海"} |
| IP地址解析(二) | geoip(ip [,fileName])      | 解析IP地址,返回MAP类型,示例：{"countryName":"中国","longitude":"116.3889","continentID":"AS","cityNameEn":"Beijing","provinceID":"BJ","countryNameEn":"China","continentNameEn":"Asia","continentName":"亚洲","latitude":"39.9288","provinceNameEn":"Beijing","cityName":"北京","provinceName":"北京市","countryID":"CN"} |
| URL解析        | decode_url(url，code)      | 解析URL，返回域名、参数等信息                                |
| UserAgent解析  | parse_useragent(useragent) | 解析UserAgent，返回browser,browser_version,operating_system,device_type,device_brand,device_model,device_version,browser_type |

#### 函数列表-日期函数

| 分类         | 函数                                        | 说明                                                       |
| ------------ | ------------------------------------------- | ---------------------------------------------------------- |
| 日期增减     | date_sub2(date,days)                        | 在输入日期的基础上增减天数                                 |
| 日期格式转换 | data_formate2(strDate,dateType,newDateType) | 转换日期类型的格式，"wd"参数的转换模式返回日期具体的星期几 |



## 函数ddl

将下面这些内容写入 `${HOME}/.hiverc` 文件, 或者也可以按需在hive命令行环境中执行.

```sql
-- add jar ${jar_location_dir}/hive-udf-${version}.jar;
create temporary function desenHiding as 'com.ct.dw.udf.desensitization.hiding.UDFDesenHiding';

create temporary function DesenFloorDateCut as 'com.ct.dw.udf.desensitization.floor.UDFDesenFloorDateCut';
create temporary function DesenFloorNumberCutRight as 'com.ct.dw.udf.desensitization.floor.UDFDesenFloorNumberCutRight';
create temporary function DesenFloorNumberReserveLeft as 'com.ct.dw.udf.desensitization.floor.UDFDesenFloorNumberReserveLeft';

create temporary function DesenLinearShift as 'com.ct.dw.udf.desensitization.shift.UDFDesenLinearShift';

create temporary function DesenConfuseLeft as 'com.ct.dw.udf.desensitization.confuse.UDFDesenConfuseLeft';
create temporary function DesenConfuseRight as 'com.ct.dw.udf.desensitization.confuse.UDFDesenConfuseRight';
create temporary function DesenConfuseSubstr as 'com.ct.dw.udf.desensitization.confuse.UDFDesenConfuseSubstr';
create temporary function DesenConfusePositions as 'com.ct.dw.udf.desensitization.confuse.UDFDesenConfusePositions';
create temporary function DesenConfuseMobilePhone as 'com.ct.dw.udf.desensitization.confuse.UDFDesenConfuseMobilePhone';

create temporary function DesenMaskIDCard as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskIDCard';
create temporary function DesenMaskMobilePhone as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskMobilePhone';
create temporary function DesenMaskFixedPhone as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskFixedPhone';
create temporary function DesenMaskBankCard as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskBankCard';
create temporary function DesenMaskPassword as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskPassword';
create temporary function DesenMaskChineseName as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskChineseName';
create temporary function DesenMaskEmail as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskEmail';

create temporary function DesenMaskLeft as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskLeft';
create temporary function DesenMaskRight as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskRight';
create temporary function DesenMaskMiddle as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskMiddle';
create temporary function DesenMaskPositionInclude as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskPositionInclude';
create temporary function DesenMaskPositionExclude as 'com.ct.dw.udf.desensitization.mask.UDFDesenMaskPositionExclude';

```



## 函数详细介绍

### 数据脱敏

#### 固定值化

##### 固定值脱敏

- 功能：脱敏成一个固定值

- 格式：desenHiding(col [,default_val])

- 参数：

  - col ：输入值，支持int,long,float,double,string类型；
  - default_val：脱敏后的输出值；可选；默认值(int,long,float,double=0,string=空字符串)

- 返回值：

  - 返回某个固定值
  - 当col为null值，返回值为null

- 示例

  ```
  desenHiding(10.1) -> 0
  desenHiding(10.1,2)-> 2
  desenHiding(’abc123‘)-> ''
  desenHiding(’abc123‘ , 'zzz' )-> 'zzz'
  ```




#### 截断

##### 整数值的截断(截右)

- 功能：整数值的截断,从右起截断后n位且置为0

- 格式：DesenFloorNumberCutRight(col ,cutRightLength)

- 参数：

  - col ：输入的数值，支持int,long,类型
  - cutRightLength：从右起被截断且置为0的长度

- 返回值：被截断的整数值

- 示例

  ```
  DesenFloorNumberCutRight(12345678,3) -> 12345000
  DesenFloorNumberCutRight(12345678L,3)-> 12345000
  DesenFloorNumberCutRight(12345000,8)-> 0
  ```


##### 整数值的截断(保左)

- 功能：整数值的截断,从左起保留n位，其余置为0

- 格式：DesenFloorNumberReserveLeft(col ,reserveLeftLength)

- 参数：

  - col ：输入的数值，支持int,long,类型
  - reserveLeftLength：从左起保留的长度

- 返回值：被截断的整数值

- 示例

  ```
  DesenFloorNumberReserveLeft(123456789876543,2) -> 1200000000000000
  DesenFloorNumberReserveLeft(1234567, 4)-> 1234000
  ```



##### 日期时间的截断

- 功能：日期时间值的截断,指定位置的值置'零'（置'零':从年到秒,被置零依次为'1970,1,1,0,0,0'）

- 格式：DesenFloorDateCut(date_or_datetime ,cuts)

- 参数：

  - col ：日期或时间值,支持string(yyyyMMddHHmmss,yyyy-MM-dd HH:mm:ss,yyyyMMdd,yyyy-MM-dd),或date/timestamp类型
  - cuts：被截断的单位，逗号隔开；从年到秒依次为YYMMDDHHMISS，示例MI,SS为将分秒置为0分0秒"

- 返回值：返回截断后string的日期值；若col为string类型，返回值格式保持不变；

- 示例

  ```
  DesenFloorDateCut('2019-01-02 03:04:05','MI,SS') -> '2019-01-02 03:00:00'
  DesenFloorDateCut('20190102030405','MI,SS')-> '20190102030000'
  ```



#### 变换

##### 线性变换

- 功能：进行线性变化，y=kx+b

- 格式：DesenLinearShift(col \[,k\]\[,b\])

- 参数：

  - col ：输入的数值，支持int,long,double,float类型
  - k：kx+b的斜率k,默认值为7
  - b:  kx+b的b为常数，默认值为997

- 返回值：被截断的整数值

- 示例

  ```sql
  select DesenLinearShift(100),DesenLinearShift(100,2),DesenLinearShift(100,2,1)
  -- 1697    1197    201
  ```

#### 混淆-通用型

##### 混淆左侧字符串

- 功能：混淆左侧len长度的字符，其余保持不变

- 格式：DesenConfuseLeft(str,len [,randomType] )

- 参数：

  - str：string,输入的字符串
  - len ：int,从左1起被混淆的位置长度
  - randomType:  可选,string,随机字符串类型(范围：NUMERIC=0,ALPHABETIC=1,ALPHABETIC_LOWER=2,ALPHABETIC_UPPER=3,ALPHA_NUMERIC=4,ASCII=5)，默认值为[ALPHABETIC]

- 返回值：混淆后的值；

- 示例

  ```sql
  select DesenConfuseLeft('1234567',-1)
         ,DesenConfuseLeft('1234567',0)
         ,DesenConfuseLeft('1234567',1)
         ,DesenConfuseLeft('1234567',2)
         ,DesenConfuseLeft('1234567',6)
         ,DesenConfuseLeft('1234567',7)
         ,DesenConfuseLeft('1234567',8)
         ,DesenConfuseLeft('1234567',2,'2')
  # 结果2次执行
  # 1234567 1234567 c234567 Yz34567 4lhAKh7 zrx6Nfh lw13kD4 ss34567
  # 1234567 1234567 1234567 oG34567 VLhI3G7 E4WOhLo dUvbh8f yi34567
  ```



##### 混淆右侧字符串

- 功能：混淆右侧len长度的字符，其余保持不变

- 格式：DesenConfuseRight(str,len [,randomType] )

- 参数：

  - str：string,输入的字符串
  - len ：int,从右被混淆的位置长度
  - randomType:  可选,string,随机字符串类型(范围：NUMERIC=0,ALPHABETIC=1,ALPHABETIC_LOWER=2,ALPHABETIC_UPPER=3,ALPHA_NUMERIC=4,ASCII=5)，默认值为[ALPHABETIC]

- 返回值：混淆后的值；

- 示例

  ```sql
  select DesenConfuseRight('1234567',-1)
         ,DesenConfuseRight('1234567',0)
         ,DesenConfuseRight('1234567',1)
         ,DesenConfuseRight('1234567',2)
         ,DesenConfuseRight('1234567',6)
         ,DesenConfuseRight('1234567',7)
         ,DesenConfuseRight('1234567',8)
         ,DesenConfuseRight('1234567',2,'2')
  -- 1234567 1234567 123456s 12345z0 1o4hUNW 61I82lp QWyniBb 12345mm
  -- 1234567 1234567 1234569 12345Cb 1QXm2VH uTSstDH uYPfkud 12345um
  ```



##### 混淆子字符串

- 功能：混淆从begin到end位置的字符，其余字符保持不变

- 格式：DesenConfuseSubstr(str ,begin,end [,randomType] )

- 参数：

  - str：string,输入的字符串
  - begin：int,从1起，被混淆的开始位置
  - end:  int,从1起，被混淆的结束位置
  - randomType:  可选,string,随机字符串类型(范围：NUMERIC=0,ALPHABETIC=1,ALPHABETIC_LOWER=2,ALPHABETIC_UPPER=3,ALPHA_NUMERIC=4,ASCII=5)，默认值为[ALPHABETIC]

- 返回值：混淆后的值；

- 示例

  ```
  DesenConfuseSubstr('1234567890',2,4)  
  DesenConfuseSubstr('1234567890',2,-3,'1')  
  # 混淆位置为第2，3，4位
  ```



##### 混淆字符串指定位置集

- 功能：指定位置混淆，其余部分保持不变(混淆：置换成随机字符如字母a-zA-Z等)

- 格式：DesenConfusePositions(str ,postitions [,randomType])

- 参数：

  - str：string,输入的字符串
  - postitions ：string,被混淆的位置集,格式n1,n2,...,n3~n4,分隔符与连续符分别为[,~],示例'1,2,-3~-1'输入的第1,2位，倒3至倒1位；
  - randomType:  可选,string,随机字符串类型(范围：NUMERIC=0,ALPHABETIC=1,ALPHABETIC_LOWER=2,ALPHABETIC_UPPER=3,ALPHA_NUMERIC=4,ASCII=5)，默认值为[ALPHABETIC]

- 返回值：混淆后的值；

- 示例

  ```sql
  select DesenConfusePositions('1234567890','2,3,-4~-2')
  ,  DesenConfusePositions('1234567890','2,3,-4~-2','2')
  -- 1en456wiY0      1uh456aez0
  ```



#### 混淆-场景型

##### 混淆手机号码

- 功能：手机号的后4位进行混淆(后4位置成随机数值)

- 格式：DesenConfuseMobilePhone(mobilePhone )

- 参数：

  - mobilePhone ：输入值，支持String类型

- 返回值：混淆后的手机号

- 示例

  ```
  DesenConfuseMobilePhone('18912345678')  --> 18912349763
  ```

#### 掩码-场景型

##### 掩码身份证号

- 功能：身份证号码保留前3位和后4位，其他脱敏成掩码

- 格式：DesenMaskIDCard(IDCard [,mask] )

- 参数：

  - IDCard ：身份证号码，支持String类型
  - mask：掩码字符，可选参数，支持String类型，默认为*

- 返回值：掩码后的身份证号码

- 示例

  ```
  DesenMaskIDCard('411678199204188911','#')  --> 411###########8911
  DesenMaskIDCard('512199204187834')  --> 512********7834
  ```

##### 掩码手机号码

- 功能：手机号码保留前3位和后4位，其他脱敏成掩码

- 格式：DesenMaskMobilePhone(mobile [,mask] )

- 参数：

  - mobile：手机号码，支持String类型
  - mask：掩码字符，可选参数，支持String类型，默认为*

- 返回值：掩码后的手机号码

- 示例

  ```
  DesenMaskMobilePhone('13700012345','#')  --> 137####2345
  DesenMaskMobilePhone('15051444560')  --> 150****4560
  ```

##### 掩码固话号码

- 功能：固定电话号码保留后4位，其他脱敏成掩码

- 格式：DesenMaskFixedPhone(fixedPhone [,mask] )

- 参数：

  - fixedPhone：固定电话号码，支持String类型
  - mask：掩码字符，可选参数，支持String类型，默认为*

- 返回值：掩码后的固定电话号码

- 示例

  ```
  DesenMaskFixedPhone('01078902300','#')  --> #######2300
  DesenMaskFixedPhone('051289003200')  --> ********3200
  ```

##### 掩码银行卡号

- 功能：银行卡号保留前6位和后4位，其他脱敏成掩码

- 格式：DesenMaskBankCard(bankCard [,mask] )

- 参数：

  - bankCard：银行卡号，支持String类型
  - mask：掩码字符，可选参数，支持String类型，默认为*

- 返回值：掩码后的银行卡号

- 示例

  ```
  DesenMaskBankCard('6224121206590423059','#')  --> 622412#########3059
  DesenMaskBankCard('6224121206590423059')  --> 622412*********3059
  ```

##### 掩码密码

- 功能：密码全部脱敏成掩码

- 格式：DesenMaskPassword(password [,mask] )

- 参数：

  - password：密码，支持String类型
  - mask：掩码字符，可选参数，支持String类型，默认为*

- 返回值：掩码后的密码

- 示例

  ```
  DesenMaskPassword('123456abc','#')  --> #########
  DesenMaskPassword('123456')  --> ******
  ```

##### 掩码中文姓名

- 功能：中文姓名保留第一个汉字,其他全部脱敏成掩码

- 格式：DesenMaskChineseName(chineseName [,mask] )

- 参数：

  - chineseName：中文姓名，支持String类型
  - mask：掩码字符，可选参数，支持String类型，默认为*

- 返回值：掩码后的中文姓名

- 示例

  ```
  DesenMaskChineseName('李小明','#')  --> 李##
  DesenMaskChineseName('张三')  --> 张*
  ```

##### 掩码电子邮箱账号

- 功能：电子邮箱账号前缀保留前两位，其他脱敏成掩码，@和后缀保持不变

- 格式：DesenMaskEmail(email [,mask] )

- 参数：

  - email：电子邮箱账号，支持String类型
  - mask：掩码字符，可选参数，支持String类型，默认为*

- 返回值：掩码后的电子邮箱账号

- 示例

  ```
  DesenMaskEmail('danny@126.com','#')  --> da###@126.com
  DesenMaskEmail('xiao_ming@yeah.net')  --> xi*******@yeah.net
  ```

#### 掩码-通用型

##### 掩码左侧字符串

- 功能：字符串左侧前len个字符脱敏成长度为fixLen的掩码，其他保持不变

- 格式：DesenMaskLeft(str,len \[,mask\]\[,fixLen\] )

- 参数：

  - str：String类型,输入的字符串
  - len：int,从左1起被掩码的位置长度
  - mask：可选参数，掩码字符，String类型，默认为*
  - fixLen：可选参数，掩码字符的长度，String类型，默认为len

- 返回值：掩码后的字符串；

- 示例

  ```sql
  select  DesenMaskLeft('abc1234def',3,'#',1)
         ,DesenMaskLeft('abc1234def',3,1)
         ,DesenMaskLeft('abc1234def',3,'#')
         ,DesenMaskLeft('abc1234def',3)
  # 结果
  # #1234def *1234def ###1234def ***1234def
  ```

##### 掩码右侧字符串

- 功能：字符串右侧前len个字符脱敏成长度为fixLen的掩码，其他保持不变

- 格式：DesenMaskRight(str,len \[,mask\]\[,fixLen\] )

- 参数：

  - str：String类型,输入的字符串
  - len：int,从右1起被掩码的位置长度
  - mask：可选参数，掩码字符，String类型，默认为*
  - fixLen：可选参数，掩码字符的长度，String类型，默认为len

- 返回值：掩码后的字符串；

- 示例

  ```sql
  select  DesenMaskRight('abc1234def',3,'#',1)
         ,DesenMaskRight('abc1234def',3,1)
         ,DesenMaskRight('abc1234def',3,'#')
         ,DesenMaskRight('abc1234def',3)
  # 结果
  # abc1234# abc1234* abc1234### abc1234***
  ```

##### 掩码中间字符串

- 功能：字符串左侧和左右分别保留leftLen和rightLen个字符，中间部分脱敏成长度为fixLen的掩码

- 格式：DesenMaskMiddle(str,leftLen,rightLen \[,mask\]\[,fixLen\] )

- 参数：

  - String类型,输入的字符串
  - leftLen：String类型,左侧保留的字符串长度
  - rightLen：String类型,右侧保留的字符串长度
  - mask：可选参数，掩码字符，String类型，默认为*
  - fixLen：可选参数，掩码字符的长度，String类型，默认跟脱敏前字符长度一致

- 返回值：掩码后的字符串；

- 示例

  ```sql
  select  DesenMaskMiddle('abc1234def',3,3,'#',1)
         ,DesenMaskMiddle('abc1234def',3,3,1)
         ,DesenMaskMiddle('abc1234def',3,3,'#')
         ,DesenMaskMiddle('abc1234def',3,3)
  # 结果
  # abc#def abc*def abc####def abc****def
  ```

##### 掩码指定位置字符串

- 功能：将指定位置的字符串掩码

- 格式：DesenMaskPositionInclude(str,numsStr [,mask] )

- 参数：

  - String类型,输入的字符串
  - numsStr ：String类型,位置字符串,分隔符为',' ,连续符为'~'
  - mask：可选参数，掩码字符，String类型，默认为*

- 返回值：掩码后的字符串；

- 示例

  ```sql
  select  DesenMaskPositionInclude('abc1234def','1~3,6,-1','#')
         ,DesenMaskPositionInclude('abc1234def','1~-4,9')
  # 结果
  # ###12#4de# *******d*f
  ```

##### 掩码除指定位置外字符串

- 功能：将除指定位置以外的字符串掩码

- 格式：DesenMaskPositionExclude(str,numsStr [,mask] )

- 参数：

  - String类型,输入的字符串
  - numsStr ：String类型,位置字符串,分隔符为',' ,连续符为'~'
  - mask：可选参数，掩码字符，String类型，默认为*

- 返回值：掩码后的字符串；

- 示例

  ```sql
  select  DesenMaskPositionExclude('abc1234def','1~3,6,-1','#')
         ,DesenMaskPositionExclude('abc1234def','1~-4,9')
  # 结果
  # abc##3###f abc1234*e
  ```


