import pymongo
import sys, getopt
import time
from pymongo import MongoClient
from collections import deque

client = MongoClient()
db = client.fb_db
collection = db.network_dict

def createGraphFromDB():
	edgeList = collection.find()
	for edge in edgeList:
		
		print int(edge['u']),type(int(edge['u']))

if __name__ == '__main__':
	createGraphFromDB()