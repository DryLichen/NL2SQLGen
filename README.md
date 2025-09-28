基于DeepSeek的自动化取数脚本生成工具应用研究计划

1.	第一阶段 8.8 – 8.14
形成整体研究课题框架，研究内容，以及明确分工。本部分需小组成员调研
产出：
Word版本的研究报告框架
研究内容部分初稿
分工内容初稿

2.	第二阶段 8.14 – 9.12
总体：RAG（检索增强生成）+ Prompt工程
利用JAVA, Spring Boot搭建工具框架，RAG 检索相关表结构 + DeepSeek。用户输入和正确SQL作为样本，微调提升准确率。
产出：
形成初版的自动化脚本生成工具，包括前端界面，后端服务器，

实施：
2.1	表结构入库
将所有表名、字段名、字段注释提取出来（用 DatabaseMetaData 或 SQL 查询系统表）。存成JSON，同时把这份 JSON 做成向量（使用本地embedding模型），存到 Milvus / FAISS。


2.2	取数脚本生成规范
把规范文档拆成规则点（如“不使用SELECT *”“金额单位为元”“日期格式为yyyy-mm-dd”）
在 Prompt 中以固定格式注入，例如：
sql
CopyEdit
SQL生成规范：
1. 不使用SELECT *，请列出具体字段
2. 日期用yyyy-mm-dd格式
3. 金额单位为元
4. 字段命名以数据库实际字段为准


2.3	检索 + Prompt 例子
假设业务人员输入：
查询2024年对公客户贷款余额大于1000万的记录
检索到相关表：
makefile
CopyEdit
CORP_LOAN: CUSTOMER_ID（客户号）, BALANCE（贷款余额）, LOAN_DATE（贷款日期）, CORP_TYPE（客户类型）
拼接 Prompt：
markdown
CopyEdit
你是一名资深的Oracle SQL开发专家，请根据以下数据库表信息和规范生成SQL。

SQL生成规范：
1. 不使用SELECT *
2. 日期格式：yyyy-mm-dd
3. 金额单位：元

表信息：
表：CORP_LOAN
字段：
  - CUSTOMER_ID（客户号）
  - BALANCE（贷款余额）
  - LOAN_DATE（贷款日期）
  - CORP_TYPE（客户类型）

用户需求：
查询2024年对公客户贷款余额大于1000万的记录

2.4	Java实现关键点
表结构读取
用 DatabaseMetaData 获取所有表和字段信息。
向量检索
本地可用 milvus-sdk-java 或直接用 faiss4j
嵌入模型可用 HuggingFace 上的中文 SimCSE / BERT，离线部署。
DeepSeek调用
如果本地部署为 HTTP API，直接用 RestTemplate 或 WebClient
如果是 gRPC，按官方 SDK 调用
SQL校验
用 Apache Calcite 或 JSQLParser 检查生成 SQL 是否符合语法，并且字段/表存在。




2.5	工作流程
[业务人员输入自然语言需求]
        ↓
[Java服务]
  ① 接收需求
  ② 语义向量化（BERT/SimCSE等本地嵌入模型）
        ↓
[向量数据库（Milvus/FAISS/Elasticsearch）]
  ③ 检索相关表及字段说明（取Top N）
        ↓
[Prompt构造器]
  ④ 拼接：取数脚本规范 + 检索到的表结构 + 用户需求
        ↓
[DeepSeek本地部署]
  ⑤ 生成SQL
        ↓
[SQL解析/验证模块]
  ⑥ 校验语法、表字段存在性
        ↓
[返回SQL给业务人员]

2.6	Milvus
向量数据库，使用MSL2 Linux+Docker在本地部署

2.7	SimCSE
Dev环境：
使用python3
pip install sentence-transformers
下载模型脚本 download_simcse.py
启动服务脚本simcse_service.py


SIT部署:
WSL2 下通过 Docker 部署 SimCSE
在Windows下载Nvidia Game 驱动，会自动安装WSL2 CUDA驱动
PS：如下载包时网路不通，设置clash为tun虚拟网卡模式
PS：还不行，通过Docker Desktop 设置WSL2网络代理，host.docker.internal
 
PS：还不行，用阿里镜像地址下载
PS：还不行，在windows下载资源后wsl直接引用


新建simCSE 容器
mkdir -p ~/simcse && cd ~/simcse
 
构建并启动容器
docker-compose up --build -d


2.8	deepseek
https://huggingface.co/deepseek-ai/deepseek-coder-1.3b-instruct
WSL2配置：
memory=8GB，swap=8GB，processors=4，localhostForwarding=true
（6.7b为完整版本，但需要至少16G的内存。如果需要下载，应使用sanpshot方式而非pretained，且使用量化）

使用python3
下载模型脚本 download_model.py
启动服务脚本deepseek_service.py

3.	第三阶段 9.12 – 9.30
丰富完善自动化脚本生成工具，利用历史数据库工单测试并调优。
产出：
最终的自动化脚本生成工具应用程序

4.	第四阶段 10.9 – 10.15
完成研究报告的撰写，组员各自负责撰写的部分每日整合到云盘文档中。
产出：
最终研究报告
