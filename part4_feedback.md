# Results at time 2018-01-14T14-15-53+0000

Results for student *s1514483* aka *green deer*

* * * 

## Pass llvm-pass-my-dce

Test|Correct output|Instructions count before|Expected instruction count before|Instructions count after|Expected instruction count after|Volatile instructions before DCE|Volatile instructions after DCE
:------|:-----:|------:|------:|------:|------:|------:|------:
test01|True|2|2|1|2|0|1
test02|True|3|3|2|2|0|2
test03|True|12|12|3|12|0|3
test04|True|13|13|4|13|0|4
test05|True|8|8|5|7|0|5
test06|True|10|10|6|9|0|6
test07|True|10|10|7|8|0|7
test08|True|11|11|8|11|0|8
test09|True|11|11|9|10|0|9
test10|True|19|19|10|18|0|10
test11|True|21|21|11|20|0|11
test12|True|23|23|12|20|0|12
test13|True|7|7|13|7|1|13
test14|True|17|17|14|17|2|14
test15|True|18|18|15|18|2|15
test16|True|17|17|16|17|2|16
test17|True|24|24|17|23|1|17
test18|True|32|32|18|32|3|18


* * * 

## Pass llvm-pass-simple-dce

Test|Correct output|Instructions count before|Expected instruction count before|Instructions count after|Expected instruction count after|Volatile instructions before DCE|Volatile instructions after DCE
:------|:-----:|------:|------:|------:|------:|------:|------:
test01|True|2|2|2|2|0|0
test02|True|3|3|2|2|0|0
test03|True|12|12|12|12|0|0
test04|True|13|13|13|13|0|0
test05|True|8|8|8|8|0|0
test06|True|10|10|10|10|0|0
test07|True|10|10|9|9|0|0
test08|True|11|11|11|11|0|0
test09|True|11|11|10|10|0|0
test10|True|19|19|19|19|0|0
test11|True|21|21|21|21|0|0
test12|True|23|23|21|21|0|0
test13|True|7|7|7|7|1|1
test14|True|17|17|17|17|2|2
test15|True|18|18|18|18|2|2
test16|True|17|17|17|17|2|2
test17|True|24|24|24|24|1|1
test18|True|32|32|32|32|3|3


