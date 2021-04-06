#Do NOT Terminate path strings with a "/"

SUBMISSIONS=sub
INPUTS=testcases
OUTPUTS=Outputs
OURSRC=.
MARKS=marks
MARKSFILE=$MARKS/A3-Marks.txt
rm -f $MARKSFILE
echo "====================START==========================" >> $MARKSFILE
date >> $MARKSFILE

for FOLDER in $SUBMISSIONS/*
do
#! echo FOLDER NAME : "${FOLDER}"
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
  cp "$OURSRC/checkOutputs.out" .
# If build fails? then skip to next student
if [ $? -ne 0 ]
then
#echo $ROLLNO,BUILD FAILED!
    echo $ROLLNO,BUILD FAILED! >> $MARKS/A3Marks-BuildFailures.txt # write to a separate file    
cd ../.. # MUST
continue
fi

#date > $LOGFILE
# main.out is the name of the executable generated by the makefile
for i in {1..3} # Run all the test cases
do
rm -f studentOutput.txt
./main.out $INPUTS/testcase${i}.txt studentOutput.txt
./checkOutputs.out studentOutput.txt $OUTPUTS/output${i}.txt >> $LOGFILE
done

  # After all the test cases are run, compute the total score
SCORE=$(grep -ic Success $LOGFILE) #Counts the number of occurrences of "Success" in log
marks=$(echo "scale=2; $SCORE / 2.0" | bc -l)   #compute marks
echo "Marks: $marks / 13"

echo "ROLLNO: $ROLLNO, NO. OF TESTCASES PASSED: $SCORE, MARKS: $marks" >> $MARKSFILE # write to file

# IMPORTANT - Coming back to the directory that contains the Submissions folder
cd ../..
done
#date >> $MARKSFILE
# echo "====================DONE!==========================" >> $MARKSFILE
