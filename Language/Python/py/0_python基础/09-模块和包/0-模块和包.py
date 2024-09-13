"""
模块（Module）
模块是包含 Python 定义和声明的文件。这些定义可能包括函数、类和变量，也可能包括可执行的代码。

1.单个 .py 文件：每个 Python 文件都被视为一个模块，文件名就是模块名。
2.导入模块：可以使用 import 语句来导入模块，以便在其他 Python 脚本中使用它的功能。
3.命名空间：每个模块都有一个自己的命名空间，这意味着在模块内部定义的变量和函数不会影响其他模块的命名空间。

包（Package）
包是包含多个模块的容器，通常用于组织大型应用程序。包可以包含多个 Python 文件和子目录，这些子目录也可以包含自己的模块和包。

1.目录结构：一个包是一个包含 __init__.py 文件的目录，这个文件可以为空，但它必须存在，以便 Python 将目录视为包。
2.导入包：可以使用 import 语句来导入包，然后访问包内的模块和函数。
3.命名空间：包有自己的命名空间，可以包含多个模块的命名空间。


在 Python 中，`from ... import ...` 语句用于从模块或包中导入特定的类、函数或变量。这种导入方式允许你直接使用导入的组件，而不需要通过模块或包的名称来引用。
"""
# 导入多个组件,你可以使用括号来从模块中导入多个组件。
from mypackage.my_module1 import info_print2, info_print1

info_print1()
info_print2()

# 重命名导入的组件,你可以使用 `as` 关键字给导入的组件重命名，这在解决命名冲突或简化命名时很有用。
from mypackage import my_module1 as m1

m1.info_print1()

# 从包中导入模块,你可以从包中导入模块，然后使用模块中的组件。
from mypackage import my_module1

my_module1.info_print1()

# 从包中导入特定组件,你也可以直接从包中导入特定的函数或类。
from mypackage.my_module1 import info_print2

info_print2()
