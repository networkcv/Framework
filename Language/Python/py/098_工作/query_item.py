import requests

headers = {
    'Cookie': '<string>',
    'system-id': '<string>',
    'Accept': 'application/json',
}

params = {
    'idList': '1,2',
}

response = requests.get('http://localhost:8090//api/pilot/core/init/item/findByIds', params=params, headers=headers)
print(response.text)
# def call(param_str):
#     data['paramsStr'] = param_str
#     print(data)
#     response = requests.post('http://rock.paas.cai-inc.com/conf/dubboTest', cookies=cookies, headers=headers, data=data, verify=False)
#     print(response.text)