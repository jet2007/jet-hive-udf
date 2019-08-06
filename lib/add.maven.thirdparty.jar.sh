# 若要手动加入某个包，如jdo2-api-2.3-ec.jar 在maven中央仓库中已经不可用, 因此我们不得不自己下载并安装到本地的maven库中. 命令如下：
wget http://www.datanucleus.org/downloads/maven2/javax/jdo/jdo2-api/2.3-ec/jdo2-api-2.3-ec.jar -O ~/jdo2-api-2.3-ec.jar
mvn install:install-file -DgroupId=javax.jdo -DartifactId=jdo2-api -Dversion=2.3-ec -Dpackaging=jar -Dfile=~/jdo2-api-2.3-ec.jar
