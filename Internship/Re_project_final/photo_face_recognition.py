
# imports
import face_recognition as fr
import numpy as np
import os
import cv2
from gcloud import storage
import io
import sys


def photo_face_recognition(input_photo,testing_pic):
    
    # downloading files from cloud
#     storage_client = storage.Client.from_service_account_json(jsonPath)
#     bucket = storage_client.get_bucket(bucketName)
#     blob = bucket.get_blob(input_photo)
#     blob.download_to_filename(input_photo)
#     blob2 = bucket.get_blob(testing_pic)
#     blob2.download_to_filename(testing_pic)
    
    picture = fr.load_image_file(input_photo)
    face_encoding = fr.face_encodings(picture)[0]


    uk_picture = fr.load_image_file(testing_pic)
    uk_encoding = fr.face_encodings(uk_picture)[0]


    results = fr.compare_faces([face_encoding], uk_encoding)
    
#     os.remove(input_photo)
#     os.remove(testing_pic)
    
    count = 0
    for output in results:
        if(output == True):
            count += 1
    if(count > 0):
        print('Matched')
    else:
        print('Not Matched')


arg_num = len(sys.argv)
if(arg_num == 3):
    input_photo = sys.argv[1]
    testing_pic = sys.argv[2]
else:
    print('Give cmd line inputs')


photo_face_recognition(input_photo,testing_pic)