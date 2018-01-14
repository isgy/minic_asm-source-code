# Results at time 2018-01-14T23-39-20+0000

Results for student *s1514483* aka *green deer*

* * * 

## Pass llvm-pass-my-dce

Test|Correct output|Instructions count before|Expected instruction count before|Instructions count after|Expected instruction count after|Volatile instructions before DCE|Volatile instructions after DCE
:------|:-----:|------:|------:|------:|------:|------:|------:
test01|False|2|2|Failed|2|0|Failed
test02|False|3|3|Failed|2|0|Failed
test03|False|12|12|Failed|12|0|Failed
test04|False|13|13|Failed|13|0|Failed
test05|False|8|8|Failed|7|0|Failed
test06|False|10|10|Failed|9|0|Failed
test07|False|10|10|Failed|8|0|Failed
test08|False|11|11|Failed|11|0|Failed
test09|False|11|11|Failed|10|0|Failed
test10|False|19|19|Failed|18|0|Failed
test11|False|21|21|Failed|20|0|Failed
test12|False|23|23|Failed|20|0|Failed
test13|False|7|7|Failed|7|1|Failed
test14|False|17|17|Failed|17|2|Failed
test15|False|18|18|Failed|18|2|Failed
test16|False|17|17|Failed|17|2|Failed
test17|False|24|24|Failed|23|1|Failed
test18|False|32|32|Failed|32|3|Failed


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


