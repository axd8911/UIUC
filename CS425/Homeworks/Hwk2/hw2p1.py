def h1(x):
	return (x+1) % 32

def h2(x):
	return (2*x+x) % 32

def h3(x):
	return (3*x+x**2) % 32

def h4(x):
	return (4*x+x**3) % 32

l = [0]*32

c = 2017
h = 4
noFP = True

while(noFP):
	c += 1
	h = 4
	if (l[h1(c)] == 0):
		l[h1(c)] = 1
		h -= 1
	if (l[h2(c)] == 0):
		l[h2(c)] = 1
		h -= 1
	if (l[h3(c)] == 0):
		l[h3(c)] = 1
		h -= 1
	if (l[h4(c)] == 0):
		l[h4(c)] = 1
		h -= 1
	print(c)
	print(l)
	if (h == 4):
		noFP = False
		print(c)
