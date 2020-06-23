
# imports
from gcloud import storage
import io
import os
import sys
import numpy as np
from sklearn import preprocessing
import python_speech_features as mfcc
import audioread
from scipy.io.wavfile import read
from sklearn.mixture import GaussianMixture as GMM # some texts use GMM
from sklearn import preprocessing # break the blackbox here
import glob

def audio_recognition(input_audio,test_directory):
    
    sr,audio = read(input_audio)
    
    feature = mfcc.mfcc(audio,sr, 0.025, 0.01,20,nfft = 1200, appendEnergy = True)
    feature = preprocessing.scale(feature)
    
    gmm = GMM(n_components = 16, max_iter = 200, covariance_type='diag',n_init = 3)
    gmm.fit(feature)
    
    name = ''
    score = -100000
    wav_files = glob.glob(test_directory+'/*')
    for one in wav_files:
        srx,audiox = read(one)
        featurex = mfcc.mfcc(audiox,srx, 0.025, 0.01,20,nfft = 1200, appendEnergy = True)
        featurex = preprocessing.scale(featurex)
        scorex = gmm.score(featurex)
        if(score < scorex):
            score = scorex
            name = one
    name = name[name.index('/')+1:name.index('.')]
    
    print(name)

input_audio = 'SampleData/Karan_17.wav'
test_directory = 'SampleData'

arg_num = len(sys.argv)
if(arg_num == 3):
    input_audio = sys.argv[1]
    test_directory = sys.argv[2]
else:
    print('Give cmd line inputs')

audio_recognition(input_audio,test_directory)