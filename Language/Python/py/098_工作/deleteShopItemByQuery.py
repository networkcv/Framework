import datetime
import logging

import requests

# 配置日志记录器
logging.basicConfig(level=logging.DEBUG, filename="app.log", filemode="w",
                    format="%(asctime)s - %(levelname)s - %(message)s")

cookies = {
    '_zcy_log_client_uuid': 'b06801f0-329b-11ee-96f2-d5bbef343142',
    'token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTQ0ODIxODEsImlzcyI6ImxpdXdhbmdqaWUiLCJ1c2VyaWQiOiI2MDcyOTI0OTYiLCJ1bmlvbmlkIjoiWHdpaTdoOWlTQkJXaFlGTUhORmd4eWpnaUVpRSIsInVzZXJOYW1lIjoibGl1d2FuZ2ppZSIsIm5pY2tOYW1lIjoi5LmM5p-PIiwicmVhbE5hbWUiOiLliJjml7rmnbAiLCJtYWlsIjoibGl1d2FuZ2ppZUBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDE0MzMiLCJtb2JpbGUiOiIxNTIyOTI2NTM1MSIsImRlcGFydG1lbnQiOls4NzE5NzA2NTZdLCJkZXBhcnRtZW50cyI6WyLkvpvlupTpk74iXSwiQWxsUGF0aCI6WyLkvpvlupTpk74s5ZWG5a625Lia5YqhLOeglOWPkemDqCzmlL_ph4fkupHmnInpmZDlhazlj7giXSwiQWxsUGF0aElkcyI6W1s4NzE5NzA2NTYsNzIwOTIyMDQ5LDQyNjY0NjksMV1dLCJpc0xlYWRlciI6ZmFsc2UsInBvc2l0aW9uIjoiIiwiYXZhdGFyIjoiIn0.SPSK_hHJJYsyV3HnEc1NCbyjc3nBFj4Hwhn3EbODgoc',
    '__sso_token__': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTQ0ODIxODEsImlzcyI6ImxpdXdhbmdqaWUiLCJ1c2VyaWQiOiI2MDcyOTI0OTYiLCJ1bmlvbmlkIjoiWHdpaTdoOWlTQkJXaFlGTUhORmd4eWpnaUVpRSIsInVzZXJOYW1lIjoibGl1d2FuZ2ppZSIsIm5pY2tOYW1lIjoi5LmM5p-PIiwicmVhbE5hbWUiOiLliJjml7rmnbAiLCJtYWlsIjoibGl1d2FuZ2ppZUBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDE0MzMiLCJtb2JpbGUiOiIxNTIyOTI2NTM1MSIsImRlcGFydG1lbnQiOls4NzE5NzA2NTZdLCJkZXBhcnRtZW50cyI6WyLkvpvlupTpk74iXSwiQWxsUGF0aCI6WyLkvpvlupTpk74s5ZWG5a625Lia5YqhLOeglOWPkemDqCzmlL_ph4fkupHmnInpmZDlhazlj7giXSwiQWxsUGF0aElkcyI6W1s4NzE5NzA2NTYsNzIwOTIyMDQ5LDQyNjY0NjksMV1dLCJpc0xlYWRlciI6ZmFsc2UsInBvc2l0aW9uIjoiIiwiYXZhdGFyIjoiIn0.SPSK_hHJJYsyV3HnEc1NCbyjc3nBFj4Hwhn3EbODgoc',
    '__sso_user__': 'liuwangjie',
    '__sso_department__': '%E4%BE%9B%E5%BA%94%E9%93%BE',
    '__sso_realName__': '%E5%88%98%E6%97%BA%E6%9D%B0',
    '__sso_nickName__': '%E4%B9%8C%E6%9F%8F',
    'JSESSIONID': '942FE1C7E7413E5731A26E2A324E28B4',
}

headers = {
    'Accept': 'application/json',
    'Accept-Language': 'zh-CN,zh;q=0.9,zh-TW;q=0.8',
    'Connection': 'keep-alive',
    'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
    # 'Cookie': '_zcy_log_client_uuid=b06801f0-329b-11ee-96f2-d5bbef343142; token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTQ0ODIxODEsImlzcyI6ImxpdXdhbmdqaWUiLCJ1c2VyaWQiOiI2MDcyOTI0OTYiLCJ1bmlvbmlkIjoiWHdpaTdoOWlTQkJXaFlGTUhORmd4eWpnaUVpRSIsInVzZXJOYW1lIjoibGl1d2FuZ2ppZSIsIm5pY2tOYW1lIjoi5LmM5p-PIiwicmVhbE5hbWUiOiLliJjml7rmnbAiLCJtYWlsIjoibGl1d2FuZ2ppZUBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDE0MzMiLCJtb2JpbGUiOiIxNTIyOTI2NTM1MSIsImRlcGFydG1lbnQiOls4NzE5NzA2NTZdLCJkZXBhcnRtZW50cyI6WyLkvpvlupTpk74iXSwiQWxsUGF0aCI6WyLkvpvlupTpk74s5ZWG5a625Lia5YqhLOeglOWPkemDqCzmlL_ph4fkupHmnInpmZDlhazlj7giXSwiQWxsUGF0aElkcyI6W1s4NzE5NzA2NTYsNzIwOTIyMDQ5LDQyNjY0NjksMV1dLCJpc0xlYWRlciI6ZmFsc2UsInBvc2l0aW9uIjoiIiwiYXZhdGFyIjoiIn0.SPSK_hHJJYsyV3HnEc1NCbyjc3nBFj4Hwhn3EbODgoc; __sso_token__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTQ0ODIxODEsImlzcyI6ImxpdXdhbmdqaWUiLCJ1c2VyaWQiOiI2MDcyOTI0OTYiLCJ1bmlvbmlkIjoiWHdpaTdoOWlTQkJXaFlGTUhORmd4eWpnaUVpRSIsInVzZXJOYW1lIjoibGl1d2FuZ2ppZSIsIm5pY2tOYW1lIjoi5LmM5p-PIiwicmVhbE5hbWUiOiLliJjml7rmnbAiLCJtYWlsIjoibGl1d2FuZ2ppZUBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDE0MzMiLCJtb2JpbGUiOiIxNTIyOTI2NTM1MSIsImRlcGFydG1lbnQiOls4NzE5NzA2NTZdLCJkZXBhcnRtZW50cyI6WyLkvpvlupTpk74iXSwiQWxsUGF0aCI6WyLkvpvlupTpk74s5ZWG5a625Lia5YqhLOeglOWPkemDqCzmlL_ph4fkupHmnInpmZDlhazlj7giXSwiQWxsUGF0aElkcyI6W1s4NzE5NzA2NTYsNzIwOTIyMDQ5LDQyNjY0NjksMV1dLCJpc0xlYWRlciI6ZmFsc2UsInBvc2l0aW9uIjoiIiwiYXZhdGFyIjoiIn0.SPSK_hHJJYsyV3HnEc1NCbyjc3nBFj4Hwhn3EbODgoc; __sso_user__=liuwangjie; __sso_department__=%E4%BE%9B%E5%BA%94%E9%93%BE; __sso_realName__=%E5%88%98%E6%97%BA%E6%9D%B0; __sso_nickName__=%E4%B9%8C%E6%9F%8F; JSESSIONID=942FE1C7E7413E5731A26E2A324E28B4',
    'Origin': 'http://rock.paas.cai-inc.com',
    'Referer': 'http://rock.paas.cai-inc.com/',
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36',
    'X-Token': '235330dc-6d87-4649-90bc-be804b326d5d',
    'alias': '%E4%B9%8C%E6%9F%8F',
    'department': '%E4%BE%9B%E5%BA%94%E9%93%BE',
    'envName': 'production',
}

data = {
    'env': 'test',
    'interf': 'com.gov.zcy.operate.pilot.core.client.shopitem.api.SCMShopItemWriteService',
    'version': '1.0.0',
    'group': 'default',
    'node': '',
    'paramsStr': '[{"shopItemIdList":[1,2]}]',
    'dubboTag': '乌柏',
    'method': 'deleteShopItemByQuery(com.gov.zcy.operate.pilot.core.client.shopitem.query.SCMShopItemQuery)',
    'attachment': '',
}


def call(param_str):
    cur = datetime.datetime.now()
    data['paramsStr'] = param_str
    logging.info(f'请求参数：{data}')
    response = requests.post('http://rock.paas.cai-inc.com/conf/dubboTest', cookies=cookies, headers=headers, data=data, verify=False)
    logging.info(f'调用结果：{response.text}')
    return response.text


class DeleteQueryRequest:

    @classmethod
    def print_class_method(cls):
        print(cls)

    @staticmethod
    def print():
        print(__name__)

    def __init__(self, id_list):
        self._shop_item_id_list = id_list

    @property
    def shop_item_id_list(self):
        return self._shop_item_id_list

    @shop_item_id_list.setter
    def shop_item_id_list(self, value):
        self._shop_item_id_list = value
