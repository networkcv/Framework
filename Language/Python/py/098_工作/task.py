import concurrent.futures
import logging

import pandas as pd
from more_itertools import chunked

import deleteShopItemByQuery as dsq


def parse_excel(path):
    sheet = pd.read_excel(path, sheet_name=0)
    _list = []
    for index, row in sheet.iterrows():
        # 可以通过 row['column_name'] 获取每列的值
        # print(f"{index} {row['id']}")
        _list.append(row)
    return _list


def get_delete_ids(_id_list):
    for sub_id_list in list(chunked(_id_list, 5)):
        req_data_param = [{
            "shopItemIdList": sub_id_list
        }]
        return str(req_data_param)


if __name__ == '__main__':
    # id_list = parse_excel('file/惠民要删除数据id.xlsx')
    id_list = [1, 2, 3, 4, 5, 6, 7]
    logging.info(id_list)

    with concurrent.futures.ThreadPoolExecutor(max_workers=2) as executor:
        # 提交任务给线程池
        tasks = []
        for sub_list in get_delete_ids(id_list):
            future = executor.submit(dsq.call, sub_list)
            tasks.append(future)

        # 获取已完成任务的结果
        for future in concurrent.futures.as_completed(tasks):
            result = future.result()
            logging.info(result)
    print("所有任务执行完毕")

# print(id_list_new)
# print(typejson.dumps([1, 2, 3]))
# print(str(sub_id_list))

# request = DeleteQueryRequest([1, 2, 3])
# request.shop_item_id_list = [1]
# print(request.shop_item_id_list)
# DeleteQueryRequest.print()
# DeleteQueryRequest.print_class_method()
