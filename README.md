1. RagController中将文档存储到向量数据库的代码是注释的，如需要测试这部分功能，打开注释即可。
2. VectorStore接口没有提供删除文档的方法，想要删除文档可以使用scripts/clearchroma.py。
3. application.yml中根据自己情况配置openai的host和api key。