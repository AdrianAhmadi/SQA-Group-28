Compile the program:
g++ -std=c++17 -Wall -Wextra src/main.cpp src/Classes/User.cpp src/Classes/Admin.cpp src/Classes/StandardBuyer.cpp src/Classes/StandardSeller.cpp src/Classes/StandardFull.cpp src/Classes/Game.cpp -o distribution-system.exe

Run tests:
./scripts/run_tests.sh

Compare outputs:
./scripts/compare_outputs.sh
