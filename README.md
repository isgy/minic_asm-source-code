# Deadlines #

1. [Part 1 (parser)](desc/part1/), Thursday 12 October 2017 at 4pm, weight = 20% 2. [Part 2 (ast builder + semantic analyser)](desc/part2/), Thursday 
26 October 2017 at 4pm, weight = 20% 3. [Part 3 (code generator)](desc/part3/), Thursday 16 November 2017 at 4pm, weight = 30% 4. [Part 4 (LLVM-based 
compiler pass)](desc/part4/), Monday 15 January 2018, 10am, weight = 30%

dd your compiler about once a day. You can keep track of your progress and see how many 
tests pass/fail using the scoreboard by following this link: 
https://htmlpreview.github.io/?https://bitbucket.org/cdubach/ct-17-18/raw/master/scoreboard/scoreboard.html

# Marking #

The marking will be done using an automated test suite on a dice machine using Java 8 (1.8 runtime). Please note that you are not allowed to modify 
the `Main.java` file which is the main entry point to the compiler. A checksum on the file will be performed to ensure the file has not be tempered 
with. Also make sure that the build script provided remains unchanged so that your project can be built on dice. Furthermore, you may not use any 
external libraries.

For parts 1-3 of the coursework, the marking will be a function of the number of successful tests as shown in the scoreboard and a series of hidden 
tests.

66% of the mark will be determined by the scoreboard tests and 33% will be determined by the hidden tests. You will get one point for each passing 
test and -1 for each failing test. Then the mark is calculated by dividing the number of points achieved by the number of tests. To be more precise, 
here is the formula used for marking: 2/3\*max(0,(#passed_v_tests-#failed_v_tests)/(#passed_v_tests+#failed_v_tests)) + 
1/3\*max(0,(#passed_h_tests-#failed_h_tests)/(#passed_h_tests+#failed_h_tests)) where _v_ represents the visible tests and _h_ the hidden ones.

66% of the mark will be determined by the visible scoreboard tests and 33% will be determined by the hidden tests. The mark will directly be 
proportial to the number of passed tests (no negative point). So for instance, passing 7 out of 50 tests would result in a mark of 14/100.

