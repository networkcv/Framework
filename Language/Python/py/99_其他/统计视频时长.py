import os

from moviepy.editor import VideoFileClip

total_duration = 0


def travel_dir_get_viedo_count(cur_path):
    # 循环遍历文件夹下的所有文件
    for file_name in os.listdir(cur_path):
        sub_path = os.path.join(cur_path, file_name)
        # 检查是否为目录
        if os.path.isfile(sub_path):
            # 检查文件是否为视频文件
            if file_name.endswith(('.mp4', '.avi', '.mkv')):
                try:
                    # 使用 moviepy 打开视频文件并获取时长
                    video = VideoFileClip(sub_path)
                    duration = video.duration
                    global total_duration
                    total_duration = total_duration + duration
                except Exception as e:
                    print(f"Error processing {file_name}: {e}")
        else:
            travel_dir_get_viedo_count(sub_path)


path = "/Users/networkcavalry/Study/Python/极客-Python进阶训练营第02期/06- 第06周：Django Web开发入门"

travel_dir_get_viedo_count(path)
print(f"Total duration of all videos in the folder: {round(total_duration / 60 / 60, 2)} hour")
