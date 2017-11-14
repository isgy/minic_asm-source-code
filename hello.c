/*
 * =====================================================================================
 *
 *       Filename:  hello.c
 *
 *    Description:  
 *
 *        Version:  1.0
 *        Created:  14/11/17 15:29:27
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  YOUR NAME (), 
 *   Organization:  
 *
 * =====================================================================================
 */
#include "aksd"
char global[12];
int foo;
int main() {
    char string[12];
    char *c;
    foo = 11;
    string = "Hello world";
    global = string;
    c = (char*) string;
    print_s(c);
    print_s((char*) "Hello world");
    print_s((char*) string);
    print_i(foo);
}

