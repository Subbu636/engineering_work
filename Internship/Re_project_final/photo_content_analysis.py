# imports

from google.cloud import vision
from google.cloud import videointelligence
from google.cloud.videointelligence import enums
from gcloud import storage
import os
import sys

# constant parameters

path_to_bucket = "gs://gigx-files/"
path_to_json = "/home/subbu/PRML/Internship/p1/gigx-intern-cd3dfc7a1eca.json"
bucket_name = "gigx-files"

def photo_content_analysis(file_name):
    
    client = vision.ImageAnnotatorClient()

    image = vision.types.Image()
    image.source.image_uri = path_to_bucket + file_name
    
    response = client.safe_search_detection(image=image)
    safe = response.safe_search_annotation
    
    likelihood_name = ('UNKNOWN', 'VERY_UNLIKELY', 'UNLIKELY', 'POSSIBLE',
                   'LIKELY', 'VERY_LIKELY')
    identity = file_name[:file_name.index('.')]
    
    print('adult: {}'.format(likelihood_name[safe.adult]))
    print('medical: {}'.format(likelihood_name[safe.medical]))
    print('spoofed: {}'.format(likelihood_name[safe.spoof]))
    print('violence: {}'.format(likelihood_name[safe.violence]))
    print('racy: {}'.format(likelihood_name[safe.racy]))
    
    file = open(identity+'_content_analysis.txt',"w+")
    
    file.write('Content Analysis :\n')
    file.write('adult: {}'.format(likelihood_name[safe.adult])+'\n')
    file.write('medical: {}'.format(likelihood_name[safe.medical])+'\n')
    file.write('spoofed: {}'.format(likelihood_name[safe.spoof]))
    file.write('violence: {}'.format(likelihood_name[safe.violence])+'\n')
    file.write('racy: {}'.format(likelihood_name[safe.racy])+'\n')
    
    
    file.close()
    
    storage_client = storage.Client.from_service_account_json(path_to_json)
    bucket = storage_client.get_bucket(bucket_name )
    blob = bucket.blob(identity+'_content_analysis.txt')
    blob.upload_from_filename(identity+'_content_analysis.txt')
    
    os.remove(identity+'_content_analysis.txt')
    

arg_num = len(sys.argv)
if(arg_num == 2):
    file_name = sys.argv[1]
else:
    print('Give cmd line inputs')

photo_content_analysis(file_name)