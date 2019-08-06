[TOC]

# jet-hive-udf



## 简介

jet-hive-udf 包含了一些有用的hive udf函数，包含日期计算，ip,useragent解析函数，加密解密等.

> 注意:
> jet-hive-udf支持hive-0.11.0或更高版本.
> 其中useragent解析需要使用到java8+,其他需要java6+

## 编译

### 1. 安装依赖

本项目仅需要maven环境；不需要手动增加add jar包；

若要手动add jar包，请参考lib子目录的示例脚本



### 2. 用mvn打包 

#### mvn打包

```
cd ${project_home}
mvn clean package
```

- 如果你想跳过单元测试，可以这样运行：

```
cd ${project_home}
mvn clean package -DskipTests
```

命令执行完成后, 将会在target目录下生成[A=jet-hive-udf-\${version}-shaded.jar, B=jet-hive-udf-\${version}.jar]文件.其中A是包括所有依赖包的jar, B是最小编译jar文件

你也可以直接在发布页下载打包好了最新版本 [发布页](https://github.com/jet2007/jet-hive-udf/releases). 当前最新的版本是 `1.0.0`

#### jar依赖+编译打包说明

- jar依赖

  - hive类：hive-jdbc，hive-exec，hadoop-common
  - ip解析类：ip2region
  - useragent解析类：yauaa-hive，javacsv，juan(可选)
  - 加密解密类：commons-codec

- 编译打包

  - 使用上面的mvn clean package -DskipTests命令进行编译打包

  - 生成[A=jet-hive-udf-\${version}-shaded.jar, B=jet-hive-udf-\${version}.jar]文件

    - A[shaded]:包括所有依赖包的jar

    - B是最小编译jar文件,即是只有本项目的java和资源文件

    - 若要自定义指定依赖包，可修改pom.xml当中maven-shade-plugin的artifactSet

      - 参考写法如下方注释

      ```xml
      <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-shade-plugin</artifactId>
          <version>2.4.3</version>
          <executions>
              <execution>
                  <phase>package</phase>
                  <goals>
                      <goal>shade</goal>
                  </goals>
                  <configuration>
                      <artifactSet>
                           <excludes>
                              <exclude></exclude>
                          </excludes>
                      </artifactSet>
                      <shadedArtifactAttached>true</shadedArtifactAttached>
                  </configuration>
              </execution>
          </executions>
      </plugin>     
      
      <!-- artifactSet 指定jar的示例
      <artifactSet>
        <includes>
            <include>jmock:*</include>
            <include>*:xml-apis</include>
            <include>org.apache.maven:lib:tests</include>
        </includes>
        <excludes>
            <exclude>classworlds:classworlds</exclude>
            <exclude>junit:junit</exclude>
        </excludes>
      <artifactSet>
      -->
      
      ```

  - 最新版本 [发布页](https://github.com/jet2007/jet-hive-udf/releases)提供已编译好的jar包

    - jet-hive-udf-1.0.0-java7.jar
    - jet-hive-udf-1.0.0-shaded-java7.jar
    - jet-hive-udf-1.0.0-java8.jar
    - jet-hive-udf-1.0.0-shaded-java8.jar



## 用法

将下面这些内容写入 `${HOME}/.hiverc` 文件, 或者也可以按需在hive命令行环境中执行.

```sql
add jar ${jar_location_dir}/jet-hive-udf-${version}-shaded.jar
create temporary function md5 as 'com.jet.hive.udf.encrypt.UDFMd5';
create temporary function sha256 as 'com.jet.hive.udf.encrypt.UDFSha256';
create temporary function sha512 as 'com.jet.hive.udf.encrypt.UDFSha512';
create temporary function enBase64 as 'com.jet.hive.udf.encrypt.UDFBase64';
create temporary function enDes as 'com.jet.hive.udf.encrypt.UDFDes';
create temporary function deBase64 as 'com.jet.hive.udf.decrypt.UDFBase64';
create temporary function deDes as 'com.jet.hive.udf.decrypt.UDFDes';

create temporary function ipgeo as 'com.jet.hive.udf.ipgeo.UDFIp2Region';
create temporary function ipgeo2 as 'com.jet.hive.udf.ipgeo.UDFIp2RegionStruct';

create temporary function UserAgentParser as 'com.jet.hive.udf.ipgeo.UDFUserAgentParserYauaaSupportedDeviceDefault';
create temporary function UserAgentParserHdfs as 'com.jet.hive.udf.ipgeo.UDFUserAgentParserYauaaSupportedDeviceHdfsFile';

-- 日期
create temporary function DateDelta as 'com.jet.hive.udf.date.DateDelta' 
create temporary function CurrentDateTimeFormatDelta as 'com.jet.hive.udf.date.CurrentDateTimeFormatDelta'  ;
create temporary function CurrentDateDelta as 'com.jet.hive.udf.date.CurrentDateDelta';
create temporary function CurrentDateDelta2 as 'com.jet.hive.udf.date.CurrentDateDelta2'   ;
create temporary function CurrentDateTimeDelta as 'com.jet.hive.udf.date.CurrentDateTimeDelta'   ;
create temporary function CurrentDateTimeDelta2 as 'com.jet.hive.udf.date.CurrentDateTimeDelta2'  ;

```





## 函数

函数列表及简要说明

| 分类      | 函数                                                 | 描述                                                         |
| --------- | :--------------------------------------------------- | :----------------------------------------------------------- |
| 加密      | md5(string) -> string                                | md5 哈希                                                     |
| 加密      | sha256(string) -> string                             | sha256 哈希                                                  |
| 加密      | sha512(string) -> string                             | sha256 哈希                                                  |
| 加密      | enBase64(string) -> string                           | base64加密，可逆                                             |
| 加密      | enDes(string [,string]) -> string                    | des加密，可逆；                                              |
| 解密      | deBase64(string) -> string                           | base64解密                                                   |
| 解密      | deDes(string [,string]) -> string                    | des解密                                                      |
| IP        | ipgeo(ip_string) -> Map                              | 解析ip->国家,地区,省份,城市,ISP                              |
| IP        | ipgeo2(ip_string) -> Struct                          | 解析ip->国家,地区,省份,城市,ISP                              |
| useragent | UserAgentParse(ua_string [,fileds])                  | 解析useragent->设备,使用jar资源文件,浏览器,操作系统等；**java8+** |
| useragent | UserAgentParseHdfs(ua_string ,fileds,hdfs_fileanme ) | 解析useragent,使用hdfs文件->设备,浏览器,操作系统等; **java8+** |
| 日期计算  | DateDelta(date,offsets [,format])                    | 日期加减计算(包括年月日时分秒)                               |
| 日期计算  | CurrentDateTimeFormatDelta                           | 基于当前系统时间的日期加减计算(包括年月日时分秒)             |
| 日期计算  | CurrentDateDelta                                     | 基于当前系统时间的日期加减计算(包括年月日)，日期格式yyyy-MM-dd |
| 日期计算  | CurrentDateDelta2                                    | 基于当前系统时间的日期加减计算(包括年月日)，日期格式yyyyMMdd |
| 日期计算  | CurrentDateTimeDelta                                 | 基于当前系统时间的日期加减计算(包括年月日时分秒)，格式yyyy-MM-dd hh:mm:ss |
| 日期计算  | CurrentDateTimeDelta2                                | 基于当前系统时间的日期加减计算(包括年月日时分秒)，格式yyyyMMddhhmmss |



### 1. 加密解密函数

#### Base64的加密解密函数（enBase64,deBase64函数）

- 参数：明文或密文
- 用途：加密解密速度快，常用HTTP等简单加密



#### Des:使用密钥加密的块算法（enDes,deDes函数）

- 参数：第1个参数为明文或密文；第2个参数：密钥，可选，默认值为JetdKxzVCbsgVIwTnc1jtpWn
- 用途：使用密钥进行加解密，加密解密速度慢



### 2. Ip解析函数

#### Ip解析介绍

- 参考项目：

  - 使用ip2region项目[https://github.com/lionsoul2014/ip2region](https://github.com/lionsoul2014/ip2region),
  - ip2region - 最自由的ip地址查询库，ip到地区的映射库;
  - 数据聚合了一些知名ip到地名查询提供商的数据(淘宝IP地址库,GeoIP,纯真IP库)

- 使用

  - 依赖ip2region-1.7.jar包
  - ip地址库的资源文件=ip2region.db


#### IpGeo函数

- 类：com.jet.hive.udf.ipgeo.UDFIp2Region
- 参数：IP地址
- 返回：Map类型的地理信息，示例{"ispName":"电信","countryName":"中国","regionName":null,"cityName":"南京市","provinceName":"江苏省"}
- 示例：select ipgeo('221.226.1.11'),  ipgeo('221.226.1.11')['provinceName']

#### IpGeo2函数

- 类：com.jet.hive.udf.ipgeo.UDFIp2RegionStruct
- 参数：IP地址
- 返回：Struct类型的地理信息，示例{"ispName":"电信","countryName":"中国","regionName":null,"cityName":"南京市","provinceName":"江苏省"}
- 示例：select ipgeo2('221.226.1.11'),  ipgeo2('221.226.1.11').provinceName



### 3. UserAgent解析函数



#### 介绍-ua解析

UserAgent解析使用了第3个的相关项目，介绍如下

##### ua解析项目Yauaa：

- 项目yauaa:https://github.com/nielsbasjes/yauaa
- This is a java library that tries to parse and analyze the useragent string and extract as many relevant attributes as possible. 解析出设备、操作系统、Agent，Engine等信息。**环境JAVA8+**
- 注意：需要**JAVA8+**
- 项目提供了一个hive udf供使用，但相关设备名称+厂商信息不完整，参考https://yauaa.basjes.nl/UDF-ApacheHive.html
- 优点：解析useragent信息较全面且准确率高，
- 缺点：速度慢

##### ua解析项目juan

- 项目**juan**:<https://github.com/codesorcery/juan>
- A fast, thread-safe and dependency-free user agent parser for **Java 8+**.
- 能够解析设备，浏览器，和厂商的相关信息
- 优点：速度比yauaa快，
- 缺点：准确率低(测试几个华为的ua信息，都没有识别出设备信息)



#### 介绍-手机设备信息库

​	对于手机设备来说，useragent解析项目至多是能够解析设备型号信息，并不知道设备名称，厂商(比如ALP-AL00 Build/HUAWEIALP-AL00是华为Mate 10)；故而就需要手机设备信息库



##### 设备信息项目-Google Play Store Supported Devices

- 来源：http://storage.googleapis.com/play_public/supported_devices.csv；示例数据
  - Retail Branding,Marketing Name,Device,Model
  - Huawei,Mate 10,HWALP,ALP-L09
  - Xiaomi,MI CC 9,pyxis,MI CC 9
- 说明：来自Google Play Store,且会定期更新，值得推荐；
- 注意：编码使用UTF-16,编码存在有形式：Y93\xe6\xa0\x87\xe5\x87\x86\xe7\x89\x88 64G



##### 设备信息项目-stevenkang/model-lib

- stevenkang/model-lib：<https://github.com/stevenkang/model-lib>
- 应当是国人维护的一个手机设备库，设备信息库不全，且有重复；可当做**补充**

- 处理方式：参考doc/mobile-model.xlsx进行修复
- jet-hive-udf当中的两个类UDFUserAgentParserYauaaSupportedDeviceDefault(HdfsFile)采用Yauaa作为解析useragent的主要引擎



#### Useragent解析函数说明

​	通过手机设备库补充+增强：厂商+设备名称信息(如)；

​	注意：本项目依赖**JAVA8+**

​	**已实现**：解析工具(Yauaa) + 手机设备库(Google Play Store Supported Devices)；见UDFUserAgentParserYauaaSupportedDeviceDefault(HdfsFile)两个类；

​	**未实现**：非Yauaa非Play Store的方案，都未实现完成；另基本逻辑已完成，但测试效果不好(见类UDFUserAgentParserJuanSupportDevice，UDFUserAgentParserYauaaStevenkang)



#### UserAgentParser函数

- 说明：使用解析工具(Yauaa) + 手机设备库(Google Play Store Supported Devices)，解析ua信息，得到MAP类型的数据；手机设备库supported_devices存储于jar的资源文件上。

- 功能增强：DeviceVendor和DeviceModel 这2个列是新增的(相对于原生Yauaa项目的udf)；实现说明

  -  当DeviceClass in(Mobile,Phone,Tablet)且DeviceName与手机设备库当中的某个手机型号“**匹配**”了；详细匹配规则参见GooglePlayStoreSupportedDeviceBuild的getSupportDeviceBy方法

    ```
    	 配置规则：
         * 		1.型号精确匹配且厂商匹配
    	 *      2.型号精确匹配且厂商不匹配，型号字符长度>1
    	 *      3.型号一般匹配且厂商匹配
    	 *      4.型号一般匹配且厂商不匹配，型号字符长度>2
    ```

    - “**匹配上**”
      - DeviceVendor和DeviceModel取手机设备库上的值
    - “**不匹配**”
      - DeviceVendor和DeviceModel取DeviceBrand和DeviceName的值

- 参数：ua_string [,fileds]
  - 第1个参数：useragent值
  - 第2个参数：解析后展示出来的字段，逗号隔开；可选；
    - 默认值为空（等价于‘default’,也等价于DeviceClass,DeviceVendor,DeviceBrand,DeviceModel,DeviceName,OperatingSystemClass,OperatingSystemName,OperatingSystemVersionMajor,OperatingSystemVersion,AgentClass,AgentName,AgentVersionMajor,AgentVersion,WebviewAppName,WebviewAppVersionMajor,WebviewAppVersion）；
    - 值为'all'(展示出所有的列,约有50，60列)
    - 自定义的列，如DeviceClass,DeviceVendor,DeviceBrand......形式
    - 说明：一般情况default可满足常规的需求

- 类：com.jet.hive.udf.ipgeo.UDFUserAgentParserYauaaSupportedDeviceDefault

- 示例: 4条ua,及其解析后内容

```json
Dalvik/2.1.0 (Linux; U; Android 5.1.1; OPPO R9 Plusm A Build/L  {"DeviceName":"Oppo R9 Plusm A","OperatingSystemName":"Android","AgentVersionMajor":"2","AgentVersion":"2.1.0","OperatingSystemVersionMajor":"5","WebviewAppName":"Unknown","WebviewAppVersion":"Unknown","DeviceModel":"R9 Plus","WebviewAppVersionMajor":"Unknown","DeviceClass":"Mobile","AgentClass":"Special","DeviceVendor":"Oppo","DeviceBrand":"Oppo","OperatingSystemClass":"Mobile","AgentName":"Dalvik","OperatingSystemVersion":"5.1.1"}
QYPlayer/iOS/4.5.1;NetType/4G;QTP/1.10.8.1      {"DeviceName":"Unknown","OperatingSystemName":"Unknown","AgentVersionMajor":"iOS","AgentVersion":"iOS","OperatingSystemVersionMajor":"??","WebviewAppName":"Unknown","WebviewAppVersion":"Unknown","DeviceModel":"Unknown","WebviewAppVersionMajor":"Unknown","DeviceClass":"Unknown","AgentClass":"Special","DeviceVendor":"Unknown","DeviceBrand":"Unknown","OperatingSystemClass":"Unknown","AgentName":"QYPlayer","OperatingSystemVersion":"??"}
Dalvik/2.1.0 (Linux; U; Android 9; PAR-AL00 Build/HUAWEIPAR-AL00)       {"DeviceName":"Huawei PAR-AL00","OperatingSystemName":"Android","AgentVersionMajor":"2","AgentVersion":"2.1.0","OperatingSystemVersionMajor":"9","WebviewAppName":"Unknown","WebviewAppVersion":"Unknown","DeviceModel":"nova 3","WebviewAppVersionMajor":"Unknown","DeviceClass":"Mobile","AgentClass":"Special","DeviceVendor":"Huawei","DeviceBrand":"Huawei","OperatingSystemClass":"Mobile","AgentName":"Dalvik","OperatingSystemVersion":"9"}
live4iphone/22150 CFNetwork/978.0.7 Darwin/18.5.0       {"DeviceName":"Apple iOS Device","OperatingSystemName":"Darwin (iOS)","AgentVersionMajor":"22150","AgentVersion":"22150","OperatingSystemVersionMajor":"18","WebviewAppName":"Unknown","WebviewAppVersion":"Unknown","DeviceModel":"Unknown","WebviewAppVersionMajor":"Unknown","DeviceClass":"Mobile","AgentClass":"Mobile App","DeviceVendor":"Apple","DeviceBrand":"Apple","OperatingSystemClass":"Mobile","AgentName":"live4iphone","OperatingSystemVersion":"18.5.0"}
```



#### UserAgentParserHdfs函数

- 说明：使用解析工具(Yauaa) + 手机设备库(Google Play Store Supported Devices)，解析ua信息，得到MAP类型的数据；手机设备库supported_devices**使用用户hdfs上的文件**
- 功能上与UserAgentParser基本相同；只是采用资源文件方式不同
- 参数：
  - 3个参数都必选;
  - 前2个参数含义与UserAgentParser的前2个完全一样；
  - 第3个参数：HiveQL add hdfs 语句的文件名称。如[add file hdfs://quickstart.cloudera:8020/user/hive/warehouse/func.db/xxx.csv],则此参数值为xxx.csv
- 注意：本函数**没有**在分布式环境上测试过；请注意



### 4. 日期计算函数

​	日期计算函数，主要使用场景：HiveQL提供的原生日期函数，进行复杂日期计算，需要进行多次计算，不太方便；比如计算上月最后一天，本月最后一天等；

​	核心理解下DateDelta函数，其他5个函数，与其类似。



#### DateDelta函数

- 功能介绍

- 功能：参考python relativedelta 实现方式, 实现date/datetime 的日期加减计算(包括年月日时分秒)；比如计算上月最后一天，本月最后一天等；
- 参数：(date,offsets [,format])；前2个必选；第3可选
  - 第1个参数date: 
    - 格式支持的有"yyyyMMdd, yyyy-MM-dd, yyyyMMddHHmmss, yyyy-MM-dd HH:mm:ss"；
    - 示例'20180102','20180102030405','2018-01-02 03:04:05'
  - 第2个参数offsets：
    - 偏移量，格式如M1=D1,M2=D2,...... , 分隔符为',' 和'='；支持多个偏移，从左到右依次计算偏移；每一个偏移的含义如下：
    - M: year,month,day,hour,minute,second(或y,m,d,h,n,s)
    - D: +N,-N,N(N为数值)；解释+2，-2分别为加或减2天、2为目标赋值第2天(当M=day时)
    - 示例
      - 'year=+2,month=8,day=-16' 等价于 'y=+2,m=8,d=-16'; 含义为依次年+2，月为8，日期减16
    - 特殊
      - 日期加减简单模式：DateDelta('20180102','+3')=DateDelta('20180102','day=+3'), DateDelta('20180102','3')=DateDelta('20180102','day=3');
  - 第3个参数\[Format\]：
    - 可选；目标输出值的Date/Datetime格式，示例"yyyyMMdd, yyyy-MM-dd, yyyyMMddHHmmss, yyyy-MM-dd HH:mm:ss"
    - 若无此参数，输出值格式与第1个参数date格式相同；

- 示例

| 说明                          | 代码                                                         | 结果                |
| ----------------------------- | ------------------------------------------------------------ | ------------------- |
| day+3，简易模式               | SELECT DateDelta('20180102','+3')                            | 20180105            |
| day=3，简易模式               | SELECT DateDelta('20180102','3')                             | 20180103            |
| day+3，原日期格式不变         | SELECT DateDelta('20180102','day=+3')                        | 20180105            |
| day+3，原日期格式不变         | SELECT DateDelta('2018-01-02','day=+3')                      | 2018-01-05          |
| datetime+3day，原日期格式不变 | SELECT DateDelta('20180102030405','day=+3')                  | 20180105030405      |
| datetime+3day，原日期格式不变 | SELECT DateDelta('2018-01-02 03:04:05','day=+3')             | 2018-01-05 03:04:05 |
| 今年最后一天,多次计算         | SELECT DateDelta('2018-09-06','year=+1,month=1,day=1,day=-1') | 2018-12-31          |
| 今年最后一天,多次计算         | SELECT DateDelta('2018-09-06','y=+1,m=1,d=1,d=-1')           | 2018-12-31          |
| 上月最后一天,多次计算         | SELECT DateDelta('20180906','day=1,day=-1')                  | 20180831            |
| 上月最后一天，改变输出格式    | SELECT DateDelta('20180906','day=1,day=-1','**yyyy-MM-dd**') | 2018-08-31          |



#### CurrentDateTimeFormatDelta函数

- 功能介绍

- 功能：实现当前系统日期加减计算(包括年月日时分秒)；与DateDelta功能基本相同，只是把第1个日期换成系统当前时间；
- 参数：(offsets [,format])；第1个必选；第2可选
  - 第1个参数offsets：与DateDelta函数的第2个参数含义相同
  - 第2个参数[format]：
    - 可选；目标输出值的Date/Datetime格式，示例"yyyyMMdd, yyyy-MM-dd, yyyyMMddHHmmss, yyyy-MM-dd HH:mm:ss"
    - 若无此参数，输出值格式为"yyyy-MM-dd HH:mm:ss"；

- 示例

​	若当前系统时间为“2019-02-01 11:06:31”

| 说明     | 代码                                                         | 结果           |
| -------- | ------------------------------------------------------------ | -------------- |
| now+3day | SELECT CurrentDateTimeFormatDelta('+3','yyyyMMdd')           | 20190204       |
| now+3day | SELECT CurrentDateTimeFormatDelta('day=+3','yyyyMMddHHmmss') | 20190204110631 |
|          |                                                              |                |



#### CurrentDateDelta函数

- 功能

- 功能：实现当前系统日期加减计算(包括年月日)；与CurrentDateTimeFormatDelta类似，输出目标格式为yyyy-MM-dd；等价于CurrentDateTimeFormatDelta((offsets,'yyyy-MM-dd'))
- 参数：(offsets)；
  - 第1个参数offsets：与DateDelta函数的第2个参数含义相同

- 示例

​	若当前系统时间为“2019-02-01 11:06:31”

| DESC                  | CODE                                       | RESULT     |
| --------------------- | ------------------------------------------ | ---------- |
| now+3day              | SELECT CurrentDateDelta('+3')              | 2019-02-04 |
| now+3day              | SELECT CurrentDateDelta('day=+3')          | 2019-02-04 |
| end day of this month | SELECT CurrentDateDelta('month=+1,day=-1') | 2019-02-28 |



#### CurrentDateDelta2函数

- 功能

- 功能：实现当前系统日期加减计算(包括年月日)；与CurrentDateTimeFormatDelta类似，输出目标格式为yyyyMMdd；等价于CurrentDateTimeFormatDelta((offsets,'yyyyMMdd'))
- 参数：(offsets)；
  - 第1个参数offsets：与DateDelta函数的第2个参数含义相同

- 示例

​	若当前系统时间为“2019-02-01 11:06:31”

| DESC                  | CODE                                        | RESULT   |
| --------------------- | ------------------------------------------- | -------- |
| now+3day              | SELECT CurrentDateDelta2('+3')              | 20190204 |
| now+3day              | SELECT CurrentDateDelta2('day=+3')          | 20190204 |
| end day of this month | SELECT CurrentDateDelta2('month=+1,day=-1') | 20190228 |



#### CurrentDateTimeDelta函数

- 功能

- 功能：实现当前系统日期加减计算(包括年月日时分钞)；与CurrentDateTimeFormatDelta类似，输出目标格式为yyyyMMdd；等价于CurrentDateTimeFormatDelta((offsets,'yyyy-MM-dd hh:mm:ss'))
- 参数：(offsets)；
  - 第1个参数offsets：与DateDelta函数的第2个参数含义相同

- 示例

​	若当前系统时间为“2019-02-01 11:06:31”

| DESC                  | CODE                                           | RESULT              |
| --------------------- | ---------------------------------------------- | ------------------- |
| now+3day              | SELECT CurrentDateTimeDelta('+3')              | 2019-02-04 03:04:05 |
| now+3day              | SELECT CurrentDateTimeDelta('day=+3')          | 2019-02-04 03:04:05 |
| end day of this month | SELECT CurrentDateTimeDelta('month=+1,day=-1') | 2019-02-28 03:04:05 |



#### CurrentDateTimeDelta2函数

- 功能

- 功能：实现当前系统日期加减计算(包括年月日时分钞)；与CurrentDateTimeFormatDelta类似，输出目标格式为yyyyMMdd；等价于CurrentDateTimeFormatDelta((offsets,'yyyyMMddhhmmss'))
- 参数：(offsets)；
  - 第1个参数offsets：与DateDelta函数的第2个参数含义相同

- 示例

​	若当前系统时间为“2019-02-01 11:06:31”

| DESC                  | CODE                                            | RESULT         |
| --------------------- | ----------------------------------------------- | -------------- |
| now+3day              | SELECT CurrentDateTimeDelta2('+3')              | 20190204030405 |
| now+3day              | SELECT CurrentDateTimeDelta2('day=+3')          | 20190204030405 |
| end day of this month | SELECT CurrentDateTimeDelta2('month=+1,day=-1') | 20190228030405 |

