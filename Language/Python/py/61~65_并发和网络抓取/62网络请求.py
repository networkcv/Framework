import requests

cookies = {
    '_zcy_log_client_uuid': 'b06801f0-329b-11ee-96f2-d5bbef343142',
    'token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTQ0Mjc0NTEsImlzcyI6ImxpdXdhbmdqaWUiLCJ1c2VyaWQiOiI2MDcyOTI0OTYiLCJ1bmlvbmlkIjoiWHdpaTdoOWlTQkJXaFlGTUhORmd4eWpnaUVpRSIsInVzZXJOYW1lIjoibGl1d2FuZ2ppZSIsIm5pY2tOYW1lIjoi5LmM5p-PIiwicmVhbE5hbWUiOiLliJjml7rmnbAiLCJtYWlsIjoibGl1d2FuZ2ppZUBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDE0MzMiLCJtb2JpbGUiOiIxNTIyOTI2NTM1MSIsImRlcGFydG1lbnQiOls4NzE5NzA2NTZdLCJkZXBhcnRtZW50cyI6WyLkvpvlupTpk74iXSwiQWxsUGF0aCI6WyLkvpvlupTpk74s5ZWG5a625Lia5YqhLOeglOWPkemDqCzmlL_ph4fkupHmnInpmZDlhazlj7giXSwiQWxsUGF0aElkcyI6W1s4NzE5NzA2NTYsNzIwOTIyMDQ5LDQyNjY0NjksMV1dLCJpc0xlYWRlciI6ZmFsc2UsInBvc2l0aW9uIjoiIiwiYXZhdGFyIjoiIn0.tOlbUR83TReVcU1g-qY15Vaarn3rTuFj3sp344VNzjc',
    '__sso_token__': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTQ0Mjc0NTEsImlzcyI6ImxpdXdhbmdqaWUiLCJ1c2VyaWQiOiI2MDcyOTI0OTYiLCJ1bmlvbmlkIjoiWHdpaTdoOWlTQkJXaFlGTUhORmd4eWpnaUVpRSIsInVzZXJOYW1lIjoibGl1d2FuZ2ppZSIsIm5pY2tOYW1lIjoi5LmM5p-PIiwicmVhbE5hbWUiOiLliJjml7rmnbAiLCJtYWlsIjoibGl1d2FuZ2ppZUBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDE0MzMiLCJtb2JpbGUiOiIxNTIyOTI2NTM1MSIsImRlcGFydG1lbnQiOls4NzE5NzA2NTZdLCJkZXBhcnRtZW50cyI6WyLkvpvlupTpk74iXSwiQWxsUGF0aCI6WyLkvpvlupTpk74s5ZWG5a625Lia5YqhLOeglOWPkemDqCzmlL_ph4fkupHmnInpmZDlhazlj7giXSwiQWxsUGF0aElkcyI6W1s4NzE5NzA2NTYsNzIwOTIyMDQ5LDQyNjY0NjksMV1dLCJpc0xlYWRlciI6ZmFsc2UsInBvc2l0aW9uIjoiIiwiYXZhdGFyIjoiIn0.tOlbUR83TReVcU1g-qY15Vaarn3rTuFj3sp344VNzjc',
    '__sso_user__': 'liuwangjie',
    '__sso_department__': '%E4%BE%9B%E5%BA%94%E9%93%BE',
    '__sso_realName__': '%E5%88%98%E6%97%BA%E6%9D%B0',
    '__sso_nickName__': '%E4%B9%8C%E6%9F%8F',
    'redirect': '',
    'JSESSIONID': '6174E838B5CF82E10644DA58DDE7B155',
}

headers = {
    'Accept': 'application/json',
    'Accept-Language': 'zh-CN,zh;q=0.9,zh-TW;q=0.8',
    'Connection': 'keep-alive',
    'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
    # 'Cookie': '_zcy_log_client_uuid=b06801f0-329b-11ee-96f2-d5bbef343142; token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTQ0Mjc0NTEsImlzcyI6ImxpdXdhbmdqaWUiLCJ1c2VyaWQiOiI2MDcyOTI0OTYiLCJ1bmlvbmlkIjoiWHdpaTdoOWlTQkJXaFlGTUhORmd4eWpnaUVpRSIsInVzZXJOYW1lIjoibGl1d2FuZ2ppZSIsIm5pY2tOYW1lIjoi5LmM5p-PIiwicmVhbE5hbWUiOiLliJjml7rmnbAiLCJtYWlsIjoibGl1d2FuZ2ppZUBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDE0MzMiLCJtb2JpbGUiOiIxNTIyOTI2NTM1MSIsImRlcGFydG1lbnQiOls4NzE5NzA2NTZdLCJkZXBhcnRtZW50cyI6WyLkvpvlupTpk74iXSwiQWxsUGF0aCI6WyLkvpvlupTpk74s5ZWG5a625Lia5YqhLOeglOWPkemDqCzmlL_ph4fkupHmnInpmZDlhazlj7giXSwiQWxsUGF0aElkcyI6W1s4NzE5NzA2NTYsNzIwOTIyMDQ5LDQyNjY0NjksMV1dLCJpc0xlYWRlciI6ZmFsc2UsInBvc2l0aW9uIjoiIiwiYXZhdGFyIjoiIn0.tOlbUR83TReVcU1g-qY15Vaarn3rTuFj3sp344VNzjc; __sso_token__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTQ0Mjc0NTEsImlzcyI6ImxpdXdhbmdqaWUiLCJ1c2VyaWQiOiI2MDcyOTI0OTYiLCJ1bmlvbmlkIjoiWHdpaTdoOWlTQkJXaFlGTUhORmd4eWpnaUVpRSIsInVzZXJOYW1lIjoibGl1d2FuZ2ppZSIsIm5pY2tOYW1lIjoi5LmM5p-PIiwicmVhbE5hbWUiOiLliJjml7rmnbAiLCJtYWlsIjoibGl1d2FuZ2ppZUBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDE0MzMiLCJtb2JpbGUiOiIxNTIyOTI2NTM1MSIsImRlcGFydG1lbnQiOls4NzE5NzA2NTZdLCJkZXBhcnRtZW50cyI6WyLkvpvlupTpk74iXSwiQWxsUGF0aCI6WyLkvpvlupTpk74s5ZWG5a625Lia5YqhLOeglOWPkemDqCzmlL_ph4fkupHmnInpmZDlhazlj7giXSwiQWxsUGF0aElkcyI6W1s4NzE5NzA2NTYsNzIwOTIyMDQ5LDQyNjY0NjksMV1dLCJpc0xlYWRlciI6ZmFsc2UsInBvc2l0aW9uIjoiIiwiYXZhdGFyIjoiIn0.tOlbUR83TReVcU1g-qY15Vaarn3rTuFj3sp344VNzjc; __sso_user__=liuwangjie; __sso_department__=%E4%BE%9B%E5%BA%94%E9%93%BE; __sso_realName__=%E5%88%98%E6%97%BA%E6%9D%B0; __sso_nickName__=%E4%B9%8C%E6%9F%8F; redirect=; JSESSIONID=6174E838B5CF82E10644DA58DDE7B155',
    'Origin': 'http://rock.paas.cai-inc.com',
    'Referer': 'http://rock.paas.cai-inc.com/',
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36',
    'X-Token': '235330dc-6d87-4649-90bc-be804b326d5d',
    'alias': '%E4%B9%8C%E6%9F%8F',
    'department': '%E4%BE%9B%E5%BA%94%E9%93%BE',
    'envName': 'production',
}

data = {
    'env': 'production',
    'interf': 'com.gov.zcy.operate.pilot.core.client.isvitem.api.SCMIsvItemReadService',
    'version': '1.0.0',
    'group': 'default',
    'node': '',
    'paramsStr': '[1101200000202601]',
    'dubboTag': 'default',
    'method': 'queryIsvItemById(java.lang.Long)',
    'attachment': '',
}

# response = requests.post('http://rock.paas.cai-inc.com/conf/dubboTest', cookies=cookies, headers=headers, data=data,
#                          verify=False)
print(data.get('paramsStr'))
isvItemIdArr = [1101200000202601,
                1101200000202602,
                1101200000221301]
for isvItemId in isvItemIdArr:
    data['paramsStr'] = f'[{isvItemId}]'
    response = requests.post('http://rock.paas.cai-inc.com/conf/dubboTest', cookies=cookies, headers=headers, data=data,
                             verify=False)
    print(response.text)
    print()
