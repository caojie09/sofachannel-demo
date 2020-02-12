# SOFAChannel#11 一个例子展示 SOFAArk

## 实验内容

通过 [SOFAArk](https://github.com/sofastack/sofa-ark) 提供的动态模块能力，实现商品列表排序策略的动态变更。通过在不重启宿主机，不更改应用配置的情况下实现应用行为的改变。

## 任务

### 1、任务准备

从 github 上将 demo 工程克隆到本地

```bash
git clone https://github.com/caojie09/sofachannel-demo.git
```

然后将工程导入到 IDEA 或者 eclipse，打开工程后，工程目录结构如下：

```bash
├── conf
│   └── ark
│       ├── bootstrap.properties
│       └── log
│           └── logback-conf.xml
├── dynamic-facade
├── dynamic-provider
├── dynamic-stock-mng
└── pom.xml
```

* conf/ark 目录用于配置 ark 启动读取的变量
* dynamic-facade 定义了一个 Java 接口 io.sofastack.dynamic.facade.StrategyService，该接口用于对传入的商品列表进排序并返回
* dynamic-provider 实现了 dynamic-facade 定义的接口，并将实现类发布成一个服务
* dynamic-stock-mng 宿主应用，提供一个 web 页面，用于展示实验效果

### 2、将 dynamic-provider 打包成 ark biz
在 dynamic-provider/pom.xml 中，增加 ark 打包插件，并进行配置：

![image.png](https://gw.alipayobjects.com/mdn/rms_ff360b/afts/img/A*y2BvRKG14JUAAAAAAAAAAABkARQnAQ)


```xml
<plugin>
    <groupId>com.alipay.sofa</groupId>
    <artifactId>sofa-ark-maven-plugin</artifactId>
    <executions>
        <execution>
            <!--goal executed to generate executable-ark-jar -->
            <goals>
                <goal>repackage</goal>
            </goals>
            <!--ark-biz 包的打包配置  -->
            <configuration>
                <!--是否打包、安装和发布 ark biz，详细参考 Ark Biz 文档，默认为false-->
                <attach>true</attach>
                <!--ark 包和 ark biz 的打包存放目录，默认为工程 build 目录-->
                <outputDirectory>target</outputDirectory>
                <!--default none-->
                <arkClassifier>executable-ark</arkClassifier>
                <!-- ark-biz 包的启动优先级，值越小，优先级越高-->
                <priority>200</priority>
                <!--设置应用的根目录，用于读取 ${base.dir}/conf/ark/bootstrap.application 配置文件，默认为 ${project.basedir}-->
                <baseDir>../</baseDir>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### 3、构建宿主应用

在已下载下来的工程中，dynamic-stock-mng 作为实验的宿主应用工程模型。通过此步骤，将 dynamic-stock-mng  构建成为动态模块的宿主应用。

#### step1 : 引入动态模块依赖

> 动态模块是通过 SOFAArk 组件来实现的，因此需要引入 SOFAArk 相关的依赖即可。关于 SOFAArk 可以参考[SOFABoot 类隔离](https://www.sofastack.tech/projects/sofa-boot/sofa-ark-readme/)
一节进行了解。

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*lM_1SoNIXIYAAAAAAAAAAABkARQnAQ)

* SOFAArk 相关依赖

    ```xml
    <dependency>
        <groupId>com.alipay.sofa</groupId>
        <artifactId>sofa-ark-springboot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alipay.sofa</groupId>
        <artifactId>web-ark-plugin</artifactId>
    </dependency>
    <dependency>
        <groupId>io.sofastack</groupId>
        <artifactId>dynamic-provider</artifactId>
        <version>1.0.0</version>
        <classifier>ark-biz</classifier>
    </dependency>
    ```
    
* 宿主应用打包插件

    ```xml
    <plugin>
        <groupId>com.alipay.sofa</groupId>
        <artifactId>sofa-ark-maven-plugin</artifactId>
        <executions>
            <execution>
                <id>default-cli</id>
                <goals>
                    <goal>repackage</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <priority>100</priority>
            <baseDir>../</baseDir>
            <bizName>stock-mng</bizName>
        </configuration>
    </plugin>
    ```

#### step2 : 宿主应用配置

* 动态模块配置
 
    在当前项目的根目录 /conf/ark/bootstrap.properties 配置文件中添加配置如下：
    
    ```properties
    # 日志根目录
    logging.path=./logs
    # 宿主应用名
    com.alipay.sofa.ark.master.biz=stock-mng
    ```
    com.alipay.sofa.ark.master.biz 配置项为指定的动态模块宿主应用的名称，需与宿主应用打包插件中的 bizName 配置项保持一致。
### 4、打包 & 启动宿主应用

#### 执行 mvn clean package

配置完成之后，执行 mvn clean package 进行打包，此时 dynamic-provider 会被打包成动态模块包，如下图所示：

![image.png](https://gw.alipayobjects.com/mdn/rms_c69e1f/afts/img/A*fbgOSpPdAIkAAAAAAAAAAABkARQnAQ)

dynamic-stock-mng 会被打包成一个 ark biz 可执行包，如下图所示：

![image.png](https://gw.alipayobjects.com/mdn/rms_c69e1f/afts/img/A*11DgTaU4BhMAAAAAAAAAAABkARQnAQ)


#### 启动宿主应用

```bash
rm -rf ~/dynamic-stock-mng*
cp dynamic-stock-mng/target/dynamic-stock-mng-1.0.0-ark-executable.jar ~/
java -jar ~/dynamic-stock-mng-1.0.0-ark-executable.jar
```

启动成功之后日志信息如下：

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*3N_nS6P223IAAAAAAAAAAABkARQnAQ)

访问 http://localhost:8080 ，现在展示的是默认的排列顺序，如下所示：

![image.png](https://gw.alipayobjects.com/mdn/rms_c69e1f/afts/img/A*HpKuR7Wn44UAAAAAAAAAAABkARQnAQ)

### 5、新建按照销量排序策略模块
dynamic-provider 提供的 io.sofastack.dynamic.facade.StrategyService 实现类返回了默认排序，现在我们要开发一个新版本模块，这个新版本模块会按照销量高低返回商品列表。

首先，修改 io.sofastack.dynamic.provider.impl.StrategyServiceImpl 实现类如下：

```java
@Service
@SofaService
public class StrategyServiceImpl implements StrategyService {
    @Override
    public List<ProductInfo> strategy(List<ProductInfo> products) {
        Collections.sort(products, (m, n) -> n.getOrderCount() - m.getOrderCount());
        products.stream().forEach(p -> p.setName(p.getName()+"("+p.getOrderCount()+")"));
        return products;
    }
}
```

然后，修改 dynamic-provider 版本号 2.0.0: 

```xml
<version>2.0.0</version>
```

配置完成之后，执行 mvn clean package 进行打包，此时可以打包出新版本 dynamic-provider ark biz包，如下图所示：
![pic](https://gw.alipayobjects.com/mdn/rms_c69e1f/afts/img/A*lWUSQb95azoAAAAAAAAAAABkARQnAQ)


telnet 连接 SOFAArk，安装新版本 dynamic-provider:
```bash
## 连接 SOFAArk telnet
> telnet localhost 1234

## 安装新版本 dynamic-provider
sofa-ark>biz -i file:///Users/sample/sofachannel-demo/dynamic-provider/target/dynamic-provider-2.0.0-ark-biz.jar
Start to process install command now, pls wait and check.

## 查看安装的模块信息
sofa-ark>biz -a
stock-mng:1.0.0:activated
dynamic-provider:2.0.0:resolved
dynamic-provider:1.0.0:activated
biz count = 3

## 切换 activated 模块
sofa-ark>biz -o dynamic-provider:2.0.0
Start to process switch command now, pls wait and check.
```

切换完模块后，访问 http://localhost:8080 ，现在展示的是列表编程按照销量进行排序，如下所示：

![image.png](https://gw.alipayobjects.com/mdn/rms_c69e1f/afts/img/A*vqEJQ4775u4AAAAAAAAAAABkARQnAQ)


