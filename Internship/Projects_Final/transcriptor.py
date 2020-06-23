# constant parameters (one time setup)

path_to_directory = "/home/subbu/PRML/Internship/stuff/"
path_to_bucket = "gs://gigx-files/"
path_to_json = "/home/subbu/PRML/Internship/p1/gigx-intern-cd3dfc7a1eca.json"
bucket_name = "gigx-files"

# imports

import io
import os
from google.cloud import speech_v1p1beta1
from google.cloud import speech_v1
from google.cloud import speech
from google.cloud.speech import enums
from google.cloud.speech import types
from gcloud import storage
from oauth2client.service_account import ServiceAccountCredentials
import audioread
import glob
import time
import sys

# Main Function

def process_audio(audio,config,client,lengthy):
    if(lengthy):
        operation = client.long_running_recognize(config, audio)
        response = operation.result()
    else:
        response = client.recognize(config, audio)
    return response

def extract_output(response,separation):
    tot = 0
    cnt = 0
    output = ""
    for result in response.results:
        tot = tot + result.alternatives[0].confidence
        cnt = cnt + 1
        alternative = result.alternatives[0]
        if(separation):
            output = output + (str(result.channel_tag)+" : "+alternative.transcript+"\n")
        else:
            output = output + (alternative.transcript+"\n")
    conf = int((tot/cnt)*100)
    return output,conf

def main_transcription_process(file_name_param,primary_language_code_param,separation_param):
    
    client = speech_v1.SpeechClient()
    
    audio = {"uri": path_to_bucket+file_name_param}
    
    storage_client = storage.Client.from_service_account_json(path_to_json)
    bucket = storage_client.get_bucket(bucket_name)
    blob = bucket.get_blob(file_name_param)
    blob.download_to_filename(path_to_directory+file_name_param)

    
    with audioread.audio_open(path_to_directory+file_name_param) as ft:
        print("audio info : ",end="")
        print(ft.channels, ft.samplerate, ft.duration)
        num_channels = ft.channels
        sample_bit_rate = ft.samplerate
        duration = ft.duration
        ft.close()
    
    if(duration > 58):
        lengthy = True
    else:
        lengthy = False
    
    configuration = {
        "sample_rate_hertz": sample_bit_rate,
        "audio_channel_count": num_channels,
        "enable_separate_recognition_per_channel": separation_param,
        "language_code": primary_language_code_param,
        "enable_automatic_punctuation": True,
        "use_enhanced": True,
    }
    
    response = process_audio(audio,configuration,client,lengthy)
    
    output,confidence = extract_output(response,separation_param)
        
    if(confidence < 70):
        output = "Bad audio data, cannot be transcripted"
    
    return output, confidence

def data_transcriptor(file_name_param,primary_language_code_param,separation_param):
    
    try:
        out_info = main_transcription_process(file_name_param,primary_language_code_param,separation_param)
    except:
        out_info = "Bad input, refer document for specifications", 0
    print(out_info)
    identity = file_name_param[:file_name_param.index('.')]
    file = open(path_to_directory+identity+'_transcript.txt',"w+") 
    file.write(out_info[0]+"\nConfidence: "+str(out_info[1]))
    file.close()
    
    storage_client = storage.Client.from_service_account_json(path_to_json)
    bucket = storage_client.get_bucket(bucket_name )
    blob = bucket.blob(identity+'_transcript.txt')
    blob.upload_from_filename(path_to_directory+identity+'_transcript.txt')
    
    os.remove(path_to_directory+identity+'_transcript.txt')
    os.remove(path_to_directory+file_name_param)

# trail run

file_name_param = "cv1.wav"

separation_param = False

primary_language_code_param = "en"

arg_num = len(sys.argv)
if(arg_num == 4):
	file_name_param = sys.argv[1]
	primary_language_code_param = sys.argv[2]
	if(sys.argv[3] == "1"):
		separation_param = True


data_transcriptor(file_name_param,primary_language_code_param,separation_param)