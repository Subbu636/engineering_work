
Coordination Mechanisms
____________________________________________________________________________
	GitHub URL	: https://github.com/techhue/FlipkartJEDI
	Questions 	: 
	Discussions	: https://tinyurl.com/ydylpn64

	Must Share Your PRIMARY GMAIL ID
	Documents Will Be Shared ONLY ON your PRIMARY GMAIL ID

Assignments
____________________________________________________________________________
DAY 1: 
	Reading and Practice
	Linux Pocket Guide Till Page 63

DAY 2: 
	Reading and Practice
	Linux Pocket Guide Till Page 131

DAY 3: 
	Reading and Practice Git Commands
		Git Study Material
	
	Practical Assignments
	____________________________________________________________________________
	Simulate Branching WorkFlow As A Project Workflow
		Master, Dev and Testing Branch

	Simulate Branching Workflow As A Developer
		Dev Branch Is Developer Branch
		Implement Feature with Various Ideas and Approaches
			Use Branching To Maintain Your Ideas Implementation

DAY 4: 
	ASSIGNMENT 1
	____________________________________________
	Write Sum Function In C Language

	// Which Will Return Valid SUM For ANY X, Y
	// Otherwise Print Can't Calculate SUM

	int sum(int x, int y) {


	}

	ASSIGNMENT 2
	____________________________________________
	Write Sum Function In C Language

	// Which Will Return Valid SUM For ANY X, Y
	// Otherwise RETURN ERROR

	int sum(int x, int y) {

	}

	PROVE YOUR CODE IS RIGHT OR WRONG
		REASON WITH DEFINITION
			DATA TYPE

		
++++++++++++++++++++++++++++++++++++++++++++++++++++++
--> RAISE HAND IF DONE READING AND PRACTICE ASSIGNMENT 
++++++++++++++++++++++++++++++++++++++++++++++++++++++


Hardware/Software Requirements
____________________________________________________________________________
	Computer/Laptop with 8GB+ RAM and Minimum Core i5 Processor
	Operating System: Ubuntu 20.04, 64 Bit [ Desktop Edition ]	
	SDK: Oracle Java Development Kit(JDK) 9+
	Editors: Sublime Text, Visual Code
		Design For Programmers
		Light Weight
		Cross Platform [ Linux, MacOSX and Windows ]

	Install on Virtualisation Environment e.g. Oracle VirtualBox

Setup Environment
____________________________________________________________________________
	1. Virtual Environment Easy Maintain.
	2. Must Have Same Environment: Ubuntu 20.04, 64 Bit [ Desktop Edition ].
	3. Unix/Linux Part of Agenda
	4. Design Perceptive 

Set of Nodes Childern of intelligene
____________________________________________________________________________
	Applications			Huawei.Router.Details.txt	Public
	Calibre Library			Library				contact.txt
	CallDetails.txt			Movies				fibo.swift
	Desktop				Music				hello.swift
	Documents			NITCAAEmailID.txt		someData
	Downloads			Pictures			temp

	 /Users/intelligene

	Parent of intelligene is Users
	Parents of Users / [ Root Directory ]

	In Case Windows What is The Root Directory?
		 - C: Drive
		 - 
		 e.g. In Mac World
				Achieve	 | C:
				Aspire	 | D:
				Inspire  | E:
				MacOSX   | H:
				
				Parent of These One Is Volumes
				Volumes has parent /

	Design Questions
		Should you start your filesystem design with 
			DESIGN CHOICE: 
				One Node or Multiple Node
		Which Design Choice Is Better? and Why?

Training Pedagogy
____________________________________________________________________________
	Design Oriented


ANY QUESTION or DOUBT?
____________________________________________________________________________
	Feel Free To Ask....

	
____________________________________________________________________________
Unix OS 
	Ken Thomson and Denish Ritchie
	Assembly Language
	Created C Language, Inspired From Language B 


ls : List Immediate Child Nodes
	ls command with Options/Switches
		ls -l : Detailed Listing
		ls -a : Shows all files Including Hidden
	
	Order Doesn't Matters
		ls -l -a
		ls -a -l
		ls -al
		ls -la 

	Colored Output
		ls -lG

pwd : Present Working Directory

____________________________________________________________________________

In Unix Everything Is File 
	Files and File Type In Unix 
	
	Experiment Commands
		ls  -l

		drwx------@  4 intelligene  staff   170 Nov 27 15:36 Applications
			Directory File: Metadata About Other Files 

		-rw-r--r--   1 intelligene  staff   244 Jul  9  2019 contact.txt
			Regular File

		First Character is d or -

	Experiment Commands
		cd /dev
		ls -l

		crw-------  1 intelligene  staff       0,   0 Apr 28 08:54 console
			Character File: File Which is Read/Written Character By Character

		brw-r-----  1 root         operator    1,   0 Apr 28 08:43 disk0
			Block File: File Which is Read/Written Block By Block
			Block Size: 512 Bytes

		First Character is c or b

		File Size is 1024 Bytes -> 2 Block Sizes
			Block Size: 512 Bytes

	The file mode printed under the -l option consists of the entry type, owner permis-
     sions, and group permissions.  The entry type character describes the type of file,
     as follows:

           b     Block special file.
           c     Character special file.
           d     Directory.
           		mkdir FileName

           -     Regular file.
           		touch FileName

           l     Symbolic link.
           s     Socket link.
           p     FIFO.

____________________________________________________________________________
Discussion Questions
	What is A File?
____________________________________________________________________________


Path Concept 
____________________________________________________________________________
	Path: Branch In Tree Ending at Node
		
	Terminal Node: Documents
	Root Node: / (Root)

	Absolute Path: Path Starting From / Node
		/Users/intelligene/Documents

		cd /Users/intelligene/Documents

	Relative Path: Path Relative to Present Working Directory
		cd Documents

	Nodes Coming In Branch/Path
		/
		Users
		intelligene
		Documents

	Traversal: Bi Directional
		Child To Parent 
		cd .. : Move to Immediate Parent Node
		cd .  : Remain In Current Node(Directory)
	
		Parent To Child
		cd PATH 
		cd ./Documents/Trainings/

	Change To Home Directory
		cd ~
		cd 
____________________________________________________________________________
Creation of Nodes
	mkdir NODE
	mkdir -p PATH
		Creates BRNACH starting from FIRST NODE If It Doesn't Exists
	touch NODE 
	
	cp NODE 
	cp -r NODE

	mv SOURCE_PATH/NODE DEST_PATH/NODE
		Changes Parent Node
		SOURCE_PATH and DEST_PATH Are Same: Used For Renaming

Deletion of Nodes 
	Deletes Single Node
	___________________ 
	rm NODE 
	rmdir NODE 
		Removes Empty Directory Node
	
	Deletes Branch Provided Every Node is Empty
	rmdir -p PATH
		Removes Branch Starting From FIRST NODE OF PATH

	Deletes All Branches
	rm -R NODE 
		Subtree including Parent NODE

____________________________________________________________________________
Log Files
	/var/log/syslog

	10 Lines Of Files Starting/Ending
		head FILE
		tail FILE 
____________________________________________________________________________
File Permission Model
	POSIX Permission Model
	- rw- r-- r--   1 intelligene  staff   244 Jul  9  2019 contact.txt
		Regular File

		 Creator	Friends		Others
		 _____________________________ 
         Owner  	Group 		Others
         User
		 RWX		RWX   		RWX

		-rw-r--r--  1 intelligene  staff  0 Apr 28 12:34 AnushkaSharma
		intelligene staff   others
		rw 			r 		r

	Paranoid Approach
	____________________________________________________________________________
	Owner  	Group 		Others
	RW      R           R 

	RWX
	
	000	0
	001	1 EXECUTE
	010	2 WRITE
	011 3	
	100	4 READ
	101	5
	110	6
	111	7

	4 READ
	2 WRITE
	1 EXECUTE

	chmod u+x FILE

	Regular File : Default Permission
		umask 

		Mac OSX Default Value for umask is 0022
			666 - 0022 = 644

		Ubuntu Default Value for umask is 0002
			666 - 0002 = 664		

	Directory File : Default Permission
		rwx r-x r-x
		777 - 0022 = 755

	Link File : Default Permission
		777 - 0022 = 755

____________________________________________________________________________
System Design Thinking
____________________________________________________________________________
	Privileage Design
		Never Ever Should Be Given By Name

	Scope Design
		Never Ever Should Be BROADEST
		Narrowest Scope First, Then Broader, Then Broader...

	Scope Design Examples
		Unix File Permission Design: Owner Level, Group Level, Others Level
		Bash Configrations Design: Session Level, User Level, System Level
		Git Configrations Design: Local Level, Global Level, System Level

____________________________________________________________________________
Email: Sunny Leone On Beach!
	SunnyLeone.jpg
		It's Virus 

	Force To Download Scripts
		It's Not Virus 
		SunnyLeone.jpg > SunnyLeone.exe 
		Copy In Some Hidden Folder
		Hide Folder System Wide

System Configuration
____________________________________________________________________________
	Terminal Environment
	env
	export DING_DONG="Balleeee Balleeee"
	export PATH=$PATH:/Users/intelligene/Documents/FlipkartBatch/Heroines

	What is Terminal?
		Session To The OS
	What is Shell?
		Language Interpreter

	Configure System
		Session Level
		
		User Level: Applicable To All Sessions < 
			~/.bashrc
		
		System Level
			/etc/bashrc

	Scope Design
		Never Ever Should Be BROADEST
		Narrowest Scope First, Then Broader, Then Broader...


Vim Editor : Mode Based Editor
____________________________________________________________________________
~/.vimrc

Command Mode -----> i ----> Insert Mode
Command Mode <---- Esc----- Insert Mode
Command Mode -----> Shift+V ----> Visual Line Mode
Command Mode -----> Ctrl+V ----> Visual Block Mode

Command Mode
	/SearchCriteria After Forwards Slash
	n Go To Next Search Item
	N Go To Previous Search Item

	Shift :
	:w  Write To File
	:q  Quit 
	:q! Quit Without Saving

	:wq Write and Quit

	yy  Current Line Yanking
	p   Pasting

	Nyy N lines Yanked/Copied
	p 	N lines Pasted

	dd Current Line Deleted
	u  Undo
 	ctrl+r Redo
	Ndd N lines Deleted 

	:set number To Show Line Numbers
	:set nonumber To Disable Line Numbers
	
	:%s/SOURCE_TEXT/SUSTITUTE_TEXT/gc   
		g means globally whole file and c means confirmation

Learning and Practicing Vim Basics
____________________________________________________________________________
	vimtutor


stdout and stdin Concepts 
____________________________________________________________________________
	printf Writes To A File: stdout
		By Default stdout file is pointing to terminal file

		cat AmirKhan
			write content to stdout file ->  /dev/ttys0000

		cat AmirKhan > DingDong
			write content to DingDong file 

	scanf Reads From A File: stdin
		By Default stdout file is pointing to standard keyboard


Soft and Hard Links
____________________________________________________________________________
	ln -s Heroines/AshwaryaRai.doc ash

	IntelligeneMachine:FlipkartBatch intelligene$ ls -l
	lrwxr-xr-x  1 intelligene  staff   24 Apr 29 09:54 ash -> Heroines/AshwaryaRai.doc


	Hard Link Creation
	____________________________________________________________________________
	IntelligeneMachine:Heroines intelligene$ ln AliaBhat Alia
	IntelligeneMachine:Heroines intelligene$ ls -l
	-rw-rw-r--  2 intelligene  staff    0 Apr 29 10:17 Alia
	-rw-rw-r--  2 intelligene  staff    0 Apr 29 10:17 AliaBhat

	IntelligeneMachine:Heroines intelligene$ ln -s AliaBhat BhatDaughter
	IntelligeneMachine:Heroines intelligene$ ls -l
	-rw-rw-r--  2 intelligene  staff    0 Apr 29 10:17 Alia
	-rw-rw-r--  2 intelligene  staff    0 Apr 29 10:17 AliaBhat
	lrwxr-xr-x  1 intelligene  staff    8 Apr 29 10:48 BhatDaughter -> AliaBhat

File Idea
____________________________________________________________________________
	What is The File?

Few More Commands
____________________________________________________________________________
	du 
	du -ah

	file
	stat

	chmod
	chown
	chgrp

	find 
	locate 
	grep
	egrep
	zip
	tar
	diff -u Hello.c HelloMore.c 
	diff -ur DIR1 DIR2

	mount Make a disk partition accessible
	umount Unmount a disk partition (make it inaccessible)

	df

	mkdir /mnt/mydir 
	mount /dev/hda1 /mnt/mydir

		mydir Mount Point
	df /mnt/mydir
	
	Device Files Existing In Directory: /dev/
	Linux/Ubuntu Naming Conventions
		hda0
		hda1 // IDE Hard Disk 
		sda0 // SCSI Architecture
		sda1
		sda2

	MacOSX Naming Conventions
		disk0
		disk0s1
		disk0s2
		disk1

Mount Command Syntax
	df /mnt/mydir

	mkdir /mnt/mydir 
		Create Mount Point: mydir

	mount DEVICE_FILE MOUNT_POINT
		mount /dev/hda1 /mnt/mydir
			
	unmount DEVICE_FILE
	rmdir MOUNT_POINT
	unmount -a

Groups In Unix
	groups   : Print the group membership of a user
	groupadd : Create a new group
	groupdel : Delete a group
	groupmod : Modify a group

Change User Password
____________________________________________________________________________
	passwd 


GIT Distributed Version Control System
____________________________________________________________________________
Installation And Configuration
	
	1. Install GIT 
		MacOSX
			brew install git
		Ubuntu
			sudo apt install git
			snap install git

	2. Create GitHub Account

Getting Started with Git and GitHub
____________________________________________________________________________
	1. Create Git Repository at GitHub
	2. mkdir LearnGIT in FlipkartBatch
	3. cd LearnGit
	4. git clone of ThinkFlipkart 

Creating Git Repository Locally
____________________________________________________________________________	
	1. Create Git Repository From Already Existing Project LOCALLY
		git init
			Initialized empty Git repository
		git add FILES
		git commit

Cloning Remote Repository
____________________________________________________________________________
	git clone REMOTE_GIT_REPOSITORY_URL

Git Commands
____________________________________________________________________________
	git log : Log Of All Commits Done

Git Configuration
____________________________________________________________________________
	Configuration Levels
	____________________________________________________________________________
	Local 	: Applicable To One Git Repository
	Global  : Applicable To All Git Repositories For A User
	System 	: Applicable To All Git Repositories For All Users

	Configuration SCOPE DESIGN
	____________________________________________________________________________
	Local < Global < System

	Git Configuration Options
	____________________________________________________________________________
	--local 
		LOCAL_GIT_REPOSITORY/.git/config

	--global 
		~/.gitconfig

	--system
		IN UBUNTU: /etc/gitconfig
		IN MACOSX: /usr/local/etc/gitconfig
		IN WINDOWXP : C:\Documents and Settings\All Users\Application Data\Git\config
		IN WINDOWSVISTA : C:\ProgramData\Git\config
	
	Configuration Application Order
	____________________________________________________________________________
	System Configuration Then Global Configuration Then Local Configuration
		To Achieve: Narrowest Scope Takes Precedences

	git config --list
		Actual Configrations Which Are Got Applied Finally!
	git config --global user.name "Amarjit Singh"
	git config --global user.email "amarjitmca04@gmail.com"
	git config --global core.editor "vim"

	git config --system user.name "Ding Dong"
	git config --local user.email "amarjitlife@gmail.com"
	
	git config --system -e
	git config --global -e
	git config --local -e

	git status
	git log
	git log -p 
		-p means Patch Mode i.e. Diff Mode
	git log -p 2
	git log --pretty=online
	git diff
	git diff --cached

	git add FILE1 FILE2 FILE3
	git restore --cached FILE 
	git commit
	git rm FILE 
	git mv SOURCE_FILE DESTINATION_FILE

	git push origin master
		Push Local Repository ChangeSets To Remote Repository
	
	git pull
		Pull Remotes Repository ChangeSets To Local Repository
	
	git remote 
	git branch 
	
	git remote show origin 

____________________________________________________________________________
Pulling Changes : Happening In Remote Repository
____________________________________________________________________________
	Changes Are Pushed By Other Users of Repository
		Pushed To Remote Repository

	1. Create Commit Directly in Remote Repository
	2. git pull
			To Sync Local Repository with Remote Repository

++++++++++++++++++++++++++++++++++++++++++++
--> RAISE HAND IF ABOVE EXPERIMENTS ARE OVER
++++++++++++++++++++++++++++++++++++++++++++

____________________________________________________________________________
ALWAYS DO FOLLOWING
____________________________________________________________________________
	git pull 
		BEFORE DOING
	git push

	git pull 
		Consists of Following Two Step Processes
			git fetch
			git merge

Tagging Commit In Branch
____________________________________________________________________________
	git tag -a v0.1 -m "Version 0.1"
	git tag
	git show v0.1

	git tag -a v0.05 6f2448d9c1de
	git tag -d v0.05

	git log
	cat .git/HEAD
	cat .git/refs/heads/master
	git log

	git push origin v0.1
	git push origin --tags

Git Branching
____________________________________________________________________________
	git branch
		Show Branches Available
	git branch testing
		Creates New Branch 
	
	git branch
	git checkout testing
		Switches To testing Branch
	
	git branch


Branching Experiment
____________________________________________________________________________
	Create Testing Branch
	Make Testing Branch Current
	Commit 1 ChangeSet to Testing Branch

	Make Master Branch Current
	Commit 1 ChangeSet to Master Branch

	Check Values of Following Files and REASON IT
		cat .git/refs/heads/master
		cat .git/refs/heads/testing

	Branch Merging
		git checkout TARGET_BRANCH
		git merge BRANCH_NAME

	Merge Conflict
		Simualte Merge Conflict Scenario
		Resolve Merge Conflict While Merging
		Merge Code
	
	3-Way Merge Tools

	Distributed Version Control System
	
Sequence Of Commands To Achieve Above Idea
____________________________________________________________________________
	git branch testing
	git branch
	git checkout testing
	git branch

	git branch
	cd SourceCode/
	cd Maths/
	vim Numbers.c
	git diff
	git add Numbers.c
	git status
	git diff --cached
	git commit
	git log
	cd ../..

	cat .git/refs/heads/master
	git branch
	git log
	git branch
	git log
	
	git checkout master
	git branch
	git log
	git branch
	cd SourceCode/
	cd String/
	vim String.cpp
	git diff
	git add String.cpp
	git diff --cached
	git commit
	git log
	cd ..
	cd ..
	cat .git/refs/heads/master
	cat .git/refs/heads/testing


JAVA PROGRAMMING LANGUAGE
____________________________________________________________________________

	Java Softwares Installation
	____________________________________________________________________________
	Java JDK 9+

TEST YOUR ENVIRONMENT : COMMANDS TO COMPILE AND RUN JAVA CODE
______________________________________________________________________________
	Create Following Things
		Hello.java
		mkdir ClassFiles

	javac Hello.java -d ClassFiles  
		// Compilation By Invoking Java Compiler
		// Result Will Hello.class File
		//	-d Destination of .class Files

	java -cp ClassFiles/ learnJava.Hello 
		// Invoking JVM
		// Please Load learnJava.Hello
		// Search main Function
		// Start Execution From main Function

	.
	├── ClassFiles
	│   └── learnJava
	│       └── Hello.class
	└── Hello.java


	java Hello.java

++++++++++++++++++++++++++++++++++++++++
RAISE HAND WHEN FOLLOWING ARE DONE
++++++++++++++++++++++++++++++++++++++++

____________________________________________________________________________

ASSIGNMENT 1
____________________________________________
Write Sum Function In C Language

// Which Will Return Valid SUM For ANY X, Y
// Otherwise Print Can't Calculate SUM

int sum(int x, int y) {


}

ASSIGNMENT 2
____________________________________________
Write Sum Function In C Language

// Which Will Return Valid SUM For ANY X, Y
// Otherwise RETURN ERROR

int sum(int x, int y) {

}

PROVE YOUR CODE IS RIGHT OR WRONG
	REASON WITH DEFINITION
		DATA TYPE


++++++++++++++++++++++++++++++++++++++++
RAISE HAND WHEN ABOVE ARE DONE
++++++++++++++++++++++++++++++++++++++++


____________________________________________________________________________
DESIGN 1 : BAD CODE

int sum(int x, int y) {
    return x + y;
}

____________________________________________________________________________
DESIGN 2 : BAD CODE

int sum(int a,int b) {
	long int res = a + b; 
	// res contains already overlowed result
	//    INT_MIN <= res INT_MAX

	// Violates Closure Property/Law
	// Following CONDITION is ALWAYS FALSE
	if(res > INT_MAX || res < INT_MIN){
		printf(""Cant print sum"");
		return -1;
	}
	else
		return (int)res;
}
____________________________________________________________________________

DESIGN 3: BAD CODE

// API Design Wrong MEss

long long sum(long long int a,long long int b){ 
  // NOT Portable Code
  // Asssuming Hardware Architecture
  if(a>0 && b>0 && a+b<0 ) { 
                printf(""Can't calculate sum"");
  }
  else if(a<0 && b<0  && a+b>0){
                printf(""Can't calculate sum"");
  }
  else printf(""%lld"", a+b);
}

____________________________________________________________________________

DESIGN 4: BAD CODE
int sum(int x, int y)
{
        long nx=x,ny=y;
        nx+=ny;
        if(nx>INT_MAX || nx<INT_MIN)
                {
                        printf(""can't print valid sum"");
                        exit(1);
                }

        return (int)(nx);
}

____________________________________________________________________________
DESIGN 5: RIGHT Directional

int sum(int x,int y)
{
        int res = (x+y);
        
        if(res-y == x and res-x == y)
                return res;

        printf(""Error : Can't calculate valid sum.\n"");
        print(""Returning the first 32 bits of computed sum.\n"")
        exit(1);
}

____________________________________________________________________________
DESIGN 6: BAD CODE

int sum(int a, int b){
        long result = (long)a + (long)b;

        if(result>INT_MAX||result<INT_MIN){
                printf(""Can't calculate valid sum\n"");
                return 0;
        }
        else
 			// Overflow/Underflow
                return (int)result;
}
____________________________________________________________________________
DESIGN 8: RIGHT Directional

int sum(int a,int b){
	int x = a+b;
	if(a>0 && b>0){
		if(x>a)
			return x;
		else
		printf(""Invalid"");
	}
	else if(a<0 && b<0){
		if(x<a)
			return x;
		else
		printf(""Invalid"");
	}
	return x;
}
____________________________________________________________________________

// NOT RESPETING TYPE DEFINITION
// 
int sum (int a, int b)
 {
     long int sum = (long int)a + (long int)b;
     if (sum > (long int)INT_MAX || sum < (long int)INT_MIN)
        {printf(""Sum exceeds int range\n"");exit(1);}
     else
        return a + b;
 }

____________________________________________________________________________

int sum(int x,int y){

	int upper_bound=INT_MAX;
	int lower_bound=INT_MIN;
	if(y>=0){
	///
		if( x>=0 && upper_bound-x< y){
		
	printf(""error\n"");
	exit(0);

}

}
else{

if(x<=0 && lower_bound-x>y){
printf(""error\n"");
exit(0);

}

}
return x+y;



}
____________________________________________________________________________

Another Approach
int sum(int x, int y)
{
        int result = (x+y);

        if(x>0 && y>0 && (result)<0)
        {
                printf(""Can't Calculate Sum"");
                return -1;
        }

        else if(x<0 && y<0 && (result)>0)
        {
                printf(""Can't Calculate Sum"");
                return -1;
        }

        else
                return result;
}


____________________________________________________________________________
int sum(int a, int b){
    // Good Check
    if((a >= 0 && b > INT_MAX-a) || (a <=0 && b < INT_MIN-a)){
        printf(""Can't Calculate the Sum"");
        
        // POSSIBLE SUM
        return -1;
    }
    return (a+b);
}

_____________________________
CLIENT CODE
	int result = sum(a, b);

____________________________________________________________________________
DESIGN : GOOD CODE
DESIGN THINKING: THINKING in ONLY ONE LAYER - LANGUAGE LAYER

#include <limits.h>
// DEFAULT int is Signed

int sum(signed int si_a, signed int si_b) {
  signed int sum = 0;

  // Type Safe Code
  // Following Two Lines
  // 	Are Repecting Data Type Definition: 
  //  		int Data Type
  if (((si_b > 0) && (si_a > (INT_MAX - si_b))) ||
      ((si_b < 0) && (si_a < (INT_MIN - si_b)))) {
    /* Handle error */
	printf("Can't Calculate SUM")
  } else {
    sum = si_a + si_b;
  }

  return sum;
}
____________________________________________________________________________

Prove Int In C = Int In Java?
Prove Int In Java = Int In C++?
Prove Int In C = Int In C++?

Fundamental Thinking
____________________________________________________________________________
OverFlow And Underflow
	Fundamental Part of System [Finite]

What is Far More Fundamental 
	Value or Type
	Variable

Language Designer Thinking
____________________________________________________________________________
	Language Design
		Ballee Balleee Language
	
	Data Type
		int In Balleee Ballee
			Which Design You Will Prefer and Why?

Object Oriented Design
______________________________________________________________________________
	Behaviour Deriven Design
		Behaviour | Messages | Operations
	Objects Talks With Each Others Using Message Passging

Object Oriented Design Concepts : Revisit
______________________________________________________________________________
	1. Encapsulation
	2. Inheritance
	3. Polymorphism
	4. Abstraction
	5. Composite


System Design Principles
______________________________________________________________________________
Fundamental Principles

Scope Design Principles
	Because Localisation Happens In System

Design Towards Abstrat Type Rather Than Concerete Type
	Design Towards Interfaces Rather Than Concerete Classes
		Interfaces Brings Contract In System
			Design By Conventions
				  When Everyone Respect Conventions
				  It Brings Trust In System
				  System Based Trust Works Better	

	Design By Configuration(Rules)
		Rules Are Fixed
		System Evolution Stops

Design Towards Immutability Rather Than Mutability
	Classes Not Meant To Subclassed Must Be Final
	Member Functions Not Meant To Be Overridden Must Be Final
	Member Varibles Not Meant To Be Changing Must Be Final 


SOLID Principles
______________________________________________________________________________
	1. Single Repsonility Design Principle
	2. Open Close Principle
		Classes/Types Are Open Extension But Close Modification

Learning Principles
______________________________________________________________________________
	Chaos is a Ladder :P 
	First Choas Than Order!
	Toughest Things In This World is Unlearning!
	
System Design Thining
____________________________________________________________________________
	Where To Start?
	____________________________________________________________
		Should You Design System Based On Configuration(Rules)?
			Mind Focused Towards States and Rules
			Rules Meant To Be Broken!


		Should You Design System Based On Conventions?
			Mind Focused Towards Contracts/Trust

			e.g. 
			Design Towards Interfaces Rather Than Concerete Classes

	Design By Conventions VS Design By Configurations
	____________________________________________________________
		Design By Conventions is Better
			Con

Function As An Idea
______________________________________________________________________________
 	Function As Procedure | Task | Routine | Method
	______________________________________________________________________________
		Definition: Sequence of Instuctions

	Function Type
	______________________________________________________________________________
		Definition: Function is Type
			Function Type = { Operations, Range }

	Function is Lambda
	______________________________________________________________________________
		Special Case of Lambda

Lambda
______________________________________________________________________________
	It is a Context Which Captures Immediate Context Around It


Override equal Method : Best Practices
______________________________________________________________________
- There are a number of routine steps that you need to go through in an equals method:
	
	- Whenever you override the equals method, you must provide a compatible hashCode method as well

	- It is common for equal objects to be identical, and that test is very inexpensive.

	- Every equals method is required to return false when comparing against null.

	- Since the equals method overrides Object.equals, its parameter is of type Object, and you need to cast it to the actual type so you can look at its instance variables. Before doing that, make a type check, either with the getClass method or with the instanceof operator.

	- Finally, compare the instance variables. Use == for primitive types. How- ever, for double values, if you are concerned about ±∞ or NaN, use Double.equals. For objects, use Objects.equals, a null-safe version of the equals method. The call Objects.equals(x, y) returns false if x is null, whereas x.equals(y) would throw an exception.


	- The hashCode and equals methods must be compatible: 
		If x.equals(y), then it must be the case that x.hashCode() == y.hashCode()


	- A hash code is an integer that is derived from an object.

	- Hash codes should be scrambled—if x and y are two unequal objects, there should be a high probability that x.hashCode() and y.hashCode() are different.

equals Contract In Java Specification
_____________________________________________________________________
public boolean equals(Object obj)

	Indicates whether some other object is "equal to" this one.
	The equals method implements an equivalence relation on non-null object references:

	It is reflexive: for any non-null reference value x, x.equals(x) should return true.

	It is symmetric: for any non-null reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.

	It is transitive: for any non-null reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.

	It is consistent: for any non-null reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently return false, provided no information used in equals comparisons on the objects is modified.

	//____________________________________________________________

	For any non-null reference value x, x.equals(null) should return false.
	
	The equals method for class Object implements the most discriminating possible equivalence relation on objects; that is, for any non-null reference values x and y, this method returns true if and only if x and y refer to the same object (x == y has the value true).

	Note that it is generally necessary to override the hashCode method whenever this method is overridden, so as to maintain the general contract for the hashCode method, which states that equal objects must have equal hash codes.

_____________________________________________________________________

class Money {
    int amount;
    String currencyCode;
}

Money income = new Money(55, "USD");
Money expenses = new Money(55, "USD");
boolean balanced = income.equals(expenses)

class Money {
    int amount;
    String currencyCode;

	@Override
	public boolean equals(Object o) {
	    if (o == this)
	        return true;
	    if (!(o instanceof Money))
	        return false;
	    Money other = (Money)o;
	    boolean currencyCodeEquals = (this.currencyCode == null && other.currencyCode == null)
	      || (this.currencyCode != null && this.currencyCode.equals(other.currencyCode));
	    return this.amount == other.amount && currencyCodeEquals;
	}

}

class WrongVoucher extends Money {
    private String store;
 
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WrongVoucher))
            return false;
        WrongVoucher other = (WrongVoucher)o;
        boolean currencyCodeEquals = (this.currencyCode == null && other.currencyCode == null)
          || (this.currencyCode != null && this.currencyCode.equals(other.currencyCode));
        boolean storeEquals = (this.store == null && other.store == null)
          || (this.store != null && this.store.equals(other.store));
        return this.amount == other.amount && currencyCodeEquals && storeEquals;
    }
 
    // other methods
}

At first glance, the Voucher class and its override for equals() seem to be correct. And both equals() methods behave correctly as long as we compare Money to Money or Voucher to Voucher. 

But what happens, if we compare these two objects?

Money cash = new Money(42, "USD");
WrongVoucher voucher = new WrongVoucher(42, "USD", "Amazon");
 
voucher.equals(cash) => false // As expected.
cash.equals(voucher) => true // That's wrong.
That violates the symmetry criteria of the equals() contract















____________________________________________________________________________
From saurav anand to Everyone: (02:22 PM)
 sir rw-rwxrwx is not making the file run 
From Anil Bansal to Everyone: (02:22 PM)
 if the execute permission is given to others then will the owner be able to execute it? 
From saurav anand to Everyone: (02:22 PM)
 bur x permission is with grp

____________________________________________________________________________
// Sahaj
int a[2][2];         
printf("%p\n%p\n",a,*a);













