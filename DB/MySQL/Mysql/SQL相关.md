## 关联查询

### inter join & left join & right join 区别？

表 **ta**  & 表 **tb**

| 表名 |                     ta                      |                             tb                              |
| :--: | :-----------------------------------------: | :---------------------------------------------------------: |
| 内容 | <img src="img/image-20200611160345549.png"> | ![image-20200611160357737](img/image-20200611160357737.png) |

**inter join**

**select * from ta inter join tb;   /  select * from ta,tb;**

![image-20200611161314380](img/image-20200611161314380.png)

**select * from tb inter join ta;	/	 select * from tb,ta;**

![image-20200611161333940](img/image-20200611161333940.png)

可以看到如果只是用 **inter join** 的话相当于做了两个表的笛卡尔积。

### SQL中过滤条件放在on和where中的区别？

**在 inner join 的情况下，结果是一样的。**

**在 left join / right join 情况下 ，on 的过滤只对右边表有效 ，where 则会对 join 之后的所有记录再进行过滤。**

join过程可以这样理解：首先两个表做一个笛卡尔积，on后面的条件是对这个笛卡尔积做一个过滤形成一张临时表，如果没有where就直接返回结果，如果有where就对上一步的临时表再进行过滤。下面看实验：

拿 left join 来说，由于left join 的特性，一定会将 left join 左边表的所有记录显示出来，而left join 右边表的记录可以通过  on 来过滤（左边表的记录使用on过滤没有效果）。

下面结合sql和执行结果看一下：

- 没有使用 on 对右边表进行过滤， 注意看sql中【】的内容。

```mysql
select u.name ,u.uid ,r.rtid ,r.sleep_avg_heartrate heartrate ,r.sleep_avg_breatherate breatherate,r.oxygen,r.szy,r.ssy
from  user u left join report r on 【u.uid =r.uid 】group by u.uid;
```

![](./img\1583202054(1).jpg)

- 使用 on 对右边表进行过滤，可以看到相对应的字段的值都变成了null。

```mysql
select u.name ,u.uid ,r.rtid ,r.sleep_avg_heartrate heartrate ,r.sleep_avg_breatherate breatherate,r.oxygen,r.szy,r.ssy
from  user u left join report r on 【u.uid =r.uid and r.uid!=4 】group by u.uid;
```

![](./\img\1583201828(1).jpg)

- 在使用 on 对右边表进行过滤的基础上，同时也对左边表进行过滤，发现并没有什么效果。

```sql
select u.name ,u.uid ,r.rtid ,r.sleep_avg_heartrate heartrate ,r.sleep_avg_breatherate breatherate,r.oxygen,r.szy,r.ssy
    from  user u left join report r on 【u.uid =r.uid and r.uid!=4 and u.uid!=4 】group by u.uid;
```

![](./\img\1583201828(1).jpg)

- 在前边使用left join 的基础上，再加上 where 进行过滤。

```sql
select u.name ,u.uid ,r.rtid ,r.sleep_avg_heartrate heartrate ,r.sleep_avg_breatherate breatherate,r.oxygen,r.szy,r.ssy
    from  user u left join report r on 【u.uid =r.uid  and r.uid!=4 where u.uid=4 】group by u.uid;
```

![](./\img\1583202935(1).jpg)



### Left join  多个表

在 SQL 中，可以通过 `LEFT JOIN` 来连接多个表。假设有三个表 A、B、C，它们之间的关系如下：

```
复制代码A (a_id, a_name)
B (b_id, b_name, a_id)
C (c_id, c_name, b_id)
```

其中 B 表和 C 表都有一个外键与 A 表关联，现在要查询 A 表中的所有记录以及与其关联的 B 表和 C 表的数据，可以使用如下语句：

```
sql复制代码SELECT A.*, B.*, C.*
FROM A
LEFT JOIN B ON A.a_id = B.a_id
LEFT JOIN C ON B.b_id = C.b_id;
```

这条 SQL 语句使用了两次 `LEFT JOIN`，首先将 A 表和 B 表连接起来，然后再将 B 表和 C 表连接起来。这样就可以得到 A 表、B 表和 C 表的所有记录，其中 A 表的记录会在 B 表和 C 表之前显示出来。

需要注意的是，如果在 `LEFT JOIN` 中涉及到了多个表，那么连接的顺序会影响查询结果。在上述例子中，先将 A 表和 B 表连接再将 B 表和 C 表连接会得到不同于先将 B 表和 C 表连接再将 A 表和 B 表连接的结果。因此，在编写含有多个表的 SQL 查询语句时，需要仔细考虑连接的顺序。

## 从主表复制数据到副表

```mysql
insert into 
stock_change_record_copy1(order_id,sale_item_sku_id,quantity,ext,gmt_create,gmt_modified)
select 
order_id,sale_item_sku_id,quantity,ext,gmt_create,gmt_modified 
from stock_change_record
```

