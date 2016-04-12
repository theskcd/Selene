import pymongo
import sys, getopt
import time
from pymongo import MongoClient
from collections import defaultdict

client = MongoClient()
db = client.fb_db
collection = db.network_dict
	
def createGraphFromDB():
	edgeList = collection.find()
	G = defaultdict(list)
	edges = []
	for rowObject in edgeList:
		u = int(rowObject['u'])
		v = int(rowObject['v'])
		G[u].append(v)
		G[v].append(u)

	# for u in G:
	# 	print u,
	# 	for v in G[u]:
	# 		print v,
	# 	print '\n'


def NeighboursTillDepthK(G,source,depth):
	Q = Queue()
	visited = {}
	level = {}
	neighbours = []
	Q.put(source)
	level[source] = 0
	while True:
		u = Q.get()
		if level[u] > 5:
			break
		visited[u] = True
		for v in G[u]:
			if v no in visited:
				Q.put(v)
				level[v] = level[u] + 1
				neighbours.append(v)

	return neighbours

if __name__ == '__main__':
	createGraphFromDB()