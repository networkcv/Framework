import os


def travel_dir_get_viedo_count(cur_path):
    count = 0
    # 循环遍历文件夹下的所有文件
    for file_name in os.listdir(cur_path):
        sub_path = os.path.join(cur_path, file_name)
        # 检查是否为目录
        if os.path.isfile(sub_path):
            # 检查文件是否为markdown文件
            if file_name.endswith('.md'):
                with open(sub_path, 'r') as file:
                    lines = file.readlines()
                    new_line = f'title: "{file_name[:file_name.index('.')]}"\n'
                    for i, line in enumerate(lines):
                        if line.__contains__("title"):
                            lines[i] = new_line
                            with open(sub_path, 'w') as file:
                                file.writelines(lines)
                                count += 1
                            break
        else:
            print("ERROR")
    print(count)


travel_dir_get_viedo_count("/Users/networkcavalry/blogs/source/_posts/Java并发编程")
