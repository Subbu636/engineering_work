
#Do NOT Terminate path strings with a "/"

SUBMISSIONS=$(pwd)/Submissions
INPUTS=$(pwd)/Testcases
OUTPUTS=$(pwd)/Outputs
OURSRC=$(pwd)/src
MARKS=$(pwd)/Marks

MARKSFILE=$MARKS/A4-Marks.txt
rm -f $MARKSFILE
echo "====================START==========================" >> $MARKSFILE
date >> $MARKSFILE

for FOLDER in $SUBMISSIONS/*
do

cd "${FOLDER}"

ROLLNO=$(ls *.cu | tail -1 | cut -d'.' -f1)
LOGFILE=${ROLLNO}.log   #Log file for each student
rm -f $LOGFILE #Removing the log file if already exists
# check for single source file. If not halt script!
if [ $(ls | wc -l) -ne 1 ]
then
echo "May be cleanup files! and run evaluate.sh"   #remove files other than ROLL_NO.cu in A3/sub/ROLL_NO folder 
break
fi

# Rename student's .cu file to kernels.cu
cp ${ROLLNO}.cu main.cu
# Copy our src files to student's folder and build
cp "$OURSRC/Makefile" .
make #&> /dev/null  #creates main.out  
# checkOutputs.out is generated by compiling checkOutputs.cu in src folder
cp "$OURSRC/checkOutputs.out" .
chmod 755 checkOutputs.out

#date > $LOGFILE
#main.out is the name of the executable generated by the makefile
for i in {1..4} # Run all the test cases
do
rm -f output.txt
./main.out $INPUTS/tc${i}.txt output.txt
./checkOutputs.out output.txt $OUTPUTS/output${i}.txt >> $LOGFILE
done

  # After all the test cases are run, compute the total score
SCORE=$(grep -ic Success $LOGFILE) #Counts the number of occurrences of "Success" in log
echo "No. of testcases passed: $SCORE"
echo "ROLLNO: $ROLLNO, NO. OF TESTCASES PASSED: $SCORE" >> $MARKSFILE # write to file

# IMPORTANT - Coming back to the directory that contains the Submissions folder
cd ../..
done
#date >> $MARKSFILE
echo "====================DONE!==========================" >> $MARKSFILE