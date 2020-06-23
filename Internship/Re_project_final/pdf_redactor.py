# imports

import google.cloud.dlp
import sys
import os
from gcloud import storage
from fpdf import FPDF
from PIL import Image
from pdf2image import convert_from_path
import glob

# constant parameters

projectName = "gigx-intern"
jsonPath = "/home/subbu/PRML/Internship/p1/gigx-intern-cd3dfc7a1eca.json"
bucketName = "gigx-files"
inDir = "/home/subbu/PRML/Internship/stuff/"
outDir = "/home/subbu/PRML/Internship/stuff/"

   
def pdf_redactor(fileName): # only pdf files

    # downloadfile from cloud
    storage_client = storage.Client.from_service_account_json(jsonPath)
    bucket = storage_client.get_bucket(bucketName)
    blob = bucket.get_blob(fileName)
    blob.download_to_filename(inDir+fileName)

    # image conversion
    pages = convert_from_path(inDir+fileName, dpi=400)
    pg_count = 0
    for page in pages:
        page.save(inDir+'imj'+str(pg_count)+'.jpg', 'JPEG')
        pg_count += 1

    # setup
    dlp = google.cloud.dlp_v2.DlpServiceClient()

    parent = dlp.project_path(projectName)

    custom_regexes = ["[0-9]{10}" # 10 digit number

                     ] # add regular expressions here

    regexes = [
            {
                "info_type": {"name": "myRegex{}".format(i)},
                "regex": {"pattern": custom_regex},
            }
            for i, custom_regex in enumerate(custom_regexes)
        ]
    custom_info_types = regexes

    info_types = [{"name": "EMAIL_ADDRESS"}] # email

    inspect_config = {
            "info_types": info_types,
            "custom_info_types": custom_info_types,
        }

    image_redaction_configs = []

    for info_type in info_types:
        image_redaction_configs.append({"info_type": info_type})
    image_redaction_configs.append({"info_type": {"name":"myRegex0"}}) # add corresponding terms


    # processing and output
    for j in range(pg_count):
        with open(inDir+'imj'+str(j)+'.jpg', mode="rb") as f:
            byte_item = {"type": 1, "data": f.read()}

        response = dlp.redact_image(
            parent,
            inspect_config=inspect_config,
            image_redaction_configs=image_redaction_configs,
            byte_item=byte_item,
        )
        with open(outDir+'imj_modf'+str(j)+'.jpg', mode="wb") as f:
            f.write(response.redacted_image)

    #converting back to pdf and upload to bucket
    cover = Image.open(outDir+'imj_modf'+str(0)+'.jpg')
    width, height = cover.size
    pdf = FPDF(unit = "pt", format = [width, height])

    for image in [outDir+'imj_modf'+str(j)+'.jpg' for j in range(pg_count)]:
        pdf.add_page()
        pdf.image(image,0,0)
    pdf.output(outDir+'modf_'+fileName, "F")

    blob2 = bucket.blob('modfied_'+fileName)
    blob2.upload_from_filename(outDir+'modf_'+fileName)

    #delete files in local directories
    files1 = glob.glob(inDir+'*')
    for f in files1:
        os.remove(f)
    files2 = glob.glob(outDir+'*')
    for f in files2:
        os.remove(f)


file_name = 'Test_Resume.pdf'

arg_num = len(sys.argv)
if(arg_num == 2):
	file_name = sys.argv[1]


pdf_redactor(file_name)