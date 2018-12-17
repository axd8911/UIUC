def inc0(q):
   return q + [1,0,0,0]

def inc1(q):
   return  q + [0,1,0,0]

def inc2(q):
   return  q + [0,0,1,0]

def inc3(q):
   return  q + [0,0,0,1]

q = [0,0,0,0]

inc0(q)

print(q)
