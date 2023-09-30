def partition(list_, size=20):
    """
    拆分list
    :param list_: 列表
    :param size: 子列表大小
    :return: 子列表数组
    """
    if list_:
        return [list_[i:i + size] for i in range(0, len(list_), size)]
    else:
        return list()
