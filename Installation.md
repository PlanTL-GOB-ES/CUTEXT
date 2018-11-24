# Installation
---------------------------------------
We include here a small tutorial to install and compile CUTEXT.
If you are going to use the executable (Java ARchive, JAR) file included, you do not need to install or compile anything, just read the section 'Execution via JAR file' in the README.md file.

### Prerequisites
-----------------
You only need to have installed Java (developer version) 1.7 or later.
Neither IDE nor automated compilation tool is needed, such as Eclipse or IntelliJ IDEA.

### Java
-----------------
If you don’t have Java 1.7 or later, download the current [Java Development Kit (JDK)](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
To check if you have a compatible version of Java installed, use the following command:
<pre>
java -version
</pre>

### Environment Variables
-------------------------
It is necessary to include in the PATH, and CLASSPATH environment variables, the java path and the CUTEXT packets, respectively.

#### PATH
---------
This variable informs the Operating System (OS) where Java is located within it.
Therefore, it is dependent on the OS:
* Windows: Generally when installing Java in Windows, it automatically includes in the PATH variable the path to the Java 'bin' directory. To be sure, we have to type (cmd terminal):
<pre>
echo %PATH%
</pre>
We can also see the routes included within the PATH variable in 'Environment Variables' within 'System Properties'.
The absolute path must appear to the 'bin' directory of the version of the JDK that we have installed.
For example, if we have installed 1.8.0_152 version inside 'Program_Files', we'll see something like this:
<pre>
C:\Program_Files\Java\jdk1.8.0_152\bin
</pre>
If it does not appear, we must include it. Again, it can be done in 'Environment Variables', editing PATH, and including that path.
Also from the cmd terminal, typing the following (for the previous example):
<pre>
set PATH=C:\Program_Files\Java\jdk1.8.0_152\bin;%PATH%
</pre>
* Linux: To include it in linux, we open a terminal and type:
<pre>
export PATH='route':$PATH
</pre>
Where 'route' is the path to the Java 'bin' directory. With this the changes will be temporary, closing the terminal will disappear.
If we want them to be permanent, we can edit the *.bashrc* file, by writing:
<pre>
gedit /home/usuario/.bashrc
</pre>
This will open *.bashrc* in the 'gedit' text editor. We just have to include the following two lines, at the end of it:
<pre>
PATH=route:$PATH
export PATH
</pre>
When saving the changes in 'gedit', they will now be permanent. We can close the terminal, reopen it, and type:
<pre>
echo $PATH
</pre>
Now, we will see that the PATH variable contains the path to the Java 'bin' directory.


#### CLASSPATH
---------------
The CLASSPATH environment variable is modified with the set command. The format is:
<pre>
set CLASSPATH=path1;path2 ... (Windows)
export CLASSPATH=path1:path2 ... (Linux)
</pre>
The paths should begin with the letter specifying the drive, for example, C:\ under Windows or /home/user/ under Linux. 
That way, the classes will still be found if you happen to switch to a different drive. 

###### Clearing CLASSPATH
-------------------------
If your CLASSPATH environment variable has been set to a value that is not correct, or if your startup file or script is setting an incorrect path, 
you can unset CLASSPATH by using:
<pre>
C:\set CLASSPATH= (Windows)
$ export CLASSPATH= (Linux)
</pre>
This command unsets CLASSPATH for the current command prompt window only. 

###### Set CLASSPATH in CUTEXT
--------------------------------
If you have downloaded CUTEXT from Git-Hub, the directory structure will have been maintained, which will be:
<pre>
cutext/...
</pre>
The route to set the CLASSPATH is the 'cutext' parent directory. For example, we will assume that in **Windows** we have downloaded it at 'Software':
<pre>
C:\Software\cutext\...
</pre>
Then, the CLASSPATH must be set to 'Software':
<pre>
set CLASSPATH=C:\Software;%CLASSPATH%
</pre>
In **Linux** it is very similar. For example, if we have downloaded it at 'Software':
<pre>
/home/user/Software/cutext/...
</pre>
Then, again, the CLASSPATH must be set to 'Software':
<pre>
export CLASSPATH=/home/user/Software/:$CLASSPATH
</pre>

### Compilation and Execution
-----------------------------
Once the environment variables are fixed, we can compile and execute CUTEXT.
To compile the .java files, we only have to move, in the terminal, at the directories that contain them, and type:
<pre>
javac *.java
</pre>
For example, to compile the files that are into the 'util' directory, we type the following under **Windows** (we will assume that CUTEXT was downloaded at 'Software', see the previous section):
<pre>
cd C:\Software\cutext\util
C:\Software\cutext\util> javac *.java
</pre>
In Windows, by default, the files are encoded in ASCII (in Linux in UTF-8), so, to avoid compilation errors for certain characters, it is convenient to compile the java files, setting utf8 as the *encoding* flag, like this:
<pre>
javac -encoding utf8 *.java
</pre>
Under **Linux** it is very similar (as before, we will assume that it was downloaded at 'Software', see the previous section):
<pre>
$ cd /home/user/Software/cutext/util
$ javac *.java
</pre>
In a similar manner it would be for the rest of the directories that contain Java files (extension .java).  
Once compiled, we can execute it.
For this, the simplest way is to move (in the terminal) at 'main' directory, and type:
<pre>
java cutext.main.ExecCutext [options]
</pre>
The options are available in the README.md file. 
To prove that it runs correctly, you can ask it, for example, to show you the execution options in the following way:
<pre>
java cutext.main.ExecCutext -help
</pre>












