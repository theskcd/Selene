import pymongo
import sys, getopt
import time
import Queue
from pymongo import MongoClient
from collections import defaultdict

client = MongoClient()
db = client.fb_db
friendsCollection = db.network_dict
musicCollection = db.music_dict

def createNetworkGraphFromDB():
	edgeList = friendsCollection.find()
	networkG = defaultdict(list)
	for rowObject in edgeList:
		u = int(rowObject['u'])
		v = int(rowObject['v'])
		if v not in networkG[u]:
			networkG[u].append(v)
		if u not in networkG[v]:
			networkG[v].append(u)
	
	# print 'Printing Network Graph'
	# print len(networkG)
	# for u in networkG:
	# 	print u,
	# 	print ':',
	# 	for v in networkG[u]:
	# 		print v,
	# 	print '\n'

	return networkG


def createSongGraphFromDB():
	edgeList = musicCollection.find()
	musicG = defaultdict(list)
	for rowObject in edgeList:
		u = int(rowObject['userid'])
		song = str(rowObject['song'])
		musicG[u].append(song)
	return musicG

def neighboursTillDepthK(networkG, source, depth):
	# print 'Running BFS'
	Q = Queue.Queue()
	visited = {}
	level = {}
	neighbours = []
	Q.put(source)
	level[source] = 0
	while True:
		if Q.empty():
			break
		u = Q.get()
		if level[u] > 5:
			break
		visited[u] = True
		

		for v in networkG[u]:
			if v not in visited:
				Q.put(v)
				level[v] = level[u] + 1
				visited[v] = True
				neighbours.append((level[v],v))

	# print 'Printing neighbours of ' + str(source)
	# for lev, v in neighbours:
	# 	print str(lev) + ' ' + str(v)
	# print '\n'

	return neighbours

def populateMusicPreferences(clusterList, musicG):
	musicFrequencyDict = {}
	for level,user in clusterList:
		for song in musicG[user]:
			if(song not in musicFrequencyDict):
				musicFrequencyDict[song] = (5-level) + 1
			else:
				musicFrequencyDict[song] += (5-level) + 1
	return musicFrequencyDict


def getKMostPopularSongsinCluster(source, K):
	"""
		Returns a list of K most Popular songs in decreasing order of frequency in Cluster.
		If the total no. of songs in Cluster is < K, then it returns the list of all songs.
	"""

	networkG = createNetworkGraphFromDB()
	musicG = createSongGraphFromDB()
	neighboursTillDepthFive = neighboursTillDepthK(networkG,source,5)
	clusterList = neighboursTillDepthFive
	musicPreferences = populateMusicPreferences(clusterList,musicG)
	
	songs = []
	for song in musicPreferences.keys():
		songs.append((musicPreferences[song],song))
	songs.sort(reverse=True)

	# print 'Printing musicPreferences: '
	# for freq,song in songs:
	# 	print str(freq) + ' '  + str(song)

	kPopularSongs = []
	for i in range(min(k,len(songs))):
		kPopularSongs.append(songs[i][1])

	return kPopularSongs

def main(userId, k):
	#returns a list of K most popular songs in Cluster.
	return getKMostPopularSongsinCluster(userId, k)

if __name__ == '__main__':
	# UserId is an integer. K is the limit of songs needed from Cluster.
	userId = int(sys.argv[1])
	k = int(sys.argv[2])
	exit(main(userId, k))
