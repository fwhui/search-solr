# search-solr
A sample code for solr. Development based on SpringBoot.

| jdk  | solr  |
| :--: | :---: |
| 1.8  | 7.7.2 |

##### 一、文件结构

```
search-solr
  |- README.md 
  |- doc
      |- database  (项目数据库文件)
      |- img       (效果截图)
      |- config    (solr配置文件)
      |- static    (项目静态文件)
  |- src
  |- pom.xml
```

solr的安装参考下文附录I，现假设大家的solr环境是正常的。



##### 二、项目配置

1、将项目中的数据库文件导入数据库

2、新建一个solr core(我的solr core命名为search)并设置好需要的分词器(参考附录II)

3、因为本项目使用了solr自带的分词器和ik分词器，所以两种分词器都需要配置

4、我使用的solr的配置文件在`doc/config`文件夹下，可参考配置

5、导入数据库文件到solr中，可在solr后台测试下

6、solr数据正常后可以启动SpringBoot项目了

7、没有什么太大的问题，项目应该可以正常运行了



##### 三、运行测试

1、默认页面效果

![default](/Users/admin/Documents/me/github/search-solr/doc/img/default.png)

2、关键字查询效果

![image1](/Users/admin/Documents/me/github/search-solr/doc/img/image1.png)

3、类目价格筛选效果

![iamge3](/Users/admin/Documents/me/github/search-solr/doc/img/iamge3.png)

![image4](/Users/admin/Documents/me/github/search-solr/doc/img/image4.png)

4、价格、上架时间、更新时间排序效果

![image5](/Users/admin/Documents/me/github/search-solr/doc/img/image5.png)



##### 四、附录

- 附录I       安装solr(version 7.7.2)

1、[solr安装包下载](<https://mirrors.tuna.tsinghua.edu.cn/apache/lucene/solr/7.7.2/>)

2、解压下载的压缩包

3、进入解压后的目录，执行命令：`bin/solr  start` ，看到以下内容就证明solr启动成功了

```
Started Solr server on port 8983 (pid=65295). Happy searching!
```

4、在浏览器访问：localhost:8983，会出现以下页面：

![solr_ok](/Users/admin/Documents/me/github/search-solr/doc/img/solr_ok.png)

5、solr安装完成(需要将solr服务移到tomcat启动的可以自行查找相关资料)

- 附录II      新建solr core并配置中文分词器

1、新建solr core

​       每个solr实例都是solr的一个核心，一个solr服务可以创建多个核心，每个核心都可以进行自己独立配置。

​       先进入solr的解压目录下，然后进入`server/solr`，新建文件夹，该文件夹名称将与之后创建的solr core名称相同。将该目录下`configsets/_default`目录内的conf拷贝到上面新建的文件夹内。

（1）、solr管理后台新建core

![new_core](/Users/admin/Documents/me/github/search-solr/doc/img/new_core.png)

（2）、solr命令新建core

```
进入solr安装目录：输入命令 bin/solr create –c core_name
```

2、配置中文分词器

（1）、配置solr自带的分词器

A、在solr的解压目录下有个dist目录，将其中的`solr-analysis-extras-7.7.2.jar`和`solr-analytics-7.7.2.jar`复制到`解压目录/server/solr-webapp/webapp/WEB-INF/lib` 下；

B、进入创建的core文件夹，修改其`conf/managed-schema`文件，添加内容：

```xml
<fieldType name="text_cn" class="solr.TextField" positionIncrementGap="100">
  <analyzer type="index">
  	<tokenizer class="org.apache.lucene.analysis.cn.smart.HMMChineseTokenizerFactory"/>
  </analyzer>
  <analyzer type="query">
  	<tokenizer class="org.apache.lucene.analysis.cn.smart.HMMChineseTokenizerFactory"/>
  </analyzer>
</fieldType>
```

C、重启服务，进入solr后台测试下分词效果：

![text_cn](/Users/admin/Documents/me/github/search-solr/doc/img/text_cn.png)

（2）、配置ik分词器

A、下载中文分词器IKAnalyzer，[下载地址](https://download.csdn.net/download/qq_40804005/11162796)

B、解压下载的压缩包( ext.dic可自定义分词 )，目录如下：

![ik](/Users/admin/Documents/me/github/search-solr/doc/img/ik.png)

C、将两个jar包复制到`solr解压目录/server/solr-webapp/webapp/WEB-INF/lib`目录下，将三个配置文件复制到`solr解压目录/server/solr-webapp/webapp/WEB-INF/classes`目录下，如果没有classes文件夹就新建。

D、进入创建的core文件夹，修改其`conf/managed-schema`文件，添加内容：

```xml
<fieldType name="text_ik" class="solr.TextField">
  <analyzer type="index">
	  <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="false" conf="ik.conf"/>
	  <filter class="solr.LowerCaseFilterFactory"/>
  </analyzer>
  <analyzer type="query">
	  <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="true" conf="ik.conf"/>
	  <filter class="solr.LowerCaseFilterFactory"/>
  </analyzer>
</fieldType>
```

E、重启服务，进入solr后台测试下分词效果：

![text_ik](/Users/admin/Documents/me/github/search-solr/doc/img/text_ik.png)

- 附录III     将mysql数据导入solr

1、将mysql驱动( 我用的是`mysql-connector-java-5.1.46.jar` )复制到`solr解压目录/server/solr-webapp/webapp/WEB-INF/lib`目录下，因为要使用dataimport功能，所以需要将`solr解压目录/dist`中`solr-dataimporthandler-7.7.2.jar`和`solr-dataimporthandler-extras-7.7.2.jar`复制到`解压目录/server/solr-webapp/webapp/WEB-INF/lib` 下;

2、进入创建的core文件夹，在其`conf`文件夹内创建`data-import.xml`文件，文件内容如下：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<dataConfig>
    <dataSource type="JdbcDataSource" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost:3306/product" user="root" password="root"/>
    <document>
        <entity name="product" query="select p.id,p.title,p.sell_point,p.price,c.name,d.desc,p.created,p.updated from product p,category c,description d where p.cid=c.id and p.id = d.pid">
            <field column="id" name="id"/>
            <field column="title" name="prod_title"/>
            <field column="sell_point" name="prod_sell_point"/>
            <field column="price" name="prod_price"/>
            <field column="name" name="prod_category"/>
            <field column="desc" name="prod_desc"/>
            <field column="created" name="prod_created"/>
            <field column="updated" name="prod_updated"/>
        </entity>
    </document>
</dataConfig>
```

3、修改`conf`文件夹内的managed-schema文件，做好字段的映射，添加的内容如下：

```xml
<field name="prod_title" type="text_ik" indexed="true" stored="true" multiValued="true"/>
<field name="prod_sell_point" type="text_cn" indexed="true" stored="true"/>
<field name="prod_price" type="pint" indexed="true" stored="true"/>
<field name="prod_category" type="string" indexed="true" stored="true" />
<field name="prod_desc" type="text_cn" indexed="true" stored="true"/>
<field name="prod_created" type="pdate" indexed="true" stored="true"/>
<field name="prod_updated" type="pdate" indexed="true" stored="true"/>
```

managed-schema文件里一些常用的字段已做好映射，例如id，所以我们就不必重复去声明id字段了(重复了会报错)。

4、修改`conf`文件夹内的`solrconfig.xml`文件，引入第2步创建的`data-import.xml`文件：

```xml
<requestHandler name="/dataimport" class="org.apache.solr.handler.dataimport.DataImportHandler">
  <lst name="defaults">
    <str name="config">data-import.xml</str>
  </lst>
</requestHandler>
```

5、重启solr服务，进入solr后台导入数据

![data-execute](/Users/admin/Documents/me/github/search-solr/doc/img/data-execute.png)

6、测试数据是否导入成功

![query](/Users/admin/Documents/me/github/search-solr/doc/img/query.png)

