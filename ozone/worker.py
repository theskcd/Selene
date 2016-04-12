import pymongo
import sys, getopt
import time
from pymongo import MongoClient
from collections import deque

client = MongoClient('localhost',27017)
G = client['fb_db'].network_dict

def 


if __name__ == '__main__':
	edge = (sys.argv[1],sys.argv[2])
	print edge
	inserted_id(edge)