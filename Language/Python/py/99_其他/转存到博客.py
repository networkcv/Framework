import os
import re

note_source_path = "/Users/networkcavalry/Documents/GitHub/Framework/Design/DDD"


def dispose_md_to_blog(source_path):
    target_path = os.path.join("/Users/networkcavalry/Blog/source/_posts", os.path.basename(source_path))
    # 循环遍历文件夹下的所有文件
    un_order_list = os.listdir(source_path)
    un_order_list.sort()
    for index, file_name in enumerate(un_order_list):
        source_file_abs_path = os.path.join(source_path, file_name)
        # 检查是否为目录
        if os.path.isfile(source_file_abs_path):
            # 检查文件是否为markdown文件
            if file_name.endswith('.md'):
                with open(source_file_abs_path, 'r') as file:
                    # 读取文档内容
                    doc = file.readlines()
                    # 设置文件头
                    index += 1
                    file_name_without_suffix = file_name[:file_name.index('.')]
                    doc_head = ['---\n',
                                f'title: "{file_name_without_suffix}"\n',
                                f'date: 2019-08-{index}\n',
                                'category:\n',
                                '  - 计算机组成原理\n',
                                'tags:\n',
                                '  - 计算机组成原理\n',
                                '---\n',
                                '\n', '\n''\n', '\n']
                    doc_head.extend(doc)
                    new_doc = doc_head
                    # 修改文档中的图片路径 和 处理缩放图片格式
                    source_images_relative_path_list = []
                    for i, doc_line in enumerate(new_doc):
                        if doc_line.__contains__("<img src="):
                            match = re.search(r'src="([^"]+)"', doc_line)
                            if match:
                                source_iamge_relative_path = match.group(1)
                                source_images_relative_path_list.append(source_iamge_relative_path)
                                new_iamge_relative_path = source_iamge_relative_path.replace('img/', '')
                                new_image_relative_file_path = file_name_without_suffix + "/" + os.path.basename(new_iamge_relative_path)
                                new_doc[i] = f'![]({new_image_relative_file_path})'
                            continue
                        if doc_line.__contains__("(img/"):
                            match = re.search(r'!\[.*]\(([^)]+)', doc_line)
                            if match:
                                source_iamge_relative_path = match.group(1)
                                source_images_relative_path_list.append(source_iamge_relative_path)
                                new_iamge_relative_path = source_iamge_relative_path.replace('img/', '')
                                new_image_relative_file_path = file_name_without_suffix + "/" + os.path.basename(new_iamge_relative_path)
                                new_doc[i] = f'![]({new_image_relative_file_path})'
                            continue
                    # 拷贝图片到目标目标
                    for source_images_relative_path in source_images_relative_path_list:
                        with open(source_path + "/" + source_images_relative_path, "br") as file_read:
                            image = file_read.read()
                        iamge_file_name = os.path.basename(source_images_relative_path)
                        new_image_file_abs_path = target_path + "/" + file_name_without_suffix + "/" + iamge_file_name
                        folder_path = os.path.dirname(new_image_file_abs_path)
                        # 如果文件夹不存在，创建文件夹
                        if not os.path.exists(folder_path):
                            os.makedirs(folder_path)
                        with open(new_image_file_abs_path, 'bw') as file_write:
                            file_write.write(image)

                    # 写到目标目录
                    target_file_abs_path = target_path + "/" + file_name
                    with open(target_file_abs_path, 'w') as file_write:
                        file_write.writelines(new_doc)


dispose_md_to_blog(note_source_path)
