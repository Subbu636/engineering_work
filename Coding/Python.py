# import torch
# print(torch.cuda.is_available())




# t = int(input())
# for i in range(t):
# 	n,a,b,c = map(int,input().split())
# 	if n < a+b-c or (a-c == 0 and b-c == 0 and a != n) or a-c < 0 or b-c < 0:
# 		print('Case #'+str(i+1)+': '+'IMPOSSIBLE')
# 		continue
# 	arr = [-1]*n
# 	for j in range(a-c):
# 		arr[j] = n-a+c+j
# 	if a-c > 0:
# 		for j in range(a-c,n-b):
# 			arr[j] = 1
# 		for j in range(n-b,n-b+c):
# 			arr[j] = n
# 	if a-c == 0:
# 		for j in range(a-c,a):
# 			arr[j] = n
# 		for j in range(a,n-b+c):
# 			arr[j] = 1
# 	k = 1
# 	for j in range(n-b+c,n):
# 		arr[j] = n-k
# 		k+=1
# 	if min(arr) == -1:
# 		print('Case #'+str(i+1)+': '+'IMPOSSIBLE')
# 		continue
# 	print('Case #'+str(i+1)+': ',end='')
# 	for j in arr:
# 		print(j,end=' ')
# 	print()



# print('Case #'+str(i+1)+': '+str(maxi))

# def doit(i1):
# 	return (i1[0]+i1[1])

# t = int(input())
# for i in range(t):
# 	n = int(input())
# 	arr = []
# 	sig = 0
# 	for j in range(n):
# 		x,y = map(int,input().split())
# 		arr.append((x,y))
# 		sig += x
# 	tot = sig
# 	count = 0
# 	arr = sorted(arr,key = doit,reverse=True)
# 	# print(arr)
# 	for j in range(n):
# 		if arr[j][0] + arr[j][1] > sig:
# 			sig -= arr[j][0]
# 			count+=1
# 		else:
# 			break
# 	if sig == 0:
# 		ans = 0
# 		sig = tot
# 		count = 0
# 		while(count < n):
# 			xi = 0
# 			maxi = 0
# 			for j in range(n):
# 				if arr[j][0] + arr[j][1] > sig:
# 					maxi = max(maxi,xi)
# 					xi = 0
# 				else:
# 					xi += arr[j][0]
# 			maxi = max(maxi,xi)
# 			ans = max(maxi+sig,ans)
# 			count+=1
# 			sig -= arr[0][0]
# 			arr[0] = (0,0)
# 		print('Case #'+str(i+1)+': '+str(0)+' '+str(ans))
# 	else:
# 		print('Case #'+str(i+1)+': '+str(count)+' INDEFINITELY')



# Enter your code here. Read input from STDIN. Print output to STDOUT
import heapq
def func(i):
    return i[0]

n = int(input())
arr = []
for i in range(n):
    arr.append(list(map(int,input().split())))
arr = sorted(arr,key = func)
max_heap = [-1*arr[0][1]]
start = arr[0][0]
count = 0
for i in range(1,n):
    if arr[i][0] <= -1*max_heap[0]:
        heapq.heappush(max_heap,-1*arr[i][1])
    else:
        count += ((-1*max_heap[0])-start)
        start = arr[i][0]
        max_heap = [-1*arr[i][1]]
count += ((-1*max_heap[0])-start)
print(count)
        
        
        
        
        