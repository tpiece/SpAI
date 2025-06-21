# SpAI 项目

SpAI是一个基于Spring Boot和Vue.js的本地文章知识库，集成了OpenAI的能力，提供智能交互服务。

## 项目结构

```
SpAI/
├── front/                 # 前端Vue.js项目
│   ├── src/               # Vue源代码
│   ├── public/            # 静态资源
│   └── package.json       # 前端依赖配置
├── src/                   # 后端Spring Boot项目
│   └── main/
│       ├── java/com/spai/ # Java源代码
│       │   ├── controller/# 控制器
│       │   ├── service/   # 服务层
│       │   ├── dao/       # 数据访问层
│       │   ├── pojo/      # 实体类
│       │   ├── config/    # 配置类
│       │   └── SpAiApplication.java # 应用入口
│       └── resources/     # 资源文件
├── uploads/               # 上传文件目录
├── mp3/                   # 音频文件目录
└── pom.xml                # Maven依赖配置
```

## 技术栈

### 后端
- Spring Boot 3.4.3
- Spring AI (OpenAI集成)
- MyBatis
- MySQL
- Lombok
- Thymeleaf
- 阿里云OSS

### 前端
- Vue.js 2.6
- Element UI
- Axios
- Bootstrap
- Vue Router

## 开发环境要求

- JDK 17+
- Maven
- Node.js
- MySQL

## 如何运行

### 后端服务

1. 确保已安装JDK 17和Maven
2. 配置数据库连接（在application.properties中）
3. 执行以下命令：

```bash
mvn spring-boot:run
```

### 前端服务

1. 进入前端目录：

```bash
cd front
```

2. 安装依赖：

```bash
npm install
```

3. 启动开发服务器：

```bash
npm run serve
```

4. 构建生产版本：

```bash
npm run build
```

## 功能特点

- AI智能对话
- 文章检索与向量存储
- 基于RAG的知识问答
- 用户认证与授权
- 文件上传与管理
