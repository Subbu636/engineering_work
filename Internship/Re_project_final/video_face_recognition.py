

# imports
import face_recognition as fr
import numpy as np
import os
import cv2
from gcloud import storage
import io
import sys

jsonPath = "/home/subbu/PRML/Internship/p1/gigx-intern-cd3dfc7a1eca.json"
bucketName = "gigx-files"

path = '/home/subbu/PRML/Internship/p3/'
input_photo = path+'my_photo.jpg'
testing_video = path+'my_video.webm'

arg_num = len(sys.argv)
if(arg_num == 3):
    input_photo = sys.argv[1]
    testing_video = sys.argv[2]
else:
    print('Give cmd line inputs')


# downloading files from cloud 
#     storage_client = storage.Client.from_service_account_json(jsonPath)
#     bucket = storage_client.get_bucket(bucketName)
#     blob = bucket.get_blob(input_photo)
#     blob.download_to_filename(input_photo)
#     blob2 = bucket.get_blob(testing_video)
#     blob2.download_to_filename(testing_video)
    # other wise just put file path

pic = fr.load_image_file(input_photo)
input_face_encoding = fr.face_encodings(pic)[0]

vid = cv2.VideoCapture(testing_video)
frms = vid.get(cv2.CAP_PROP_FRAME_COUNT)
#print(frms)
if(frms > 150):
    num = 100
else:
    num = frms - 60
    
frame_cnt = 0
while(frame_cnt < num + 50):
    ret,frame = vid.read() 
    if ret:
        name = 'frame' + str(frame_cnt) + '.jpg'
        if(frame_cnt >= num):
            cv2.imwrite(name, frame)
        frame_cnt += 1
    else: 
        break
#     print('created images '+str(frame_cnt))

count = num
match = 0
while(count < num + 50):
    pic = fr.load_image_file('frame' + str(count) + '.jpg')
    try:
        encode = fr.face_encodings(pic)[0]
    except:
        count += 1
        continue
    results = fr.compare_faces([input_face_encoding], encode)
    count += 1
    if(results[0]):
        match += 1

print(match)

# deleting local files

frame_cnt = 0
while(frame_cnt < num + 50):
    name = 'frame' + str(frame_cnt) + '.jpg'
    if(frame_cnt >= num):
        os.remove(name)
    frame_cnt += 1
#     print('deleted images '+str(frame_cnt))

#     os.remove(input_photo)
#     os.remove(testing_video)

if(match > 0):
    print('Matched')
else:
    print('Not Matched')