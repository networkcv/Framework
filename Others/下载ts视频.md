### 记 一次帮朋友下载视频的经历

​	事情的经过是这样的，晚间有个朋友让我帮她下载两个公众号文章里的视频，我想这so easy，通过Chrome打开该文章，通过安装的浏览器插件（猫抓）很快搞定的第一个视频。

![](.\img\1581603417(1).jpg)



打开第二个视频后，我发现事情变得有趣起来了。

![](.\img\1581604651(1).jpg)



视频还没加载完，就抓到481个资源，每个资源都是几百kb大小的短视频，一想到下载完还需再想法办把这几百个切片视频整合成一个视频就头大，所以想看看还有没有其他法子，尝试了之前成功过的其他几个浏览器，这次都不好使。。。

​		没办法，先全部下载下来再说。



![](.\img\1581604801(1).jpg)

所有的ts( Transport Stream)格式的文件加起来一共744个，还有一个是m3u8格式的文件，这个文件就像一个播放列表，应该是记录每个文件的播放顺序。

![](.\img\1581688129(1).jpg)

**合并文件**

现在的任务是如何把这些分割的视频文件合并成一整个视频文件。

优先想到Windows的批处理命令，

```cmd
copy  /b  *.ts  new_fileName.ts 
```

这个命令好像可以，但前提是这些文件的名称必须要按照播放的先后顺序来命名，不然合并出来的视频前后内容不连贯，现在需要解决的是如何批量修改文件名称，

![](.\img\1581689047(1).jpg)

可以看出这些文件的命名都是有规律的，第二个下划线后边的数字应该就是对应的播放顺序，打开文件验证无误后，考虑如何批量修改文件名，最后打算自己写Java代码通过IO流的方式来修改，代码如下：

```java
public static void rename(){
        String path = "E://a";
        File file = new File(path);
        File[] files = file.listFiles();
        for (File f : files) {
            String old_name = f.getName();
            String[] s = old_name.split("_");
            f.renameTo(new File(path + "/" + s[2]));
        }
    }
```



执行完毕后，名称已经被修改过来了。

![](.\img\1581689311(1).jpg)

终于要搞定了，执行copy 命令。

结果我还是高兴的太早了，视频时长正确，但是内容还是存在不连续的问题，画面播放一会突然就跳到另外一段内容了。

想看下问题在哪，遂复制了20个视频，又执行了一遍，过程和结果如下图：

![](.\img\1581688673(1).jpg)

看到右边的的复制顺序就明白了，copy命令应该是将文件名当作字符串了，没有当作数字，因此会先合并0开头的文件，再合并1开头的文件，依次类推，既然找到问题所在，那只要在执行copy命令时带上相关参数就可以了。

![](.\img\1581688489(1).jpg)

查看之后发现并没有指定相关参数。。。这个方法以失败告终。

也懒得去网上搜软件了，于是打算继续写Java代码来搞。本质上就是读取每个文件的字节然后写在一个文件中。

```java
   public static void merge() throws IOException {
        String path = "E://a";
        File file = new File(path);
        File[] files = file.listFiles();
        FileOutputStream fileOutputStream = new FileOutputStream("E://b/a.mp4");
        int len;
        byte[] bytes = new byte[4096];
        int index = files.length - 1;
        for (int i = 0; i <= index; i++) {
            File tmpFile = new File("E://a/" + i + ".ts");
            if (tmpFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(tmpFile);
                while (true) {
                    len = fileInputStream.read(bytes);
                    if (len == -1) break;
                    fileOutputStream.write(bytes, 0, len);
                }
            }
        }
        fileOutputStream.close();
        fileOutputStream.close();
    }
```



执行代码后，没有报错，查看合成后的文件，没问题，终于搞定了！

虽然花了2个小时，但是心里还是挺有满足感的。

之前以为学习编程就是为了工作，是一份职业，现在可能又有了新的看法，也是还是一种生活方式吧。hhh