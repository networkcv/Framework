doc as code

# 交互接口介绍

- 数据库SQL语句的总入口：exec_imoocdb_query（sql_stmt: str）-> Result 用于接收用户的访问SQL，协调数据庰
- SQL引擎SQL解析部分的入口：query_parse（sql_stmt: str）-> AsTNode 用于解析SQL语句，返回AST抽象语法树
- SQL引/擎SQL优化过程的入口：‘query_plan（ast: ASTNode）-> PlanTree 用于将解析后的AST转换为具体的执行计戈
- 执行引擎的入口：exec_plan（plan_tree: PlanTree） 用于将具体的执行计划进行执行，并返回结果
