t1 = ('aa', 'bb', 'cc', 'bb')


# t1[0] = 'aaa'

t2 = ('aa', 'bb', ['cc', 'dd'])
# print(t2[2])
# print(t2[2][0])
t2[2][0] = 'TOM'
print(t2)

