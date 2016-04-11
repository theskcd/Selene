import pymongo
import sys, getopt
import time
from pymongo import MongoClient
from collections import deque

client = MongoClient()
db = client.music_db
collection = db.music_dict

def insertEdgeIntoDb(edge):
	row = { "userid": edge[0],
			"song": edge[1]
		  }
	edgeId = collection.insert_one(row)


if __name__ == '__main__':
	edge = (sys.argv[1],sys.argv[2])
	insertEdgeIntoDb(edge)