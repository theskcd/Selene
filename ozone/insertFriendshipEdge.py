import pymongo
import sys, getopt
import time
from pymongo import MongoClient
from collections import deque

client = MongoClient()
db = client.fb_db
friendsCollection = db.network_dict

def insertEdgeIntoDb(edge):
	row = { "u": edge[0],
			"v": edge[1]
		  }
	edgeId = friendsCollection.insert_one(row)


if __name__ == '__main__':
	edge = (sys.argv[1],sys.argv[2])
	insertEdgeIntoDb(edge)