import pandas
from superstream import Stream


def write_excel(dispose_entity, res_file_path, sheet_name='sheet1'):
    """
    写excel
    :param dispose_entity: [{"key":a,"value":b},..]
    :param res_file_path:
    :param sheet_name:
    :return:
    """
    dispose_entity_data = {
        '参数': Stream(dispose_entity).map(lambda res_map: res_map['key']).to_list(),
        '结果': Stream(dispose_entity).map(lambda res_map: res_map['value']).to_list()
    }

    global df, writer, worksheet
    df = pandas.DataFrame(dispose_entity_data)
    with pandas.ExcelWriter(res_file_path, engine='xlsxwriter') as writer:
        # 数据传给Excel的writer
        df.to_excel(writer, index=False, sheet_name=sheet_name)
        # 再从writer加载回该sheet
        worksheet = writer.sheets[sheet_name]
        # 指定列宽设置的范围
        worksheet.set_column("A:G", 40)
        # 设置缩放比例
        worksheet.set_zoom(zoom=105)
        # writer保存数据到本地
        # writer.save()
