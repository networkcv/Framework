class ASTNode(object):
    def __init__(self):
        pass

    # def __str__(self):
    #     pass

    # 类似于 Java toString,如果没有为类定义 __str__ 方法，Python 会使用 __repr__ 方法。
    def __repr__(self):
        # 拿到内部所有的成员变量
        fields = []
        for k, v in self.__dict__.items():
            if k.startswith('_'):
                continue
            fields.append(f'{k}={v}')
        return f'<{self.__class__.__name__}> {" ".join(fields)}'


class Select(ASTNode):

    def __init__(self, targets):
        super().__init__()
        self.targets = targets
        self.from_table = None
        # 下面这些子句 (clause)，也是继承了ASTNode
        self.where = None
        self.group_by = None
        self.order_by = None
        # 如果我们后面想要支持更多语法元素，在下面添加新的字段（属性）就可以了
        # 例如，self.distinct = True


class OrderBy(ASTNode):
    def __init__(self, column, direction):
        super().__init__()
        self.column = column
        self.direction = direction


class JoinType:
    LEFT_JOIN = 'LEFT JOIN'  # 左连接
    RIGHT_JOIN = 'RIGHT JOIN'  # 右连接
    INNER_JOIN = 'INNER JOIN'  # 内连接  mysql 不支持全连接
    FULL_JOIN = 'FULL JOIN'  # 全连接
    CROSS_JOIN = 'CROSS JOIN'  # 交叉连接，笛卡尔积


class Join(ASTNode):
    def __init__(self, left, right, join_type, condition=None):
        super().__init__()
        self.left = left
        self.right = right
        self.join_type = join_type
        self.condition = condition


class Identifier(ASTNode):
    def __init__(self, parts=None):
        super().__init__()
        self.parts = parts


class Star(ASTNode):
    pass


class Constant(ASTNode):
    def __init__(self, value=None):
        super().__init__()
        self.value = value


class Operation(ASTNode):
    def __init__(self, op, args):
        super().__init__()
        self.op = op.lower()  # TODO: 最好后面实现把结果表达更干净，删除空格之类的
        self.args = list(args)


class BinaryOperation(Operation):
    pass


class FunctionOperation(Operation):
    pass
