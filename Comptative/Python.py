from collections import deque
t = int(input())
for i in range(t):
	n = int(input())
	a = input()
	b = input()
	graph = [[] for j in range(25)]
	visited = [True for j in range(25)]
	look = False
	m = 0
	for j in range(n):
		if a[j] > b[j]:
			look = True
		if a[j] != b[j]:
			graph[ord(a[j])-ord('a')].append(ord(b[j])-ord('a'))
			graph[ord(b[j])-ord('a')].append(ord(a[j])-ord('a'))
			visited[ord(a[j])-ord('a')] = False
			visited[ord(b[j])-ord('a')] = False
	if look:
		print(-1)
		continue
	que = deque()
	count = 0
	for j in range(25):
		if not visited[j]:
			count += 1
			que.append(j)
			visited[j] = True
			while que:
				x = que.popleft()
				m += 1
				for k in graph[x]:
					if not visited[k]:
						que.append(k)
						visited[k] = True
	print(m-count)
