from moviepy.editor import VideoFileClip
import os

# 指定视频文件所在的文件夹路径
path="/Users/networkcavalry/Study/系分资料/视频/基础视频"

total_duration = 0

# 循环遍历文件夹下的所有文件
for file_name in os.listdir(path):
    file_path = os.path.join(path, file_name)

    # 检查文件是否为视频文件
    if file_name.endswith(('.mp4', '.avi', '.mkv')):
        try:
            # 使用 moviepy 打开视频文件并获取时长
            video = VideoFileClip(file_path)
            duration = video.duration
            total_duration += duration
        except Exception as e:
            print(f"Error processing {file_name}: {e}")

print(f"Total duration of all videos in the folder: {round(total_duration/60/60,0)} hour")
