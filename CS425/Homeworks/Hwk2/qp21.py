m = 32

def h1(x):
	return ((x**2+x**3)*1) % m

def h2(x):
	return ((x**2+x**3)*2) % m

def h3(x):
	return ((x**2+x**3)*3) % m

l = [0]*m

c = 2017
h = 4
noFP = True
numbers = [2013,2010,2007,2004,3200,2001,1998]

for n in numbers:
	h = 3
	if (l[h1(n)] == 0):
		l[h1(n)] = 1
		h -= 1
	if (l[h2(n)] == 0):
		l[h2(n)] = 1
		h -= 1
	if (l[h3(n)] == 0):
		l[h3(n)] = 1
		h -= 1
	print(n)
        s = ""
        for i in l:
            s+= str(i)+ ","
        print(s)
	if (h == 4):
		noFP = False
		print(n)
