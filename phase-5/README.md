PHASE 5 - BACK END TESTS (JAVA + JUNIT, RUN IN WSL)

1. OPEN THE PROJECT FOLDER IN POWERSHELL, START WSL

cd C:\Users\ddjem\CSCI_3060U\Project\phase-5
wsl


2. INSTALL JAVA AND MAVEN (ONE TIME)

sudo apt update
sudo apt install -y openjdk-17-jdk maven
java -version
mvn -version


3. GO TO THE PROJECT IN WSL

cd /mnt/c/Users/ddjem/CSCI_3060U/Project/phase-5


4. RUN ALL TESTS

mvn test


OTHER COMMANDS

mvn test -Dtest=TransactionManagerTest
mvn test -Dtest=TextInterpreterTest
mvn test -Dtest=TextFilePrinterTest
mvn test -Dtest=MainTest
mvn test -Dtest=CreateFilesPathCoverageTest
mvn -q test


RESULTS

target/surefire-reports/