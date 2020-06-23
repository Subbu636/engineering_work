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

def video_content_analysis(file_name):
    video_client = videointelligence.VideoIntelligenceServiceClient()
    features = [videointelligence.enums.Feature.EXPLICIT_CONTENT_DETECTION]

    operation = video_client.annotate_video(path_to_bucket+file_name, features=features)
    print('\nProcessing video for explicit content annotations:')

    result = operation.result(timeout=90)
    print('\nFinished processing.')
    
    identity = file_name[:file_name.index('.')]
    file = open(identity+'_content_analysis.txt',"w+")
    
    for frame in result.annotation_results[0].explicit_annotation.frames:
        likelihood = enums.Likelihood(frame.pornography_likelihood)
        frame_time = frame.time_offset.seconds + frame.time_offset.nanos / 1e9
        print('Time: {}s'.format(frame_time))
        print('\tpornography: {}'.format(likelihood.name))
        file.write('Time: {}s'.format(frame_time)+'\n')
        file.write('\tpornography: {}'.format(likelihood.name) + '\n')
        
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

video_content_analysis(file_name)